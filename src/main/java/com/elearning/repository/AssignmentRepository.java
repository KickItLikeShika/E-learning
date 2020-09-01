package com.elearning.repository;

import com.elearning.model.Assignment;
import com.elearning.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    Assignment findAssignmentById(long id);
    List<Assignment> findAssignmentByCourse(Course course);
}
