package com.elearning.controller;

import com.elearning.service.EnrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enroll")
public class EnrollController {

    private EnrollService enrollService;

    @Autowired
    public EnrollController(EnrollService enrollService) {
        this.enrollService = enrollService;
    }

    @RequestMapping(value = "/send-enrollment-request/{id}", method = RequestMethod.POST)
    public ResponseEntity sendEnrollmentRequest(@PathVariable("id") long id) { // course id
        return enrollService.sendEnrollmentRequest(id);
    }

    @RequestMapping(value = "/cancel-enrollment-request/{id}", method = RequestMethod.DELETE)
    public ResponseEntity cancelEnrollmentRequest(@PathVariable("id") long id) { // enrollment request id
        return enrollService.cancelEnrollmentRequest(id);
    }

    @RequestMapping(value = "/accept-enrollment-request/{id}", method = RequestMethod.POST)
    public ResponseEntity acceptEnrollmentRequest(@PathVariable("id") long id) { // enrollment request id
        return enrollService.acceptEnrollmentRequest(id);
    }

    @RequestMapping(value = "/reject-enrollment-request/{id}", method = RequestMethod.POST)
    public ResponseEntity rejectEnrollmentRequest(@PathVariable("id") long id){ // enrollment request id
        return enrollService.rejectEnrollmentRequest(id);
    }
}
