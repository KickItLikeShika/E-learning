package com.elearning.controller;

import com.elearning.dto.AssignmentDto;
import com.elearning.dto.CourseInfoDto;
import com.elearning.dto.LessonDto;
import com.elearning.repository.CourseRepository;
import com.elearning.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    // edit course
    // delete course
    // get course by id
    // get courses by name
}
