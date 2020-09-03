package com.elearning.controller;

import com.elearning.dto.AssignmentDto;
import com.elearning.dto.CourseInfoDto;
import com.elearning.dto.LessonDto;
import com.elearning.model.Course;
import com.elearning.repository.CourseRepository;
import com.elearning.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    private CourseRepository courseRepository;
    private CourseService courseService;

    @Autowired
    public CourseController(CourseRepository courseRepository,
                            CourseService courseService) {
        this.courseRepository = courseRepository;
        this.courseService = courseService;
    }

    // add course
    @RequestMapping(value = "/add-course", method = RequestMethod.POST)
    public ResponseEntity addCourseInfo(@RequestBody CourseInfoDto courseInfoDto) {
        return courseService.addCourseInfo(courseInfoDto);
    }

    // add lessons
    @RequestMapping(value = "/add-course-lesson/{id}", method = RequestMethod.POST)
    public ResponseEntity addCourseLessons(@PathVariable("id") long id,
                                           @RequestBody LessonDto lessonDto) {
        return courseService.addCourseLessons(id, lessonDto);
    }

    // add assignments
    @RequestMapping(value = "/add-course-assignment/{id}", method = RequestMethod.POST)
    public ResponseEntity addCourseAssignments(@PathVariable("id") long id,
                                               @RequestBody AssignmentDto assignmentDto) {
        return courseService.addCourseAssignments(id, assignmentDto);
    }

    // get all courses
    @RequestMapping(value = "get-all-courses", method = RequestMethod.GET)
    public ResponseEntity<List<Course>> getAllCourses() {
        return courseService.getAllCourses();
    }

    // get course by id
    @RequestMapping(value = "get-course/{id}", method = RequestMethod.GET)
    public ResponseEntity<Course> getCourseById(@PathVariable("id") long id) {
        return courseService.getCourseById(id);
    }

    // get courses by name

    // edit course
    // delete course

}
