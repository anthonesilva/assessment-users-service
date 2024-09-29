package com.assesment.users_service.domain;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.assesment.users_service.domain.ports.out.AvatarResourcePort;
import com.assesment.users_service.domain.ports.out.FriendRepositoryPort;
import com.assesment.users_service.domain.ports.out.LoggerPort;
import com.assesment.users_service.domain.ports.out.UserRepositoryPort;
import com.assesment.users_service.domain.services.UserService;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private FriendRepositoryPort friendRepository;

    @Mock
    private AvatarResourcePort avatarResource;

    @Mock
    private LoggerPort logger;

    @InjectMocks
    private UserService userService;

    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Given no users in DB, when finding all users, then should return an empty list")
    public void whenFindingAll_shouldReturnEmpty() {
        List<User> allUsers = userService.findAll();
        
        verify(userRepository, times(1)).findAll();
        assertTrue(allUsers.isEmpty());
    }

    @Test
    @DisplayName("Given existing users in DB, when finding all users, then should return a list with records")
    public void whenFindingAll_shouldReturnList() {
        when(userRepository.findAll()).thenReturn(listUsersMock());

        List<User> allUsers = userService.findAll();

        verify(userRepository, times(1)).findAll();
        assertFalse(allUsers.isEmpty());
    }

    @Test
    @DisplayName("Given user no present in DB, when adding new user, then should return the user added")
    public void whenAddingUser_shouldReturnNewUser() throws Exception {
        List<Friend> friends = List.of(new Friend("firstName", "lastName"));
        User user = new User(1L, "test@test.com", "firstName", "lastName", "http://avatar-url.com", friends);
        when(userRepository.save(any())).thenReturn(user);
        when(avatarResource.findAvatarUrl(any())).thenReturn("http://avatar-url.com");

        User savedUser = userService.addUser(any());

        verify(userRepository, atLeastOnce()).save(any());
        verify(avatarResource, atLeastOnce()).findAvatarUrl(any());
        assertNotNull(savedUser);
        assertFalse(savedUser.getAvatar().isEmpty());
    }

    @Test
    @DisplayName("Given user present in DB, when requesting by ID, then should return the user")
    public void whenFindingById_shouldReturnExistingUser() {
        List<Friend> friends = List.of(new Friend("firstName", "lastName"));
        User user = new User(1L, "test@test.com", "firstName", "lastName", "http://avatar-url.com", friends);
        when(userRepository.findById(any())).thenReturn(user);

        User requestedUser = userService.findById(any());

        verify(userRepository, atLeastOnce()).findById(any());
        assertNotNull(requestedUser);
    }

    @Test
    @DisplayName("Given user present in DB, when updating entity, then should return the updated user")
    public void whenUpdatingByUser_shouldReturnUpdatedUser() {
        List<Friend> friends = List.of(new Friend("firstName", "lastName"));
        User inputUser = new User(1L, "test@test.com", "firstNameNew", "lastName", "http://avatar-url.com", friends);
        when(userRepository.save(any())).thenReturn(inputUser);

        User updatedUser = userService.update(any());

        verify(userRepository, atLeastOnce()).save(any());
        assertNotNull(updatedUser);
        assertTrue(updatedUser.getFirstName().contentEquals("firstNameNew"));
    }

    @Test
    @DisplayName("Given user present in DB and avatar present in external API, when adding avatar, then should return the user with avatar")
    public void whenAddingAvatar_shouldReturnExistingUserWithAvatar() throws Exception {
        List<Friend> friends = List.of(new Friend("firstName", "lastName"));
        User user = new User(1L, "test@test.com", "firstName", "lastName", "http://avatar-url.com", friends);
        when(userRepository.findById(any())).thenReturn(user);
        when(userRepository.save(any())).thenReturn(user);
        when(avatarResource.findAvatarUrl(any())).thenReturn("http://avatar-url.com");

        User savedUser = userService.addAvatar(any());

        verify(userRepository, atLeastOnce()).findById(any());
        verify(userRepository, atLeastOnce()).save(any());
        verify(avatarResource, atLeastOnce()).findAvatarUrl(any());
        assertNotNull(savedUser);
        assertFalse(savedUser.getAvatar().isEmpty());
    }

    public List<User> listUsersMock() {
        User user = new User(1L, "test@test.com", "firstName", "lastName", "url");
        List<User> users = List.of(user);
        return users;
    }


}
