package org.example.projectRepository.user.service;


import org.example.projectRepository.user.model.User;
import org.example.projectRepository.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminInit implements CommandLineRunner {


    private final UserService userService;

    @Autowired
    public AdminInit(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {

       if(!userService.getAllUsers().isEmpty()) {
           return;
       }

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("admin123")
                .password("admin123")
                .build();
       userService.register(registerRequest);
    }
}
