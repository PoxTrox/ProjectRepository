package org.example.projectRepository.search.service;

import jakarta.transaction.Transactional;
import org.example.projectRepository.media.Service.MediaService;
import org.example.projectRepository.media.model.Media;
import org.example.projectRepository.search.SearchClient;
import org.example.projectRepository.search.dto.RestMediaResponse;
import org.example.projectRepository.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public List<RestMediaResponse> search(User user, String query) {


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // Търсим в локалната база (REST API DB)
        List<RestMediaResponse> restMediaResponses = searchClient.returnAllByTitle(query);

        Pattern pattern = Pattern.compile(query, Pattern.CASE_INSENSITIVE);

        // Филтрираме резултатите с Regex
        restMediaResponses = restMediaResponses.stream()
                .filter(media -> {
                    Matcher matcher = pattern.matcher(media.getTitle());
                    return matcher.find(); // Връща само съвпадащите заглавия
                })
                .toList();

        // Ако няма резултати -> търсим в TMDB
        if (restMediaResponses.isEmpty()) {
            List<Media> fetchedMedia = searchClient.searchAndSaveMedia(query);

            // Запазваме в локалната база
            if (!fetchedMedia.isEmpty()) {
          //  mediaService.saveListMediaFromRest(fetchedMedia);  // Добавяме ги в нашата DB
                mediaService.saveListMediaFromRest(fetchedMedia);
                            restMediaResponses = fetchedMedia.stream()
                                    .map(media -> RestMediaResponse.builder()
                                            .title(media.getTitle())
                                            .release_date(media.getReleaseDate() != null ? media.getReleaseDate().format(formatter) : "Unknown")
                                            .build()
                                    ).toList();

            searchClient.returnAllByTitle(query);
            }
        }

        return restMediaResponses;
    }


}



