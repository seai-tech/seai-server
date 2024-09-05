package com.seai.common.exception;

public class ResourceNotFoundException extends GeneralException {

    public ResourceNotFoundException(String message, Object... args) {
        super(message, args);
    }
}
