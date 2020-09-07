package com.elearning.service;

import com.elearning.model.*;
import com.elearning.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private UserAnswerRepository userAnswerRepository;

    @Autowired
    public Utility(CourseRepository courseRepository,
                         AssignmentRepository assignmentRepository,
                         LessonRepository lessonRepository,
                         InstructionRepository instructionRepository,
                         AuthService authService,
                         UserRepository userRepository,
                         EnrollmentRepository enrollmentRepository,
                         ReviewRepository reviewRepository,
                         EnrollmentRequestRepository enrollmentRequestRepository,
                         UserAnswerRepository userAnswerRepository) {
        this.courseRepository = courseRepository;
        this.assignmentRepository = assignmentRepository;
        this.lessonRepository = lessonRepository;
        this.instructionRepository = instructionRepository;
        this.authService = authService;
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.reviewRepository = reviewRepository;
        this.enrollmentRequestRepository = enrollmentRequestRepository;
        this.userAnswerRepository = userAnswerRepository;
    }

    public boolean checkInstruction(Course course) {
        User user  = authService.getCurrUser();
        List<Instruction> instructions = instructionRepository.
                findInstructionsByCourseId(course.getId());
        long userId = user.getId();
        for (int i = 0; i < instructions.size(); i++) {
            Instruction instruction = instructions.get(i);
            if(instruction.getUserId() == userId) {
                return true;
            }
        }
        return false;
    }

    public boolean checkEnrollment(Course course) {
        User user = authService.getCurrUser();

        List<Enrollment> enrollments = enrollmentRepository.
                findEnrollmentsByUserId(user.getId());
        long id = course.getId();
        for (int i = 0; i < enrollments.size(); i++) {
            Enrollment enrollment = enrollments.get(i);
            long courseId = enrollment.getCourseId();
            if (id == courseId) {
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


    public boolean checkEnrollmentRequest(long id, User user) {
        List<EnrollmentRequest> enrollmentRequests =
                enrollmentRequestRepository.findEnrollmentRequestsByUser(user);
        for(int i = 0; i < enrollmentRequests.size(); i++) {
            EnrollmentRequest enrollmentRequest1 = enrollmentRequests.get(i);
            Course course1 = enrollmentRequest1.getCourse();
            long course1Id = course1.getId();
            if (id == course1Id
                    && ((enrollmentRequest1.getStatus().equals("PENDING"))
                    || (enrollmentRequest1.getStatus().equals("ACCEPTED")))) {
                return true;
            }
        }

        List<Enrollment> enrollments = enrollmentRepository
                .findEnrollmentsByUserId(user.getId());
        for (int i = 0; i < enrollments.size(); i++) {
            Enrollment enrollment = enrollments.get(i);
            long courseId = enrollment.getCourseId();
            if (id == courseId) {
                return true;
            }
        }
        return false;
    }

    public void checkUserAnswer(Assignment assignment,
                                 List<UserAnswer> userAnswers,
                                 User user) {
        for (int i = 0; i < userAnswers.size(); i++) {
            UserAnswer userAnswerr = userAnswers.get(i);
            User user1 = userAnswerr.getUser();
            if (user.getId() == user1.getId()) {
                assignment.removeUserAnswer(userAnswerr);
                userAnswerr.setUser(null);
                userAnswerr.setAssignment(null);
                userAnswerRepository.delete(userAnswerr);
            }
        }
    }
}
