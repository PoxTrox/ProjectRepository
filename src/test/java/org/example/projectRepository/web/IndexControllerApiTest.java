package org.example.projectRepository.web;

import org.example.projectRepository.book.service.BookService;
import org.example.projectRepository.exception.UserNameAlreadyExistException;
import org.example.projectRepository.media.Service.MediaService;
import org.example.projectRepository.search.SearchClient;
import org.example.projectRepository.security.AuthenticationDetails;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.user.model.UserRole;
import org.example.projectRepository.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.UUID;

import static org.example.projectRepository.TestBuilder.aMockUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(indexController.class)
public class IndexControllerApiTest {

    @Autowired
    private MockMvc mockMvc;


    @MockitoBean
    private UserService userService;
    @MockitoBean
    private MediaService mediaService;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private SearchClient searchClient;

    @Test
    void getIndexRequest_shouldReturnIndexPage() throws Exception {

        MockHttpServletRequestBuilder builder = get("/");

        mockMvc.perform(builder)
                .andExpect(view().name("index"))
                .andExpect(status().isOk());
    }

    @Test
    void getRegisterRequest_shouldReturnRegisterPage() throws Exception {

        MockHttpServletRequestBuilder builder = get("/register");
        mockMvc.perform(builder)
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("registerRequest"))
                .andExpect(status().isOk());
    }

    @Test
    void getLoginRequest_shouldReturnLoginPage() throws Exception {

        MockHttpServletRequestBuilder builder = get("/login");
        mockMvc.perform(builder)
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("loginRequest"))
                .andExpect(status().isOk());
    }

    @Test
    void getLoginRequestWithGivenErrorParam_shouldReturnLoginPageWIthErrorMessage() throws Exception {

        MockHttpServletRequestBuilder builder = get("/login").param("error", "");
        mockMvc.perform(builder)
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("loginRequest"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(status().isOk());
    }

    @Test
    void postRegisterRequest_shouldReturnLoginPage() throws Exception {

        MockHttpServletRequestBuilder builder = post("/register")
                .formField("username", "admin")
                .formField("password", "admin123")
                .formField("confirmPassword", "admin123")
                .formField("agreeTermOfUse", "true")
                .with(csrf());
        mockMvc.perform(builder)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(userService, times(1)).register(any());

    }

    @Test
    void postRegisterRequestWishInvalidDate_shouldReturnRegisterPage() throws Exception {

        MockHttpServletRequestBuilder builder = post("/register")
                .formField("username", "admin")
                .formField("password", "")
                .formField("confirmPassword", "")
                .formField("agreeTermOfUse", "true")
                .with(csrf());
        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(view().name("register"));

        verify(userService, times(0)).register(any());

    }

    @Test
    void getRequestForTermsOfUser_shouldReturnTermsPage() throws Exception {

        MockHttpServletRequestBuilder builder = get("/TermOfUse");

        mockMvc.perform(builder)
                .andExpect(view().name("termsOfUse"))
                .andExpect(status().isOk());
    }


    @Test
    void getAuthenticatedUser_shouldReturnAuthenticatedHomePage() throws Exception {


        when(userService.getById(any())).thenReturn(aMockUser());

        UUID uuid = UUID.randomUUID();
        AuthenticationDetails authenticationUser = new AuthenticationDetails(uuid
                , "user123", "123123", UserRole.USER, true);

        MockHttpServletRequestBuilder builder = get("/home").with(user(authenticationUser));

        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(view().name("home"));

        verify(userService,times(1)).getById(uuid);

    }

    @Test
    void getNotAuthenticatedUser_shouldRedirectLoginPage() throws Exception {

        MockHttpServletRequestBuilder builder = get("/home");
        mockMvc.perform(builder)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));

        verify(userService, never()).getById(any());

    }
//
//    @Test
//    void postRegisterRequestWithUserNameAlreadyExist_shouldRedirectToRegister() throws Exception {
//
//        MockHttpServletRequestBuilder builder = post("/register")
//                .formField("username", "admin")
//                .formField("password", "admin123")
//                .formField("confirmPassword", "admin123")
//                .formField("agreeTermOfUse", "true")
//                .with(csrf());
//
//        when(userService.register(any())).thenThrow( new UserNameAlreadyExistException("User name already exists"));
//
//        mockMvc.perform(builder)
//                .andExpect(status().is3xxRedirection())
//                .andExpect(flash().attribute("errorMessage", "User name already exists"))
//              .andExpect(redirectedUrl("/register"));
//
//        verify(userService, times(1)).register(any());
//
//    }
}



