package com.assesment.users_service.domain.ports.in;

import java.util.List;

import com.assesment.users_service.domain.Friend;
import com.assesment.users_service.domain.User;

public interface UsersUseCase {

    User addUser(User user) throws Exception;

    User addFriendToUser(Long userId, Friend friend) throws Exception;
    
    void removeFriendFromUser(Long userId, Long friendId) throws Exception;
    
    List<User> findAll();

    User findById(Long userId);

    void deleteById(Long userId);

    User update(Long userId, User user);

    User addAvatar(Long userId) throws Exception;

}
