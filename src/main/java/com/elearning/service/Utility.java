package com.elearning.service;

import com.elearning.model.Course;
import com.elearning.model.Enrollment;
import com.elearning.model.Instruction;
import com.elearning.model.User;
import com.elearning.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class Utility {

    private CourseRepository courseRepository;
    private AssignmentRepository assignmentRepository;
    private LessonRepository lessonRepository;
    private InstructionRepository instructionRepository;
    private AuthService authService;
    private UserRepository userRepository;
    private EnrollmentRepository enrollmentRepository;
    private ReviewRepository reviewRepository;
    private EnrollmentRequestRepository enrollmentRequestRepository;

    @Autowired
    public Utility(CourseRepository courseRepository,
                         AssignmentRepository assignmentRepository,
                         LessonRepository lessonRepository,
                         InstructionRepository instructionRepository,
                         AuthService authService,
                         UserRepository userRepository,
                         EnrollmentRepository enrollmentRepository,
                         ReviewRepository reviewRepository,
                         EnrollmentRequestRepository enrollmentRequestRepository) {
        this.courseRepository = courseRepository;
        this.assignmentRepository = assignmentRepository;
        this.lessonRepository = lessonRepository;
        this.instructionRepository = instructionRepository;
        this.authService = authService;
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.reviewRepository = reviewRepository;
        this.enrollmentRequestRepository = enrollmentRequestRepository;
    }

    public boolean checkInstruction(Course course) {
        User user  = authService.getCurrUser();
        List<Instruction> instructions = instructionRepository.
                findInstructionsByCourseId(course.getId());
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

    public Course publishCourse(Course course) {
        if(course.getLessons().isEmpty() == false
                && course.getAssignments().isEmpty() == false) {
            course.setPublished("YES");
            course.setPublishedOn(Date.from(Instant.now()));
        }
        return course;
    }

    public Course checkEnrollment(Course course) {
        User user = authService.getCurrUser();

        List<Enrollment> enrollments = enrollmentRepository.
                findEnrollmentsByUserId(user.getId());
        if (enrollments.isEmpty() == true) {
            course.clearAssignments();
            course.clearLessons();
        }

        boolean enrolled = false;
        long id = course.getId();
        for (int i = 0; i < enrollments.size(); i++) {
            Enrollment enrollment = enrollments.get(i);
            long courseId = enrollment.getCourseId();
            if (id == courseId) {
                enrolled = true;
            }
        }
        if (enrolled == false) {
            course.clearLessons();
            course.clearAssignments();
            return course;
        }

        return course;
    }

    public boolean checkAbilityToManageCourse(long id) {
        // must be instructor
        User user = authService.getCurrUser();
        long currLoggedUserId = user.getId();

        List<Instruction> instructions = instructionRepository
                .findInstructionsByCourseId(id);
        for (int i = 0; i < instructions.size(); i++) {
            Instruction instruction = instructions.get(i);
            long instructorId = instruction.getUserId();
            if (currLoggedUserId == instructorId) {
                return true;
            }
        }
        return false;
    }

    public List<Course> checkPublish(List<Course> courses) {
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            if (course.getPublished().equals("NO")) {
                courses.remove(course);
            }
        }
        return courses;
    }

    public Course checkPublishAfterDeletion(Course course) {
        if (course.getAssignments().isEmpty() == true
                || course.getLessons().isEmpty() == true) {
            course.setPublished("NO");
        }
        return course;
    }
}
