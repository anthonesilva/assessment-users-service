package com.assesment.users_service.application.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "User")
public class UserDTO {

    @Schema(description = "User identifier", example = "1")
    @JsonProperty("id")    
    private Long id;
    
    @Schema(description = "Email", example = "chuck@norris.com")
    @JsonProperty("email")
    @Email
    @NotEmpty
    private String email;

    @Schema(description = "First name", example = "Chuck")
    @JsonProperty("first_name")
    private String firstName;

    @Schema(description = "Last name", example = "Norris")
    @JsonProperty("last_name")
    private String lastName;

    @Schema(description = "Avatar", example = "http://url.to/avatar")
    @JsonProperty("avatar")
    private String avatar;

    @Schema(description = "Friends list")
    @JsonProperty("friends")
    @JsonInclude(Include.NON_NULL)
    private List<@Valid FriendDTO> friends;

}
