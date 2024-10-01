package com.assesment.users_service.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Friend")
public class FriendDTO {

    @Schema(description = "Friend identifier", example = "2")
    @JsonProperty("id")
    private Long id;

    @Schema(description = "First name", example = "Anthony")
    @JsonProperty("first_name")
    @NotEmpty
    private String firstName;

    @Schema(description = "Last name", example = "Kiedis")
    @JsonProperty("last_name")
    @NotEmpty
    private String lastName;

}
