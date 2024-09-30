package com.assesment.users_service.infra.repositories;

import org.springframework.stereotype.Component;

import com.assesment.users_service.domain.Friend;
import com.assesment.users_service.domain.ports.out.FriendRepositoryPort;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class FriendRepositoryAdapter implements FriendRepositoryPort {

    private final FriendRepository friendRepository;

    @Override
    public Friend save(Friend friend) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public Friend deleteById(Long friendId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

}
