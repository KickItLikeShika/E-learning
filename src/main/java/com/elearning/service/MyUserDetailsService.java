package com.elearning.service;

import com.elearning.model.Role;
import com.elearning.model.User;
import com.elearning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("USER NOT FOUND");
        }
        return buildUserForAuthentication(user);
    }

    private UserDetails buildUserForAuthentication(User user) {
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), user.isEnabled(), user.isEnabled(), true,
                true, getAuthorities(user));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        Set<Role> roles = user.getRoles();
        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
        for(Role role : roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return grantedAuthorities;
    }

    private Collection<? extends GrantedAuthority> getAuthorities2(String role_user) {
        return Collections.singleton(new SimpleGrantedAuthority(role_user));
    }
}
