package com.elearning.service;

import com.elearning.model.Course;
import com.elearning.model.Enrollment;
import com.elearning.model.EnrollmentRequest;
import com.elearning.model.User;
import com.elearning.repository.CourseRepository;
import com.elearning.repository.EnrollmentRepository;
import com.elearning.repository.EnrollmentRequestRepository;
import com.elearning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollService {

    private EnrollmentRequestRepository enrollmentRequestRepository;
    private EnrollmentRepository enrollmentRepository;
    private CourseRepository courseRepository;
    private AuthService authService;
    private UserRepository userRepository;
    private Utility utility;

    @Autowired
    public EnrollService(EnrollmentRequestRepository enrollmentRequestRepository,
                         EnrollmentRepository enrollmentRepository,
                         CourseRepository courseRepository,
                         AuthService authService,
                         UserRepository userRepository,
                         Utility utility) {
        this.enrollmentRepository = enrollmentRepository;
        this.enrollmentRequestRepository = enrollmentRequestRepository;
        this.courseRepository = courseRepository;
        this.authService = authService;
        this.userRepository = userRepository;
        this.utility = utility;
    }

    public ResponseEntity sendEnrollmentRequest(long id) {
        return mapEnrollmentRequestToInstructor(id);
    }

    private ResponseEntity mapEnrollmentRequestToInstructor(long id) {
        Course course = courseRepository.findCourseById(id);
        User user = authService.getCurrUser();
        EnrollmentRequest enrollmentRequest = new EnrollmentRequest();

        boolean enrolled = utility.checkEnrollmentRequest(id, user);
        if (enrolled == true) {
            return new ResponseEntity("You can not send an " +
                    "enrollment request to this course",
                    HttpStatus.BAD_REQUEST);
        }

        enrollmentRequest.setCourse(course);
        enrollmentRequest.setUser(user);
        enrollmentRequest.setStatus("PENDING");
        enrollmentRequestRepository.save(enrollmentRequest);

        course.addEnrollmentRequest(enrollmentRequest);
        courseRepository.save(course);

        user.addEnrollmentRequest(enrollmentRequest);
        userRepository.save(user);

        return new ResponseEntity("You have sent a " +
                "request to enroll in this course", HttpStatus.OK);
    }

    public ResponseEntity cancelEnrollmentRequest(long id) {
        return mapCancellingEnrollmentRequest(id);
    }

    private ResponseEntity mapCancellingEnrollmentRequest(long id) {
        EnrollmentRequest enrollmentRequest =
                enrollmentRequestRepository.findEnrollmentRequestById(id);
        if (enrollmentRequest.getStatus().equals("PENDING") == false) {
            return new ResponseEntity("You can not cancel" +
                    " this enrollment request", HttpStatus.BAD_REQUEST);
        }
        Course course = enrollmentRequest.getCourse();
        User user = enrollmentRequest.getUser();
        long userId = user.getId();
        if (authService.getCurrUser().getId() != userId) {
            return new ResponseEntity("You can not cancel" +
                    " this enrollment request", HttpStatus.BAD_REQUEST);
        }

        user.removeEnrollmentRequest(enrollmentRequest);
        course.removeEnrollmentRequest(enrollmentRequest);
        userRepository.save(user);
        courseRepository.save(course);

        enrollmentRequest.setUser(null);
        enrollmentRequest.setCourse(null);
        enrollmentRequestRepository.delete(enrollmentRequest);

        return new ResponseEntity("Your enrollment request to " +
                course.getTitle() + " has been canceled", HttpStatus.OK);
    }

    public ResponseEntity acceptEnrollmentRequest(long id) {
        return mapAcceptingEnrollmentRequest(id);
    }

    private ResponseEntity mapAcceptingEnrollmentRequest(long id) {
        EnrollmentRequest enrollmentRequest =
                enrollmentRequestRepository.findEnrollmentRequestById(id);
        Course course = enrollmentRequest.getCourse();

        boolean instruction = utility.checkInstruction(course);
        if (instruction == false) {
            return new ResponseEntity("You are not " +
                    "allowed to accept requests", HttpStatus.BAD_REQUEST);
        }

        Enrollment enrollment = new Enrollment();
        enrollmentRequest.setStatus("ACCEPTED");
        enrollment.setUserId(enrollmentRequest.getUser().getId());
        enrollment.setCourseId(course.getId());
        enrollmentRequestRepository.save(enrollmentRequest);
        enrollmentRepository.save(enrollment);

        return new ResponseEntity("This user is now" +
                " enrolled in this course", HttpStatus.OK);
    }

    public ResponseEntity rejectEnrollmentRequest(long id) {
        return mapRejectingEnrollmentRequest(id);
    }

    private ResponseEntity mapRejectingEnrollmentRequest(long id) {
        EnrollmentRequest enrollmentRequest =
                enrollmentRequestRepository.findEnrollmentRequestById(id);

        enrollmentRequest.setStatus("REJECTED");
        enrollmentRequestRepository.save(enrollmentRequest);

        return new ResponseEntity("You reject this " +
                "user", HttpStatus.OK);
    }
}
