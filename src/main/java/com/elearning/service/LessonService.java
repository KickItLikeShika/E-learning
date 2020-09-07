package com.elearning.service;

import com.elearning.dto.LessonDto;
import com.elearning.model.Course;
import com.elearning.model.Lesson;
import com.elearning.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LessonService {

    private CourseRepository courseRepository;
    private LessonRepository lessonRepository;
    private AuthService authService;
    private UserRepository userRepository;
    private Utility utility;

    @Autowired
    public LessonService(CourseRepository courseRepository,
                         LessonRepository lessonRepository,
                         AuthService authService,
                         UserRepository userRepository,
                         Utility utility) {
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
        this.authService = authService;
        this.userRepository = userRepository;
        this.utility = utility;
    }

    public ResponseEntity addCourseLessons(long id,
                                           LessonDto lessonDto) {
        return mapAddingCourseLessons(id, lessonDto);
    }

    private ResponseEntity mapAddingCourseLessons(long id,
                                                  LessonDto lessonDto) {
        Course course = courseRepository.findCourseById(id);

        boolean flag = utility.checkInstruction(course);
        if(flag == false)
            return new ResponseEntity("You're not an " +
                    "instructor in this course, " +
                    "you can not add anything!", HttpStatus.OK);

        Lesson lesson = new Lesson();
        lesson.setReading(lessonDto.getReading());
        lesson.setCourse(course);
        course.addLessons(lesson);
        lessonRepository.save(lesson);

        courseRepository.save(course);

        return new ResponseEntity("Lesson added to " +
                "this course -" +
                course.getTitle() + "-", HttpStatus.OK);
    }

    public ResponseEntity editCourseLesson(long id,
                                           LessonDto lessonDto) {
        return mapEditingCourseLesson(id, lessonDto);
    }

    private ResponseEntity mapEditingCourseLesson(long id,
                                                  LessonDto lessonDto) {
        Lesson lesson = lessonRepository.findLessonById(id);
        Course course = lesson.getCourse();

        boolean edit = utility.checkInstruction(course);
        if (edit == false)
            return new ResponseEntity("You are not" +
                    "allowed to edit this course", HttpStatus.BAD_REQUEST);

        course.removeLessons(lesson);

        if (lessonDto.getReading().isEmpty() == true
                || lessonDto.getReading().isBlank() == true) {
            return new ResponseEntity("Lesson can not " +
                    "be empty", HttpStatus.BAD_REQUEST);
        } else {
            if (lessonDto.getReading() != null)
                lesson.setReading(lessonDto.getReading());
            else
                lesson.setReading(lesson.getReading());
        }

        course.addLessons(lesson);
        lessonRepository.save(lesson);
        courseRepository.save(course);

        return new ResponseEntity("Lesson has " +
                "been edited!", HttpStatus.OK);
    }

    public ResponseEntity deleteCourseLesson(long id) {
        return mapDeletingCourseLesson(id);
    }

    private ResponseEntity mapDeletingCourseLesson(long id) {
        Lesson lesson = lessonRepository.findLessonById(id);
        Course course = lesson.getCourse();
        boolean delete = utility.checkInstruction(course);
        if (delete == false)
            return new ResponseEntity("You are not " +
                    "allowed to delete this lesson", HttpStatus.BAD_REQUEST);

        course.removeLessons(lesson);
        lesson.setCourse(null);
        courseRepository.save(course);
        lessonRepository.delete(lesson);
        courseRepository.save(course);
        return new ResponseEntity("Lesson has been" +
                " deleted", HttpStatus.OK);
    }

}
