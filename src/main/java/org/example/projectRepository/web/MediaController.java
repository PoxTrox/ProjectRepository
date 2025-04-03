package org.example.projectRepository.web;

import jakarta.validation.Valid;

import org.example.projectRepository.media.Service.MediaService;
import org.example.projectRepository.media.model.Media;
import org.example.projectRepository.search.SearchClient;
import org.example.projectRepository.search.service.SearchService;
import org.example.projectRepository.security.AuthenticationDetails;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.user.service.UserService;
import org.example.projectRepository.web.dto.MediaTvshowRequest;
import org.example.projectRepository.web.dto.MovieTvShowEditRequest;
import org.example.projectRepository.web.dto.RestMediaRequest;
import org.example.projectRepository.web.mapper.MovieTvshowDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/Media")
public class MediaController {

    private final UserService userService;
    private final MediaService mediaService;
    private final SearchClient searchClient;

    @Autowired
    public MediaController(UserService userService, MediaService mediaService, SearchClient searchClient) {
        this.userService = userService;
        this.mediaService = mediaService;
        this.searchClient = searchClient;


    }


    @GetMapping("/movie/tvshow")
    public ModelAndView getMediaPage(@AuthenticationPrincipal AuthenticationDetails authenticationDetails, @RequestParam(name = "sort", defaultValue = "title") String sortField,
                                     @RequestParam(name = "direction", defaultValue = "asc") String sortDirection) {
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.getById(authenticationDetails.getUserId());

        List<Media> media = mediaService.returnAllMediaSorted(user, sortField, sortDirection);
        modelAndView.addObject("shows", media);
        modelAndView.addObject("user", user);
        modelAndView.setViewName("movie-TvshowMainPage");

        return modelAndView;
    }

    //Media/movie/tvshow/add
    @GetMapping("/movie/tvshow/add")
    public ModelAndView addMediaPage(@AuthenticationPrincipal AuthenticationDetails details) {

        User user = userService.getById(details.getUserId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("movie-Tvshow-add");
        modelAndView.addObject("user", user);
        modelAndView.addObject("mediaTvshowRequest", new MediaTvshowRequest());
        return modelAndView;
    }

    @PostMapping("/movie/tvshow/add")
    public ModelAndView postMediaPage(@Valid MediaTvshowRequest mediaTvshowRequest, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {

        ModelAndView modelAndView = new ModelAndView();
        User user = userService.getById(authenticationDetails.getUserId());
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("movie-Tvshow-add");
            modelAndView.addObject("mediaTvshowRequest", mediaTvshowRequest);
            modelAndView.addObject("user", user);
            return modelAndView;
        }
        mediaService.saveMedia(mediaTvshowRequest, user);

        return new ModelAndView("redirect:/Media/movie/tvshow");
    }


    @GetMapping("/{id}/edit")
    public ModelAndView editMediaTvShow(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {

        ModelAndView modelAndView = new ModelAndView();

        Media media = mediaService.findById(id);
        User user = userService.getById(authenticationDetails.getUserId());

        modelAndView.addObject("user", user);
        modelAndView.addObject("media", media);
        modelAndView.addObject("movieTvShowEditRequest", MovieTvshowDtoMapper.mapToMovieTvShowEditRequest(media));
        modelAndView.setViewName("movie-TvshowEditPage");

        return modelAndView;
    }


    @PutMapping("/{id}/edit")
    public ModelAndView postEditMediaTvShow(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationDetails authenticationDetails, @Valid MovieTvShowEditRequest movieTvShowEditRequest,
                                            BindingResult bindingResult) {

        ModelAndView modelAndView = new ModelAndView();
        Media byId = mediaService.findById(id);
        User user = userService.getById(authenticationDetails.getUserId());

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("movie-TvshowEditPage");
            modelAndView.addObject("media", byId);
            modelAndView.addObject("movieTvShowEditRequest", movieTvShowEditRequest);
            modelAndView.addObject("user", user);
            return modelAndView;
        }

        mediaService.editMedia(id, movieTvShowEditRequest);
        return new ModelAndView("redirect:/Media/movie/tvshow");
    }

    @DeleteMapping("/{id}/delete")
    public String deleteMedia(@PathVariable UUID id) {

        mediaService.removeBy(id);
        return "redirect:/Media/movie/tvshow";
    }

}
