package com.example.demo.exception;

/**
 * Exception for invalid meeting Ids
 */
public class InvalidMeetingException extends SwivelMeetServiceException {

    public InvalidMeetingException(String errorMessage) {
        super(errorMessage);
    }
}
