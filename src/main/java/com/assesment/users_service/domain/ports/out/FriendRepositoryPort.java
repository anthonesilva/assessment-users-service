package com.assesment.users_service.domain.ports.out;

import com.assesment.users_service.domain.Friend;

public interface FriendRepositoryPort {

    Friend save(Friend friend);

    Friend deleteById(Long friendId);

}
