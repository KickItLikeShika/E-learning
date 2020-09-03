package com.elearning.service;

import com.elearning.dto.RegisterRequest;
import com.elearning.model.Role;
import com.elearning.model.User;
import com.elearning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserPropertiesService {

    private UserRepository userRepository;
    private AuthService authService;

    @Autowired
    public UserPropertiesService(UserRepository userRepository,
                                 AuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
    }

    public ResponseEntity editUserInfo(RegisterRequest registerRequest) {
        return mapEditingUser(registerRequest);
    }

    private ResponseEntity mapEditingUser(RegisterRequest registerRequest) {
        User user = authService.getCurrUser();

        if (registerRequest.getFirstname().isEmpty() == true || registerRequest.getFirstname().isBlank() == true) {
            return new ResponseEntity("Firstname can not be blank", HttpStatus.BAD_REQUEST);
        } else {
            if (registerRequest.getFirstname() != null) {
                user.setFirstname(registerRequest.getFirstname());
            }
        }
        if (registerRequest.getLastname().isEmpty() == true || registerRequest.getLastname().isBlank() == true) {
            return new ResponseEntity("Lastname can not be blank", HttpStatus.BAD_REQUEST);
        } else {
            if (registerRequest.getLastname() != null) {
                user.setLastname(registerRequest.getLastname());
            }
        }
        if (registerRequest.getPhone().isEmpty() == true || registerRequest.getLastname().isBlank() == true) {
            return new ResponseEntity("Phone number can not be blank", HttpStatus.BAD_REQUEST);
        } else {
            if (registerRequest.getPhone() != null) {
                user.setPhone(registerRequest.getPhone());
            }
        }
        if (registerRequest.getEmail().isEmpty() == true || registerRequest.getEmail().isBlank() == true) {
            return new ResponseEntity("Email can not be blank", HttpStatus.BAD_REQUEST);
        } else {
            String newEmail = registerRequest.getEmail();
            String oldEmail = user.getEmail();
            if (newEmail.equals(oldEmail)) {
                user.setEmail(registerRequest.getEmail());
            } else {
                User tempUser = userRepository.findUserByEmail(newEmail);
                if (tempUser == null) {
                    user.setEmail(registerRequest.getEmail());
                } else {
                    return new ResponseEntity("This email is already taken", HttpStatus.BAD_REQUEST);
                }
            }
        }
        if (registerRequest.getUsername().isEmpty() == true || registerRequest.getUsername().isBlank() == true) {
            return new ResponseEntity("Username can not be blank", HttpStatus.BAD_REQUEST);
        } else {
            String newUsername = registerRequest.getUsername();
            String oldUsername = user.getUsername();
            if (newUsername.equals(oldUsername)) {
                user.setUsername(registerRequest.getUsername());
            } else {
                User tempUser = userRepository.findUserByUsername(newUsername);
                if (tempUser == null) {
                    user.setUsername(registerRequest.getUsername());
                } else {
                    return new ResponseEntity("This username is already taken", HttpStatus.BAD_REQUEST);
                }
            }
        }
        if (registerRequest.getPassword().isEmpty() == true || registerRequest.getPassword().isBlank() == true) {
            return new ResponseEntity("Password can not be blank", HttpStatus.BAD_REQUEST);
        } else {
            if (registerRequest.getPassword() != null) {
                user.setPassword(authService.encodePassword(registerRequest.getPassword()));
            }
        }
        if (user.getRoles().isEmpty() == true) {
            user.addRoles(new Role("ADMIN"));
        }
        user.setEnabled(true);
        userRepository.save(user);
        return new ResponseEntity("User has been updated!", HttpStatus.OK);
    }

}
