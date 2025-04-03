package org.example.projectRepository.IntergraionTests;

import org.example.projectRepository.user.model.User;
import org.example.projectRepository.user.model.UserRole;
import org.example.projectRepository.user.repository.UserRepository;
import org.example.projectRepository.user.service.UserService;
import org.example.projectRepository.web.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class RegisterITest {


    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @Test
    void testRegister_happyPath() {

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("testB")
                .password("password")
                .agreeTermOfUse(true)
                .build();

        User registerUser = userService.register(registerRequest);

        assertEquals("testB", registerUser.getUsername());
        assertTrue(registerUser.isActive());

        User userFromDb = userRepository.findByUsername("testB").orElseThrow();
        assertEquals(registerUser.getId(), userFromDb.getId());
        assertEquals(UserRole.USER, userFromDb.getRole());
    }
}
