package com.assesment.users_service.application.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomError {

    private String message;
    private String details;

}
