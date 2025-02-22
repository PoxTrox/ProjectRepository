package org.example.projectRepository.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
public class ProfileEditRequest {

    @Size(max = 69, message = "First name can't be more then 69 symbols")
    private String firstName;

    @Size(max = 69, message = "First name can't be more then 69 symbols")
    private String lastName;

    @Email (message = "Requires valid email")
    private String email;

    @Min(value = 18 ,message = "Years must be at least 18 ,can't be more then 100")
    @Max(value = 100, message = "Years must be at least 18 ,can't be more then 100")
    private int age;
    @Size(max = 100,message = "Country name can't be more then 100 symbols")
    private String country;

    @URL(message = "Enter Valid URL")
    private String profilePicture;

}
