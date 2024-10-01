package com.assesment.users_service.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "UserUpdate")
public class UserPatchDTO {

    @Schema(description = "First name", example = "Chuck")
    @JsonProperty("first_name")
    private String firstName;

    @Schema(description = "Last name", example = "Norris")
    @JsonProperty("last_name")
    private String lastName;

    @Schema(description = "Avatar", example = "http://url.to/avatar")
    @JsonProperty("avatar")
    private String avatar;

}
