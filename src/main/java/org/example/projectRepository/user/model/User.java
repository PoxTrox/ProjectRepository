package org.example.projectRepository.user.model;


import jakarta.persistence.*;
import lombok.*;
import org.example.projectRepository.book.model.Book;
import org.example.projectRepository.movie.model.Movie;
import org.example.projectRepository.serial.model.TvShows;
import org.example.projectRepository.wishList.model.WishList;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String email;

    private String firstName;

    private String lastName;

    private int age;

    private String country;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String profilePic;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<Book> books = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<Movie> movies = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<TvShows> tvShows = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<WishList> wishLists = new ArrayList<>();


}
