package com.assesment.users_service.domain.ports.out;

import java.util.List;

import com.assesment.users_service.domain.User;

public interface UserRepositoryPort {

    User save(User user);

    User findByEmail(String email);

    List<User> findByFirstName(String firstName);
    
    List<User> findAll();

    User findById(Long userId);

    User deleteById(Long userId);

    User update(User user);

}
