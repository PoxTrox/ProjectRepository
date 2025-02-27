package org.example.projectRepository.web.mapper;

import lombok.experimental.UtilityClass;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.web.dto.ProfileEditRequest;

@UtilityClass
public class DtoMapper {

    public static ProfileEditRequest mapToProfileEditRequest(User user ) {

        return ProfileEditRequest.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .age(user.getAge())
                .email(user.getEmail())
                .profilePicture(user.getProfilePic())
                .build();
    }
}
