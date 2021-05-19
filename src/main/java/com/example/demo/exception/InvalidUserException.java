package com.example.demo.exception;

/**
 * Exception for get user objects from get user external API
 */
public class InvalidUserException extends SwivelMeetServiceException {

    public InvalidUserException(String errorMessage) {
        super(errorMessage);
    }
}
