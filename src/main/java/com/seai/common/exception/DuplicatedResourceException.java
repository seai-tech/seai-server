package com.seai.common.exception;

public class DuplicatedResourceException extends GeneralException {

    public DuplicatedResourceException(String message, Object... args) {
        super(message, args);
    }
}
