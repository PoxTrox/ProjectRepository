package org.example.projectRepository.book.service;
import org.example.projectRepository.author.model.Author;
import org.example.projectRepository.author.service.AuthorService;
import org.example.projectRepository.book.model.Book;
import org.example.projectRepository.book.repository.BookRepository;
import org.example.projectRepository.exception.DomainException;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.web.dto.BookAuthorRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;


    @Autowired
    public BookService(BookRepository bookRepository, AuthorService authorService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
    }

    public void createBook(BookAuthorRequest bookAuthorRequest , User user )  {

        Optional<Book> byTitle = bookRepository.findByTitle(bookAuthorRequest.getTitle());

        if(byTitle.isPresent()) {
            throw new RuntimeException("Title already exists");
        }
        Book book = Book.builder()
                .title(bookAuthorRequest.getTitle())
                .price(bookAuthorRequest.getPrice())
                .user(user)
                .build();
        bookRepository.save(book);


    }

    public void saveBook(BookAuthorRequest bookAuthorRequest , User user ) throws DomainException {

//

        Optional<Author> author1 = authorService.findAuthor(bookAuthorRequest.getFirstName(),bookAuthorRequest.getLastName());

        if(author1.isEmpty()) {
            Author newAuthor = new Author();
            newAuthor.setFirstName(bookAuthorRequest.getFirstName());
            newAuthor.setLastName(bookAuthorRequest.getLastName());
            authorService.save(newAuthor);
        }

        Book book = new Book();
        book.setTitle(bookAuthorRequest.getTitle());
        book.setPrice(bookAuthorRequest.getPrice());
        book.setAuthor(author1.get());
        book.setUser(user);

        bookRepository.save(book);
    }


}
