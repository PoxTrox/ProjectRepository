package org.example.projectRepository.web;

import jakarta.validation.Valid;
import org.example.projectRepository.security.AuthenticationDetails;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.user.service.UserService;
import org.example.projectRepository.web.dto.MediaTvshowRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/Media")
public class MediaController {

    private final UserService userService;

    @Autowired
    public MediaController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/movie/tvshow")
    public ModelAndView getMediaPage(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.getById(authenticationDetails.getUserId());
        modelAndView.addObject("user", user);
        modelAndView.setViewName("movie-TvshowMainPage");

        return modelAndView;
    }



    @GetMapping("/movie/tvshow/add")
    public ModelAndView addMediaPage(@AuthenticationPrincipal AuthenticationDetails details, MediaTvshowRequest mediaTvshowRequest){

        User user = userService.getById(details.getUserId());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("movie-Tvshow-add");
        modelAndView.addObject("user", user);
        modelAndView.addObject("mediaTvshowRequest",   new MediaTvshowRequest());
        return modelAndView;
    }

    @PostMapping("/movie/tvshow/add")
    public ModelAndView postMediaPage(@Valid MediaTvshowRequest mediaTvshowRequest, @AuthenticationPrincipal AuthenticationDetails authenticationDetails,
                                      BindingResult bindingResult) {

        ModelAndView modelAndView = new ModelAndView();
        User user = userService.getById(authenticationDetails.getUserId());
        if(bindingResult.hasErrors()) {
            modelAndView.setViewName("movie-Tvshow-add");
            modelAndView.addObject("mediaTvshowRequest",mediaTvshowRequest);
            modelAndView.addObject("user", user);
            return modelAndView;
        }



        return new ModelAndView("redirect:Media/movie/tvshow");
    }


}
