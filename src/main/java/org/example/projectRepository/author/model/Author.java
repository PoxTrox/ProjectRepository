package org.example.projectRepository.author.model;


import jakarta.persistence.*;
import lombok.*;
import org.example.projectRepository.book.model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "author")
    private List<Book> books = new ArrayList<>();

}
