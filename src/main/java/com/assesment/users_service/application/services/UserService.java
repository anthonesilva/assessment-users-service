package com.assesment.users_service.application.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.assesment.users_service.domain.Friend;
import com.assesment.users_service.domain.User;
import com.assesment.users_service.domain.ports.in.UsersUseCase;
import com.assesment.users_service.domain.ports.out.AvatarResourcePort;
import com.assesment.users_service.domain.ports.out.FriendRepositoryPort;
import com.assesment.users_service.domain.ports.out.LoggerPort;
import com.assesment.users_service.domain.ports.out.UserRepositoryPort;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class UserService implements UsersUseCase {

    private static final String LOG_LEVEL = "INFO";
    private static final String DEFAULT_AVATAR = "https://www.kindpng.com/picc/m/22-223863_no-avatar-png-circle-transparent-png.png";

    private final UserRepositoryPort userRepository;
    private final FriendRepositoryPort friendRepository;
    private final AvatarResourcePort avatarResource;
    private final LoggerPort logger;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User addUser(User user) throws Exception {
        User savedUser = userRepository.save(user);
        String avatar = findAvatar(savedUser.getId());
        savedUser.setAvatar(avatar);
        return userRepository.save(savedUser);
    }    

    @Override
    public User addFriendToUser(Long userId, Friend friend) throws Exception {
        User user = userRepository.findById(userId);
        user.addFriend(friend);
        return userRepository.save(user);
    }

    @Override
    public void removeFriendFromUser(Long userId, Long friendId) throws Exception{
        User user = userRepository.findById(userId);
        friendRepository.deleteById(friendId);
        user.setFriends(user.getFriends().stream().filter(friend -> !friend.getId().equals(friendId)).collect(Collectors.toList()));
        userRepository.save(user);
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public void deleteById(Long userId) {
        try {
            userRepository.deleteById(userId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public User update(Long userId, User user) {
        return userRepository.update(userId, user);
    }

    @Override
    public User addAvatar(Long userId) throws Exception {
        User user = userRepository.findById(userId);
        String avatarUrl = findAvatar(userId);
        user.setAvatar(avatarUrl);
        return userRepository.save(user);
    }

    private String findAvatar(Long userId) {
        String avatarUrl = avatarResource.findAvatarUrl(userId);
        
        if (avatarUrl == null) {
            logger.log(LOG_LEVEL, String.format("[findAvatar] It has not been possible to find the avatar for user ID: %s.", userId));
            avatarUrl = DEFAULT_AVATAR;
        }
        return avatarUrl;
    }

}
