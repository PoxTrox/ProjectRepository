package org.example.projectRepository.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.projectRepository.wishList.model.TypeEntertainment;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishListRequest {


    @Size(min = 1 , max = 30,message = "Title must be between 1 and 30 Characters")
    private String title;

    @NotNull(message = "Field can't be empty")
    private TypeEntertainment typeEntertainment;

    @Size(max = 100,message = "Description can't be more then 100 Characters ")
    private String description;

    @Positive
    private int seasons;


    private boolean completed;
}
