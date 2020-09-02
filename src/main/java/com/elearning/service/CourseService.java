package com.elearning.service;

import com.elearning.dto.AssignmentDto;
import com.elearning.dto.CourseInfoDto;
import com.elearning.dto.LessonDto;
import com.elearning.model.Assignment;
import com.elearning.model.Course;
import com.elearning.model.Lesson;
import com.elearning.repository.AssignmentRepository;
import com.elearning.repository.CourseRepository;
import com.elearning.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class CourseService {

    private CourseRepository courseRepository;
    private AssignmentRepository assignmentRepository;
    private LessonRepository lessonRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository,
                         AssignmentRepository assignmentRepository,
                         LessonRepository lessonRepository) {
        this.courseRepository = courseRepository;
        this.assignmentRepository = assignmentRepository;
        this.lessonRepository = lessonRepository;
    }

    public ResponseEntity addCourseInfo(CourseInfoDto courseInfoDto) {
        return mapAddingCourseInfo(courseInfoDto);
    }

    private ResponseEntity mapAddingCourseInfo(CourseInfoDto courseInfoDto) {
        Course course = new Course();
        course.setTitle(courseInfoDto.getTitle());
        course.setDescription(courseInfoDto.getDescription());
        course.setPublished("NO");
        course.setPublishedOn(Date.from(Instant.now()));
        return new ResponseEntity("Course info added but the course is not published yet, " +
                "you have to add the lessons and the assignments", HttpStatus.OK);
    }

    public ResponseEntity addCourseLessons(long id, LessonDto lessonDto) {
        return mapAddingCourseLessons(id, lessonDto);
    }

    private ResponseEntity mapAddingCourseLessons(long id, LessonDto lessonDto) {
        Course course = courseRepository.findCourseById(id);
        Lesson lesson = new Lesson();
        lesson.setReading(lessonDto.getReading());
        lesson.setCourse(course);
        course.addLessons(lesson);
        lessonRepository.save(lesson);
        return new ResponseEntity("Lesson added to this course -" +
                course.getTitle()+"-", HttpStatus.OK);
    }

    public ResponseEntity addCourseAssignments(long id, AssignmentDto assignmentDto) {
        return mapAddingCourseAssignment(id, assignmentDto);
    }

    private ResponseEntity mapAddingCourseAssignment(long id, AssignmentDto assignmentDto) {
        Course course = courseRepository.findCourseById(id);
        Assignment assignment = new Assignment();
        assignment.setQuestion(assignmentDto.getQuestion());
        assignment.setCorrectAnswer(assignmentDto.getCorrectAnswer().toLowerCase());
        assignment.setCourse(course);
        course.addAssignments(assignment);
        assignmentRepository.save(assignment);
        return new ResponseEntity("Assignment has been added to this course -" +
                course.getTitle() + "-", HttpStatus.OK);
    }
}
