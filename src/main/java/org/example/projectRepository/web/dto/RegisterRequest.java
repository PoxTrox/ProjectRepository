package org.example.projectRepository.web.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class RegisterRequest {

    @Size(min = 5 , max = 60, message = "Username must be at least 5 symbols")
    private String username;

    @Size(min = 5 , message = " Password must be at least 5 symbols")
    private String password;

    @Size(min = 5 , message = " Confirm Password must be at least 5 symbols")
    private String confirmPassword;


    private boolean agreeTermOfUse;

}
