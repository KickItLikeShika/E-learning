package com.elearning.repository;

import com.elearning.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Course findCourseById(long id);
    List<Course> findCoursesByTitle(String title);
}
