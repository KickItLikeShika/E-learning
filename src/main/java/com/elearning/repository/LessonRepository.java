package com.elearning.repository;

import com.elearning.model.Course;
import com.elearning.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    Lesson findLessonById(long id);
    List<Lesson> findLessonByCourse(Course course);
}
