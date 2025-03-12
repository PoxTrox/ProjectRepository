package org.example.projectRepository.web;

import jakarta.validation.Valid;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.user.service.UserService;
import org.example.projectRepository.web.dto.ProfileEditRequest;
import org.example.projectRepository.web.mapper.DtoMapper;
import org.example.projectRepository.wishList.model.WishList;
import org.example.projectRepository.wishList.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(value = "/users")

public class UserController {

    private final UserService userService;
    private final WishListService wishListService;

    @Autowired
    public UserController(UserService userService, WishListService wishListService) {
        this.userService = userService;
        this.wishListService = wishListService;
    }

    @GetMapping("/{id}/profile")
    public ModelAndView profilePage (@PathVariable UUID id) {




        ModelAndView modelAndView = new ModelAndView();
        User user = userService.getById(id);
        modelAndView.addObject("user", user);
        modelAndView.setViewName("editProfile");
        modelAndView.addObject("profileEdit", DtoMapper.mapToProfileEditRequest(user));
        return modelAndView;
    }

    @PutMapping("/{id}/profile")
    public ModelAndView editProfilePage (@PathVariable UUID id, @Valid  ProfileEditRequest profileEditRequest, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            System.out.println("has some errors"+ bindingResult.getAllErrors());
            User user = userService.getById(id);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("user", user);
            modelAndView.setViewName("editProfile");
            modelAndView.addObject("profileEdit", profileEditRequest);
            return modelAndView;
        }
        userService.editProfileUser(id, profileEditRequest);
        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/getAllUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView getAllUsers() {
        ModelAndView modelAndView = new ModelAndView();
        List<User> users = userService.getAllUsers();
        modelAndView.addObject("users", users);
        modelAndView.setViewName("getAllUsers");
        return modelAndView;
    }

    @PutMapping("{id}/status")
    public String changeStatusUser(@PathVariable UUID id) {

        userService.changeStatus(id);
        return "redirect:/users/getAllUsers";
    }

    @GetMapping("{id}/getWishList")
    public ModelAndView getWishList(@PathVariable UUID id) {
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.getById(id);
        List<WishList> wishLists = wishListService.returnWishList(user);
        modelAndView.addObject("user", user);
        modelAndView.addObject("wishList", wishLists);
        modelAndView.setViewName("userDescriptionPage");
        return modelAndView;
    }

}
