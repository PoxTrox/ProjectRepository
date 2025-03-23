package org.example.projectRepository.book;

import org.example.projectRepository.author.model.Author;
import org.example.projectRepository.author.service.AuthorService;
import org.example.projectRepository.book.model.Book;
import org.example.projectRepository.book.repository.BookRepository;
import org.example.projectRepository.book.service.BookService;

import org.example.projectRepository.exception.DomainException;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.web.dto.BookAuthorRequest;
import org.example.projectRepository.web.dto.BookEditRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;


import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;
import java.util.UUID;



import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class BookUTest {


    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorService authorService;


    @InjectMocks
    private BookService bookService;


    @Test
    void whenTryingToSaveABookSuccessfully() {

        BookAuthorRequest bookAuthorRequest = BookAuthorRequest.builder()
                .title("title")
                .price(BigDecimal.valueOf(19.99))
                .firstName("John")
                .lastName("Doe")
                .build();

        Author author = Author.builder()
                .firstName("John")
                .lastName("Doe")
                .build();


        User user = User.builder()
                .firstName("Pesho")
                .lastName("PeshoTest")
                .age(19)
                .isActive(true)
                .build();

        when(authorService.findAuthor(author.getFirstName(), author.getLastName())).thenReturn(Optional.of(author));

        Optional<Author> author1 = authorService.findAuthor(author.getFirstName(), author.getLastName());


        bookService.saveBook(bookAuthorRequest, user);

        assertThat(author1.get().getFirstName()).isEqualTo(author.getFirstName());

        assertThat(author1.get().getLastName()).isEqualTo(author.getLastName());

        verify(authorService, times(2)).findAuthor(author.getFirstName(), author.getLastName());

    }

    @Test
    void whenTryingToSaveABookButAlreadyExists_throwsException() {



        Author author = Author.builder()
                .firstName("John")
                .lastName("Doe")
                .build();
        Book book = Book.builder()
                .title("title")
                .author(author)
                .build();

        when(bookRepository.findByTitleAndAuthor(book.getTitle(), author)).thenThrow(DomainException.class);

        assertThrows(DomainException.class, () -> bookRepository.findByTitleAndAuthor(book.getTitle(), author));
    }


    @Test
    void whenTryingToFindBookByTitleAndAuthorSuccessfully() {


        Author author = Author.builder()
                .firstName("John")
                .lastName("Doe")
                .build();

        Book book = Book.builder()
                .title("title")
                .author(author)
                .build();
        when(bookRepository.findByTitleAndAuthor(book.getTitle(), author)).thenReturn(Optional.of(book));

        Optional<Book> byTitleAndAuthor = bookRepository.findByTitleAndAuthor(book.getTitle(), author);

        assertThat(byTitleAndAuthor.isPresent()).isTrue();
        assertThat(byTitleAndAuthor.get().getTitle()).isEqualTo(book.getTitle());
        assertThat(byTitleAndAuthor.get().getAuthor().getFirstName()).isEqualTo(author.getFirstName());
        assertThat(byTitleAndAuthor.get().getAuthor().getLastName()).isEqualTo(author.getLastName());
        verify(bookRepository, times(1)).findByTitleAndAuthor(book.getTitle(), author);

    }

    @Test
    void whenTryingToFindBookByTitleAndAuthor_throwsException() {


        Author author = Author.builder()
                .firstName("John")
                .lastName("Doe")
                .build();

        Book book = Book.builder()
                .title("title")
                .author(author)
                .build();

        when(bookRepository.findByTitleAndAuthor(book.getTitle(), author)).thenThrow(DomainException.class);

        assertThrows(DomainException.class, () -> bookRepository.findByTitleAndAuthor(book.getTitle(), author));
        verify(bookRepository, times(1)).findByTitleAndAuthor(book.getTitle(), author);

    }

    @Test
    void whenTryingToDeleteBookById_successfully() {

        UUID id = UUID.randomUUID();

        Book book = Book.builder()
                .title("title")
                .id(id)
                .build();
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        bookService.deleteBookById(id);
        verify(bookRepository, times(1)).findById(id);
    }

    @Test
    void whenTryingToDeleteBookById_throwsException() {
        UUID id = UUID.randomUUID();


        when(bookRepository.findById(id)).thenThrow(DomainException.class);

        assertThrows(DomainException.class, () -> bookService.findById(id));
    }


    @Test
    void tryingToEditBook() {

        BookEditRequest bookEditRequest = BookEditRequest.builder()
                .title("title")
                .firstName("John")
                .lastName("Doe")
                .price(BigDecimal.valueOf(19.99))
                .build();

        Author author = Author.builder()
                .firstName("John")
                .lastName("Doe")
                .id(UUID.randomUUID())
                .build();
        UUID id = UUID.randomUUID();
        Book book = Book.builder()
                .title("title")
                .author(author)
                .id(id)
                .build();

        User user = User.builder()
                .firstName("Pesho")
                .lastName("PeshoTest")
                .age(19)
                .isActive(true)
                .build();

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        bookService.editBook(id, bookEditRequest);

        verify(bookRepository, times(1)).findById(id);
        assertThat(book.getTitle()).isEqualTo(bookEditRequest.getTitle());

    }


    @Test
    void tryingToSortBookByThereTitleAsc() {

        UUID id = UUID.randomUUID();

        User user = User.builder()
                .firstName("Pesho")
                .lastName("PeshoTest")
                .age(19)
                .id(id)
                .isActive(true)
                .build();
        Author author = Author.builder()
                .firstName("John")
                .lastName("Doe")
                .id(UUID.randomUUID())
                .build();
        Author authorSecond = Author.builder()
                .firstName("JohnTest")
                .lastName("DoeTest2")
                .id(UUID.randomUUID())
                .build();

        List<Book> bookList = List.of(Book.builder().title("testA")
                        .author(author)
                        .price(BigDecimal.valueOf(19.99))
                        .createdAt(LocalDateTime.now())
                        .build(),
                Book.builder().title("testB")
                        .author(author)
                        .price(BigDecimal.valueOf(12.99))
                        .build(),
                Book.builder().title("testC")
                        .createdAt(LocalDateTime.now())
                        .author(authorSecond)
                        .price(BigDecimal.valueOf(15.99))
                        .build());

        when(bookRepository.findAllByUserOrderByTitleAsc(user)).thenReturn(bookList);
        when(bookRepository.findAllByUserOrderByTitleDesc(user)).thenReturn(bookList);
        when(bookRepository.findAllByUserOrderByAuthorAsc(user)).thenReturn(bookList);
        when(bookRepository.findAllByUserOrderByAuthorDesc(user)).thenReturn(bookList);
        when(bookRepository.findAllByUserOrderByPriceAsc(user)).thenReturn(bookList);
        when(bookRepository.findAllByUserOrderByPriceDesc(user)).thenReturn(bookList);
        List<Book> bookList1 = bookService.returnAllBooksSorted(user, "title", "asc");
        List<Book> bookList2 = bookService.returnAllBooksSorted(user, "title", "desc");
        List<Book> bookList3 = bookService.returnAllBooksSorted(user, "Author", "asc");
        List<Book> bookList4 = bookService.returnAllBooksSorted(user, "Author", "desc");
        List<Book> bookList5 = bookService.returnAllBooksSorted(user, "price", "asc");
        List<Book> bookList6 = bookService.returnAllBooksSorted(user, "price", "desc");

        assertThat(bookList1).isEqualTo(bookList);
        assertThat(bookList2).isEqualTo(bookList1);
        assertThat(bookList3).isEqualTo(bookList);
        assertThat(bookList3).isEqualTo(bookList);
        assertThat(bookList4).isEqualTo(bookList);
        assertThat(bookList5).isEqualTo(bookList);
        assertThat(bookList6).isEqualTo(bookList);

    }

    @Test
    void tryingToSortBookByAuthorDesc() {

        UUID id = UUID.randomUUID();

        User user = User.builder()
                .firstName("Pesho")
                .lastName("PeshoTest")
                .age(19)
                .id(id)
                .isActive(true)
                .build();
        Author author = Author.builder()
                .firstName("John")
                .lastName("Doe")
                .id(UUID.randomUUID())
                .build();
        Author authorSecond = Author.builder()
                .firstName("JohnTest")
                .lastName("DoeTest2")
                .id(UUID.randomUUID())
                .build();

        List<Book> bookList = List.of(Book.builder().title("testA")
                        .author(author)
                        .price(BigDecimal.valueOf(19.99))
                        .createdAt(LocalDateTime.now())
                        .build(),
                Book.builder().title("testB")
                        .author(author)
                        .price(BigDecimal.valueOf(12.99))
                        .build(),
                Book.builder().title("testC")
                        .createdAt(LocalDateTime.now())
                        .author(authorSecond)
                        .price(BigDecimal.valueOf(15.99))
                        .build());

        when(bookRepository.findAllByUserOrderByAuthorDesc(user)).thenReturn(bookList);

        List<Book> bookList1 = bookService.returnAllBooksSorted(user, "Author", "desc");
        assertThat(bookList1).isEqualTo(bookList);
    }

    @Test
    void TryingToReturnAllBookByGivenUser() {
        UUID id = UUID.randomUUID();

        User user = User.builder()
                .firstName("Pesho")
                .lastName("PeshoTest")
                .age(19)
                .id(id)
                .isActive(true)
                .build();
        Author author = Author.builder()
                .firstName("John")
                .lastName("Doe")
                .id(UUID.randomUUID())
                .build();
        Author authorSecond = Author.builder()
                .firstName("JohnTest")
                .lastName("DoeTest2")
                .id(UUID.randomUUID())
                .build();

        List<Book> bookList = List.of(Book.builder().title("testA")
                        .author(author)
                        .price(BigDecimal.valueOf(19.99))
                        .createdAt(LocalDateTime.now())
                        .build(),
                Book.builder().title("testB")
                        .author(author)
                        .price(BigDecimal.valueOf(12.99))
                        .build(),
                Book.builder().title("testC")
                        .createdAt(LocalDateTime.now())
                        .author(authorSecond)
                        .price(BigDecimal.valueOf(15.99))
                        .build());


        when(bookRepository.findAllByUserOrderByCreatedAtDesc(user)).thenReturn(bookList);
        List<Book> bookList1 = bookService.returnAllBook(user);
        assertThat(bookList1).isEqualTo(bookList);

    }

//    @Test
//    void whenTryingToSaveABook_not_Successfully() {
//
//
//
//        Author author = Author.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .build();
//
//        BookAuthorRequest bookAuthorRequest = BookAuthorRequest.builder()
//                .title("title")
//                .price(BigDecimal.valueOf(19.99))
//                .firstName("John")
//                .lastName("Doe")
//                .build();
//
//        Book book = Book.builder()
//                .author(author)
//                .title("title")
//                .build();
//
//        User user = User.builder()
//                .firstName("Pesho")
//                .lastName("PeshoTest")
//                .age(19)
//                .isActive(true)
//                .build();
//
//        when(authorService.findAuthor(author.getFirstName(), author.getLastName())).thenReturn(Optional.of(author));
//        // when(bookRepository.findByTitleAndAuthor(bookAuthorRequest.getTitle(),author)).thenThrow(DomainException.class);
//        when(bookRepository.findByTitleAndAuthor(bookAuthorRequest.getTitle(),author)).thenReturn(Optional.of(book));
//
//        Optional<Author> author1 = authorService.findAuthor(author.getFirstName(), author.getLastName());
//
//
////        bookService.saveBook(bookAuthorRequest, user);
//
//
//        assertThrows(DomainException.class, () -> bookRepository.findByTitleAndAuthor(book.getTitle(),author1.get()));
//        //  assertThrows(DomainException.class, () -> bookService.saveBook(bookAuthorRequest,user));
//
////        assertThat(author1.get().getFirstName()).isEqualTo(author.getFirstName());
////
////        assertThat(author1.get().getLastName()).isEqualTo(author.getLastName());
////
////        verify(authorService, times(2)).findAuthor(author.getFirstName(), author.getLastName());
//
//    }

}

