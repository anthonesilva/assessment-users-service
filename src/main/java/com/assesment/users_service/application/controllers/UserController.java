package com.assesment.users_service.application.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.assesment.users_service.application.dto.UserDTO;
import com.assesment.users_service.domain.User;
import com.assesment.users_service.domain.ports.in.UsersUseCase;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

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

        return ResponseEntity.ok(allUsers);
    }

    @PostMapping
    public ResponseEntity<UserDTO> addUser(@Valid @RequestBody UserDTO user) {
        User userDomainEntity = modelMapper.map(user, User.class);

        UserDTO savedUser = modelMapper.map(usersUseCase.addUser(userDomainEntity), UserDTO.class);

        return ResponseEntity
                .created(UriComponentsBuilder.fromPath("/users/{id}").buildAndExpand(savedUser.getId()).toUri())
                .body(savedUser);
    }

}
