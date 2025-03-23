package org.example.projectRepository.web;

import org.example.projectRepository.author.model.Author;
import org.example.projectRepository.author.service.AuthorService;
import org.example.projectRepository.book.model.Book;
import org.example.projectRepository.book.service.BookService;
import org.example.projectRepository.security.AuthenticationDetails;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.user.model.UserRole;
import org.example.projectRepository.user.service.UserService;
import org.example.projectRepository.web.dto.BookAuthorRequest;
import org.example.projectRepository.web.dto.BookEditRequest;
import org.example.projectRepository.web.dto.ProfileEditRequest;
import org.example.projectRepository.web.mapper.BookDtoMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;


import java.math.BigDecimal;
import java.util.UUID;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = BookController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Rollback
public class BookControllerApiTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AuthorService authorService;
    @MockitoBean
    private BookService bookService;

    @Autowired
    private MockMvc mockMvc;


    @AfterEach
    void tearDown() {
        MockedStatic<BookDtoMapper> mockedStatic = mockStatic(BookDtoMapper.class);
        mockedStatic.close();
    }


    @Test
    void requestToBookPage_shouldReturnBookPage() throws Exception {


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
        when(bookService.returnAllBook(user)).thenReturn(any());

        MockHttpServletRequestBuilder builder = get("/books/get")
                .param("sort", "title").param("direction", "asc")
                .with(user(authenticationUser)).with(csrf());

        mockMvc.perform(builder).andExpect(status().isOk())
                .andExpect(model().attributeExists("books"))
                .andExpect(view().name("books"));
        verify(userService, times(1)).getById(any());

    }

    @Test
    void requestToBookEditPage_shouldReturnBookEditPage() throws Exception {

        User user = User.builder()
                .isActive(true)
                .age(18)
                .username("user123")
                .role(UserRole.USER)
                .id(UUID.randomUUID())
                .build();

        UUID uuid = UUID.randomUUID();
        AuthenticationDetails authenticationUser = new AuthenticationDetails(uuid
                , "user123", "123123", UserRole.ADMIN, true);

        Author author = Author.builder()
                .firstName("Pesho")
                .lastName("Petrov")
                .id(UUID.randomUUID())
                .build();

        Book book = Book.builder()
                .id(UUID.randomUUID())
                .price(new BigDecimal("19.99"))
                .title("TestA")
                .author(author)
                .user(user)
                .build();

        when(userService.getById(any())).thenReturn(user);
        when(bookService.findById(any())).thenReturn(book);


        userService.getById(user.getId());
        MockHttpServletRequestBuilder builder = get("/books/{id}/edit", book.getId()).with(user(authenticationUser)).with(csrf());

        mockMvc.perform(builder)
                .andExpect(view().name("BookEditPage"))
                .andExpect(model().attributeExists("bookById"))
                .andExpect(status().isOk());

        verify(userService, times(1)).getById(any());
        verify(bookService, times(1)).findById(any());
    }

    //
    @Test
    void requestToDeleteBookByGivenId_shouldRedirectBookGetPage() throws Exception {

        UUID uuid = UUID.randomUUID();
        AuthenticationDetails authenticationUser = new AuthenticationDetails(uuid
                , "user123", "123123", UserRole.ADMIN, true);

        User user = User.builder()
                .isActive(true)
                .age(18)
                .username("user123")
                .role(UserRole.USER)
                .id(UUID.randomUUID())
                .build();

        Author author = Author.builder()
                .firstName("Pesho")
                .lastName("Petrov")
                .id(UUID.randomUUID())
                .build();
        Book book = Book.builder()
                .id(UUID.randomUUID())
                .price(new BigDecimal("19.99"))
                .title("TestA")
                .author(author)
                .user(user)
                .build();

        when(bookService.findById(any())).thenReturn(book);
        doNothing().when(userService).editProfileUser(eq(uuid), any(ProfileEditRequest.class));


        MockHttpServletRequestBuilder builder = post("/books/{id}/delete", book.getId()).with(user(authenticationUser)).with(csrf());

        mockMvc.perform(builder)
                .andExpect(redirectedUrl("/books/get"))
                .andExpect(status().is3xxRedirection());

    }

    @Test
    void getRequestToAddBookPage_shouldReturnAddBookPage() throws Exception {

        UUID uuid = UUID.randomUUID();
        AuthenticationDetails authenticationUser = new AuthenticationDetails(uuid
                , "user123", "123123", UserRole.ADMIN, true);

        User user = User.builder()
                .isActive(true)
                .age(18)
                .username("user123")
                .role(UserRole.USER)
                .id(UUID.randomUUID())
                .build();

        Author author = Author.builder()
                .firstName("Pesho")
                .lastName("Petrov")
                .id(UUID.randomUUID())
                .build();
        Book book = Book.builder()
                .id(UUID.randomUUID())
                .price(new BigDecimal("19.99"))
                .title("TestA")
                .author(author)
                .user(user)
                .build();

        BookEditRequest bookEditRequest = BookEditRequest.builder()
                .title("TestA")
                .price(new BigDecimal("19.99"))
                .build();

        when(userService.getById(any())).thenReturn(user);

        try (MockedStatic<BookDtoMapper> mockedStatic = mockStatic(BookDtoMapper.class)) {
            mockedStatic.when(() -> BookDtoMapper.bookToBookEditRequest(book))
                    .thenReturn(bookEditRequest);

            MockHttpServletRequestBuilder builder = get("/books/add")
                    .with(user(authenticationUser))
                    .with(csrf());

            mockMvc.perform(builder)
                    .andExpect(view().name("addBook"))
                    .andExpect(model().attributeExists("bookAuthorRequest"))
                    .andExpect(status().isOk());

            verify(userService, times(1)).getById(any());


        }
    }

    @Test
    void putRequestToAddBook_shouldRedirectToBookPage() throws Exception {

        UUID uuid = UUID.randomUUID();
        AuthenticationDetails authenticationUser = new AuthenticationDetails(uuid
                , "user123", "123123", UserRole.ADMIN, true);

        User user = User.builder()
                .isActive(true)
                .age(18)
                .username("user123")
                .role(UserRole.USER)
                .id(UUID.randomUUID())
                .build();

        Author author = Author.builder()
                .firstName("Pesho")
                .lastName("Petrov")
                .id(UUID.randomUUID())
                .build();
        Book book = Book.builder()
                .id(UUID.randomUUID())
                .price(new BigDecimal("19.99"))
                .title("TestA")
                .author(author)
                .user(user)
                .build();

        BookEditRequest bookEditRequest = BookEditRequest.builder()
                .title("TestA")
                .price(new BigDecimal("19.99"))
                .build();

        when(userService.getById(any())).thenReturn(user);

        try (MockedStatic<BookDtoMapper> mockedStatic = mockStatic(BookDtoMapper.class)) {
            mockedStatic.when(() -> BookDtoMapper.bookToBookEditRequest(book))
                    .thenReturn(bookEditRequest);

            MockHttpServletRequestBuilder builder = post("/books/add").param("title", "TestA")
                    .param("author", "Pesho")
                    .param("lastName", "Petrov")
                    .param("price", "19.99")
                    .with(user(authenticationUser))
                    .with(csrf());

            mockMvc.perform(builder)
                    .andExpect(redirectedUrl("/books/get"))
                    .andExpect(status().is3xxRedirection());

            verify(userService, times(1)).getById(any());
            verify(authorService, times(1)).createAuthor(any(BookAuthorRequest.class));
            verify(bookService, times(1)).saveBook(any(BookAuthorRequest.class), any(User.class));

        }
    }

    @Test
    void putRequestToAddBookWithInvalidDate_shouldRedirectToAddBookPage() throws Exception {


        UUID uuid = UUID.randomUUID();
        AuthenticationDetails authenticationUser = new AuthenticationDetails(uuid
                , "user123", "123123", UserRole.ADMIN, true);

        User user = User.builder()
                .isActive(true)
                .age(18)
                .username("user123")
                .role(UserRole.USER)
                .id(UUID.randomUUID())
                .build();

        Author author = Author.builder()
                .firstName("Pesho")
                .lastName("Petrov")
                .id(UUID.randomUUID())
                .build();
        Book book = Book.builder()
                .id(UUID.randomUUID())
                .price(new BigDecimal("19.99"))
                .title("TestA")
                .author(author)
                .user(user)
                .build();


        BookEditRequest bookEditRequest = BookEditRequest.builder()
                .title("TestA")
                .price(new BigDecimal("19.99"))
                .build();

        when(userService.getById(any())).thenReturn(user);

        try (MockedStatic<BookDtoMapper> mockedStatic = mockStatic(BookDtoMapper.class)) {
            mockedStatic.when(() -> BookDtoMapper.bookToBookEditRequest(book))
                    .thenReturn(bookEditRequest);

            MockHttpServletRequestBuilder builder = post("/books/add").param("title", "TestA")
                    .param("author", "")
                    .param("lastName", "")
                    .param("price", "-12")
                    .with(user(authenticationUser))
                    .with(csrf());

            mockMvc.perform(builder)
                    .andExpect(view().name("addBook"))
                    .andExpect(status().isOk());

            verify(userService, times(1)).getById(any());
            verify(authorService, times(0)).createAuthor(any(BookAuthorRequest.class));
            verify(bookService, times(0)).saveBook(any(BookAuthorRequest.class), any(User.class));

        }
    }

    @Test
    void putRequestToEditBook_shouldGetCorrectBook() throws Exception {

        UUID uuid = UUID.randomUUID();
        AuthenticationDetails authenticationUser = new AuthenticationDetails(uuid
                , "user123", "123123", UserRole.ADMIN, true);

        User user = User.builder()
                .isActive(true)
                .age(18)
                .username("user123")
                .role(UserRole.USER)
                .id(UUID.randomUUID())
                .build();

        Author author = Author.builder()
                .firstName("Pesho")
                .lastName("Petrov")
                .id(UUID.randomUUID())
                .build();
        Book book = Book.builder()
                .id(UUID.randomUUID())
                .price(new BigDecimal("19.99"))
                .title("TestA")
                .author(author)
                .user(user)
                .build();

        BookEditRequest bookEditRequest = BookEditRequest.builder()
                .title("TestA")
                .price(new BigDecimal("19.99"))
                .firstName("drago")
                .lastName("tosho")
                .build();

            BookAuthorRequest bookAuthorRequest = BookAuthorRequest.builder()
                    .title("TestB")
                    .price(new BigDecimal("19.99"))
                    .firstName("drago")
                    .lastName("tosho")
                    .build();

        when(bookService.findById(any())).thenReturn(book);
        doNothing().when(bookService).editBook(book.getId(), bookEditRequest);
        MockHttpServletRequestBuilder builder = put("/books/{id}/edit",book.getId()).with(user(authenticationUser)).with(csrf())
                .param("title",bookAuthorRequest.getTitle())
                .param("firstName", bookAuthorRequest.getFirstName())
                .param("lastName", bookAuthorRequest.getLastName())
                .param("price", bookAuthorRequest.getPrice().toString());

        mockMvc.perform(builder).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/books/get"));

       // verify(bookService,times(1)).editBook(book.getId(), bookEditRequest);
    }

    @Test
    void putRequestToEditBookWithInvalidDate_shouldGoToEditBookPage() throws Exception {

        UUID uuid = UUID.randomUUID();
        AuthenticationDetails authenticationUser = new AuthenticationDetails(uuid
                , "user123", "123123", UserRole.ADMIN, true);

        User user = User.builder()
                .isActive(true)
                .age(18)
                .username("user123")
                .role(UserRole.USER)
                .id(UUID.randomUUID())
                .build();

        Author author = Author.builder()
                .firstName("Pesho")
                .lastName("Petrov")
                .id(UUID.randomUUID())
                .build();
        Book book = Book.builder()
                .id(UUID.randomUUID())
                .price(new BigDecimal("19.99"))
                .title("TestA")
                .author(author)
                .user(user)
                .build();

        BookEditRequest bookEditRequest = BookEditRequest.builder()
                .title("TestA")
                .price(new BigDecimal("19.99"))
                .firstName("drago")
                .lastName("tosho")
                .build();

        BookAuthorRequest bookAuthorRequest = BookAuthorRequest.builder()
                .title("TestB")
                .price(new BigDecimal("19.99"))
                .firstName("drago")
                .lastName("tosho")
                .build();

        when(bookService.findById(any())).thenReturn(book);
        MockHttpServletRequestBuilder builder = put("/books/{id}/edit",book.getId()).with(user(authenticationUser)).with(csrf())
                .param("title","")
                .param("firstName", "")
                .param("lastName", "")
                .param("price", "-19.99");

        mockMvc.perform(builder).andExpect(status().isOk())
                .andExpect(view().name("BookEditPage"));

    }
}
