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
        courseRepository.save(course);

        instruction.setCourseId(course.getId());
        instruction.setUserId(user.getId());
        instructionRepository.save(instruction);

        Enrollment enrollment = new Enrollment();
        enrollment.setCourseId(course.getId());
        enrollment.setUserId(user.getId());
        enrollmentRepository.save(enrollment);

        return new ResponseEntity("Course info has " +
                "been added", HttpStatus.OK);
    }

    public ResponseEntity publishCourse(long id) {
        return mapPublishingCourse(id);
    }

    private ResponseEntity mapPublishingCourse(long id) {
        Course course = courseRepository.findCourseById(id);

        boolean instruction = utility.checkInstruction(course);
        if (instruction == false)
            return new ResponseEntity("You are not an " +
                    "instructor in this course", HttpStatus.BAD_REQUEST);

        if (course.getPublished().equals("YES"))
            return new ResponseEntity("The course is " +
                    "already published", HttpStatus.BAD_REQUEST);

        course.setPublished("YES");
        course.setPublishedOn(Date.from(Instant.now()));
        courseRepository.save(course);
        return new ResponseEntity("The course has been " +
                "published", HttpStatus.OK);
    }

    public ResponseEntity hideCourse(long id) {
        return mapHidingCourse(id);
    }

    private ResponseEntity mapHidingCourse(long id) {
        Course course = courseRepository.findCourseById(id);
        boolean instruction = utility.checkInstruction(course);
        if (instruction == false)
            return new ResponseEntity("You are not an " +
                    "instructor in this course", HttpStatus.BAD_REQUEST);

        if (course.getPublished().equals("NO"))
            return new ResponseEntity("The course is " +
                    "already hidden", HttpStatus.BAD_REQUEST);

        course.setPublished("NO");
        courseRepository.save(course);
        return new ResponseEntity("The course has been " +
                "hidden", HttpStatus.OK);
    }

    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        courses = utility.checkPublish(courses);
        for(int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            course.clearEnrollmentRequests();
            boolean enrolled = utility.checkEnrollment(course);
            if (enrolled == false) {
                course.clearEnrollmentRequests();
                course.clearLessons();
                course.clearAssignments();
            }
        }
        return new ResponseEntity(courses, HttpStatus.OK);
    }

    public ResponseEntity<Course> getCourseById(long id){
        Course course = courseRepository.findCourseById(id);
        course.clearEnrollmentRequests();
        if (course.getPublished().equals("NO"))
            return new ResponseEntity<Course>(HttpStatus.OK);

        boolean enrolled = utility.checkEnrollment(course);
        if (enrolled == false) {
            course.clearEnrollmentRequests();
            course.clearLessons();
            course.clearAssignments();
        }
        return new ResponseEntity(course, HttpStatus.OK);
    }

    public ResponseEntity<List<Course>> getCoursesByName(String title) {
        List<Course> courses = courseRepository.
                findCoursesByTitle(title);
        courses = utility.checkPublish(courses);
        for(int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            boolean enrolled = utility.checkEnrollment(course);
            if (enrolled == false) {
                course.clearEnrollmentRequests();
                course.clearLessons();
                course.clearAssignments();
            }
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

        boolean editing = utility.checkInstruction(course);
        if (editing == false)
            return new ResponseEntity("You are not allowed " +
                    "to edit this course", HttpStatus.BAD_REQUEST);

        if (courseInfoDto.getTitle().isEmpty() == true
                || courseInfoDto.getTitle().isBlank() == true) {
            return new ResponseEntity("Course title can " +
                    "not be empty", HttpStatus.BAD_REQUEST);
        } else {
            if (courseInfoDto.getTitle() != null)
                course.setTitle(courseInfoDto.getTitle());
            else
                course.setTitle(course.getTitle());
        }
        if (courseInfoDto.getDescription().isEmpty() == true
                || courseInfoDto.getDescription().isBlank() == true) {
            return new ResponseEntity("Course Description " +
                    "can not be empty", HttpStatus.BAD_REQUEST);
        } else {
            if (courseInfoDto.getDescription() != null)
                course.setDescription(courseInfoDto.getDescription());
            else
                course.setDescription(course.getDescription());
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

        boolean delete = utility.checkInstruction(course);
        if (delete == false)
            return new ResponseEntity("You are not allowed" +
                    " to delete this course", HttpStatus.BAD_REQUEST);

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
