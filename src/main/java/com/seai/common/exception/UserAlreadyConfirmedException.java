package com.seai.common.exception;

public class UserAlreadyConfirmedException extends GeneralException{

    public UserAlreadyConfirmedException (String message, Object... args) {
        super(message, args);
    }
}
