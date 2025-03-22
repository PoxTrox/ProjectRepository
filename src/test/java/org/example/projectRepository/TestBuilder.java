package org.example.projectRepository;

import lombok.experimental.UtilityClass;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.user.model.UserRole;

import java.util.UUID;

@UtilityClass
public class TestBuilder {


    public static User aMockUser() {

        return User.builder()
                .id(UUID.randomUUID())
                .username("userTest")
                .password("passwordTest")
                .role(UserRole.USER)
                .isActive(true)
                .age(18).build();
    }
}
