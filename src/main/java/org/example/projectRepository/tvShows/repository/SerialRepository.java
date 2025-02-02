package org.example.projectRepository.tvShows.repository;

import org.example.projectRepository.tvShows.model.TvShows;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.UUID;

@Repository
public interface SerialRepository extends JpaRepository<TvShows, UUID> {
}
