package org.example.projectRepository.web;


import org.example.projectRepository.media.Service.MediaService;
import org.example.projectRepository.media.model.Media;
import org.example.projectRepository.media.model.MediaType;
import org.example.projectRepository.search.SearchClient;
import org.example.projectRepository.security.AuthenticationDetails;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.user.model.UserRole;
import org.example.projectRepository.user.service.UserService;
import org.example.projectRepository.web.dto.MediaTvshowRequest;
import org.example.projectRepository.web.mapper.MovieTvshowDtoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;


import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(MediaController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MediaControllerApiTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private MediaService mediaService;

        @MockitoBean
     private  SearchClient searchClient;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getRequestToMediaMainPage_shouldReturnMediaMainPage() throws Exception {


        UUID uuid = UUID.randomUUID();
        AuthenticationDetails authenticationUser = new AuthenticationDetails(uuid
                , "user123", "123123", UserRole.ADMIN, true);

        User user = User.builder()
                .isActive(true)
                .age(18)
                .username("user123")
                .role(UserRole.USER)
                .id(uuid)
                .build();

        when(userService.getById(any())).thenReturn(user);
        when(mediaService.returnAllMediaSorted(user,"title","asc")).thenReturn(anyList());

        MockHttpServletRequestBuilder request = get("/Media/movie/tvshow").param("sort","title")
                .param("direction","asc").with(user(authenticationUser)).with(csrf());

        mockMvc.perform(request)
                .andExpect(model().attributeExists("shows"))
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("movie-TvshowMainPage"))
                .andExpect(status().isOk());
    }

    @Test
    void getRequestToMediaAddPage_shouldReturnMediaAddPage() throws Exception {

        UUID uuid = UUID.randomUUID();
        AuthenticationDetails authenticationUser = new AuthenticationDetails(uuid
                , "user123", "123123", UserRole.ADMIN, true);

        User user = User.builder()
                .isActive(true)
                .age(18)
                .username("user123")
                .role(UserRole.USER)
                .id(uuid)
                .build();

        when(userService.getById(any())).thenReturn(user);

        MockHttpServletRequestBuilder request = get("/Media/movie/tvshow/add").with(user(authenticationUser)).with(csrf());

        mockMvc.perform(request)
                .andExpect(view().name("movie-Tvshow-add"))
                .andExpect(model().attributeExists("mediaTvshowRequest"))
                .andExpect(model().attributeExists("user"))
                .andExpect(status().isOk());
    }

    @Test
    void getRequestToPostMediaPageWithInValidData_shouldStayInSamePage() throws Exception {

        UUID uuid = UUID.randomUUID();
        AuthenticationDetails authenticationUser = new AuthenticationDetails(uuid
                , "user123", "123123", UserRole.ADMIN, true);

        User user = User.builder()
                .isActive(true)
                .age(18)
                .username("user123")
                .role(UserRole.USER)
                .id(uuid)
                .build();


            when(userService.getById(any())).thenReturn(user);

            MockHttpServletRequestBuilder builder= post("/Media/movie/tvshow/add")
                    .param("title", "")
                    .param("genre", "")
                    .param("releaseDate", "")
                    .param("seasons","-1")
                    .with(user(authenticationUser)).with(csrf());

            mockMvc.perform(builder)
                    .andExpect(view().name("movie-Tvshow-add"))
                    .andExpect(model().attributeExists("mediaTvshowRequest"))
                    .andExpect(model().attributeExists("user"))
                    .andExpect(status().isOk());

    }

    @Test

    void getRequestToPostMediaPageWithValidData_shouldRedirectToMediaMovieTvshowPage_happyPath() throws Exception {


        //TODO must fix this ...
        UUID uuid = UUID.randomUUID();
        AuthenticationDetails authenticationUser = new AuthenticationDetails(uuid
                , "user123", "123123", UserRole.ADMIN, true);

        User user = User.builder()
                .isActive(true)
                .age(18)
                .username("user123")
                .role(UserRole.USER)
                .id(uuid)
                .build();

        MediaTvshowRequest mediaTvshowRequest = MediaTvshowRequest.builder()
                .title("testA")
                .mediaType(MediaType.TVSHOWS)
                .genre("asd")
                .releaseDate("18.10.2010")
                .seasons(1).build();

        Media media = Media.builder()
                .title("testA")
                .mediaType(MediaType.TVSHOWS)
                .genre("asd")
                .season(1)
                .build();

        when(userService.getById(any())).thenReturn(user);
        mediaService.saveMedia(mediaTvshowRequest,user);
        MockHttpServletRequestBuilder builder= post("/Media/movie/tvshow/add")
                .param("title",mediaTvshowRequest.getTitle())
                .param("genre", mediaTvshowRequest.getGenre())
                .param("releaseDate",mediaTvshowRequest.getReleaseDate())
                .param("seasons", String.valueOf(mediaTvshowRequest.getSeasons()))
                .with(user(authenticationUser)).with(csrf());

        mockMvc.perform(builder).andExpect(status().isOk());


    }
}
