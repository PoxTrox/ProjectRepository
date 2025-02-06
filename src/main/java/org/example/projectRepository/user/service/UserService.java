package org.example.projectRepository.user.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.projectRepository.exception.DomainException;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.user.model.UserRole;
import org.example.projectRepository.user.repository.UserRepository;
import org.example.projectRepository.web.dto.LoginRequest;
import org.example.projectRepository.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public User login(LoginRequest loginRequest) {

        Optional<User> byUsername = userRepository.findByUsername(loginRequest.getUsername());

        if (byUsername.isEmpty()) {
            throw new DomainException("Username or password are incorrect.");
        }

        User user = byUsername.get();
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new DomainException("Username or password are incorrect.");
        }


        return user;
    }

    @Transactional
    public User register(RegisterRequest registerRequest) {

        Optional<User> user = userRepository.findByUsername(registerRequest.getUsername());

        if (user.isPresent()) {

            throw new DomainException("Username is already in use.( %s )".formatted(user.get().getUsername()));

        }
        User newUSer = userRepository.save(initializeUser(registerRequest));


        log.info("User {%s} registered.".formatted(newUSer.getUsername()));

        return newUSer;

    }

    private User initializeUser(RegisterRequest registerRequest) {

        return User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(UserRole.USER)
                .build();

    }

    public User getById(UUID userId) {

        return userRepository.findById(userId).orElseThrow(() -> new DomainException("User with id [%s] does not exist.".formatted(userId)));
    }
}
