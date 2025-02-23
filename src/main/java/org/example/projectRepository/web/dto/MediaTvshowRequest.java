package org.example.projectRepository.web.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.projectRepository.media.model.MediaType;




@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class MediaTvshowRequest {


    @Size(min = 1, max = 50,message = "Title must be between 1 end 50 Characters")
    private String title;

    @NotNull(message = "Field can't be empty")
    private String releaseDate;

    private int seasons;

    private String genre;

    private MediaType mediaType;




}
