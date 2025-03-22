package org.example.projectRepository.wishList;


import org.assertj.core.api.Assertions;
import org.example.projectRepository.exception.DomainException;
import org.example.projectRepository.exception.WishListItemAlreadyExist;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.user.service.UserService;
import org.example.projectRepository.web.dto.WishListEditRequest;
import org.example.projectRepository.web.dto.WishListRequest;
import org.example.projectRepository.wishList.model.TypeEntertainment;
import org.example.projectRepository.wishList.model.WishList;
import org.example.projectRepository.wishList.repository.WishlistRepository;
import org.example.projectRepository.wishList.service.WishListService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
public class WishListUTest {

    @Mock
    private WishlistRepository wishlistRepository;
    @Mock
    private UserService userService;


    @InjectMocks
    private WishListService wishListService;

    @Test
    void tryingToSaveWishList_Successfully() {

        UUID uuid = UUID.randomUUID();
        User user = User.builder()
                .firstName("Pesho")
                .lastName("PeshoTest")
                .age(19)
                .id(uuid)
                .isActive(true)
                .build();

        WishListRequest wishListRequest = WishListRequest.builder()
                .title("title")
                .completed(false)
                .description("description")
                .seasons(1)
                .typeEntertainment(TypeEntertainment.TvShow)
                .build();
        when(userService.getById(uuid)).thenReturn(user);
        when(wishlistRepository.save(any(WishList.class))).thenReturn(WishList.builder().seasons(1).completed(false).build());

        wishListService.saveItemToWishlist(wishListRequest, user);

        verify(wishlistRepository, times(1)).save(any(WishList.class));
        verify(userService, times(1)).getById(uuid);
    }

    @Test
    void tryingToSaveWishList_ButAlreadyExist_ThrowsException() {

        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .firstName("Test")
                .lastName("User")
                .isActive(true)
                .age(19).build();

        WishListRequest wishListRequest = WishListRequest.builder()
                .title("ExistingTitle")
                .description("Test description")
                .typeEntertainment(TypeEntertainment.TvShow)
                .seasons(1)
                .completed(true).build();


        WishList existingWishlistItem = WishList.builder()
                .title("ExistingTitle")
                .typeEntertainment(TypeEntertainment.TvShow)
                .seasons(1).completed(true).build();

        when(userService.getById(userId)).thenReturn(user);
        when(wishlistRepository.findByTitleAndTypeEntertainment(
                wishListRequest.getTitle(),
                wishListRequest.getTypeEntertainment())
        ).thenReturn(Optional.of(existingWishlistItem));


        assertThrows(WishListItemAlreadyExist.class, () -> {
            wishListService.saveItemToWishlist(wishListRequest, user);
        });

        verify(wishlistRepository, times(1))
                .findByTitleAndTypeEntertainment(wishListRequest.getTitle(), wishListRequest.getTypeEntertainment());
    }

    @Test
    void tryingToFindWishListById_Successfully() {

        UUID uuid = UUID.randomUUID();

        WishList wishList = WishList.builder()
                .id(uuid)
                .title("title")
                .typeEntertainment(TypeEntertainment.TvShow)
                .seasons(1)
                .completed(true)
                .build();

        when(wishlistRepository.findById(uuid)).thenReturn(Optional.of(wishList));
        wishListService.findById(uuid);
        verify(wishlistRepository, times(1)).findById(uuid);

    }

    @Test
    void tryingToFindWishListById_throwsDomainException() {

        UUID uuid = UUID.randomUUID();

        when(wishlistRepository.findById(uuid)).thenThrow(DomainException.class);

        assertThrows(DomainException.class, () -> wishListService.findById(uuid));

    }

    @Test
    void tryingToEditWishList_Successfully() {

        UUID uuid = UUID.randomUUID();

        WishListEditRequest wishListEditRequest = WishListEditRequest.builder()
                .title("title")
                .completed(true)
                .seasons(1)
                .build();

        WishList wishList = WishList.builder()
                .id(uuid)
                .seasons(1)
                .completed(true).build();

        when(wishlistRepository.findById(uuid)).thenReturn(Optional.of(wishList));
        wishListService.editWishListData(uuid, wishListEditRequest);

        verify(wishlistRepository, times(1)).findById(uuid);
        Assertions.assertThat(wishList.getTitle()).isEqualTo(wishListEditRequest.getTitle());
        Assertions.assertThat(wishListEditRequest.isCompleted()).isEqualTo(wishList.isCompleted());
        Assertions.assertThat(wishListEditRequest.getSeasons()).isEqualTo(wishList.getSeasons());
    }

    @Test
    void tryingToReturnWishList_Successfully() {

        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .firstName("Test")
                .lastName("User")
                .isActive(true)
                .age(19).build();

        List<WishList> wishList = List.of(new WishList(), new WishList());

        when(wishlistRepository.findAllByUser(user)).thenReturn(wishList);
        List<WishList> wishLists = wishListService.returnWishList(user);

        Assertions.assertThat(wishLists.size()).isEqualTo(2);
    }

    @Test
    void tryingToReturnCompletedWishList_Successfully() {

        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .firstName("Test")
                .lastName("User")
                .isActive(true)
                .age(19).build();

        List<WishList> wishList = List.of(WishList.builder().seasons(1).completed(true).build(), WishList.builder().seasons(1).completed(true).build());
        when(wishlistRepository.findAllByCompletedTrue()).thenReturn(wishList);
        List<WishList> wishLists = wishListService.returnWishListCompleted();

        Assertions.assertThat(wishLists.size()).isEqualTo(2);
    }

    @Test
    void tryingToReturnSortedListOfWishLists_Successfully() {

        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .firstName("Test")
                .lastName("User")
                .isActive(true)
                .age(19).build();

        List<WishList> wishList = List.of(new WishList(), new WishList());
        when(wishlistRepository.findAllByUserOrderByTitleAsc(user)).thenReturn(wishList);
        when(wishlistRepository.findAllByUserOrderByTitleDesc(user)).thenReturn(wishList);
        when(wishlistRepository.findAllByUserOrderByCompletedAsc(user)).thenReturn(wishList);
        when(wishlistRepository.findAllByUserOrderByCompletedDesc(user)).thenReturn(wishList);
        when(wishlistRepository.findAllByUserOrderByTypeEntertainmentAsc(user)).thenReturn(wishList);
        when(wishlistRepository.findAllByUserOrderByTypeEntertainmentDesc(user)).thenReturn(wishList);
        when(wishlistRepository.findAllByUserOrderByCreationDateAsc(user)).thenReturn(wishList);
        when(wishlistRepository.findAllByUserOrderByCreationDateDesc(user)).thenReturn(wishList);


        List<WishList> sorted = wishListService.returnAllWishListSorted(user, "title", "asc");
        List<WishList> sorted1 = wishListService.returnAllWishListSorted(user, "title", "desc");
        List<WishList> sorted2 = wishListService.returnAllWishListSorted(user, "completed", "asc");
        List<WishList> sorted3 = wishListService.returnAllWishListSorted(user, "completed", "desc");
        List<WishList> sorted4 = wishListService.returnAllWishListSorted(user, "typeEntertainment", "asc");
        List<WishList> sorted5 = wishListService.returnAllWishListSorted(user, "typeEntertainment", "desc");
        List<WishList> sorted6 = wishListService.returnAllWishListSorted(user, "creationDate", "asc");
        List<WishList> sorted7 = wishListService.returnAllWishListSorted(user, "creationDate", "desc");

        verify(wishlistRepository, times(1)).findAllByUserOrderByTitleAsc(user);
        verify(wishlistRepository, times(1)).findAllByUserOrderByTitleDesc(user);
        verify(wishlistRepository, times(1)).findAllByUserOrderByCompletedAsc(user);
        verify(wishlistRepository, times(1)).findAllByUserOrderByCompletedDesc(user);
        verify(wishlistRepository, times(1)).findAllByUserOrderByTypeEntertainmentAsc(user);
        verify(wishlistRepository, times(1)).findAllByUserOrderByTypeEntertainmentDesc(user);
        verify(wishlistRepository, times(1)).findAllByUserOrderByCreationDateAsc(user);
        verify(wishlistRepository, times(1)).findAllByUserOrderByCreationDateDesc(user);


        Assertions.assertThat(sorted).isEqualTo(wishList);
        Assertions.assertThat(sorted1).isEqualTo(wishList);
        Assertions.assertThat(sorted2).isEqualTo(wishList);
        Assertions.assertThat(sorted3).isEqualTo(wishList);
        Assertions.assertThat(sorted4).isEqualTo(wishList);
        Assertions.assertThat(sorted5).isEqualTo(wishList);
        Assertions.assertThat(sorted6).isEqualTo(wishList);
        Assertions.assertThat(sorted7).isEqualTo(wishList);
    }

    @Test
    void tryingToFindWishListWithGivenTitleAndTypeEntertainment_Successfully() {

        UUID uuid = UUID.randomUUID();
        WishList wishList = WishList.builder()
                .id(uuid)
                .title("title")
                .typeEntertainment(TypeEntertainment.TvShow)
                .seasons(1)
                .completed(true)
                .build();


        when(wishlistRepository.findByTitleAndTypeEntertainment(wishList.getTitle(), wishList.getTypeEntertainment())).thenReturn(Optional.of(wishList));
        WishList wishList1 = wishListService.returnWishListByTitle(wishList.getTitle(), wishList.getTypeEntertainment());
        Assertions.assertThat(wishList1).isNotNull();
        Assertions.assertThat(wishList1.getTitle()).isEqualTo(wishList.getTitle());
        Assertions.assertThat(wishList1.getTypeEntertainment()).isEqualTo(wishList.getTypeEntertainment());
        Assertions.assertThat(wishList).isEqualTo(wishList1);

    }

//    @Test
//    void tryingToFindWishListWithGivenTitleAndTypeEntertainment_throwException() {
//
//        UUID uuid = UUID.randomUUID();
//        WishList wishList = WishList.builder()
//                .id(uuid)
//                .title("title")
//                .typeEntertainment(TypeEntertainment.TvShow)
//                .seasons(1)
//                .completed(true)
//                .build();
//
//        when(wishlistRepository.findByTitleAndTypeEntertainment(wishList.getTitle(), wishList.getTypeEntertainment())).thenReturn(Optional.empty());
//
//        assertThrows(DomainException.class, () -> wishListService.returnWishListByTitle(wishList.getTitle(), wishList.getTypeEntertainment()));
//
//    }
}
