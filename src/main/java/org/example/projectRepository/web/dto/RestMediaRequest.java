package org.example.projectRepository.web.dto;


import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestMediaRequest {
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String releaseDate;
}
