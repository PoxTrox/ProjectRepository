package org.example.projectRepository.web;

import jakarta.validation.Valid;
import org.example.projectRepository.author.service.AuthorService;
import org.example.projectRepository.book.model.Book;
import org.example.projectRepository.book.service.BookService;
import org.example.projectRepository.security.AuthenticationDetails;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.user.service.UserService;
import org.example.projectRepository.web.dto.BookAuthorRequest;
import org.example.projectRepository.web.dto.BookEditRequest;
import org.example.projectRepository.web.mapper.BookDtoMapper;
import org.example.projectRepository.web.mapper.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import java.util.List;
import java.util.UUID;


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
    public ModelAndView getBooksPage(@AuthenticationPrincipal AuthenticationDetails details, @RequestParam(name = "sort", defaultValue = "title") String sortField,
                                     @RequestParam(name = "direction", defaultValue = "asc") String sortDirection) {

        ModelAndView mav = new ModelAndView();
        User user = userService.getById(details.getUserId());

        List<Book> sortedList = bookService.returnAllBooksSorted(user, sortField, sortDirection);


        mav.addObject("user", user);
        mav.addObject("books", sortedList);
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
        return mav;
    }

    @PostMapping("/add")
    public String addBook(@Valid BookAuthorRequest bookAuthorRequest, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationDetails details) {

        User user = userService.getById(details.getUserId());

        if (bindingResult.hasErrors()) {
            return "addBook";
        }

        authorService.createAuthor(bookAuthorRequest);
        bookService.saveBook(bookAuthorRequest, user);
        return "redirect:/books/get";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteBook(@PathVariable UUID id) {
        bookService.deleteBookById(id);
        return "redirect:/books/get";
    }


    @GetMapping("/{id}/edit")
    public ModelAndView editBookPage(@PathVariable UUID id) {

        Book bookById = bookService.findById(id);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("BookEditPage");
        mav.addObject("bookById", bookById);
        mav.addObject("bookEditRequest", BookDtoMapper.bookToBookEditRequest(bookById));
        return mav;

    }

    @PutMapping("/{id}/edit")
    public ModelAndView editBook(@PathVariable UUID id, @Valid BookEditRequest bookEditRequest, BindingResult bindingResult) {

        ModelAndView mav = new ModelAndView();
        Book byId = bookService.findById(id);
        if (bindingResult.hasErrors()) {
            mav.addObject("bookEditRequest", bookEditRequest);
            mav.setViewName("BookEditPage");
            mav.addObject("bookById", byId);
            return mav;
        }
        bookService.editBook(id, bookEditRequest);
        return new ModelAndView("redirect:/books/get");

    }

}
