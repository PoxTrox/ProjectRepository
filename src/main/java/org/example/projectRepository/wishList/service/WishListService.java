package org.example.projectRepository.wishList.service;

import org.example.projectRepository.exception.DomainException;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.user.service.UserService;
import org.example.projectRepository.web.dto.WishListRequest;
import org.example.projectRepository.wishList.model.WishList;
import org.example.projectRepository.wishList.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WishListService {

    private final WishlistRepository wishlistRepository;
    private final UserService userService;


    @Autowired
    public WishListService(WishlistRepository wishlistRepository, UserService userService) {
        this.wishlistRepository = wishlistRepository;
        this.userService = userService;
    }


    public void saveItemToWishlist(WishListRequest wishListRequest, User user) {

        User user1 = userService.getById(user.getId());


        WishList newWishMedia = WishList.builder()
                .title(wishListRequest.getTitle())
                .description(wishListRequest.getDescription())
                .completed(false)
                .creationDate(LocalDate.now())
                .typeEntertainment(wishListRequest.getTypeEntertainment())
                .user(user1)
                .build();
        wishlistRepository.save(newWishMedia);
    }

    public List<WishList> returnAllWishListSorted(User user, String sortedBy, String direction) {

        if(sortedBy.equals("title")&&direction.equals("asc")) {
            return wishlistRepository.findAllByUserOrderByTitleAsc(user);
        }else if(sortedBy.equals("title")&&direction.equals("desc")) {
            return wishlistRepository.findAllByUserOrderByTitleDesc(user);
        }else if(sortedBy.equals("typeEntertainment")&&direction.equals("asc")) {
            return wishlistRepository.findAllByUserOrderByTypeEntertainmentAsc(user);
        }else if(sortedBy.equals("typeEntertainment")&&direction.equals("desc")) {
            return wishlistRepository.findAllByUserOrderByTypeEntertainmentDesc(user);
        } else if (sortedBy.equals("creationDate")&&direction.equals("asc")) {
            return wishlistRepository.findAllByUserOrderByCreationDateAsc(user);
        }else if(sortedBy.equals("creationDate")&&direction.equals("desc")) {
            return wishlistRepository.findAllByUserOrderByCreationDateDesc(user);
        }else if(sortedBy.equals("completed")&&direction.equals("asc")) {
            return wishlistRepository.findAllByUserOrderByCompletedAsc(user);
        }else {
            return wishlistRepository.findAllByUserOrderByCompletedDesc(user);
        }

    }
}
