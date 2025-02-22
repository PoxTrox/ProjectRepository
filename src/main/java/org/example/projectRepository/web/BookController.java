package org.example.projectRepository.web;

import jakarta.validation.Valid;
import org.example.projectRepository.author.service.AuthorService;
import org.example.projectRepository.book.model.Book;
import org.example.projectRepository.book.service.BookService;
import org.example.projectRepository.security.AuthenticationDetails;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.user.service.UserService;
import org.example.projectRepository.web.dto.AuthorRequest;
import org.example.projectRepository.web.dto.BookAuthorRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller()
@RequestMapping("/books")
public class BookController {

    private final UserService userService;
    private final BookService bookService;
    private final AuthorService authorService;

    @Autowired
    public BookController(UserService userService, BookService bookService, AuthorService authorService) {
        this.userService = userService;
        this.bookService = bookService;
        this.authorService = authorService;
    }


    @GetMapping("/get")
    public ModelAndView getBooksPage(@AuthenticationPrincipal AuthenticationDetails details) {

        ModelAndView mav = new ModelAndView();

        User user = userService.getById(details.getUserId());
        List<Book> books = user.getBooks();
        mav.addObject("user", user);
        mav.addObject("booksList", books);
        mav.setViewName("books");
        return mav;
    }


    @GetMapping("/add")
    public ModelAndView addBookPage(@AuthenticationPrincipal AuthenticationDetails details) {
        ModelAndView mav = new ModelAndView();
        User user = userService.getById(details.getUserId());

        mav.setViewName("addBook");
        mav.addObject("bookAuthorRequest", new BookAuthorRequest());
        mav.addObject("user", user);
        mav.addObject("authorRequest", AuthorRequest.builder().build());
        return mav;
    }
    @PostMapping("/add")
    public String addBook(@Valid  BookAuthorRequest bookAuthorRequest, @Valid AuthorRequest authorRequest,   BindingResult bindingResult ,  @AuthenticationPrincipal AuthenticationDetails details) {


        User user = userService.getById(details.getUserId());


        if (bindingResult.hasErrors()) {
            return "addBook";
        }


        bookService.saveBook(bookAuthorRequest,user,authorRequest);
        return "redirect:/books/get";
    }

}
