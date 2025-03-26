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
import org.example.projectRepository.web.dto.MovieTvShowEditRequest;
import org.example.projectRepository.web.dto.ProfileEditRequest;
import org.example.projectRepository.web.mapper.DtoMapper;
import org.example.projectRepository.web.mapper.MovieTvshowDtoMapper;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
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
    private SearchClient searchClient;
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
        when(mediaService.returnAllMediaSorted(user, "title", "asc")).thenReturn(anyList());

        MockHttpServletRequestBuilder request = get("/Media/movie/tvshow").param("sort", "title")
                .param("direction", "asc").with(user(authenticationUser)).with(csrf());

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

        MockHttpServletRequestBuilder builder = post("/Media/movie/tvshow/add")
                .param("title", "")
                .param("genre", "")
                .param("releaseDate", "")
                .param("seasons", "-1")
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

        when(userService.getById(any())).thenReturn(user);

        MockHttpServletRequestBuilder builder = post("/Media/movie/tvshow/add")
                .param("title", mediaTvshowRequest.getTitle())
                .param("genre", mediaTvshowRequest.getGenre())
                .param("releaseDate", mediaTvshowRequest.getReleaseDate())
                .param("mediaType", String.valueOf(mediaTvshowRequest.getMediaType()))
                .param("seasons", String.valueOf(mediaTvshowRequest.getSeasons()))
                .with(user(authenticationUser)).with(csrf());

        mockMvc.perform(builder)
                .andExpect(redirectedUrl("/Media/movie/tvshow"))
                .andExpect(status().is3xxRedirection());
        verify(userService).getById(any());

    }


    @Test
    void getRequestToEditMediaTvSHow_shouldReturnCorrectPage() throws Exception {

        Media media = Media.builder()
                .title("testA")
                .mediaType(MediaType.TVSHOWS)
                .genre("asd")
                .id(UUID.randomUUID())
                .season(1)
                .build();

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

        MovieTvShowEditRequest movieTvShowEditRequest = MovieTvShowEditRequest.builder()
                .title("testA")
                .mediaType(MediaType.TVSHOWS)
                .genre("asda")
                .releaseDate("18.10.2010")
                .seasons(1).build();

        when(userService.getById(any())).thenReturn(user);
        when(mediaService.findById(any())).thenReturn(media);

        try (MockedStatic<MovieTvshowDtoMapper> mockedStatic = mockStatic(MovieTvshowDtoMapper.class)) {
            mockedStatic.when(() -> MovieTvshowDtoMapper.mapToMovieTvShowEditRequest(media))
                    .thenReturn(movieTvShowEditRequest);

            MockHttpServletRequestBuilder request = get("/Media/{id}/edit", media.getId())
                    .with(user(authenticationUser))
                    .with(csrf());

            mockMvc.perform(request)
                    .andExpect(status().isOk())
                    .andExpect(view().name("movie-TvshowEditPage"))
                    .andExpect(model().attribute("user", user))
                    .andExpect(model().attribute("media", media))
                    .andExpect(model().attribute("movieTvShowEditRequest", movieTvShowEditRequest));

            verify(userService, times(1)).getById(user.getId());
            verify(mediaService, times(1)).findById(media.getId());

        }
    }

    @Test
    void putRequestToEditMediaTvShowWithInvalidDate_shouldRedirectToSamePage() throws Exception {

        Media media = Media.builder()
                .title("testA")
                .mediaType(MediaType.TVSHOWS)
                .genre("asd")
                .id(UUID.randomUUID())
                .season(1)
                .build();
        UUID uuid = UUID.randomUUID();

        User user = User.builder()
                .isActive(true)
                .age(18)
                .username("user123")
                .role(UserRole.USER)
                .id(uuid)
                .build();


        AuthenticationDetails authenticationUser = new AuthenticationDetails(uuid
                , "user123", "123123", UserRole.ADMIN, true);


        when(userService.getById(uuid)).thenReturn(user);
        when(mediaService.findById(any())).thenReturn(media);

        MockHttpServletRequestBuilder request = put("/Media/{id}/edit", media.getId())
                .param("title", "")
                .param("genre", "")
                .param("releaseDate", "")
                .param("mediaType", "")
                .with(user(authenticationUser)).with(csrf());

        mockMvc.perform(request)
                .andExpect(model().attribute("user", user))
                .andExpect(model().attribute("media", media))
                .andExpect(view().name("movie-TvshowEditPage"))
                .andExpect(status().isOk());
    }

    @Test
    void putRequestToEditMediaTvShowWithValidData_shouldRedirectToMediaMovieTvshowPage() throws Exception {

        UUID uuid = UUID.randomUUID();
        User user = User.builder()
                .isActive(true)
                .age(18)
                .username("user123")
                .role(UserRole.USER)
                .id(uuid)
                .build();

        Media media = Media.builder()
                .title("testA")
                .mediaType(MediaType.TVSHOWS)
                .genre("asda")
                .id(UUID.randomUUID())
                .season(1)
                .build();


        AuthenticationDetails authenticationUser = new AuthenticationDetails(uuid
                , "user123", "123123", UserRole.ADMIN, true);


        when(userService.getById(user.getId())).thenReturn(user);
       when(mediaService.findById(any())).thenReturn(media);

        doNothing().when(mediaService).editMedia(eq(media.getId()), any(MovieTvShowEditRequest.class));

        MockHttpServletRequestBuilder request = put("/Media/{id}/edit", media.getId())
                .param("title", "testA")
                .param("genre", "asda")
                .param("releaseDate", "18.10.2010")
                .param("mediaType", "TVSHOWS")
                .param("seasons", "1")
                .with(user(authenticationUser)).with(csrf());

        mockMvc.perform(request)
                .andExpect(redirectedUrl("/Media/movie/tvshow"))
                .andExpect(status().is3xxRedirection());


    }

}