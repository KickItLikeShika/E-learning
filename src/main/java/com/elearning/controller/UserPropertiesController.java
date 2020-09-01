package com.elearning.controller;

import com.elearning.dto.RegisterRequest;
import com.elearning.service.UserPropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/edit")
public class UserPropertiesController {

    private UserPropertiesService userPropertiesService;

    @Autowired
    public UserPropertiesController(UserPropertiesService userPropertiesService) {
        this.userPropertiesService = userPropertiesService;
    }

    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    public ResponseEntity editUser(@RequestBody RegisterRequest registerRequest) {
        return userPropertiesService.editUserInfo(registerRequest);
    }

}
