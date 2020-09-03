package com.elearning.service;

import com.elearning.dto.AssignmentDto;
import com.elearning.dto.CourseInfoDto;
import com.elearning.dto.LessonDto;
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

    @Autowired
    public CourseService(CourseRepository courseRepository,
                         AssignmentRepository assignmentRepository,
                         LessonRepository lessonRepository,
                         InstructionRepository instructionRepository,
                         AuthService authService,
                         UserRepository userRepository,
                         EnrollmentRepository enrollmentRepository) {
        this.courseRepository = courseRepository;
        this.assignmentRepository = assignmentRepository;
        this.lessonRepository = lessonRepository;
        this.instructionRepository = instructionRepository;
        this.authService = authService;
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
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

        return new ResponseEntity("Course info added but the course is not published yet, " +
                "you have to add the lessons and the assignments", HttpStatus.OK);
    }

    public ResponseEntity addCourseLessons(long id, LessonDto lessonDto) {
        return mapAddingCourseLessons(id, lessonDto);
    }

    private ResponseEntity mapAddingCourseLessons(long id, LessonDto lessonDto) {
        Course course = courseRepository.findCourseById(id);

        boolean flag = checkInstruction(course);
        if(flag == false) {
            return new ResponseEntity("You're not an instructor in this course, " +
                    "you can not add anything!", HttpStatus.OK);
        }

        Lesson lesson = new Lesson();
        lesson.setReading(lessonDto.getReading());
        lesson.setCourse(course);
        course.addLessons(lesson);
        lessonRepository.save(lesson);

        course = checkPublish(course);
        courseRepository.save(course);

        return new ResponseEntity("Lesson added to this course -" +
                course.getTitle() + "-", HttpStatus.OK);
    }

    public ResponseEntity addCourseAssignments(long id, AssignmentDto assignmentDto) {
        return mapAddingCourseAssignment(id, assignmentDto);
    }

    private ResponseEntity mapAddingCourseAssignment(long id, AssignmentDto assignmentDto) {
        Course course = courseRepository.findCourseById(id);

        boolean flag = checkInstruction(course);
        if(flag == false) {
            return new ResponseEntity("You're not an instructor in this course, " +
                    "you can not add anything!", HttpStatus.OK);
        }

        Assignment assignment = new Assignment();
        assignment.setQuestion(assignmentDto.getQuestion());
        assignment.setCorrectAnswer(assignmentDto.getCorrectAnswer().toLowerCase());
        assignment.setCourse(course);
        course.addAssignments(assignment);
        assignmentRepository.save(assignment);

        course = checkPublish(course);
        courseRepository.save(course);

        return new ResponseEntity("Assignment has been added to this course -" +
                course.getTitle() + "-", HttpStatus.OK);
    }

    private boolean checkInstruction(Course course) {
        User user  = authService.getCurrUser();
        List<Instruction> instructions = instructionRepository.findInstructionsByCourseId(course.getId());
        boolean flag = false;
        long userId = user.getId();
        for (int i = 0; i < instructions.size(); i++) {
            Instruction instruction = instructions.get(i);
            if(instruction.getUserId() == userId) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    private Course checkPublish(Course course) {
        if(course.getLessons().isEmpty() == false && course.getAssignments().isEmpty() == false) {
            course.setPublished("YES");
            course.setPublishedOn(Date.from(Instant.now()));
        }
        return course;
    }

    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        for(int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            course.clearEnrollmentRequests();
            if(course.getPublished().equals("NO")) {
                courses.remove(course);
            }
        }
        return new ResponseEntity(courses, HttpStatus.OK);
    }

    public ResponseEntity<Course> getCourseById(long id){
        Course course = courseRepository.findCourseById(id);
        course.clearEnrollmentRequests();
        if (course.getPublished().equals("NO")) {
            return new ResponseEntity<Course>(HttpStatus.OK);
        }
        User user = authService.getCurrUser();
        List<Enrollment> enrollments = enrollmentRepository.findEnrollmentsByUserId(user.getId());
        if (enrollments.isEmpty() == true) {
            course.clearAssignments();
            course.clearLessons();
        }
        for(int i = 0; i < enrollments.size(); i++) {
            Enrollment enrollment = enrollments.get(i);
            long courseId = enrollment.getCourseId();
            if (courseId != id) {
                course.clearAssignments();
                course.clearLessons();
                return new ResponseEntity(course, HttpStatus.OK);
            }
        }
        return new ResponseEntity(course, HttpStatus.OK);
    }
}
