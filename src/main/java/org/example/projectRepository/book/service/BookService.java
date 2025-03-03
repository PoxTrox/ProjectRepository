package org.example.projectRepository.book.service;

import jakarta.transaction.Transactional;
import org.example.projectRepository.author.model.Author;
import org.example.projectRepository.author.service.AuthorService;
import org.example.projectRepository.book.model.Book;
import org.example.projectRepository.book.repository.BookRepository;
import org.example.projectRepository.exception.DomainException;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.web.dto.BookAuthorRequest;
import org.example.projectRepository.web.dto.BookEditRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

        Optional<Author> author1 = authorService.findAuthor(bookAuthorRequest.getFirstName(), bookAuthorRequest.getLastName());

        if (author1.isEmpty()) {
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

    public List<Book> returnAllBooksSorted(User user, String sortedBy, String direction) {


        if (sortedBy.equals("title") && direction.equals("asc")) {
            return bookRepository.findAllByUserOrderByTitleAsc(user);
        } else if (sortedBy.equals("title") && direction.equals("desc")) {
            return bookRepository.findAllByUserOrderByTitleDesc(user);
        }else if(sortedBy.equals("Author") && direction.equals("asc")) {
            return bookRepository.findAllByUserOrderByAuthorAsc(user);
        }else if(sortedBy.equals("Author") && direction.equals("desc")) {
            return bookRepository.findAllByUserOrderByAuthorDesc(user);
        }else if(sortedBy.equals("price") && direction.equals("asc")) {
            return bookRepository.findAllByUserOrderByPriceAsc(user);
        }else  {
            return bookRepository.findAllByUserOrderByPriceDesc(user);
        }

    }
    @Transactional
    @Modifying
    @Query("DELETE FROM Book b WHERE b.id = :id")
    public void deleteBookById(UUID id) {
        Book byId = findById(id);
        if (byId != null) {
            bookRepository.delete(byId);
        }
        throw new DomainException("Book with id " + id + " not found");
    }


    public Book findById(UUID id) {
        return bookRepository.findById(id).orElseThrow(() -> new DomainException("Book not found"));
    }

    public void editBook(UUID id, BookEditRequest bookEditRequest) {

        Optional<Book> book = bookRepository.findById(id);

        Book editBook = book.get();
        Author byId = authorService.findById(book.get().getAuthor().getId());
        editBook.setTitle(bookEditRequest.getTitle());
        editBook.setPrice(bookEditRequest.getPrice());
        editBook.setAuthor(byId);
        bookRepository.save(editBook);
    }
}
