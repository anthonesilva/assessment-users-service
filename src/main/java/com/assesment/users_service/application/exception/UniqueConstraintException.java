package com.assesment.users_service.application.exception;

public class UniqueConstraintException extends RuntimeException {
    
    private String message;

    public UniqueConstraintException() {}

    public UniqueConstraintException(String msg) {
        super(msg);
        this.message = msg;
    }
}
