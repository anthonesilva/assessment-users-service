package com.assesment.users_service.application.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.assesment.users_service.domain.Friend;
import com.assesment.users_service.domain.User;
import com.assesment.users_service.domain.ports.in.UsersUseCase;
import com.assesment.users_service.domain.ports.out.AvatarResourcePort;
import com.assesment.users_service.domain.ports.out.FriendRepositoryPort;
import com.assesment.users_service.domain.ports.out.LoggerPort;
import com.assesment.users_service.domain.ports.out.UserRepositoryPort;

import lombok.AllArgsConstructor;


// TODO: do refactoring using Optional
// TODO: handle exceptions properly
// TODO: better usage of jpa repository methods
@Service
@AllArgsConstructor
public class UserService implements UsersUseCase {

    private static final String LOG_LEVEL = "INFO";

    private final UserRepositoryPort userRepository;
    private final FriendRepositoryPort friendRepository;
    private final AvatarResourcePort avatarResource;
    private final LoggerPort logger;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User addUser(User user) {
        User savedUser = userRepository.save(user);
        addAvatarExistingUser(savedUser);
        return userRepository.save(user);
    }    

    @Override
    public User addFriend(Long userId, Friend friend) {
        User user = userRepository.findById(userId);
        Friend savedFriend = friendRepository.save(friend);
        user.addFriend(savedFriend);
        return userRepository.save(user);
    }

    @Override
    public User removeFriend(Long userId, Long friendId) {
        User user = userRepository.findById(userId);
        Friend removedFriend = friendRepository.deleteById(friendId);
        user.removeFriend(removedFriend);
        return userRepository.save(user);
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public User deleteById(Long userId) {
        User user = userRepository.findById(userId);
        if (user != null) {
            return userRepository.deleteById(user.getId());
        } else {
            return null;
        }        
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public User addAvatar(Long userId) {
        User user = userRepository.findById(userId);
        String avatarUrl = findAvatar(userId);
        user.setAvatar(avatarUrl);
        return userRepository.save(user);
    }

    private void addAvatarExistingUser(User user) {
        String avatarUrl = findAvatar(user.getId());
        user.setAvatar(avatarUrl);
    }

    private String findAvatar(Long userId) {
        try {
            String avatarUrl = avatarResource.findAvatarUrl(userId);
            logger.log(LOG_LEVEL, avatarUrl);
            return avatarUrl;
        } catch (Exception e) {
            logger.log(LOG_LEVEL, "It was not possible to find the avatar");
        }
        return null;
    }

}
