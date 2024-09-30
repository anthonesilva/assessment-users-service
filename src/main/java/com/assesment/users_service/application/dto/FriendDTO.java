package com.assesment.users_service.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("first_name")
    @NotEmpty
    private String firstName;

    @JsonProperty("last_name")
    @NotEmpty
    private String lastName;

}
