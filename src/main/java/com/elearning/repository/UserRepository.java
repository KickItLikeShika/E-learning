package com.elearning.repository;

import com.elearning.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserById(long id);
    User findUserByEmail(String email);
    User findUserByUsername(String username);
}
