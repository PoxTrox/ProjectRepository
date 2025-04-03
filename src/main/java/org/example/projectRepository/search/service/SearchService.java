package org.example.projectRepository.search.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.projectRepository.media.Service.MediaService;
import org.example.projectRepository.media.model.Media;
import org.example.projectRepository.search.SearchClient;
import org.example.projectRepository.search.dto.RestMediaResponse;
import org.example.projectRepository.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
public class SearchService {


    @Autowired
    private final SearchClient searchClient;

    @Autowired
    private final MediaService mediaService;


    public SearchService(SearchClient searchClient, MediaService mediaService) {
        this.searchClient = searchClient;
        this.mediaService = mediaService;
    }

    @Transactional
    public List<RestMediaResponse> search(User user, String title) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Pattern pattern = Pattern.compile(title, Pattern.CASE_INSENSITIVE);

        ResponseEntity<List<RestMediaResponse>> response;
        try {
            response = searchClient.returnAllByTitle(title);
        } catch (Exception e) {
            log.error("Failed to extract data with name title {} : {}", title, e.getMessage());
            return Collections.emptyList();
        }

        if (response == null || response.getBody() == null) {
            return Collections.emptyList();
        }

        List<RestMediaResponse> filteredResults = response.getBody().stream()
                .filter(media -> pattern.matcher(media.getTitle()).find())
                .toList();

        if (!filteredResults.isEmpty()) {
            return filteredResults;
        }

        ResponseEntity<List<Media>> responseEntityFetch;
        try {
            responseEntityFetch = searchClient.searchAndSaveMedia(title);
        } catch (Exception e) {
            log.error("Failed to save data with name title {} : {}", title, e.getMessage());
            return Collections.emptyList();
        }

        if (responseEntityFetch == null || responseEntityFetch.getBody() == null || responseEntityFetch.getBody().isEmpty()) {
            return Collections.emptyList();
        }

        return responseEntityFetch.getBody().stream()
                .map(media -> RestMediaResponse.builder()
                        .title(media.getTitle())
                        .release_date(media.getReleaseDate() != null ? media.getReleaseDate().format(formatter) : "Unknown")
                        .build())
                .toList();

    }

    public List<RestMediaResponse> searchMovieByTitle(String title) {

        ResponseEntity<List<RestMediaResponse>> response ;

        try{
            response= searchClient.searchMovies(title);

        }catch (Exception e) {
            log.error("Error  calling external API for title '{}': {}", title, e.getMessage(), e);
            return Collections.emptyList();
        }

        if (!response.getStatusCode().is2xxSuccessful()|| response.getBody() == null) {
            log.error("Search movies failed");
            return Collections.emptyList();
        }
        return response.getBody();
    }

    public Media mediaByTitleAndReleaseDate(String title, String releaseDate) {

        ResponseEntity<Media>responseEntity = searchClient.mediaByTitleAndReleaseDate(title, releaseDate);

        try{
            if(!responseEntity.getStatusCode().is2xxSuccessful()){
                log.error("Media with Title : releaseDate not found  {} {}", title, releaseDate);
            }
        }catch (Exception e) {
            log.warn("Media not found");
        }

        return responseEntity != null ? responseEntity.getBody() : null;
    }

}



