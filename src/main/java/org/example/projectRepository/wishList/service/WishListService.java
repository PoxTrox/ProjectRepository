package org.example.projectRepository.wishList.service;

import org.example.projectRepository.exception.DomainException;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.user.service.UserService;
import org.example.projectRepository.web.dto.WishListEditRequest;
import org.example.projectRepository.web.dto.WishListRequest;
import org.example.projectRepository.wishList.model.WishList;
import org.example.projectRepository.wishList.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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

        if (sortedBy.equals("title") && direction.equals("asc")) {
            return wishlistRepository.findAllByUserOrderByTitleAsc(user);
        } else if (sortedBy.equals("title") && direction.equals("desc")) {
            return wishlistRepository.findAllByUserOrderByTitleDesc(user);
        } else if (sortedBy.equals("typeEntertainment") && direction.equals("asc")) {
            return wishlistRepository.findAllByUserOrderByTypeEntertainmentAsc(user);
        } else if (sortedBy.equals("typeEntertainment") && direction.equals("desc")) {
            return wishlistRepository.findAllByUserOrderByTypeEntertainmentDesc(user);
        } else if (sortedBy.equals("creationDate") && direction.equals("asc")) {
            return wishlistRepository.findAllByUserOrderByCreationDateAsc(user);
        } else if (sortedBy.equals("creationDate") && direction.equals("desc")) {
            return wishlistRepository.findAllByUserOrderByCreationDateDesc(user);
        } else if (sortedBy.equals("completed") && direction.equals("asc")) {
            return wishlistRepository.findAllByUserOrderByCompletedAsc(user);
        } else {
            return wishlistRepository.findAllByUserOrderByCompletedDesc(user);
        }

    }

    public WishList findById(UUID id) {
        return wishlistRepository.findById(id).orElseThrow(() -> new DomainException("Wished Media with %s not found".formatted(id)));
    }


    public void editWishListData(UUID id, WishListEditRequest wishListEditRequest) {

        WishList editWishDat = findById(id);

        editWishDat.setTitle(wishListEditRequest.getTitle());
        editWishDat.setDescription(wishListEditRequest.getDescription());
        editWishDat.setTypeEntertainment(wishListEditRequest.getTypeEntertainment());
        editWishDat.setSeasons(wishListEditRequest.getSeasons());
        editWishDat.setCreationDate(LocalDate.now());
        editWishDat.setCompleted(wishListEditRequest.isCompleted());

        wishlistRepository.save(editWishDat);

    }

    public List<WishList> returnWishList(User user) {

        return wishlistRepository.findAllByUser(user);
    }

    public List<WishList> returnWishListCompleted() {

        return wishlistRepository.findAllByCompletedTrue();
    }

}
