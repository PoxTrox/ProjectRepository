package org.example.projectRepository.book.model;


import jakarta.persistence.*;
import lombok.*;
import org.example.projectRepository.author.model.Author;
import org.example.projectRepository.user.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne
    private Author author;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    private User user;

}
