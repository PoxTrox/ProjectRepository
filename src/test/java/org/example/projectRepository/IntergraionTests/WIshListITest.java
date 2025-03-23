package org.example.projectRepository.IntergraionTests;


import org.example.projectRepository.media.model.Media;
import org.example.projectRepository.media.model.MediaType;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.user.service.UserService;
import org.example.projectRepository.web.dto.RegisterRequest;
import org.example.projectRepository.web.dto.WishListRequest;
import org.example.projectRepository.wishList.model.TypeEntertainment;
import org.example.projectRepository.wishList.model.WishList;
import org.example.projectRepository.wishList.service.WishListService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class WIshListITest {

    @Autowired
    private WishListService wishListService;

    @Autowired
    private UserService userService;

    @Test
    void tryingToReturnAllMediaSorted(){



        WishListRequest wishListRequest = WishListRequest.builder()
                .title("TitleA")
                .seasons(2)
                .typeEntertainment(TypeEntertainment.TvShow)
                .description("DescriptionA")
                .completed(true)
                .build();

        WishListRequest wishListRequestA = WishListRequest.builder()
                .title("TitleB")
                .seasons(1)
                .typeEntertainment(TypeEntertainment.Movie)
                .description("DescriptionB")
                .completed(false)
                .build();

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("testSubject")
                .password("password")
                .agreeTermOfUse(true)
                .build();

        User registerUser = userService.register(registerRequest);
        wishListService.saveItemToWishlist(wishListRequest,registerUser);
        wishListService.saveItemToWishlist(wishListRequestA,registerUser);

        List<WishList> wishLists = wishListService.returnAllWishListSorted(registerUser, "title", "asc");
        List<WishList> wishLists1 = wishListService.returnAllWishListSorted(registerUser, "title", "desc");
        List<WishList> wishLists2 = wishListService.returnAllWishListSorted(registerUser, "typeEntertainment", "asc");
        List<WishList> wishLists3 = wishListService.returnAllWishListSorted(registerUser, "typeEntertainment", "desc");
        List<WishList> wishLists4 = wishListService.returnAllWishListSorted(registerUser, "completed", "asc");
        List<WishList> wishLists5 = wishListService.returnAllWishListSorted(registerUser, "completed", "desc");



        assertThat(wishLists.size()).isEqualTo(2);
        assertThat(wishLists.get(0).getTitle()).isEqualTo("TitleA");
        assertThat(wishLists.get(1).getTitle()).isEqualTo("TitleB");

        assertThat(wishLists1.size()).isEqualTo(2);
        assertThat(wishLists1.get(0).getTitle()).isEqualTo("TitleB");
        assertThat(wishLists1.get(1).getTitle()).isEqualTo("TitleA");

        assertThat(wishLists2.size()).isEqualTo(2);
        assertThat(wishLists2.get(1).getTypeEntertainment()).isEqualTo(TypeEntertainment.TvShow);
        assertThat(wishLists2.get(0).getTypeEntertainment()).isEqualTo(TypeEntertainment.Movie);

        assertThat(wishLists3.size()).isEqualTo(2);
        assertThat(wishLists2.get(1).getTypeEntertainment()).isEqualTo(TypeEntertainment.TvShow);
        assertThat(wishLists2.get(0).getTypeEntertainment()).isEqualTo(TypeEntertainment.Movie);

        assertThat(wishLists4.size()).isEqualTo(2);
        assertThat(wishLists2.get(0).isCompleted()).isEqualTo(false);
        assertThat(wishLists2.get(1).isCompleted()).isEqualTo(false);

        assertThat(wishLists5.size()).isEqualTo(2);
        assertThat(wishLists2.get(0).isCompleted()).isEqualTo(false);
        assertThat(wishLists2.get(1).isCompleted()).isEqualTo(false);




    }
}
