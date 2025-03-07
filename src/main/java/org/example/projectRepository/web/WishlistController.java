package org.example.projectRepository.web;

import jakarta.validation.Valid;
import org.example.projectRepository.security.AuthenticationDetails;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.user.service.UserService;
import org.example.projectRepository.web.dto.WishListEditRequest;
import org.example.projectRepository.web.dto.WishListRequest;
import org.example.projectRepository.web.mapper.WishListDtoMapper;
import org.example.projectRepository.wishList.model.WishList;
import org.example.projectRepository.wishList.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/wishlist")
public class WishlistController {

    private final UserService userService;
    private final WishListService wishListService;

    @Autowired
    public WishlistController(UserService userService, WishListService wishListService) {
        this.userService = userService;
        this.wishListService = wishListService;
    }

    @GetMapping
    public ModelAndView wishlistMainPage(@AuthenticationPrincipal AuthenticationDetails authenticationDetails, @RequestParam(name = "sort", defaultValue = "title") String sortField,
                                         @RequestParam(name = "direction", defaultValue = "asc") String sortDirection) {

        User user = userService.getById(authenticationDetails.getUserId());
        ModelAndView modelAndView = new ModelAndView("wishList");
        List<WishList> wishLists = wishListService.returnAllWishListSorted(user, sortField, sortDirection);
        modelAndView.addObject("user", user);
        modelAndView.addObject("wishLists", wishLists);


        return modelAndView;
    }


    @GetMapping("/add")
    public ModelAndView wishlistAddPage(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        User user = userService.getById(authenticationDetails.getUserId());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("wishListRequest", new WishListRequest());
        modelAndView.addObject("user", user);
        modelAndView.setViewName("addWishList");
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView addItemInWishList(@AuthenticationPrincipal AuthenticationDetails authenticationDetails,
                                          @Valid WishListRequest wishListRequest, BindingResult bindingResult) {

        ModelAndView modelAndView = new ModelAndView();
        User user = userService.getById(authenticationDetails.getUserId());
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("wishListRequest", wishListRequest);
            modelAndView.addObject("user", user);
            modelAndView.setViewName("addWishList");

        }
        wishListService.saveItemToWishlist(wishListRequest, user);
        return new ModelAndView("redirect:/wishlist");
    }

    @GetMapping("/{id}/edit")
    public ModelAndView editWishListMedia(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {

        User user = userService.getById(authenticationDetails.getUserId());
        WishList byId = wishListService.findById(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("wishList",byId);
        modelAndView.addObject("wishlistEdit", WishListDtoMapper.mapWishlistDtoToWishListEditRequest(byId));
        modelAndView.addObject("user", user);
        modelAndView.setViewName("editWishList");
        return modelAndView;
    }

    @PutMapping("/{id}/edit")
    public ModelAndView postEditWishList(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationDetails authenticationDetails,  @Valid WishListEditRequest wishListEditRequest, BindingResult bindingResult) {

        ModelAndView modelAndView = new ModelAndView();
        WishList byId = wishListService.findById(id);
        User user = userService.getById(authenticationDetails.getUserId());
        if(bindingResult.hasErrors()) {
            modelAndView .addObject("user", user);
            modelAndView.addObject("wishListEdit", wishListEditRequest);
            modelAndView.addObject("wishList", byId);
            modelAndView.setViewName("editWishList");
        }
        wishListService.editWishListData(id,wishListEditRequest);

        return new ModelAndView("redirect:/wishlist");


    }

    @GetMapping("/{id}/description")
    public ModelAndView getDescription(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {

        User user = userService.getById(authenticationDetails.getUserId());
        WishList wishItem = wishListService.findById(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.addObject("wishList", wishListService.findById(id));
        modelAndView.addObject("wishItem", wishItem);
        modelAndView.setViewName("modelWindow");
        return modelAndView;
    }


}
