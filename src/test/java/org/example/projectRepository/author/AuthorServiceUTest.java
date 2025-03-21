package org.example.projectRepository.author;


import org.example.projectRepository.author.model.Author;
import org.example.projectRepository.author.repository.AuthorRepository;
import org.example.projectRepository.author.service.AuthorService;
import org.example.projectRepository.exception.DomainException;
import org.example.projectRepository.web.dto.BookAuthorRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class AuthorServiceUTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    @Test
    void TryingToFindAuthorWithGivenName_Successfully() {


        Author author = Author.builder()
                .firstName("John")
                .lastName("Doe")
                .build();
        when(authorRepository.findByFirstNameAndLastName(author.getFirstName(), author.getLastName())).thenReturn(Optional.of(author));
        Optional<Author> author1 = authorService.findAuthor(author.getFirstName(), author.getLastName());

        assertThat(author1).isPresent();
        assertThat(author1.get().getFirstName()).isEqualTo("John");
        assertThat(author1.get().getLastName()).isEqualTo("Doe");
        verify(authorRepository, times(1)).findByFirstNameAndLastName("John", "Doe");


    }

    @Test
    void TryingToFindAuthorWithGivenName_Failure() {

        Author author = Author.builder()
                .firstName("John")
                .lastName("Doe")
                .build();
        when(authorRepository.findByFirstNameAndLastName(author.getFirstName(), author.getLastName())).thenThrow(DomainException.class);

        assertThrows(DomainException.class, () -> authorService.findAuthor(author.getFirstName(), author.getLastName()));
    }

    @Test
    void tryingToSaveAuthor_Successfully() {
        Author author = Author.builder()
                .firstName("John")
                .lastName("Doe")
                .build();

        authorService.save(author);
        verify(authorRepository, times(1)).save(author);

    }

    @Test
    void tryingToFindAuthorById_Successfully() {

        Author author = Author.builder()
                .firstName("John")
                .lastName("Doe")
                .id(UUID.randomUUID())
                .build();

        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
        Author byId = authorService.findById(author.getId());
        assertThat(byId.getFirstName()).isEqualTo("John");
        assertThat(byId.getLastName()).isEqualTo("Doe");
        verify(authorRepository, times(1)).findById(author.getId());

    }

    @Test
    void tryingToFindAuthorById_Failure() {

        Author author = Author.builder()
                .firstName("John")
                .lastName("Doe")
                .id(UUID.randomUUID())
                .build();
        when(authorRepository.findById(author.getId())).thenReturn(Optional.empty());
        assertThrows(DomainException.class, () -> authorService.findById(author.getId()));
    }

    @Test
    void tryingToCreateAuthor_Successfully() {

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
        when(authorService.findAuthor(author.getFirstName(),author.getLastName())).thenReturn(Optional.empty());
       authorService.createAuthor(bookAuthorRequest);

       authorService.save(author);
       verify(authorRepository, times(1)).save(author);
        assertThat(author.getFirstName()).isEqualTo("John");
        assertThat(author.getLastName()).isEqualTo("Doe");


    }


    @Test
    void tryingToCreateAuthor_IfExist_justReturnSameAuthor() {

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
        when(authorService.findAuthor(author.getFirstName(),author.getLastName())).thenReturn(Optional.of(author));
        authorService.createAuthor(bookAuthorRequest);

        assertThat(author.getFirstName()).isEqualTo("John");
        assertThat(author.getLastName()).isEqualTo("Doe");


    }

}
