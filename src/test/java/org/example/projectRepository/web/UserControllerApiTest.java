package org.example.projectRepository.web;

import org.example.projectRepository.security.AuthenticationDetails;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.user.model.UserRole;
import org.example.projectRepository.user.service.UserService;
import org.example.projectRepository.web.dto.ProfileEditRequest;
import org.example.projectRepository.web.mapper.DtoMapper;
import org.example.projectRepository.wishList.model.TypeEntertainment;
import org.example.projectRepository.wishList.model.WishList;
import org.example.projectRepository.wishList.service.WishListService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerApiTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private WishListService wishListService;
    @Autowired
    private MockMvc mockMvc;


    //TODO must fix this i don't know why behave like this...
    @Test
    void putRequestToSwitchRoleNotAuthenticated_shouldReturn404() throws Exception {

        UUID uuid = UUID.randomUUID();
        AuthenticationDetails authenticationUser = new AuthenticationDetails(uuid
                , "user123", "123123", UserRole.USER, true);


        MockHttpServletRequestBuilder request = put("/users/{id}/role", uuid)
                .with(user(authenticationUser))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(view().name("404NotFound"));
    }

    @Test
    void putRequestToSwitchRoleAuthenticated_shouldReturnCorrectPage() throws Exception {


        UUID uuid = UUID.randomUUID();
        AuthenticationDetails authenticationUser = new AuthenticationDetails(uuid
                , "admin123", "admin123", UserRole.ADMIN, true);

        MockHttpServletRequestBuilder request = put("/users/{id}/role", uuid)
                .with(user(authenticationUser))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/getAllUsers"));
        verify(userService, times(1)).changeUserRole(uuid);
    }

    @Test
    void putRequestForProfilePage_shouldReturnCorrectPage() throws Exception {

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

        ProfileEditRequest profileEditRequest = ProfileEditRequest.builder().age(19)
                .firstName("John")
                .lastName("Doe")
                .build();

        when(userService.getById(uuid)).thenReturn(user);
        mockStatic(DtoMapper.class).when(() -> DtoMapper.mapToProfileEditRequest(user))
                .thenReturn(profileEditRequest);

        MockHttpServletRequestBuilder request = get("/users/{id}/profile", user.getId()).with(user(authenticationUser))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("editProfile"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("profileEditRequest"));

    }


    @Test
    void putAuthorizedRequestToSwitchRole_shouldRedirectToUsers() throws Exception {

        UUID uuid = UUID.randomUUID();
        AuthenticationDetails authenticationUser = new AuthenticationDetails(uuid
                , "user123", "123123", UserRole.ADMIN, true);
        MockHttpServletRequestBuilder request = put("/users/{id}/role", UUID.randomUUID())
                .with(user(authenticationUser))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/getAllUsers"));
        verify(userService, times(1)).changeUserRole(any());
    }


    //TODO must fix this i don't know why behave like this...
    @Test
    void putAuthorizedRequestToSwitchRole_shouldRedirect404() throws Exception {

        // 1. Build Request
        UUID uuid = UUID.randomUUID();
        AuthenticationDetails authenticationUser = new AuthenticationDetails(uuid
                , "user123", "123123", UserRole.USER, true);
        MockHttpServletRequestBuilder request = put("/users/{id}/role", UUID.randomUUID())
                .with(user(authenticationUser))
                .with(csrf());


        mockMvc.perform(request).andExpect(status().is3xxRedirection());

        verify(userService, times(0)).changeUserRole(any());
    }

    @Test
    void getRequestToWishListPage_shouldReturnCorrectPage() throws Exception {


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

        when(userService.getById(uuid)).thenReturn(user);
        WishList wishList = WishList.builder()
                .title("title")
                .typeEntertainment(TypeEntertainment.TvShow)
                .user(user)
                .seasons(1).completed(true).build();
        when(wishListService.returnWishList(user)).thenReturn(List.of(wishList));

        MockHttpServletRequestBuilder request = get("/users/{id}/getWishList", user.getId())
                .with(user(authenticationUser))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("wishList"))
                .andExpect(view().name("userDescriptionPage"));

    }

    @Test
    void getRequestToGetAllUsers_shouldReturnCorrectPage() throws Exception {

        UUID uuid = UUID.randomUUID();
        AuthenticationDetails authenticationUser = new AuthenticationDetails(uuid
                , "user123", "123123", UserRole.ADMIN, true);

        when(userService.getAllUsers()).thenReturn(List.of(User.builder()
                .username("user123")
                .age(19).isActive(true).build()));

        MockHttpServletRequestBuilder builder = get("/users/getAllUsers").with(user(authenticationUser))
                .with(csrf());


        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("users"))
                .andExpect(view().name("getAllUsers"));

        verify(userService, times(1)).getAllUsers();

    }

    @Test
    void getRequestChangingStatusRoleOnUser_shouldReturnCorrectPage() throws Exception {

        UUID uuid = UUID.randomUUID();
        AuthenticationDetails authenticationUser = new AuthenticationDetails(uuid
                , "user123", "123123", UserRole.ADMIN, true);

        MockHttpServletRequestBuilder request = put("/users/{id}/status", UUID.randomUUID()).with(user(authenticationUser))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(redirectedUrl("/users/getAllUsers"));
    }

    @Test
    void getRequestToEditProfilePage_shouldReturnCorrectPage() throws Exception {


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

        ProfileEditRequest profileEditRequest = ProfileEditRequest.builder().age(19)
                .firstName("John")
                .lastName("Doe")
                .age(19)
                .build();

        doNothing().when(userService).editProfileUser(eq(uuid), any(ProfileEditRequest.class));

        MockHttpServletRequestBuilder request = put("/users/{id}/profile", uuid)
                .with(user(authenticationUser))
                .with(csrf())
                .param("firstName", profileEditRequest.getFirstName())
                .param("lastName", profileEditRequest.getLastName())
                .param("age", String.valueOf(profileEditRequest.getAge()));

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        verify(userService, times(1)).editProfileUser(eq(uuid), any(ProfileEditRequest.class));

    }

    @Test
    void getRequestToEditProfilePageWithInvalidData_shouldRedirectEditProfilePage() throws Exception {


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

        when(userService.getById(uuid)).thenReturn(user);
        MockHttpServletRequestBuilder request = put("/users/{id}/profile", uuid)
                .with(user(authenticationUser))
                .with(csrf())
                .param("firstName", "")
                .param("lastName", "")
                .param("age", String.valueOf(-1));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("editProfile"));


    }
}
