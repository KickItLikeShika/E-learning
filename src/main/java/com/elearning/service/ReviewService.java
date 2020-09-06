package com.elearning.service;

import com.elearning.dto.ReviewDto;
import com.elearning.model.Course;
import com.elearning.model.Review;
import com.elearning.model.User;
import com.elearning.repository.CourseRepository;
import com.elearning.repository.ReviewRepository;
import com.elearning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class ReviewService {

    private ReviewRepository reviewRepository;
    private CourseRepository courseRepository;
    private UserRepository userRepository;
    private AuthService authService;
    private Utility utility;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository,
                         CourseRepository courseRepository,
                         UserRepository userRepository,
                         AuthService authService,
                         Utility utility) {
        this.reviewRepository = reviewRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.authService = authService;
        this.utility = utility;
    }

    public ResponseEntity addReview(long id, ReviewDto reviewDto) {
        return mapAddingReview(id, reviewDto);
    }

    private ResponseEntity mapAddingReview(long id, ReviewDto reviewDto) {
        Course course = courseRepository.findCourseById(id);
        User user = authService.getCurrUser();

        boolean enrolled = utility.checkEnrollment(course);
        if (enrolled == false) {
            return new ResponseEntity("You have to be enrolled " +
                    "to add a review", HttpStatus.BAD_REQUEST);
        }
        boolean instruction = utility.checkInstruction(course);
        if (instruction == true) {
            return new ResponseEntity("You are an instructor " +
                    "in this course, You can not add reviews!",
                    HttpStatus.BAD_REQUEST);
        }
        Review review = new Review();
        if (reviewDto.getFeedback().isBlank()
                || reviewDto.getFeedback().isEmpty()) {
            return new ResponseEntity("Your feedback " +
                    "can not be empty", HttpStatus.BAD_REQUEST);
        } else {
            if (reviewDto.getFeedback() != null) {
                review.setFeedback(reviewDto.getFeedback());
            }
        }
        int rank = reviewDto.getRank();
        if (rank > 5 || rank < 0) {
            return new ResponseEntity("You rank must be " +
                    "from 0 to 5", HttpStatus.BAD_REQUEST);
        }
        review.setRank(rank);
        review.setCreatedOn(Date.from(Instant.now()));

        review.setCourse(course);
        review.setUser(user);
        reviewRepository.save(review);

        course.addReview(review);
        courseRepository.save(course);

        return new ResponseEntity("Your review has " +
                "been added!", HttpStatus.OK);
    }

    public ResponseEntity editReview(long id, ReviewDto reviewDto) {
        return mapEditingReview(id, reviewDto);
    }

    private ResponseEntity mapEditingReview(long id, ReviewDto reviewDto) {
        Review review = reviewRepository.findReviewById(id);
        Course course = review.getCourse();
        User user = authService.getCurrUser();
        if (user.getId() != review.getUser().getId()) {
            return new ResponseEntity("You can not " +
                    "edit this review", HttpStatus.BAD_REQUEST);
        }

        course.removeReview(review);

        if (reviewDto.getFeedback().isBlank()
                || reviewDto.getFeedback().isEmpty()) {
            return new ResponseEntity("Your feedback " +
                    "can not be empty", HttpStatus.BAD_REQUEST);
        } else {
            if (reviewDto.getFeedback() != null) {
                review.setFeedback(reviewDto.getFeedback());
            }
        }
        int rank = reviewDto.getRank();
        if (rank > 5 || rank < 0) {
            return new ResponseEntity("You rank must be " +
                    "from 0 to 5", HttpStatus.BAD_REQUEST);
        }
        review.setRank(rank);

        course.addReview(review);
        reviewRepository.save(review);
        courseRepository.save(course);
        return new ResponseEntity("Your review has " +
                "been updated!", HttpStatus.OK);
    }

    public ResponseEntity deleteReview(long id) {
        return mapDeletingReview(id);
    }

    private ResponseEntity mapDeletingReview(long id) {
        Review review = reviewRepository.findReviewById(id);
        Course course = review.getCourse();
        User user = authService.getCurrUser();
        if (user.getId() != review.getUser().getId()) {
            return new ResponseEntity("You can not " +
                    "delete this review", HttpStatus.BAD_REQUEST);
        }
        course.removeReview(review);
        courseRepository.save(course);
        review.setCourse(null);
        review.setUser(null);
        reviewRepository.delete(review);
        return new ResponseEntity("You review has " +
                "been deleted!", HttpStatus.OK);
    }
}
