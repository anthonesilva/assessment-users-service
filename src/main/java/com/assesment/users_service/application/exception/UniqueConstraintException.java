package com.assesment.users_service.application.exception;

public class UniqueConstraintException extends RuntimeException {

    public UniqueConstraintException() {}

    public UniqueConstraintException(String msg) {
        super(msg);
    }
}
