package org.example.projectRepository.media.Service;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.projectRepository.exception.DomainException;
import org.example.projectRepository.media.model.Media;
import org.example.projectRepository.media.repository.MediaRepository;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.user.service.UserService;
import org.example.projectRepository.web.dto.MediaTvshowRequest;
import org.example.projectRepository.web.dto.MovieTvShowEditRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        Media media = Media.builder()
                .title(mediaTvshowRequest.getTitle())
                .releaseDate(LocalDate.parse(mediaTvshowRequest.getReleaseDate()))
                .genre(mediaTvshowRequest.getGenre())
                .mediaType(mediaTvshowRequest.getMediaType())
                .user(currenUser)
                .build();
        mediaRepository.save(media);
    }


    public Media  findById(UUID id) {

        return mediaRepository.findById(id).orElseThrow(() -> new DomainException("Media not found"));

    }

    public void editMedia(UUID id,  MovieTvShowEditRequest movieTvShowEditRequest) {

        Media mediaById = findById(id);
        mediaById.setTitle(movieTvShowEditRequest.getTitle());
        mediaById.setGenre(movieTvShowEditRequest.getGenre());
        mediaById.setMediaType(movieTvShowEditRequest.getMediaType());
        mediaById.setSeason(movieTvShowEditRequest.getSeasons());
        mediaById.setReleaseDate(LocalDate.parse(movieTvShowEditRequest.getReleaseDate()));

        mediaRepository.save(mediaById);
    }

    public List<Media> returnAllMediaSorted(User user, String sortedBy, String direction) {


        List<Media> allShows = user.getShows();

        Comparator<Media>comparator = null;

        if(sortedBy.equals("title")) {
            comparator = Comparator.comparing(Media::getTitle);
        }else if(sortedBy.equals("genre")) {
            comparator = Comparator.comparing(Media::getGenre);
        }else if(sortedBy.equals("releaseDate")) {
            comparator = Comparator.comparing(Media::getReleaseDate);
        }else if(sortedBy.equals("seasons")) {
            comparator = Comparator.comparing(Media::getSeason);
        }

        if(direction.equals("desc")) {
            assert comparator != null;
            comparator=comparator.reversed();
        }

        assert comparator != null;
        return allShows.stream().sorted(comparator).collect(Collectors.toList());

    }

    public void removeBy(UUID id) {

        mediaRepository.deleteById(id);
    }
}
