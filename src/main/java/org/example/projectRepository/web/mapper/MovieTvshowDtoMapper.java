package org.example.projectRepository.web.mapper;

import lombok.experimental.UtilityClass;

import org.example.projectRepository.media.model.Media;
import org.example.projectRepository.web.dto.MovieTvShowEditRequest;

@UtilityClass
public class MovieTvshowDtoMapper {

    public static MovieTvShowEditRequest mapToMovieTvShowEditRequest(Media media) {

        return MovieTvShowEditRequest.builder()
                .title(media.getTitle())
                .genre(media.getGenre())
                .releaseDate(media.getReleaseDate().toString())
                .mediaType(media.getMediaType())
                .seasons(media.getSeason())
                .build();
    }

}
