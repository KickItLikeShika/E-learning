package com.elearning.repository;

import com.elearning.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    Enrollment findEnrollmentById(long id);
    List<Enrollment> findEnrollmentsByUserId(long userId);
    List<Enrollment> findEnrollmentsByCourseId(long courseId);
}
