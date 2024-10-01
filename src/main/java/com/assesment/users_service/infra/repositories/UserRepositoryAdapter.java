package com.assesment.users_service.infra.repositories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.assesment.users_service.application.exception.UniqueConstraintException;
import com.assesment.users_service.domain.User;
import com.assesment.users_service.domain.ports.out.UserRepositoryPort;
import com.assesment.users_service.infra.repositories.entities.FriendEntity;
import com.assesment.users_service.infra.repositories.entities.UserEntity;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        try {
            UserEntity userEntity = UserEntity.fromDomain(user);

            if (userEntity.getFriends() != null) {
                for (FriendEntity friendEntity : userEntity.getFriends()) {
                    friendEntity.setUser(userEntity);
                }
            }

            UserEntity savedEntity = userRepository.save(userEntity);
            return savedEntity.toDomain();
        } catch (DataIntegrityViolationException ex) {
            if (ex.getMessage().contains("Unique")) {
                throw new UniqueConstraintException("Email already exists in the database.");
            } else {
                throw ex;
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public User findByEmail(String email) {
        UserEntity entity = userRepository.findByEmail(email);
        return entity.toDomain();
    }

    @Override
    public List<User> findByFirstName(String firstName) {
        List<UserEntity> entities = userRepository.findByFirstName(firstName);
        return entities.stream()
                .map(UserEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findAll() {
        List<UserEntity> allUsers = userRepository.findAllWithChildren();
        return allUsers.stream()
                .map(UserEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public User findById(Long userId) {
        Optional<UserEntity> entity = userRepository.findById(userId);

        if (entity.isPresent()) {
            UserEntity userEntity = entity.get();
            return userEntity.toDomain();
        } else {
            throw new EntityNotFoundException("User has not be found.");
        }

    }

    @Override
    public void deleteById(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new EntityNotFoundException("Targeted entity has not be found for deletion.");
        }
    }

    @Override
    public User update(Long userId, User user) {

        if (user.getAvatar() != null && user.getAvatar().isEmpty()) {
            throw new IllegalArgumentException("Avatar cannot be empty.");
        }

        Optional<UserEntity> existingEntity = userRepository.findById(userId);
        if (existingEntity.isPresent()) {
            UserEntity entity = existingEntity.get();

            if (user.getFirstName() != null)
                entity.setFirstName(user.getFirstName());
            if (user.getLastName() != null)
                entity.setLastName(user.getLastName());
            if (user.getAvatar() != null)
                entity.setAvatar(user.getAvatar());
            if (user.getFriends() != null)
                entity.setFriends(user.getFriends().stream()
                        .map(friend -> FriendEntity.fromDomain(friend))
                        .collect(Collectors.toList()));

            UserEntity updatedEntity = userRepository.save(entity);
            return updatedEntity.toDomain();
        } else {
            throw new EntityNotFoundException("Targeted entity has not be found for update.");
        }
    }

}
