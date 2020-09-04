package com.elearning.service;

import com.elearning.dto.AssignmentDto;
import com.elearning.model.Assignment;
import com.elearning.model.Course;
import com.elearning.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AssignmentService {

    private CourseRepository courseRepository;
    private AssignmentRepository assignmentRepository;
    private AuthService authService;
    private UserRepository userRepository;
    private Utility utility;

    @Autowired
    public AssignmentService(CourseRepository courseRepository,
                         AssignmentRepository assignmentRepository,
                         AuthService authService,
                         UserRepository userRepository,
                         Utility utility) {
        this.courseRepository = courseRepository;
        this.assignmentRepository = assignmentRepository;
        this.authService = authService;
        this.userRepository = userRepository;
        this.utility = utility;
    }

    public ResponseEntity addCourseAssignments(long id,
                                               AssignmentDto assignmentDto) {
        return mapAddingCourseAssignment(id, assignmentDto);
    }

    private ResponseEntity mapAddingCourseAssignment(long id,
                                                     AssignmentDto assignmentDto) {
        Course course = courseRepository.findCourseById(id);

        boolean flag = utility.checkInstruction(course);
        if(flag == false) {
            return new ResponseEntity("You're not an " +
                    "instructor in this course, " +
                    "you can not add anything!", HttpStatus.OK);
        }

        Assignment assignment = new Assignment();
        assignment.setQuestion(assignmentDto.getQuestion());
        assignment.setCorrectAnswer(assignmentDto.
                getCorrectAnswer().toLowerCase());
        assignment.setCourse(course);
        course.addAssignments(assignment);
        assignmentRepository.save(assignment);

        course = utility.publishCourse(course);
        courseRepository.save(course);

        return new ResponseEntity("Assignment has been " +
                "added to this course -" +
                course.getTitle() + "-", HttpStatus.OK);
    }

    public ResponseEntity editCourseAssignment(long id,
                                               AssignmentDto assignmentDto) {
        return mapEditingCourseAssignment(id, assignmentDto);
    }

    private ResponseEntity mapEditingCourseAssignment(long id,
                                                      AssignmentDto assignmentDto) {
        Assignment assignment = assignmentRepository.findAssignmentById(id);
        Course course = assignment.getCourse();

        boolean edit = utility.checkAbilityToManageCourse(course.getId());
        if (edit == false) {
            return new ResponseEntity("You are not" +
                    "allowed to edit this course", HttpStatus.BAD_REQUEST);
        }

        course.removeAssignments(assignment);

        if (assignmentDto.getQuestion().isEmpty() == true
                || assignmentDto.getQuestion().isBlank() == true) {
            return new ResponseEntity("Question can" +
                    " not be empty", HttpStatus.BAD_REQUEST);
        } else {
            if (assignmentDto.getQuestion() != null) {
                assignment.setQuestion(assignmentDto.getQuestion());
            }
        }
        if (assignmentDto.getCorrectAnswer().isEmpty() == true
                || assignmentDto.getCorrectAnswer().isBlank() == true) {
            return new ResponseEntity("Correct answer can" +
                    " not be empty", HttpStatus.BAD_REQUEST);
        } else {
            if (assignmentDto.getCorrectAnswer() != null) {
                assignment.setCorrectAnswer(assignmentDto.getCorrectAnswer());
            }
        }

        course.addAssignments(assignment);
        assignmentRepository.save(assignment);
        courseRepository.save(course);

        return new ResponseEntity("Assignment has" +
                "been edited", HttpStatus.OK);
    }

    public ResponseEntity deleteCourseAssignment(long id) {
        return mapDeletingCourseAssignment(id);
    }

    private ResponseEntity mapDeletingCourseAssignment(long id) {
        Assignment assignment = assignmentRepository.findAssignmentById(id);
        Course course = assignment.getCourse();
        boolean delete = utility.checkAbilityToManageCourse(course.getId());
        if (delete == false) {
            return new ResponseEntity("You are not " +
                    "allowed to delete this assignment", HttpStatus.BAD_REQUEST);
        }
        course.removeAssignments(assignment);
        assignment.setCourse(null);
        courseRepository.save(course);
        assignmentRepository.delete(assignment);
        course = utility.checkPublishAfterDeletion(course);
        courseRepository.save(course);
        return new ResponseEntity("Assignment has been " +
                "deleted", HttpStatus.OK);
    }
}
