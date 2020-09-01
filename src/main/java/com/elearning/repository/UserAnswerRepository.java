package com.elearning.repository;

import com.elearning.model.Assignment;
import com.elearning.model.Course;
import com.elearning.model.User;
import com.elearning.model.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {

    UserAnswer findUserAnswerById(long id);
    List<UserAnswer> findUserAnswerByUser(User user);
    List<UserAnswer> findUserAnswerByAssignment(Assignment assignment);
}
