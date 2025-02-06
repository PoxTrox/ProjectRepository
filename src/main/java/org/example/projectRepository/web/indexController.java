package org.example.projectRepository.web;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.user.service.UserService;
import org.example.projectRepository.web.dto.LoginRequest;
import org.example.projectRepository.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/")
public class indexController {

    private final UserService userService;

    @Autowired
    public indexController(UserService userService) {
        this.userService = userService;
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
    public ModelAndView getLoginPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject("loginRequest", new LoginRequest());
        return modelAndView;
    }

    @PostMapping("login")
    public String postLogin(@Valid LoginRequest loginRequest, BindingResult bindingResult,HttpSession session) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> System.out.println(error.toString()));
            return "login";
        }

        User login = userService.login(loginRequest);
        session.setAttribute("user_id", login.getId());


        return "redirect:/home";
    }

    @GetMapping("/home")
    public ModelAndView getHomePage(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        UUID userId = (UUID) session.getAttribute("user_id");
        User user = userService.getById(UUID.fromString(userId.toString()));
        modelAndView.addObject("user", user);
        modelAndView.setViewName("home");
        return modelAndView;
    }
    @GetMapping("/logout")
    public String getLogoutPage(HttpSession session) {

        session.invalidate();

        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logoutPost(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }


}
