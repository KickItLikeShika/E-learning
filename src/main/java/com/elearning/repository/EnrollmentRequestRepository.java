package com.elearning.repository;

import com.elearning.model.Course;
import com.elearning.model.EnrollmentRequest;
import com.elearning.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRequestRepository extends JpaRepository<EnrollmentRequest, Long> {

    EnrollmentRequest findEnrollmentRequestById(long id);
    List<EnrollmentRequest> findEnrollmentRequestsByUser(User user);
    List<EnrollmentRequest> findEnrollmentRequestsByCourse(Course course);
    List<EnrollmentRequest> findEnrollmentByStatus(String status);
}
