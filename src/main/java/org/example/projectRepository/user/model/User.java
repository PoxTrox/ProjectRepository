package org.example.projectRepository.user.model;


import jakarta.persistence.*;
import lombok.*;
import org.example.projectRepository.book.model.Book;
import org.example.projectRepository.media.model.Media;
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

//Note  !!!! when run tests change this @Table to "app_user"
////because Integration test does not run without it after that make shore you change it back to "user"
//// or application will not work correctly i don't know what missing to fix this..
@Table(name = "user")
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

    private boolean isActive;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<Book> books = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<WishList> wishLists = new ArrayList<>();

    @OneToMany(fetch =FetchType.EAGER, mappedBy = "user")
    private List<Media> shows = new ArrayList<>();


}
