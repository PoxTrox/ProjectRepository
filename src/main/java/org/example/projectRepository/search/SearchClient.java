package org.example.projectRepository.search;

import org.example.projectRepository.media.model.Media;
import org.example.projectRepository.search.dto.RestMediaResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient (name="restApiProjectRepository", url = "http://localhost:8080/api/v1/movies")
public interface SearchClient {

 @GetMapping("/test")
 ResponseEntity<String> getMessage(@RequestParam (name="name") String name);


//@GetMapping
// ResponseEntity<Media>searchAndSaveMedia(@RequestParam (name="title")String title);

 @GetMapping
 List<Media> searchAndSaveMedia(@RequestParam (name="title")String title);

// @GetMapping
// List<RestMediaResponse>searchMedia(@RequestParam (name="title")String title);

 @GetMapping()
 List<RestMediaResponse>returnMovies(@RequestParam (name="title")String title);

// @PostMapping
// List<RestMediaResponse> returnMediaByTitle(@RequestParam (name="title")String title);

 @PostMapping("/title")
 Media mediaByTitleAndReleaseDate(@RequestParam (name="title")String title,@RequestParam (name="releaseDate")String releaseDate);



}
