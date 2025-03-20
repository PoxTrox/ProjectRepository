package org.example.projectRepository.Users;

import org.example.projectRepository.exception.DomainException;
import org.example.projectRepository.exception.UserNameAlreadyExistException;
import org.example.projectRepository.exception.UserWithIdDoesNotExist;
import org.example.projectRepository.user.model.User;
import org.example.projectRepository.user.model.UserRole;
import org.example.projectRepository.user.repository.UserRepository;
import org.example.projectRepository.user.service.UserService;
import org.example.projectRepository.web.dto.ProfileEditRequest;
import org.example.projectRepository.web.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceUTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;


    @Test
    void whenUserTryingToRegisterWithGivenNameButGivenUserNameAlreadyExists_trowsException() {

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("username")
                .password("password")
                .password("password")
                .agreeTermOfUse(true)
                .build();

        Mockito.when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.of(new User()));

        assertThrows(UserNameAlreadyExistException.class, () -> userService.register(registerRequest));
        verify(userRepository, never()).save(Mockito.any(User.class));
    }

    @Test
    void registerFirstUser_MakeThemAdmin_UserSuccessfullyRegistered() {

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("username")
                .password("password")
                .confirmPassword("password")
                .agreeTermOfUse(true)
                .build();

        // При търсене на потребител с това име, да връща празно
        when(userRepository.findByUsername("username")).thenReturn(Optional.empty());

        // При извикване на findAll (което е реално това, което initializeUser ползва през getAllUsers)
        when(userRepository.findAll()).thenReturn(List.of());  // Празен списък => ще създаде ADMIN

        // При кодиране на паролата връщаме фиктивен енкоднат стринг
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        // Когато се опита да запише нов потребител, върни същия потребител обратно
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User registeredUser = userService.register(registerRequest);

        assertThat(registeredUser.getUsername()).isEqualTo("username");
        assertThat(registeredUser.getRole()).isEqualTo(UserRole.ADMIN); // защото списъкът беше празен
        assertThat(registeredUser.getPassword()).isEqualTo("encodedPassword");

        verify(userRepository, times(1)).save(any(User.class));
        verify(userRepository, times(1)).findAll();
        verify(userRepository, times(1)).findByUsername("username");

    }

    @Test
    void whenGivenUserTryingToRegister_ThereRoleWillBeUser_SuccessfullyRegistered() {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("username")
                .password("password")
                .confirmPassword("password")
                .agreeTermOfUse(true)
                .build();

        when(userRepository.findByUsername("username")).thenReturn(Optional.empty());
        when(userRepository.findAll()).thenReturn(List.of(new User()));
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        User registeredUser = userService.register(registerRequest);
        assertThat(registeredUser.getUsername()).isEqualTo("username");
        assertThat(registeredUser.getRole()).isEqualTo(UserRole.USER);
        assertThat(registeredUser.getPassword()).isEqualTo("encodedPassword");
        verify(userRepository, times(1)).save(any(User.class));
        verify(userRepository, times(1)).findAll();
        verify(userRepository, times(1)).findByUsername("username");

    }

    @Test
    void whenTryingToReturnAllUsersInDBThenReturnCorrectNumberOfUsers() {

        List<User> userList = List.of(new User(), new User());
        Mockito.when(userService.getAllUsers()).thenReturn(userList);
        List<User> users = userService.getAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    void whenTryingToFindUserById_returnSuccessfully() {

        User user = User.builder()
                .username("username")
                .password("password")
                .id(UUID.randomUUID())
                .age(18)
                .isActive(true)
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Optional<User> byId = userRepository.findById(user.getId());
        assertThat((user.getId())).isEqualTo(byId.get().getId());
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void whenTryingToFindUserById_ThrowException() {

        User test = User.builder()
                .username("username")
                .password("password")
                .age(18)
                .id(UUID.randomUUID())
                .isActive(true)
                .build();

        when(userRepository.findById(test.getId())).thenThrow(UserWithIdDoesNotExist.class);
        assertThrowsExactly(UserWithIdDoesNotExist.class, () -> userRepository.findById(test.getId()));
        verify(userRepository, times(1)).findById(test.getId());

    }

    @Test
    void whenChangingStatusOnUser_UserStatusIsNowInActive() {

        User test = User.builder()
                .username("username")
                .password("password")
                .age(18)
                .id(UUID.randomUUID())
                .isActive(true)
                .build();

        when(userRepository.findById(test.getId())).thenReturn(Optional.of(test));
        userService.changeStatus(test.getId());
        assertThat(test.isActive()).isFalse();
        verify(userRepository, times(1)).findById(test.getId());
        verify(userRepository,times(1)).save(test);

    }

    @Test
    void whenChangingStatusOnUser_UserStatusIsNowActive() {

        User test = User.builder()
                .username("username")
                .password("password")
                .age(18)
                .id(UUID.randomUUID())
                .isActive(false)
                .build();

        when(userRepository.findById(test.getId())).thenReturn(Optional.of(test));
        userService.changeStatus(test.getId());
        assertThat(test.isActive()).isTrue();
        verify(userRepository, times(1)).findById(test.getId());
        verify(userRepository,times(1)).save(test);

    }

    @Test
    void whenUserChangeHisInfo(){
        ProfileEditRequest profileEditRequest = ProfileEditRequest.builder()
                .firstName("firstName")
                .lastName("lastName")
                .profilePicture("profilePicture")
                .email("email@Test.bg")
                .age(22)
                .country("country")
                .build();

        User user = User.builder()
                .id(UUID.randomUUID())
                .username("username")
                .password("password")
                .isActive(true)
                .age(18)
                .build();


        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.editProfileUser(user.getId(), profileEditRequest);
        assertThat(user.getFirstName()).isEqualTo("firstName");
        assertThat(user.getLastName()).isEqualTo("lastName");
        assertThat(user.getProfilePic()).isEqualTo("profilePicture");
        assertThat(user.getEmail()).isEqualTo("email@Test.bg");
        assertThat(user.getAge()).isEqualTo(22);
        assertThat(user.getCountry()).isEqualTo("country");
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository,times(1)).save(user);



    }


}
