package org.example.projectRepository.media.repository;

import org.example.projectRepository.media.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface MediaRepository extends JpaRepository<Media, UUID> {
}
