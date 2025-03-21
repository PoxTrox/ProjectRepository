package org.example.projectRepository.media;


import org.example.projectRepository.book.model.Book;
import org.example.projectRepository.book.service.BookService;
import org.example.projectRepository.exception.DomainException;
import org.example.projectRepository.media.Service.MediaService;
import org.example.projectRepository.media.model.Media;
import org.example.projectRepository.media.model.MediaType;

import org.example.projectRepository.media.repository.MediaRepository;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.user.repository.UserRepository;
import org.example.projectRepository.user.service.UserService;
import org.example.projectRepository.web.dto.MediaTvshowRequest;
import org.example.projectRepository.web.dto.MovieTvShowEditRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class MediaUTest {



    @Mock
    private UserService userService;

    @Mock
    private MediaRepository mediaRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MediaService mediaService;


    @Test
    void tryingToSaveMedia_successFully() throws Exception {

        UUID uuid = UUID.randomUUID();
        User user = User.builder()
                .firstName("Pesho")
                .lastName("PeshoTest")
                .age(19)
                .id(uuid)
                .isActive(true)
                .build();
        LocalDateTime now = LocalDateTime.now();

        MediaTvshowRequest mediaTvshowRequest = MediaTvshowRequest.builder()
                .seasons(1)
                .releaseDate(String.valueOf(LocalDate.from(now)))
                .genre("BigFAN")
                .title("testB")
                .mediaType(MediaType.MOVIE)
                .build();

        when(userService.getById(user.getId())).thenReturn(user);
        when(mediaRepository.save(any(Media.class))).thenReturn(Media.builder().season(1).build());
        User user2 = userService.getById(user.getId());
        mediaService.saveMedia(mediaTvshowRequest, user2);

        verify(userService, times(2)).getById(user.getId());
        verify(mediaRepository, times(1)).save(any());

    }

    @Test
    void tryingToSaveMedia_ThrowException() throws Exception {

        UUID uuid = UUID.randomUUID();
        User user = User.builder()
                .firstName("Pesho")
                .lastName("PeshoTest")
                .age(19)
                .id(uuid)
                .isActive(true)
                .build();
        LocalDateTime now = LocalDateTime.now();

        MediaTvshowRequest mediaTvshowRequest = MediaTvshowRequest.builder()
                .seasons(1)
                .releaseDate(String.valueOf(LocalDate.from(now)))
                .genre("BigFAN")
                .title("testB")
                .mediaType(MediaType.MOVIE)
                .build();

          when(userService.getById(user.getId())).thenReturn(null);


        assertThrows(DomainException.class, () -> {
            mediaService.saveMedia(mediaTvshowRequest, user);
        });
    }

    @Test
    void tryingToEditMedia_successFully() throws Exception {

        MovieTvShowEditRequest movieTvShowEditRequest = MovieTvShowEditRequest.builder()
                .title("testB")
                .mediaType(MediaType.MOVIE)
                .genre("BigFAN")
                .releaseDate(String.valueOf(LocalDate.from(LocalDateTime.now())))
                .seasons(1)
                .build();
        UUID uuid = UUID.randomUUID();
        Media media = Media.builder()
                .id(uuid)
                .season(1)
                .build();
        when(mediaRepository.findById(uuid)).thenReturn(Optional.of(media));
        mediaService.editMedia(media.getId(), movieTvShowEditRequest);
        verify(mediaRepository, times(1)).findById(uuid);
        assertThat(media.getTitle()).isEqualTo(movieTvShowEditRequest.getTitle());
        assertThat(media.getMediaType()).isEqualTo(movieTvShowEditRequest.getMediaType());
        assertThat(media.getGenre()).isEqualTo(movieTvShowEditRequest.getGenre());
    }

    @Test
    void tryingToDeleteMediaById_trowException() throws Exception {

        UUID id = UUID.randomUUID();


        when(mediaRepository.findById(id)).thenThrow(DomainException.class);

        assertThrows(DomainException.class, () -> mediaRepository.findById(id));
    }
    @Test
    void tryingToDeleteMediaById_successFully() throws Exception {

        UUID id = UUID.randomUUID();

      Media media = Media.builder()
              .id(id)
              .season(1)
              .title("testB")
              .build();
        when(mediaRepository.findById(id)).thenReturn(Optional.of(media));

        mediaService.removeBy(id);
        verify(mediaRepository, times(1)).deleteById(id);
    }

    @Test
    void tryingToReturnListOfMedia_successFully() throws Exception {

        User user = User.builder()
                .firstName("Pesho")
                .lastName("PeshoTest")
                .age(19)
                .id(UUID.randomUUID())
                .isActive(true)
                .build();

        List<Media> shows = List.of(mock(Media.class), mock(Media.class));

        when(mediaRepository.findAllByUserOrderByCreationAtDesc(user)).thenReturn(shows);
        List<Media> media1 = mediaService.returnAllMedia(user);
        verify(mediaRepository, times(1)).findAllByUserOrderByCreationAtDesc(user);
        assertThat(media1).isEqualTo(shows);
    }

    @Test
    void tryingToSortMedia_successFully() throws Exception {
        User user = User.builder()
                .firstName("Pesho")
                .lastName("PeshoTest")
                .age(19)
                .id(UUID.randomUUID())
                .isActive(true)
                .build();

        List<Media> shows = List.of(mock(Media.class), mock(Media.class));
        when(mediaRepository.findAllByUserOrderByTitleAsc(user)).thenReturn(shows);
        when(mediaRepository.findAllByUserOrderByTitleDesc(user)).thenReturn(shows);
        when(mediaRepository.findAllByUserOrderBySeasonAsc(user)).thenReturn(shows);
        when(mediaRepository.findAllByUserOrderBySeasonDesc(user)).thenReturn(shows);
        when(mediaRepository.findAllByUserOrderByReleaseDateAsc(user)).thenReturn(shows);
        when(mediaRepository.findAllByUserOrderByReleaseDateDesc(user)).thenReturn(shows);
        when(mediaRepository.findAllByUserOrderByMediaTypeAsc(user)).thenReturn(shows);
        when(mediaRepository.findAllByUserOrderByMediaTypeDesc(user)).thenReturn(shows);

        List<Media> media = mediaService.returnAllMediaSorted(user, "title", "asc");
        List<Media> media1 = mediaService.returnAllMediaSorted(user, "title", "desc");
        List<Media> media2 = mediaService.returnAllMediaSorted(user, "seasons", "asc");
        List<Media> media3 = mediaService.returnAllMediaSorted(user, "seasons", "desc");
        List<Media> media4 = mediaService.returnAllMediaSorted(user, "releaseDate", "asc");
        List<Media> media5 = mediaService.returnAllMediaSorted(user, "releaseDate", "desc");
        List<Media> media6 = mediaService.returnAllMediaSorted(user, "mediaType", "asc");
        List<Media> media7 = mediaService.returnAllMediaSorted(user, "mediaType", "desc");

        assertThat(media).isEqualTo(shows);
        assertThat(media1).isEqualTo(shows);
        assertThat(media2).isEqualTo(shows);
        assertThat(media3).isEqualTo(shows);
        assertThat(media4).isEqualTo(shows);
        assertThat(media5).isEqualTo(shows);
        assertThat(media6).isEqualTo(shows);
        assertThat(media7).isEqualTo(shows);

    }

}
