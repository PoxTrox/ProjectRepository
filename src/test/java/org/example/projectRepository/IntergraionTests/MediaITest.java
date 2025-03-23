package org.example.projectRepository.IntergraionTests;

import jakarta.transaction.Transactional;
import org.example.projectRepository.media.Service.MediaService;
import org.example.projectRepository.media.model.Media;
import org.example.projectRepository.media.model.MediaType;
import org.example.projectRepository.search.dto.RestMediaResponse;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.user.repository.UserRepository;
import org.example.projectRepository.user.service.UserService;
import org.example.projectRepository.web.dto.RegisterRequest;
import org.example.projectRepository.web.dto.RestMediaRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class MediaITest {

    @Autowired
    private MediaService mediaService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;



    @Test
            void TryingToSaveMediaFromRestapi() throws Exception {

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("testA")
                .password("password")
                .agreeTermOfUse(true)
                .build();

        User registerUser = userService.register(registerRequest);

        Media media = Media.builder()
                .title("testA")
                .genre("asd")
                .releaseDate(LocalDate.now())
                .season(1)
                .mediaType(MediaType.TVSHOWS)
                .build();

        mediaService.saveMediaFromRest(media,registerUser);


        assertEquals("testA", media.getTitle());
        assertEquals("asd", media.getGenre());

    }





}
