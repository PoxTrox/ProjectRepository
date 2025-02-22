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
public class AuthorRequest {

    @Size( min = 2 , message = "First name must be more then 2 symbols")
    private String firstName;

    @Size( min =2 , message = "Last name must be more then 2 symbols")
    private String lastName;
}
