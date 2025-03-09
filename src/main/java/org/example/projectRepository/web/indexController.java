package org.example.projectRepository.web;

import jakarta.validation.Valid;
import org.example.projectRepository.book.model.Book;
import org.example.projectRepository.book.service.BookService;
import org.example.projectRepository.media.Service.MediaService;
import org.example.projectRepository.media.model.Media;
import org.example.projectRepository.security.AuthenticationDetails;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.user.service.UserService;
import org.example.projectRepository.web.dto.LoginRequest;
import org.example.projectRepository.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Stream;


@Controller
@RequestMapping("/")
public class indexController {

    private final UserService userService;
    private final MediaService mediaService;
    private final BookService bookService;


    @Autowired
    public indexController(UserService userService, MediaService mediaService, BookService bookService) {
        this.userService = userService;
        this.mediaService = mediaService;
        this.bookService = bookService;
    }


    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        modelAndView.addObject("registerRequest", new RegisterRequest());
        return modelAndView;

    }

    @PostMapping("/register")
    public ModelAndView postRegister(@Valid RegisterRequest registerRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("register");
        }
        userService.register(registerRequest);
        return new ModelAndView("redirect:/login");


    }

    @GetMapping("/login")
    public ModelAndView getLoginPage(@RequestParam(value = "error",required = false) String errorParam) {


        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject("loginRequest", new LoginRequest());

        if(errorParam != null) {
            modelAndView.addObject("errorMessage", "Invalid username or password!");
        }


        return modelAndView;
    }



    @GetMapping("/home")
    public ModelAndView getHomePage(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        ModelAndView modelAndView = new ModelAndView();

        User user = userService.getById(authenticationDetails.getUserId());
        List<Media> media = mediaService.returnAllMedia(user);
        List<Book> books = bookService.returnAllBook(user);


        modelAndView.addObject("user", user);
        modelAndView.addObject("media", media);
        modelAndView.addObject("books", books);
        modelAndView.setViewName("home");
        return modelAndView;
    }




    @GetMapping("/TermOfUse")
    public ModelAndView getTermOfUsePage() {

        return new ModelAndView("termsOfUse");
    }


}
