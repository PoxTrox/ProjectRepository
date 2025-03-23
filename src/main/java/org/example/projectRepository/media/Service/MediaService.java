package org.example.projectRepository.media.Service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.projectRepository.exception.DomainException;
import org.example.projectRepository.exception.MediaAlreadyExist;
import org.example.projectRepository.media.model.Media;
import org.example.projectRepository.media.model.MediaType;
import org.example.projectRepository.media.repository.MediaRepository;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.user.service.UserService;
import org.example.projectRepository.web.dto.MediaTvshowRequest;
import org.example.projectRepository.web.dto.MovieTvShowEditRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class MediaService {

    private final UserService userService;
    private final MediaRepository mediaRepository;

    @Autowired
    public MediaService(UserService userService, MediaRepository mediaRepository) {
        this.userService = userService;
        this.mediaRepository = mediaRepository;
    }


    public void saveMedia(MediaTvshowRequest mediaTvshowRequest, User user) {


        User currenUser = userService.getById(user.getId());


        if(currenUser == null) {
            throw new DomainException("User not found");
        }

        if(mediaRepository.findByTitleAndMediaTypeAndReleaseDate(mediaTvshowRequest.getTitle(), mediaTvshowRequest.getMediaType(), LocalDate.parse(mediaTvshowRequest.getReleaseDate())).isPresent()) {
            throw new MediaAlreadyExist("Media already exists!");
        }
        LocalDateTime now = LocalDateTime.now();
        Media media = Media.builder()
                .title(mediaTvshowRequest.getTitle())
                .releaseDate(LocalDate.parse(mediaTvshowRequest.getReleaseDate()))
                .genre(mediaTvshowRequest.getGenre())
                .mediaType(mediaTvshowRequest.getMediaType())
                .user(currenUser)
                .creationAt(now)
                .season(mediaTvshowRequest.getSeasons())
                .build();
        mediaRepository.save(media);
    }

    public void saveMediaFromRest(Media restMediaRequest, User user) {
        User currenUser = userService.getById(user.getId());
        LocalDateTime now = LocalDateTime.now();
        Media media = Media.builder()
                .title(restMediaRequest.getTitle())
                .releaseDate(LocalDate.parse(restMediaRequest.getReleaseDate().toString()))
                .mediaType(MediaType.MOVIE)
                .creationAt(now)
                .user(currenUser)
                .season(1).build();
        mediaRepository.save(media);
    }


    public Media findById(UUID id) {

        return mediaRepository.findById(id).orElseThrow(() -> new DomainException("Media not found"));

    }

    public void editMedia(UUID id, MovieTvShowEditRequest movieTvShowEditRequest) {

        Media mediaById = findById(id);
        mediaById.setTitle(movieTvShowEditRequest.getTitle());
        mediaById.setGenre(movieTvShowEditRequest.getGenre());
        mediaById.setMediaType(movieTvShowEditRequest.getMediaType());
        mediaById.setSeason(movieTvShowEditRequest.getSeasons());
        mediaById.setReleaseDate(LocalDate.parse(movieTvShowEditRequest.getReleaseDate()));

        mediaRepository.save(mediaById);
    }

    public List<Media> returnAllMediaSorted(User user, String sortedBy, String direction) {

        if (sortedBy.equals("title") && direction.equals("asc")) {
            return mediaRepository.findAllByUserOrderByTitleAsc(user);
        }else if (sortedBy.equals("title") && direction.equals("desc")) {
            return mediaRepository.findAllByUserOrderByTitleDesc(user);
        }else if(sortedBy.equals("releaseDate") && direction.equals("asc")) {
            return mediaRepository.findAllByUserOrderByReleaseDateAsc(user);
        }else if (sortedBy.equals("releaseDate") && direction.equals("desc")) {
            return mediaRepository.findAllByUserOrderByReleaseDateDesc(user);
        }else if (sortedBy.equals("mediaType") && direction.equals("asc")) {
            return mediaRepository.findAllByUserOrderByMediaTypeAsc(user);
        }else if (sortedBy.equals("mediaType") && direction.equals("desc")) {
            return mediaRepository.findAllByUserOrderByMediaTypeDesc(user);
        }else if (sortedBy.equals("seasons") && direction.equals("asc")) {
            return mediaRepository.findAllByUserOrderBySeasonAsc(user);
        }else {
            return mediaRepository.findAllByUserOrderBySeasonDesc(user);
        }
    }

    public void removeBy(UUID id) {

        mediaRepository.findById(id).orElseThrow(() -> new DomainException("Media not found"));
        mediaRepository.deleteById(id);
    }

    public List<Media> returnAllMedia (User user) {

        return mediaRepository.findAllByUserOrderByCreationAtDesc(user);
    }
}
