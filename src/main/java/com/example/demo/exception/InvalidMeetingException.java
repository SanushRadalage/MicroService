package com.example.demo.exception;

public class InvalidMeetingException extends SwivelMeetServiceException {

    public InvalidMeetingException(String errorMessage) {
        super(errorMessage);
    }
}
