package com.seai.common.exception;

public class MaxSeatsReachedException extends GeneralException {

    public MaxSeatsReachedException(String message, Object... args) {
        super(message, args);
    }
}
