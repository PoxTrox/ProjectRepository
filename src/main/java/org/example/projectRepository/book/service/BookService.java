package org.example.projectRepository.book.service;

import jakarta.transaction.Transactional;
import org.example.projectRepository.author.model.Author;
import org.example.projectRepository.author.service.AuthorService;
import org.example.projectRepository.book.model.Book;
import org.example.projectRepository.book.repository.BookRepository;
import org.example.projectRepository.exception.BookAlreadyExist;
import org.example.projectRepository.exception.DomainException;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.web.dto.BookAuthorRequest;
import org.example.projectRepository.web.dto.BookEditRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;


    @Autowired
    public BookService(BookRepository bookRepository, AuthorService authorService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;

    }


    public void saveBook(BookAuthorRequest bookAuthorRequest, User user) throws DomainException {


        Author author = authorService.findAuthor(bookAuthorRequest.getFirstName(), bookAuthorRequest.getLastName())
                .orElseThrow(() -> new DomainException("Author not found"));

        if (bookRepository.findByTitleAndAuthor(bookAuthorRequest.getTitle(), author).isPresent()) {
            throw new BookAlreadyExist("Book with this title and author already exists");
        }

        Book book = Book.builder()
                .title(bookAuthorRequest.getTitle())
                .price(bookAuthorRequest.getPrice())
                .author(author)
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();

        bookRepository.save(book);
    }

    public List<Book> returnAllBooksSorted(User user, String sortedBy, String direction) {


        if (sortedBy.equals("title") && direction.equals("asc")) {
            return bookRepository.findAllByUserOrderByTitleAsc(user);
        } else if (sortedBy.equals("title") && direction.equals("desc")) {
            return bookRepository.findAllByUserOrderByTitleDesc(user);
        } else if (sortedBy.equals("Author") && direction.equals("asc")) {
            return bookRepository.findAllByUserOrderByAuthorAsc(user);
        } else if (sortedBy.equals("Author") && direction.equals("desc")) {
            return bookRepository.findAllByUserOrderByAuthorDesc(user);
        } else if (sortedBy.equals("price") && direction.equals("asc")) {
            return bookRepository.findAllByUserOrderByPriceAsc(user);
        } else {
            return bookRepository.findAllByUserOrderByPriceDesc(user);
        }

    }

    @Transactional

    public void deleteBookById( UUID id) {

        Book book = bookRepository.findById(id).orElseThrow(() -> new DomainException("Book not found"));
        bookRepository.deleteById(book.getId());
    }


    public Book findById(UUID id) {
        return bookRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Book not found"));
    }

    public void editBook(UUID bookId, BookEditRequest bookEditRequest) {

        Optional<Book> book = bookRepository.findById(bookId);

        Book editBook = book.get();
        Author byId = authorService.findById(book.get().getAuthor().getId());
        editBook.setTitle(bookEditRequest.getTitle());
        editBook.setPrice(bookEditRequest.getPrice());
        editBook.setAuthor(byId);
        bookRepository.save(editBook);
    }

    public List<Book> returnAllBook(User user) {
        return bookRepository.findAllByUserOrderByCreatedAtDesc(user);
    }
}
