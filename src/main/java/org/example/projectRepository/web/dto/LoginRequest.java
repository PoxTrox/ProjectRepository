package org.example.projectRepository.web.dto;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @Size(min = 5, max = 60, message = " username must be at least 5 symbols")
    private String username;

    @Size(min = 5, message = " Password must be at least 5 symbols")
    private String password;
}
