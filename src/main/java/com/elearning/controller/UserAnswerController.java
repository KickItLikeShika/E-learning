package com.elearning.controller;

import com.elearning.dto.UserAnswerDto;
import com.elearning.service.UserAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/answer")
public class UserAnswerController {

    private UserAnswerService userAnswerService;

    @Autowired
    public UserAnswerController(UserAnswerService userAnswerService) {
        this.userAnswerService = userAnswerService;
    }

    @RequestMapping(value = "/submit-answer/{id}", method = RequestMethod.POST)
    public ResponseEntity submitAnswer(@PathVariable("id") long id, // assignment id
                                       @RequestBody UserAnswerDto userAnswerDto) {
        return userAnswerService.submitAnswer(id, userAnswerDto);
    }

}
