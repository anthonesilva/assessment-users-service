package com.assesment.users_service.infra.repositories;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.assesment.users_service.domain.User;
import com.assesment.users_service.domain.ports.out.UserRepositoryPort;
import com.assesment.users_service.infra.repositories.entities.UserEntity;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        UserEntity userEntity = UserEntity.fromDomain(user);
        UserEntity savedEntity = userRepository.save(userEntity);
        return savedEntity.toDomain();
    }

    @Override
    public User findByEmail(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByEmail'");
    }

    @Override
    public List<User> findByFirstName(String firstName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByFirstName'");
    }

    @Override
    public List<User> findAll() {
        List<UserEntity> allUsers = userRepository.findAll();
        return allUsers.stream()
                .map(UserEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public User findById(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public User deleteById(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public User update(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

}
