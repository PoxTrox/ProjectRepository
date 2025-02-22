package org.example.projectRepository.author.service;

import org.example.projectRepository.author.model.Author;
import org.example.projectRepository.author.repository.AuthorRepository;
import org.example.projectRepository.exception.DomainException;
import org.example.projectRepository.web.dto.AuthorRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorService {

    private  final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author findAuthorFirstAndLastName(String firstName, String lastName) throws DomainException {

        return authorRepository.findByFirstNameAndLastName(firstName, lastName).orElseThrow( () -> new DomainException( "Author not found" ) );
    }

    public void createAuthor(AuthorRequest authorRequest) throws DomainException {

        Optional<Author> authorOptional = authorRepository.findByFirstNameAndLastName(authorRequest.getFirstName(), authorRequest.getLastName());

        if (authorOptional.isPresent()) {
            throw new DomainException("Author already exists");
        }
        Author author = Author.builder()
                .firstName(authorRequest.getFirstName())
                .lastName(authorRequest.getLastName())
                .build();

        authorRepository.save(author);
    }


}
