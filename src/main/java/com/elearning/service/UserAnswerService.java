package com.elearning.service;

import com.elearning.dto.UserAnswerDto;
import com.elearning.model.Assignment;
import com.elearning.model.Course;
import com.elearning.model.User;
import com.elearning.model.UserAnswer;
import com.elearning.repository.AssignmentRepository;
import com.elearning.repository.UserAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAnswerService {

    private UserAnswerRepository userAnswerRepository;
    private AssignmentRepository assignmentRepository;
    private AuthService authService;
    private Utility utility;

    @Autowired
    public UserAnswerService(UserAnswerRepository userAnswerRepository,
                             AssignmentRepository assignmentRepository,
                             AuthService authService,
                             Utility utility) {
        this.userAnswerRepository = userAnswerRepository;
        this.assignmentRepository = assignmentRepository;
        this.authService = authService;
        this.utility = utility;
    }

    public ResponseEntity submitAnswer(long id,
                                       UserAnswerDto userAnswerDto) {
        return mapSubmittingAnswer(id, userAnswerDto);
    }

    private ResponseEntity mapSubmittingAnswer(long id,
                                               UserAnswerDto userAnswerDto) {
        Assignment assignment = assignmentRepository.findAssignmentById(id);
        Course course = assignment.getCourse();
        User user = authService.getCurrUser();
        List<UserAnswer> userAnswers = assignment.getUserAnswers();


        boolean enrolled = utility.checkEnrollment(course);
        if (enrolled == false)
            return new ResponseEntity("You are not enrolled " +
                    "in this course", HttpStatus.BAD_REQUEST);

        boolean instruction = utility.checkInstruction(course);
        if (instruction == true)
            return new ResponseEntity("You are a instructor " +
                    "in this course, you can not submit answers",
                    HttpStatus.BAD_REQUEST);

        if (userAnswers.size() != 0)
            utility.checkUserAnswer(assignment, userAnswers, user);

        UserAnswer userAnswer = new UserAnswer();

        if (userAnswerDto.getAnswer().isEmpty()
                || userAnswerDto.getAnswer().isBlank()) {
            return new ResponseEntity("You have to type " +
                    "an answer", HttpStatus.BAD_REQUEST);
        } else {
            if (userAnswerDto.getAnswer() != null) {
                userAnswer.setAnswer(userAnswerDto.getAnswer());
            }
        }

        userAnswer.setAssignment(assignment);
        userAnswer.setUser(user);
        assignment.addUserAnswer(userAnswer);

        if (userAnswerDto.getAnswer().equals(assignment.getCorrectAnswer())) {
            userAnswer.setGrade("100");
        }
        if (userAnswerDto.getAnswer().equals(assignment.getCorrectAnswer()) == false) {
            userAnswer.setGrade("0");
        }

        userAnswerRepository.save(userAnswer);

        return new ResponseEntity("Your answer has been " +
                "submitted", HttpStatus.OK);
    }
}
