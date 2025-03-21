package org.example.projectRepository.author.service;

import jakarta.validation.Valid;
import org.example.projectRepository.author.model.Author;
import org.example.projectRepository.author.repository.AuthorRepository;
import org.example.projectRepository.exception.DomainException;
import org.example.projectRepository.web.dto.BookAuthorRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }


    public Optional<Author> findAuthor(String firstName, String lastName) throws DomainException {


        return authorRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    public void save(Author newAuthor) {
        authorRepository.save(newAuthor);
    }

    public Author findById(UUID id) {
        return authorRepository.findById(id).orElseThrow(() -> new DomainException("Author not found"));
    }


    public Author createAuthor(BookAuthorRequest bookAuthorRequest) {

        Optional<Author> optionalAuthor = findAuthor(bookAuthorRequest.getFirstName(), bookAuthorRequest.getLastName());
        if (optionalAuthor.isEmpty()) {
            Author author = Author.builder()
                    .firstName(bookAuthorRequest.getFirstName())
                    .lastName(bookAuthorRequest.getLastName())
                    .build();
            return authorRepository.save(author);
        } else {
            return optionalAuthor.get();
        }
    }
}
