package com.elearning.controller;

import com.elearning.dto.AssignmentDto;
import com.elearning.dto.CourseInfoDto;
import com.elearning.dto.LessonDto;
import com.elearning.model.Course;
import com.elearning.repository.CourseRepository;
import com.elearning.service.AssignmentService;
import com.elearning.service.CourseService;
import com.elearning.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    private CourseRepository courseRepository;
    private CourseService courseService;
    private LessonService lessonService;
    private AssignmentService assignmentService;

    @Autowired
    public CourseController(CourseRepository courseRepository,
                            CourseService courseService,
                            LessonService lessonService,
                            AssignmentService assignmentService) {
        this.courseRepository = courseRepository;
        this.courseService = courseService;
        this.lessonService = lessonService;
        this.assignmentService = assignmentService;
    }

    @RequestMapping(value = "/add-course", method = RequestMethod.POST)
    public ResponseEntity addCourseInfo(@RequestBody CourseInfoDto courseInfoDto) {
        return courseService.addCourseInfo(courseInfoDto);
    }

    @RequestMapping(value = "/add-course-lesson/{id}", method = RequestMethod.POST)
    public ResponseEntity addCourseLessons(@PathVariable("id") long id,
                                           @RequestBody LessonDto lessonDto) {
        return lessonService.addCourseLessons(id, lessonDto);
    }

    @RequestMapping(value = "/add-course-assignment/{id}", method = RequestMethod.POST)
    public ResponseEntity addCourseAssignments(@PathVariable("id") long id,
                                               @RequestBody AssignmentDto assignmentDto) {
        return assignmentService.addCourseAssignments(id, assignmentDto);
    }

    @RequestMapping(value = "/get-all-courses", method = RequestMethod.GET)
    public ResponseEntity<List<Course>> getAllCourses() {
        return courseService.getAllCourses();
    }

    @RequestMapping(value = "/get-course-id/{id}", method = RequestMethod.GET)
    public ResponseEntity<Course> getCourseById(@PathVariable("id") long id) {
        return courseService.getCourseById(id);
    }

    @RequestMapping(value = "/get-course-name/{name}", method = RequestMethod.GET)
    public ResponseEntity<List<Course>> getCoursesByName(@PathVariable("name") String name) {
        return courseService.getCoursesByName(name);
    }

    @RequestMapping(value = "/edit-course-info/{id}", method = RequestMethod.PUT)
    public ResponseEntity editCourseInfo(@PathVariable("id") long id,
                                     @RequestBody CourseInfoDto courseInfoDto) {
        return courseService.editCourseInfo(id, courseInfoDto);
    }

    @RequestMapping(value = "/edit-course-lesson/{id}", method = RequestMethod.PUT)
    public ResponseEntity editCourseLesson(@PathVariable("id") long id, // lesson id
                                           @RequestBody LessonDto lessonDto) {
        return lessonService.editCourseLesson(id, lessonDto);
    }

    @RequestMapping(value = "/edit-course-assignment/{id}", method = RequestMethod.PUT)
    public ResponseEntity editCourseAssignment(@PathVariable("id") long id, // assignment id
                                               @RequestBody AssignmentDto assignmentDto) {
        return assignmentService.editCourseAssignment(id, assignmentDto);
    }

    @RequestMapping(value = "/delete-course/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteCourse(@PathVariable("id") long id) {
        return courseService.deleteCourse(id);
    }

    @RequestMapping(value = "/delete-course-lesson/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteCourseLesson(@PathVariable("id") long id) { // lesson id
        return lessonService.deleteCourseLesson(id);
    }

    // delete assignment
    @RequestMapping(value = "/delete-course-assignment/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteCourseAssignment(@PathVariable("id") long id) { // assignment id
        return assignmentService.deleteCourseAssignment(id);
    }
}
