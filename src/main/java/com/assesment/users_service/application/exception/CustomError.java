package com.assesment.users_service.application.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "Error", description = "Standardized error handling object")
public class CustomError {

    private String message;
    private String details;

}
