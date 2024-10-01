package com.assesment.users_service.infra.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.assesment.users_service.infra.repositories.entities.UserEntity;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);
    List<UserEntity> findByFirstName(String firstName);

    @Query("SELECT u FROM user u JOIN FETCH u.friends")
    List<UserEntity> findAllWithChildren();
    
}
