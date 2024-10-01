package com.assesment.users_service.application.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.assesment.users_service.application.dto.FriendDTO;
import com.assesment.users_service.application.dto.UserDTO;
import com.assesment.users_service.application.dto.UserPatchDTO;
import com.assesment.users_service.domain.Friend;
import com.assesment.users_service.domain.User;
import com.assesment.users_service.domain.ports.in.UsersUseCase;

@SpringBootTest
@ActiveProfiles("local")
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class UserControllerTest {

    private static final String USER_RESOURCE = "/users";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<UserDTO> userDtoTester;

    @Autowired
    private JacksonTester<UserPatchDTO> userPatchDtoTester;

    @Autowired
    private JacksonTester<FriendDTO> friendDtoTester;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private UsersUseCase useCaseMock;

    @Test
    @DisplayName("Given no users in application, when finding all users, then should return no content")
    void whenFindingAllUsers_shouldReturnNoContent() throws Exception {
        given(useCaseMock.findAll()).willReturn(new ArrayList<>());

        mockMvc.perform(get(USER_RESOURCE))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Given existing users in application, when finding all users, then should return success")
    void whenFindingAllUsers_shouldReturnSucess() throws Exception {
        given(useCaseMock.findAll()).willReturn(listUsersMock());

        mockMvc.perform(get(USER_RESOURCE))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Given existing users in application, when finding by identifier, then should return success")
    void whenFindingOnlyUserByIdentifier_shouldReturnSucess() throws Exception {
        given(useCaseMock.findById(1L)).willReturn(listUsersMock().get(0));

        mockMvc.perform(get(USER_RESOURCE.concat("/1")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Given creating valid user, when it is created, then should return created")
    void whenCreatingUser_shouldReturnCreated() throws Exception {
        UserDTO userInput = UserDTO.builder().email("test@test.com").firstName("firstName").lastName("lastName").build();
        given(useCaseMock.addUser(modelMapper.map(userInput, User.class))).willReturn(listUsersMock().get(1));

        mockMvc.perform(
                post(USER_RESOURCE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoTester.write(userInput).getJson()))
                .andExpect(status().isCreated());
    }
    
    @Test
    @DisplayName("Given creating invalid user, when validating it, then should return bad request")
    void whenCreatingUser_shouldReturnBadRequest() throws Exception {
        UserDTO userInput = UserDTO.builder().firstName("firstName").lastName("lastName").build();

        mockMvc.perform(
                post(USER_RESOURCE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoTester.write(userInput).getJson()))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("Given existing user, when updating it with a valid input, then should return success")
    void whenUpdatingUser_shouldReturnSuccess() throws Exception {
        UserPatchDTO userInput = UserPatchDTO.builder().lastName("secondToLastName").build();
        given(useCaseMock.update(1L, modelMapper.map(userInput, User.class))).willReturn(listUsersMock().get(0));


        mockMvc.perform(
                patch(String.format("%s/%s", USER_RESOURCE, 1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userPatchDtoTester.write(userInput).getJson()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Given existing user, when deleting it, then should return success")
    void whenDeletingUser_shouldReturnSuccess() throws Exception {
        doNothing().when(useCaseMock).deleteById(1L);

        mockMvc.perform(
                delete(String.format("%s/%s", USER_RESOURCE, 1L))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Given creating valid friend input and existing user, when creating friend, then should return created")
    void whenCreatingFriend_shouldReturnBadRequest() throws Exception {
        FriendDTO friendInput = FriendDTO.builder().firstName("friendFirstName").lastName("friendLastName").build();
        given(useCaseMock.addFriendToUser(1L, modelMapper.map(friendInput, Friend.class))).willReturn(listUsersMock().get(1));

        mockMvc.perform(
                post(String.format("%s/%s/friends", USER_RESOURCE, 1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(friendDtoTester.write(friendInput).getJson()))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Given existing user and existing friend, when deleting friend, then should return success")
    void whenDeletingFriend_shouldReturnSuccess() throws Exception {
        doNothing().when(useCaseMock).removeFriendFromUser(1L, 1L);

        mockMvc.perform(
                delete(String.format("%s/%s/friends/%s", USER_RESOURCE, 1L, 1L))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private List<User> listUsersMock() {
        User user1 = new User(1L, "test@test.com", "firstName", "lastName", "http://avatar-url.com", Arrays.asList(new Friend(1L, "firstName", "lastName"), new Friend(2L, "firstName", "lastName")));
        User user2 = new User(2L, "test@test.com", "firstName", "lastName", "http://avatar-url.com", new ArrayList<>());
        return Arrays.asList(user1, user2);
    }



}
