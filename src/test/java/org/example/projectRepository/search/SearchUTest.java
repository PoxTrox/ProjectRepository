package org.example.projectRepository.search;


import org.example.projectRepository.media.model.Media;
import org.example.projectRepository.search.dto.RestMediaResponse;
import org.example.projectRepository.search.service.SearchService;
import org.example.projectRepository.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.*;
import static reactor.core.publisher.Mono.when;

@ExtendWith(MockitoExtension.class)
public class SearchUTest {

    @Mock
    private SearchClient searchClient;


    @InjectMocks
    SearchService searchService;

    @Mock
    private Logger log;

    @Test
    void TryingToFindMediaByGivenNameAndReleaseDate_HappyPath() {

        String title = "Test Title";
        String releaseDate = "2010-07-16";
        Media mockMedia = new Media();
        mockMedia.setTitle(title);
        mockMedia.setReleaseDate(LocalDate.parse(releaseDate));
        mockMedia.setId(UUID.randomUUID());
        mockMedia.setSeason(1);

        ResponseEntity<Media> mockResponse = ResponseEntity.ok(mockMedia);

        Mockito.when(searchClient.mediaByTitleAndReleaseDate(title, releaseDate)).thenReturn(mockResponse);


        Media result = searchService.mediaByTitleAndReleaseDate(title, releaseDate);

        assertEquals(title, result.getTitle());
        assertNotNull(result);


    }

    @Test
    void mediaByTitleAndReleaseDate_shouldReturnNull_whenApiReturns404() {

        String title = "NonExistingMovie";
        String releaseDate = "2000-01-01";

        ResponseEntity<Media> mockResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        Mockito.when(searchClient.mediaByTitleAndReleaseDate(title, releaseDate)).thenReturn(mockResponse);


        Media result = searchService.mediaByTitleAndReleaseDate(title, releaseDate);

        assertNull(result);
    }


    @Test
    void tryingToSearchMovieByGivenTitle_HappyPath() {

        RestMediaResponse mediaResponse = new RestMediaResponse();
        mediaResponse.setTitle("gladiator");

        Mockito.when(searchClient.searchMovies(mediaResponse.getTitle())).thenReturn(ResponseEntity.ok(List.of(mediaResponse)));

        List<RestMediaResponse> restMediaResponses = searchService.searchMovieByTitle(mediaResponse.getTitle());

        assertThat(restMediaResponses).hasSize(1);
        assertThat(restMediaResponses.get(0)).isEqualTo(mediaResponse);
        verify(searchClient, times(1)).searchMovies(mediaResponse.getTitle());
    }

    @Test
    void tryingToSearchMovieByGivenTitle_UnHappyPath() {

        RestMediaResponse mediaResponse = new RestMediaResponse();
        mediaResponse.setTitle("gladiator");

       Mockito.when(searchClient.searchMovies(mediaResponse.getTitle())).thenReturn(ResponseEntity.notFound().build());

        List<RestMediaResponse> restMediaResponses = searchService.searchMovieByTitle(mediaResponse.getTitle());
        assertThat(restMediaResponses).isEmpty();
        verify(searchClient, times(1)).searchMovies(mediaResponse.getTitle());

    }

    @Test
    void tryingToSearchMovieByGivenTitle_ThrowsException() {

        RestMediaResponse mediaResponse = new RestMediaResponse();
        mediaResponse.setTitle("gladiator");

        Mockito.when(searchClient.searchMovies(mediaResponse.getTitle())).thenThrow( new RuntimeException ("Something went wrong"));
        List<RestMediaResponse> restMediaResponses = searchService.searchMovieByTitle(mediaResponse.getTitle());
        assertThat(restMediaResponses).isEmpty();
        verify(searchClient, times(1)).searchMovies(mediaResponse.getTitle());


    }


    @Test
    void TryingToSearch_ShouldReturnFilteredResults_WhenMatchingMoviesFound() {
        String title = "gladiator";
        Pattern pattern = Pattern.compile(title, Pattern.CASE_INSENSITIVE);
        User user = User.builder()
                .username("TestA")
                .password("TestP")
                .age(18).isActive(true).build();

        RestMediaResponse mediaResponse = new RestMediaResponse();
        mediaResponse.setTitle("gladiator");

        Mockito.when(searchClient.returnAllByTitle(title)).thenReturn(ResponseEntity.ok(List.of(mediaResponse)));

        List<RestMediaResponse> results = searchService.search(user, title);

        assertFalse(results.isEmpty());
        assertTrue(pattern.matcher(results.get(0).getTitle()).find());
        verify(searchClient, times(1)).returnAllByTitle(title);
    }

    @Test
    void search_ShouldReturnSavedResults_WhenNoMatchingMoviesInitiallyFound() {
        String title = "TestMovieTitle";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Media media = new Media();
        media.setTitle("TestMovieTitle");
        media.setReleaseDate(LocalDate.of(2018, 11, 7));
        User user = User.builder()
                .username("TestA")
                .password("TestP")
                .age(18).isActive(true).build();

        Mockito.when(searchClient.returnAllByTitle(title)).thenReturn(ResponseEntity.ok(Collections.emptyList()));
        Mockito.when(searchClient.searchAndSaveMedia(title)).thenReturn(ResponseEntity.ok(List.of(media)));

        List<RestMediaResponse> results = searchService.search(user, title);

        assertFalse(results.isEmpty());
        assertEquals("TestMovieTitle", results.get(0).getTitle());
        assertEquals("2018-11-07", results.get(0).getRelease_date());
        verify(searchClient, times(1)).returnAllByTitle(title);
        verify(searchClient, times(1)).searchAndSaveMedia(title);
    }

    @Test
    void search_ShouldReturnEmptyList_WhenExceptionOccurs() {
        String title = "TestTitle";
        User user = User.builder()
                .username("TestA")
                .password("TestP")
                .age(18).isActive(true).build();

        Mockito.when(searchClient.returnAllByTitle(title)).thenThrow(new RuntimeException("Service unavailable"));

        List<RestMediaResponse> results = searchService.search(user, title);

        assertTrue(results.isEmpty());
        verify(searchClient, times(1)).returnAllByTitle(title);
    }
}
