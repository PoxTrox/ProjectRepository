package org.example.projectRepository.web.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.example.projectRepository.media.model.MediaType;

@Data
@Builder
public class MovieTvShowEditRequest {


    @Size(min = 1, max = 50, message = "Title must be between 1 end 50 Characters")
    private String title;

    @NotNull(message = "Field can't be empty")
    private String releaseDate;

    @Positive(message = "Value must be positive")
    private int seasons;

    @Size(min =2, max=30,message = "Genre must be between 2 end 30 Characters")
    private String genre;

    @NotNull(message = "Field can't be empty")
    private MediaType mediaType;
}
