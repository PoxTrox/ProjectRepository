package org.example.projectRepository.web.mapper;

import lombok.experimental.UtilityClass;
import org.example.projectRepository.media.model.Media;
import org.example.projectRepository.media.model.MediaType;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.web.dto.ProfileEditRequest;
import org.example.projectRepository.web.dto.RestMediaRequest;

import java.time.LocalDate;

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

   public static Media mapToMedia(RestMediaRequest restMediaRequest) {
        return Media.builder()
                .title(restMediaRequest.getTitle())
                .releaseDate(LocalDate.parse(restMediaRequest.getReleaseDate()))
                .mediaType(MediaType.MOVIE)
                .season(1).build();
   }
}
