package com.elearning.service;

import com.elearning.dto.CourseInfoDto;
import com.elearning.model.*;
import com.elearning.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class CourseService {

    private CourseRepository courseRepository;
    private AssignmentRepository assignmentRepository;
    private LessonRepository lessonRepository;
    private InstructionRepository instructionRepository;
    private AuthService authService;
    private UserRepository userRepository;
    private EnrollmentRepository enrollmentRepository;
    private ReviewRepository reviewRepository;
    private EnrollmentRequestRepository enrollmentRequestRepository;
    private Utility utility;

    @Autowired
    public CourseService(CourseRepository courseRepository,
                         AssignmentRepository assignmentRepository,
                         LessonRepository lessonRepository,
                         InstructionRepository instructionRepository,
                         AuthService authService,
                         UserRepository userRepository,
                         EnrollmentRepository enrollmentRepository,
                         ReviewRepository reviewRepository,
                         EnrollmentRequestRepository enrollmentRequestRepository,
                         Utility utility) {
        this.courseRepository = courseRepository;
        this.assignmentRepository = assignmentRepository;
        this.lessonRepository = lessonRepository;
        this.instructionRepository = instructionRepository;
        this.authService = authService;
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.reviewRepository = reviewRepository;
        this.enrollmentRequestRepository = enrollmentRequestRepository;
        this.utility = utility;
    }

    public ResponseEntity addCourseInfo(CourseInfoDto courseInfoDto) {
        return mapAddingCourseInfo(courseInfoDto);
    }

    private ResponseEntity mapAddingCourseInfo(CourseInfoDto courseInfoDto) {
        Course course = new Course();
        Instruction instruction = new Instruction();
        User user  = authService.getCurrUser();

        course.setTitle(courseInfoDto.getTitle());
        course.setDescription(courseInfoDto.getDescription());
        course.setPublished("NO");
        course.setPublishedOn(Date.from(Instant.now()));
        courseRepository.save(course);

        instruction.setCourseId(course.getId());
        instruction.setUserId(user.getId());
        instructionRepository.save(instruction);

        Enrollment enrollment = new Enrollment();
        enrollment.setCourseId(course.getId());
        enrollment.setUserId(user.getId());
        enrollmentRepository.save(enrollment);

        return new ResponseEntity("Course info added " +
                "but the course is not published yet, " +
                "you have to add the lessons " +
                "and the assignments", HttpStatus.OK);
    }

    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        courses = utility.checkPublish(courses);
        for(int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            course.clearEnrollmentRequests();
            course = utility.checkEnrollment(course);
        }
        return new ResponseEntity(courses, HttpStatus.OK);
    }

    public ResponseEntity<Course> getCourseById(long id){
        Course course = courseRepository.findCourseById(id);
        course.clearEnrollmentRequests();
        if (course.getPublished().equals("NO")) {
            return new ResponseEntity<Course>(HttpStatus.OK);
        }
        course = utility.checkEnrollment(course);
        return new ResponseEntity(course, HttpStatus.OK);
    }

    public ResponseEntity<List<Course>> getCoursesByName(String title) {
        List<Course> courses = courseRepository.
                findCoursesByTitle(title);
        courses = utility.checkPublish(courses);
        for(int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            course = utility.checkEnrollment(course);
        }
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    public ResponseEntity editCourseInfo(long id,
                                         CourseInfoDto courseInfoDto) {
        return mapEditingCourseInfo(id, courseInfoDto);
    }

    private ResponseEntity mapEditingCourseInfo(long id,
                                                CourseInfoDto courseInfoDto) {
        Course course = courseRepository.findCourseById(id);

        boolean editing = utility.checkAbilityToManageCourse(id);
        if (editing == false) {
            return new ResponseEntity("You are not allowed " +
                    "to edit this course", HttpStatus.BAD_REQUEST);
        }

        if (courseInfoDto.getTitle().isEmpty() == true
                || courseInfoDto.getTitle().isBlank() == true) {
            return new ResponseEntity("Course title can " +
                    "not be empty", HttpStatus.BAD_REQUEST);
        } else {
            if (courseInfoDto.getTitle() != null) {
                course.setTitle(courseInfoDto.getTitle());
            } else {
                course.setTitle(course.getTitle());
            }
        }
        if (courseInfoDto.getDescription().isEmpty() == true
                || courseInfoDto.getDescription().isBlank() == true) {
            return new ResponseEntity("Course Description " +
                    "can not be empty", HttpStatus.BAD_REQUEST);
        } else {
            if (courseInfoDto.getDescription() != null) {
                course.setDescription(courseInfoDto.getDescription());
            } else {
                course.setDescription(course.getDescription());
            }
        }

        courseRepository.save(course);
        return new ResponseEntity("Course info " +
                "has been edited!", HttpStatus.OK);
    }

    public ResponseEntity deleteCourse(long id) {
        return mapDeletingCourse(id);
    }

    private ResponseEntity mapDeletingCourse(long id) {
        Course course = courseRepository.findCourseById(id);
        List<Assignment> assignments = course.getAssignments();
        List<Lesson> lessons = course.getLessons();
        List<Review> reviews = course.getReviews();
        List<EnrollmentRequest> enrollmentRequests =
                course.getEnrollmentRequests();

        boolean delete = utility.checkAbilityToManageCourse(id);
        if (delete == false) {
            return new ResponseEntity("You are not allowed" +
                    " to delete this course", HttpStatus.BAD_REQUEST);
        }

        deleteAnythingRelatedToCourse(course, reviews,
                lessons, assignments, enrollmentRequests);

        return new ResponseEntity("Course has been " +
                "deleted!", HttpStatus.OK);
    }

    private void deleteAnythingRelatedToCourse(Course course,
                                               List<Review> reviews,
                                               List<Lesson> lessons,
                                               List<Assignment> assignments,
                                               List<EnrollmentRequest> enrollmentRequests) {
        for(int i = 0; i < assignments.size(); i++) {
            Assignment assignment = assignments.get(i);
            assignmentRepository.delete(assignment);
        }
        for(int i = 0; i < lessons.size(); i++) {
            Lesson lesson = lessons.get(i);
            lessonRepository.delete(lesson);
        }
        for(int i = 0; i < reviews.size(); i++) {
            Review review = reviews.get(i);
            reviewRepository.delete(review);
        }
        for(int i = 0; i < enrollmentRequests.size(); i++) {
            EnrollmentRequest enrollmentRequest = enrollmentRequests.get(i);
            enrollmentRequestRepository.delete(enrollmentRequest);
        }
        course.clearAssignments();
        course.clearLessons();
        course.clearEnrollmentRequests();
        course.clearReviews();
        courseRepository.delete(course);
    }

}
