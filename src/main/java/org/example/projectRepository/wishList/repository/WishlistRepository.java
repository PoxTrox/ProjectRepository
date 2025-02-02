package org.example.projectRepository.wishList.repository;

import org.example.projectRepository.wishList.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WishlistRepository extends JpaRepository <WishList, UUID> {
}
