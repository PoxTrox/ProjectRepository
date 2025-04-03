package org.example.projectRepository.web;

import org.example.projectRepository.media.Service.MediaService;
import org.example.projectRepository.media.model.Media;
import org.example.projectRepository.search.SearchClient;
import org.example.projectRepository.search.dto.RestMediaResponse;
import org.example.projectRepository.search.service.SearchService;
import org.example.projectRepository.security.AuthenticationDetails;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.user.service.UserService;
import org.example.projectRepository.web.dto.RestMediaRequest;
import org.example.projectRepository.wishList.service.WishListService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/clientSearch")
public class SearchController {

    private final UserService userService;
    private final SearchClient searchClient;
    private final MediaService mediaService;
    private final SearchService searchService;
    private final WishListService wishListService;

    public SearchController(UserService userService, SearchClient searchClient, MediaService mediaService, SearchService searchService, WishListService wishListService) {
        this.userService = userService;
        this.searchClient = searchClient;
        this.mediaService = mediaService;
        this.searchService = searchService;
        this.wishListService = wishListService;
    }

    @GetMapping()
    public ModelAndView search(@AuthenticationPrincipal AuthenticationDetails details) {
        ModelAndView modelAndView = new ModelAndView("ClientSearchPage");
        User user = userService.getById(details.getUserId());
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @GetMapping("/Dmdb")
    public ModelAndView searchInDMDB(@AuthenticationPrincipal AuthenticationDetails details) {
        ModelAndView modelAndView = new ModelAndView("searchPage");
        User user = userService.getById(details.getUserId());
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @GetMapping("/titleInRepository")
    public ModelAndView searchByTitleInRepository(@AuthenticationPrincipal AuthenticationDetails details,  @RequestParam(name = "title") String title) {
        ModelAndView modelAndView = new ModelAndView("searchPage");
        User user = userService.getById(details.getUserId());
     //   List<RestMediaResponse> restMediaResponses = searchClient.searchMovies(title);
      //  ResponseEntity<List<RestMediaResponse>> response = searchClient.searchMovies(title);
        List<RestMediaResponse> restMediaResponses = searchService.searchMovieByTitle(title);


        System.out.println("movie found :"+ restMediaResponses.size());
        modelAndView.addObject("restMediaResponses", restMediaResponses);
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @GetMapping("/title")
    public ModelAndView searchByTitleInDtMb(@AuthenticationPrincipal AuthenticationDetails details, @RequestParam(name = "title") String title) {
        ModelAndView modelAndView = new ModelAndView("ClientSearchPage");
        User user = userService.getById(details.getUserId());

        List<RestMediaResponse> search = searchService.search(user, title);


        System.out.println("movie found :" + search.size());
        modelAndView.addObject("mediaTvshowRequest", new RestMediaRequest());
        modelAndView.addObject("search", search);
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @PostMapping("/saveMedia")
    public ModelAndView saveMedia( RestMediaRequest request,@AuthenticationPrincipal AuthenticationDetails details) {

        User user = userService.getById(details.getUserId());


        Media media1 = searchService.mediaByTitleAndReleaseDate(request.getTitle(), request.getReleaseDate());

        mediaService.saveMediaFromRest(media1,user);
        return new ModelAndView("redirect:/Media/movie/tvshow");
    }

    @PostMapping("/saveMediaToWishList")
    public ModelAndView saveMediaToWishList( RestMediaRequest request,@AuthenticationPrincipal AuthenticationDetails details) {

        User user = userService.getById(details.getUserId());

        Media media1 = searchService.mediaByTitleAndReleaseDate(request.getTitle(), request.getReleaseDate());

        wishListService.saveMediaFromRest(media1,user);

        return new ModelAndView("redirect:/wishlist");
    }

}