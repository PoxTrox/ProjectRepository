package org.example.projectRepository.web.mapper;

import lombok.experimental.UtilityClass;
import org.example.projectRepository.web.dto.WishListEditRequest;
import org.example.projectRepository.wishList.model.WishList;

@UtilityClass
public class WishListDtoMapper {

    public static WishListEditRequest mapWishlistDtoToWishListEditRequest(WishList wishlist) {

        return  WishListEditRequest.builder()
                .title(wishlist.getTitle())
                .typeEntertainment(wishlist.getTypeEntertainment())
                .completed(wishlist.isCompleted())
                .seasons(wishlist.getSeasons())
                .description(wishlist.getDescription())
                .build();
    }



}
