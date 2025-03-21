package org.example.projectRepository.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.projectRepository.user.model.User;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookAuthorRequest {


    @NotNull(message = "Please enter title")
    private String title;

    @Size(min=2,  max = 35,message = "First name must by between 2 and 35 characters")
    private String firstName;
    @Size(min=2,  max = 35,message = "First name must by between 2 and 35 characters")
    private String lastName;

    @Positive(message = "Value must be positive")
    private BigDecimal price;

    private User user;


}
