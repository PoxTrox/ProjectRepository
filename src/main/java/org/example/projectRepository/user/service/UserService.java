package org.example.projectRepository.user.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.projectRepository.exception.DomainException;
import org.example.projectRepository.security.AuthenticationDetails;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.user.model.UserRole;
import org.example.projectRepository.user.repository.UserRepository;
import org.example.projectRepository.web.dto.LoginRequest;
import org.example.projectRepository.web.dto.ProfileEditRequest;
import org.example.projectRepository.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService  implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
                .isActive(true)
                .build();

    }

    public User getById(UUID userId) {

        return userRepository.findById(userId).orElseThrow(() -> new DomainException("User with id [%s] does not exist.".formatted(userId)));
    }

    public void editProfileUser(UUID id, ProfileEditRequest profileEditRequest) {
        User byId = getById(id);
        byId.setFirstName(profileEditRequest.getFirstName());
        byId.setLastName(profileEditRequest.getLastName());
        byId.setEmail(profileEditRequest.getEmail());
        byId.setCountry(profileEditRequest.getCountry());
        byId.setAge(profileEditRequest.getAge());
        byId.setProfilePic(profileEditRequest.getProfilePicture());
        userRepository.save(byId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        User user = userRepository.findByUsername(username).orElseThrow(() -> new DomainException("User with name [%s] does not exist.".formatted(username)));

        return new AuthenticationDetails(user.getId(),user.getUsername(),user.getPassword(),user.getRole(),user.isActive());

    }
}
