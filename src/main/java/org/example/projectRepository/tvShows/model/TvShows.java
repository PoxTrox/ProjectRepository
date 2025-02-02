package org.example.projectRepository.tvShows.model;


import jakarta.persistence.*;
import lombok.*;
import org.example.projectRepository.user.model.User;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TvShows {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int season;


    @Column(nullable = false)
    private LocalDate releaseDate;


    private String genre;

    @ManyToOne
    private User user;

}
