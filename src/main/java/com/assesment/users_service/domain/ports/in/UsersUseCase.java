package com.assesment.users_service.domain.ports.in;

import java.util.List;

import com.assesment.users_service.domain.Friend;
import com.assesment.users_service.domain.User;

public interface UsersUseCase {

    User addUser(User user);

    User addFriend(Long userId, Friend friend);
    
    User removeFriend(Long userId, Long friendId);
    
    List<User> findAll();

    User findById(Long userId);

    User deleteById(Long userId);

    User update(User user);

    User addAvatar(Long userId);

}
