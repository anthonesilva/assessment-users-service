package com.assesment.users_service.infra.repositories.impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.assesment.users_service.application.exception.UniqueConstraintException;
import com.assesment.users_service.domain.Friend;
import com.assesment.users_service.domain.ports.out.FriendRepositoryPort;
import com.assesment.users_service.infra.repositories.FriendRepository;
import com.assesment.users_service.infra.repositories.entities.FriendEntity;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class FriendRepositoryAdapter implements FriendRepositoryPort {

    private final FriendRepository friendRepository;

    @Override
    public Friend save(Friend friend) {
        try {
            FriendEntity friendEntity = FriendEntity.fromDomain(friend);
            FriendEntity savedEntity = friendRepository.save(friendEntity);
            return savedEntity.toDomain();
        } catch (DataIntegrityViolationException ex) {
            throw new UniqueConstraintException("Email already exists in the database.");
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void deleteById(Long friendId) {
        if (friendRepository.existsById(friendId)) {
            friendRepository.deleteById(friendId);
        } else {
            throw new EntityNotFoundException("Friend has not be found.");
        }
    }

}
