package org.example.projectRepository.author.service;

import org.example.projectRepository.author.model.Author;
import org.example.projectRepository.author.repository.AuthorRepository;
import org.example.projectRepository.exception.DomainException;
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


   public Optional<Author> findAuthor (String firstName, String lastName) throws DomainException {

        return authorRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    public void save(Author newAuthor) {
        authorRepository.save(newAuthor);
    }



}
