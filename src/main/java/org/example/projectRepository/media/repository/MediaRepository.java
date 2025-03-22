package org.example.projectRepository.media.repository;

import org.example.projectRepository.media.model.Media;
import org.example.projectRepository.media.model.MediaType;
import org.example.projectRepository.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface MediaRepository extends JpaRepository<Media, UUID> {

    Optional<Media> findByTitleAndMediaTypeAndReleaseDate(String title, MediaType mediaType, LocalDate releaseDate);

    List<Media> findAllByUserOrderByTitleAsc(User user);

    List<Media> findAllByUserOrderByTitleDesc(User user);

    List<Media> findAllByUserOrderByMediaTypeAsc(User user);

    List<Media> findAllByUserOrderByMediaTypeDesc(User user);

    List<Media> findAllByUserOrderByReleaseDateAsc(User user);

    List<Media> findAllByUserOrderByReleaseDateDesc(User user);

    List<Media>findAllByUserOrderBySeasonAsc(User user);

    List<Media>findAllByUserOrderBySeasonDesc(User user);

    List<Media>findAllByUserOrderByCreationAtDesc(User user);






}
