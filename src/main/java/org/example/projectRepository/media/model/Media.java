package org.example.projectRepository.media.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.projectRepository.user.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private  UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate releaseDate;


    private int season;

    private String genre;

    @Column(nullable = false)
    private LocalDateTime creationAt;

    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    @ManyToOne
    private User user;


}
