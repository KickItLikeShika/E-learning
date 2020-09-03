package com.elearning.service;

import com.elearning.dto.LoginRequest;
import com.elearning.dto.RegisterRequest;
import com.elearning.model.Role;
import com.elearning.model.User;
import com.elearning.repository.UserRepository;
import com.elearning.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private JwtProvider jwtProvider;

    @Autowired
    public AuthService(PasswordEncoder passwordEncoder,
                       UserRepository userRepository,
                       AuthenticationManager authenticationManager,
                       JwtProvider jwtProvider) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    public ResponseEntity register(RegisterRequest registerRequest) {
        User user = mapUserForRegister(registerRequest);
        String validation = validateUser(user);
        if (validation == "NOT VALID") {
            return new ResponseEntity("SEE ANOTHER EMAIL OR USERNAME", HttpStatus.SEE_OTHER);
        } else {
            user.setEnabled(true);
            userRepository.save(user);
            return new ResponseEntity("Registered!", HttpStatus.OK);
        }
    }

    private User mapUserForRegister(RegisterRequest registerRequest) {
        User user = new User();
        user.setFirstname(registerRequest.getFirstname());
        user.setLastname(registerRequest.getLastname());
        user.setEmail(registerRequest.getEmail());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(this.encodePassword(registerRequest.getPassword()));
        user.setPhone(registerRequest.getPhone());
        user.addRoles(new Role("ADMIN"));
        return user;
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public String validateUser(User user) {
        String email = user.getEmail();
        String username = user.getUsername();
        if (userRepository.findUserByUsername(username) != null || userRepository.findUserByEmail(email) != null) {
            return "NOT VALID";
        } else {
            return "VALID";
        }
    }

    public ResponseEntity login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity(jwtProvider.generateToken(authentication), HttpStatus.OK);
    }

    public User getCurrUser() {
        org.springframework.security.core.userdetails.User principal =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder
                        .getContext().getAuthentication().getPrincipal();
        User user = userRepository.findUserByUsername(principal.getUsername());
        return user;
    }
}
