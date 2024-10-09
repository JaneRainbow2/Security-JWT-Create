package com.softserve.itacademy.todolist.security;

import com.softserve.itacademy.todolist.model.User;
import com.softserve.itacademy.todolist.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceDetails implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceDetails(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Trying to load user by email: {}", email);

        UserDetails  userDetails =userRepository.findByEmail(email);
        if (userDetails == null) {
            log.warn("User with email {} not found", email);
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        log.info("User with email {} found", email);
        return userDetails;
    }

        public UserDetailsService userDetailsService() {
        return this;
    }
}