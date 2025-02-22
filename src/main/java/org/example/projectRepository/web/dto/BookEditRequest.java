package org.example.projectRepository.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class BookEditRequest {

    @NotNull(message = "Please enter title")
    private String title;

    @NotNull(message = "Field must be not Empty")
    private String firstName;
    @NotNull(message = "Field must be not Empty")
    private String lastName;

    @Positive(message = "Value must be positive")
    private BigDecimal price;
}
