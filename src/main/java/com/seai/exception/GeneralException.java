package com.seai.exception;

public class GeneralException extends RuntimeException {

    public GeneralException(String message, Object... args) {
        super(String.format(message, args));
    }
}
