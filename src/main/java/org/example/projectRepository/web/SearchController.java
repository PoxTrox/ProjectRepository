package org.example.projectRepository.web;

import org.example.projectRepository.media.Service.MediaService;
import org.example.projectRepository.media.model.Media;
import org.example.projectRepository.search.SearchClient;
import org.example.projectRepository.search.dto.RestMediaResponse;
import org.example.projectRepository.security.AuthenticationDetails;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.user.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/clientSearch")
public class SearchController {

    private final UserService userService;
    private final SearchClient searchClient;
    private final MediaService mediaService;

    public SearchController(UserService userService, SearchClient searchClient, MediaService mediaService) {
        this.userService = userService;
        this.searchClient = searchClient;
        this.mediaService = mediaService;
    }

//    @GetMapping()
//    public ModelAndView search(@AuthenticationPrincipal AuthenticationDetails authenticationDetails )
//    {
//        List<Media> mediaList = new ArrayList<>();
//        ModelAndView modelAndView = new ModelAndView();
//        User user = userService.getById(authenticationDetails.getUserId());
//        modelAndView.addObject("user", user);
//        modelAndView.addObject("mediaList", mediaList);
//        modelAndView.addObject("mediaTvshowRequest", new RestMediaResponse());
//        modelAndView.setViewName("searchPage");
//        return modelAndView;
//    }
//    @GetMapping("/search/byTitle")
//    public ModelAndView searchResult(@AuthenticationPrincipal AuthenticationDetails authenticationDetails
//            , @RequestParam(name = "title") String title) {
//         List<RestMediaResponse> mediaList = searchClient.searchAndSaveMedia(title);
//      //  List<RestMediaResponse> mediaList = searchClient.searchMedia(title);
////        List<RestMediaResponse> mediaList= searchClient.returnMovies(title);
//        ModelAndView modelAndView = new ModelAndView();
//        User user = userService.getById(authenticationDetails.getUserId());
//        modelAndView.addObject("user", user);
//        modelAndView.addObject("mediaList", mediaList);
//        modelAndView.setViewName("searchPage");
//        return modelAndView;
//    }
//
//    @PostMapping("/search")
//    public ModelAndView saveMediaByTitle(  RestMediaResponse request, @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
//        Media media = searchClient.mediaByTitleAndReleaseDate(request.getTitle(), request.getRelease_date());
//
//        User user = userService.getById(authenticationDetails.getUserId());
//
//
//        mediaService.saveMediaFromRest(media,user);
//        return new ModelAndView("redirect:/Media/movie/tvshow");
//
//    }

}