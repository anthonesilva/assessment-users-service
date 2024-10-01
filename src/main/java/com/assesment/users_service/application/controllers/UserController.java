package com.assesment.users_service.application.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.assesment.users_service.application.dto.FriendDTO;
import com.assesment.users_service.application.dto.UserDTO;
import com.assesment.users_service.application.dto.UserPatchDTO;
import com.assesment.users_service.domain.Friend;
import com.assesment.users_service.domain.User;
import com.assesment.users_service.domain.ports.in.UsersUseCase;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("users")
@AllArgsConstructor
public class UserController {

    private final UsersUseCase usersUseCase;

    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserDTO> allUsers = usersUseCase.findAll()
                .stream()
                .map(userDomainEntity -> modelMapper.map(userDomainEntity, UserDTO.class))
                .collect(Collectors.toList());

        if (allUsers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allUsers);
    }

    @PostMapping
    public ResponseEntity<UserDTO> addUser(@Valid @RequestBody UserDTO user) throws Exception {
        User userDomainEntity = modelMapper.map(user, User.class);

        UserDTO savedUser = modelMapper.map(usersUseCase.addUser(userDomainEntity), UserDTO.class);

        return ResponseEntity
                .created(UriComponentsBuilder.fromPath("/users/{id}").buildAndExpand(savedUser.getId()).toUri())
                .body(savedUser);
    }

    @PatchMapping("{id}")
    public ResponseEntity<UserDTO> updateEntity(@PathVariable Long id, @RequestBody UserPatchDTO user) {
        User userDomainEntity = modelMapper.map(user, User.class);

        UserDTO updatedUser = modelMapper.map(usersUseCase.update(id, userDomainEntity), UserDTO.class);
        
        return new ResponseEntity<UserDTO>(updatedUser, HttpStatus.OK);
    }

    @PostMapping("{id}/friends")
    public ResponseEntity<UserDTO> addFriend(@PathVariable Long id, @Valid @RequestBody FriendDTO friend) throws Exception {
        Friend friendDomainEntity = modelMapper.map(friend, Friend.class);

        UserDTO savedUser = modelMapper.map(usersUseCase.addFriendToUser(id, friendDomainEntity), UserDTO.class);

        return ResponseEntity
                .created(UriComponentsBuilder.fromPath("/users/{id}").buildAndExpand(savedUser.getId()).toUri())
                .body(savedUser);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable Long id) {
        usersUseCase.deleteById(id);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Long id, @PathVariable Long friendId) throws Exception {
        usersUseCase.removeFriendFromUser(id, friendId);
    }

}
