package org.example.projectRepository.wishList.repository;

import org.example.projectRepository.user.model.User;
import org.example.projectRepository.wishList.model.TypeEntertainment;
import org.example.projectRepository.wishList.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WishlistRepository extends JpaRepository <WishList, UUID> {

    List<WishList>findAllByUserOrderByTitleAsc(User user);

    List<WishList>findAllByUserOrderByTitleDesc(User user);

    List<WishList>findAllByUserOrderByTypeEntertainmentAsc(User user);

    List<WishList>findAllByUserOrderByTypeEntertainmentDesc(User user);

    List<WishList>findAllByUserOrderByCreationDateAsc(User user);

    List<WishList>findAllByUserOrderByCreationDateDesc(User user);

    List<WishList>findAllByUserOrderByCompletedAsc(User user);

    List<WishList>findAllByUserOrderByCompletedDesc(User user);

    List<WishList>findAllByUser(User user);

    List<WishList>findAllByCompletedTrue();


}
