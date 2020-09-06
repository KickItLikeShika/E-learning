package com.elearning.controller;

import com.elearning.dto.ReviewDto;
import com.elearning.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")
public class ReviewController {

    private ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @RequestMapping(value = "/add-review/{id}", method = RequestMethod.POST)
    public ResponseEntity addReview(@PathVariable("id") long id,
                                    @RequestBody ReviewDto reviewDto) { // course id
        return reviewService.addReview(id, reviewDto);
    }

    @RequestMapping(value = "/edit-review/{id}", method = RequestMethod.PUT)
    public ResponseEntity editReview(@PathVariable("id") long id, // review id
                                     @RequestBody ReviewDto reviewDto) {
        return reviewService.editReview(id, reviewDto);
    }

    @RequestMapping(value = "/delete-review/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteReview(@PathVariable("id") long id) { // review id
        return reviewService.deleteReview(id);
    }
}
