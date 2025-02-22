package org.example.projectRepository.book.repository;

import org.example.projectRepository.author.model.Author;
import org.example.projectRepository.book.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {

    Optional<Book> findByTitle(String title);

    List<Book> findByAuthor(Author author);
}
