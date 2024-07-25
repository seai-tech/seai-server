package com.seai.exception;

public class UserAlreadyConfirmedException extends GeneralException{

    public UserAlreadyConfirmedException (String message, Object... args) {
        super(message, args);
    }
}
