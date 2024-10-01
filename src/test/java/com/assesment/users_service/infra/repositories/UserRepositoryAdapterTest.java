package com.assesment.users_service.infra.repositories;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import com.assesment.users_service.application.exception.UniqueConstraintException;
import com.assesment.users_service.domain.Friend;
import com.assesment.users_service.domain.User;
import com.assesment.users_service.infra.repositories.impl.UserRepositoryAdapter;

import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
@ActiveProfiles("local")
public class UserRepositoryAdapterTest {

    @MockBean
    private UserRepository userRepositoryMock;

    @Autowired
    private UserRepositoryAdapter userRepositoryAdapter;

    @Test
    @DisplayName("Given existing user in DB, when saving user with same email, then should raise exception")
    public void whenSavingUserWithSameEmail_shouldRaiseUniqueConstraintException() {
        List<Friend> friends = List.of(new Friend("firstName", "lastName"));
        User user = new User(1L, "test@test.com", "firstName", "lastName", null, friends);

        when(userRepositoryMock.save(any())).thenThrow(new DataIntegrityViolationException("Unique constraint violated"));

        assertThrows(UniqueConstraintException.class, () -> {
            userRepositoryAdapter.save(user);
        });
    }

    @Test
    @DisplayName("Given non existing user in DB, when querying by ID, then should raise exception")
    public void whenQueryingNonExistingUserById_shouldRaiseEntityNotFoundException() {
        when(userRepositoryMock.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            userRepositoryAdapter.findById(1L);
        });
    }

    @Test
    @DisplayName("Given non existing user in DB, when deleting by ID, then should raise exception")
    public void whenDeletingNonExistingUserById_shouldRaiseEntityNotFoundException() {
        when(userRepositoryMock.existsById(any())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> {
            userRepositoryAdapter.deleteById(1L);
        });
    }

    @Test
    @DisplayName("Given non existing user in DB, when updating entity by ID, then should raise exception")
    public void whenUpdatingNonExistingUserById_shouldRaiseEntityNotFoundException() {
        when(userRepositoryMock.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            userRepositoryAdapter.update(1L, mock(User.class));
        });
    }
}
