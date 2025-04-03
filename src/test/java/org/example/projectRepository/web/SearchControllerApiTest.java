package org.example.projectRepository.web;

import org.example.projectRepository.media.Service.MediaService;
import org.example.projectRepository.media.model.Media;
import org.example.projectRepository.search.SearchClient;
import org.example.projectRepository.search.dto.RestMediaResponse;
import org.example.projectRepository.search.service.SearchService;
import org.example.projectRepository.security.AuthenticationDetails;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.user.model.UserRole;
import org.example.projectRepository.user.service.UserService;
import org.example.projectRepository.wishList.service.WishListService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = SearchController.class)
public class SearchControllerApiTest {

    @MockitoBean
    private UserService userService;
    @MockitoBean
    private SearchClient searchClient;
    @MockitoBean
    private MediaService mediaService;

    @MockitoBean
    private  SearchService searchService;

    @MockitoBean
    private  WishListService wishListService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getRequestToSearch_shouldReturnCorrectPage() throws Exception {

        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .isActive(true)
                .age(18)
                .username("user123")
                .role(UserRole.USER)
                .id(userId)
                .build();

        UUID uuid = UUID.randomUUID();
        AuthenticationDetails authenticationUser = new AuthenticationDetails(uuid
                , "user123", "123123", UserRole.ADMIN, true);

        when(userService.getById(any())).thenReturn(user);

        MockHttpServletRequestBuilder builder = get("/clientSearch").with(user(authenticationUser)).with(csrf());

        mockMvc.perform(builder)
                .andExpect(view().name("ClientSearchPage"))
                .andExpect(model().attributeExists("user"))
                .andExpect(status().isOk());

    }
    @Test
    void getRequestToSearchDTMB_shouldReturnCorrectPage() throws Exception {

        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .isActive(true)
                .age(18)
                .username("user123")
                .role(UserRole.USER)
                .id(userId)
                .build();

        UUID uuid = UUID.randomUUID();
        AuthenticationDetails authenticationUser = new AuthenticationDetails(uuid
                , "user123", "123123", UserRole.ADMIN, true);

        when(userService.getById(any())).thenReturn(user);

        MockHttpServletRequestBuilder builder = get("/clientSearch/Dmdb").with(user(authenticationUser)).with(csrf());

        mockMvc.perform(builder)
                .andExpect(view().name("searchPage"))
                .andExpect(model().attributeExists("user"))
                .andExpect(status().isOk());
    }

    @Test
    void getRequestToSearchByTitleInRepository_shouldReturnCorrectPage() throws Exception {


        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .isActive(true)
                .age(18)
                .username("user123")
                .role(UserRole.USER)
                .id(userId)
                .build();

        UUID uuid = UUID.randomUUID();
        AuthenticationDetails authenticationUser = new AuthenticationDetails(uuid
                , "user123", "123123", UserRole.ADMIN, true);

        RestMediaResponse response = RestMediaResponse.builder()
                .title("title")
                .release_date("2001-09-11")
                .build();

        String name = "title";

        when(userService.getById(any())).thenReturn(user);
       when(searchService.searchMovieByTitle(response.getTitle())).thenReturn(anyList());
        MockHttpServletRequestBuilder builder = get("/clientSearch/titleInRepository").param("title", name).
                with(user(authenticationUser)).with(csrf());

        mockMvc.perform(builder)
                .andExpect(view().name("searchPage"))
                .andExpect(model().attributeExists("user"))
                .andExpect(status().isOk());

        verify(searchService,times(1)).searchMovieByTitle(response.getTitle());
    }

    @Test
    void getRequestToSearchByTitleInDTMB_shouldReturnCorrectPage() throws Exception {


        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .isActive(true)
                .age(18)
                .username("user123")
                .role(UserRole.USER)
                .id(userId)
                .build();

        UUID uuid = UUID.randomUUID();
        AuthenticationDetails authenticationUser = new AuthenticationDetails(uuid
                , "user123", "123123", UserRole.ADMIN, true);

        RestMediaResponse response = RestMediaResponse.builder()
                .title("title")
                .release_date("2001-09-11")
                .build();

        String name = "title";

        when(userService.getById(any())).thenReturn(user);
        when(searchService.search(user,response.getTitle())).thenReturn(anyList());
        MockHttpServletRequestBuilder builder = get("/clientSearch/title").param("title", name).
                with(user(authenticationUser)).with(csrf());

        mockMvc.perform(builder)
                .andExpect(view().name("ClientSearchPage"))
                .andExpect(model().attributeExists("user"))
                .andExpect(status().isOk());

        verify(searchService,times(1)).search(user,response.getTitle());
    }


    @Test
    void tryingToSaveMediaToMediaTvShowSuccessfully() throws Exception {

        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .isActive(true)
                .age(18)
                .username("user123")
                .role(UserRole.USER)
                .id(userId)
                .build();

        UUID uuid = UUID.randomUUID();
        AuthenticationDetails authenticationUser = new AuthenticationDetails(uuid
                , "user123", "123123", UserRole.ADMIN, true);

        RestMediaResponse response = RestMediaResponse.builder()
                .title("title")
                .release_date("2001-09-11")
                .build();
        Media media = Media.builder()
                .title("title")
                .releaseDate(LocalDate.parse("2001-09-11"))
                .season(1).build();
        when(userService.getById(any())).thenReturn(user);
        when(searchService.mediaByTitleAndReleaseDate(response.getTitle(),response.getRelease_date())).thenReturn(media);

        MockHttpServletRequestBuilder builder = post("/clientSearch/saveMedia").with(user(authenticationUser)).with(csrf());


        mockMvc.perform(builder)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/Media/movie/tvshow"));

    }

    @Test
    void tryingToSaveMediaToWishListSuccessfully() throws Exception {

        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .isActive(true)
                .age(18)
                .username("user123")
                .role(UserRole.USER)
                .id(userId)
                .build();

        UUID uuid = UUID.randomUUID();
        AuthenticationDetails authenticationUser = new AuthenticationDetails(uuid
                , "user123", "123123", UserRole.ADMIN, true);

        RestMediaResponse response = RestMediaResponse.builder()
                .title("title")
                .release_date("2001-09-11")
                .build();
        Media media = Media.builder()
                .title("title")
                .releaseDate(LocalDate.parse("2001-09-11"))
                .season(1).build();
        when(userService.getById(any())).thenReturn(user);
        when(searchService.mediaByTitleAndReleaseDate(response.getTitle(),response.getRelease_date())).thenReturn(media);

        MockHttpServletRequestBuilder builder = post("/clientSearch/saveMediaToWishList").with(user(authenticationUser)).with(csrf());


        mockMvc.perform(builder)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/wishlist"));


    }

}
