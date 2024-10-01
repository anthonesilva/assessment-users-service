package com.assesment.users_service.application.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.assesment.users_service.application.dto.FriendDTO;
import com.assesment.users_service.application.dto.UserDTO;
import com.assesment.users_service.application.dto.UserPatchDTO;
import com.assesment.users_service.application.exception.CustomError;
import com.assesment.users_service.domain.Friend;
import com.assesment.users_service.domain.User;
import com.assesment.users_service.domain.ports.in.UsersUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("users")
@Tag(name = "Users", description = "Service responsible for maintaining applications users and their friends")
public class UserController {

        private final UsersUseCase usersUseCase;

        private final ModelMapper modelMapper;

        @GetMapping
        @Operation(summary = "Finds all users and friends", description = "Resource where all users registered in the application will be retrieved", tags = {
                        "users", "get" })
        @ApiResponses({
                        @ApiResponse(responseCode = "200", content = {
                                        @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json") }),
                        @ApiResponse(responseCode = "204", description = "No users existing in the application", content = {
                                        @Content(schema = @Schema()) })
        })
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

        @GetMapping("{id}")
        @Operation(summary = "Finds an existing user by identifier", description = "Resource where a single registered user in the application will be retrieved", tags = {
                        "users", "get" })
        @ApiResponses({
                        @ApiResponse(responseCode = "200", content = {
                                        @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json") }),
                        @ApiResponse(responseCode = "404", description = "Resource not found", content = {
                                        @Content(schema = @Schema(implementation = CustomError.class), mediaType = "application/json") })
        })
        public ResponseEntity<UserDTO> findUserById(
                        @Parameter(description = "Existing user identifier") @PathVariable Long id) {
                UserDTO existingUser = modelMapper.map(usersUseCase.findById(id), UserDTO.class);
                return new ResponseEntity<>(existingUser, HttpStatus.OK);
        }

        @PostMapping
        @Operation(summary = "Creates users and friends list", description = "Resource where it is possible to create only users or users and list of friends", tags = {
                        "users", "post" })
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Users and friends have been created successfully", content = {
                                        @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json") }),
                        @ApiResponse(responseCode = "400", description = "Validation constraints were broken", content = {
                                        @Content(schema = @Schema(implementation = CustomError.class), mediaType = "application/json") }),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                                        @Content(schema = @Schema(implementation = CustomError.class), mediaType = "application/json") })
        })
        public ResponseEntity<UserDTO> addUser(
                        @Parameter(description = "User request body, friends list is optional") @Valid @RequestBody UserDTO user)
                        throws Exception {
                User userDomainEntity = modelMapper.map(user, User.class);

                UserDTO savedUser = modelMapper.map(usersUseCase.addUser(userDomainEntity), UserDTO.class);

                return ResponseEntity
                                .created(UriComponentsBuilder.fromPath("/users/{id}").buildAndExpand(savedUser.getId())
                                                .toUri())
                                .body(savedUser);
        }

        @PatchMapping("{id}")
        @Operation(summary = "Updates users", description = "Resource where it is possible to update some users' atributes", tags = {
                        "users", "patch" })
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "User has been updated successfully", content = {
                                        @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json") }),
                        @ApiResponse(responseCode = "400", description = "Validation constraints were broken", content = {
                                        @Content(schema = @Schema(implementation = CustomError.class), mediaType = "application/json") }),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                                        @Content(schema = @Schema(implementation = CustomError.class), mediaType = "application/json") })
        })
        public ResponseEntity<UserDTO> updateEntity(
                        @Parameter(description = "Existing user identifier") @PathVariable Long id,
                        @Parameter(description = "User patch request body") @RequestBody UserPatchDTO user) {
                User userDomainEntity = modelMapper.map(user, User.class);

                UserDTO updatedUser = modelMapper.map(usersUseCase.update(id, userDomainEntity), UserDTO.class);

                return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }

        @PostMapping("{id}/friends")
        @Operation(summary = "Adds a friend to an user", description = "Resource where it is possible to add a friend to an existing user", tags = {
                        "friends", "post" })
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Friend has been created successfully", content = {
                                        @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json") }),
                        @ApiResponse(responseCode = "400", description = "Validation constraints were broken", content = {
                                        @Content(schema = @Schema(implementation = CustomError.class), mediaType = "application/json") }),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                                        @Content(schema = @Schema(implementation = CustomError.class), mediaType = "application/json") })
        })
        public ResponseEntity<UserDTO> addFriend(
                        @Parameter(description = "Existing user identifier") @PathVariable Long id,
                        @Parameter(description = "Friend request body to be added") @Valid @RequestBody FriendDTO friend)
                        throws Exception {
                Friend friendDomainEntity = modelMapper.map(friend, Friend.class);

                UserDTO savedUser = modelMapper.map(usersUseCase.addFriendToUser(id, friendDomainEntity),
                                UserDTO.class);

                return ResponseEntity
                                .created(UriComponentsBuilder.fromPath("/users/{id}").buildAndExpand(savedUser.getId())
                                                .toUri())
                                .body(savedUser);
        }

        @DeleteMapping("{id}")
        @ResponseStatus(HttpStatus.OK)
        @Operation(summary = "Removes an existing user and respective friends", description = "Resource where users and respective friends are removed", tags = {
                        "users", "delete" })
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "User has been removed successfully", content = {
                                        @Content(schema = @Schema()) }),
                        @ApiResponse(responseCode = "404", description = "Resource not found", content = {
                                        @Content(schema = @Schema(implementation = CustomError.class), mediaType = "application/json") })
        })
        public void deleteById(@Parameter(description = "Existing user identifier") @PathVariable Long id) {
                usersUseCase.deleteById(id);
        }

        @DeleteMapping("{id}/friends/{friendId}")
        @ResponseStatus(HttpStatus.OK)
        @Operation(summary = "Removes an existing friend from an existing user", description = "Resource where friends are removed from users, users are not removed", tags = {
                        "friends", "delete" })
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Friend has been removed successfully", content = {
                                        @Content(schema = @Schema()) }),
                        @ApiResponse(responseCode = "404", description = "Resource not found", content = {
                                        @Content(schema = @Schema(implementation = CustomError.class), mediaType = "application/json") })
        })
        public void removeFriend(@Parameter(description = "Existing user identifier") @PathVariable Long id,
                        @Parameter(description = "Existing friend identifier") @PathVariable Long friendId)
                        throws Exception {
                usersUseCase.removeFriendFromUser(id, friendId);
        }

}
