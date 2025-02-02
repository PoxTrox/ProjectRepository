package org.example.projectRepository.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class indexController {


    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("register-demo");
        return mav;

    }

    @GetMapping("/login")
    public ModelAndView getLoginPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("loginDemo");
        return mav;
    }


}
