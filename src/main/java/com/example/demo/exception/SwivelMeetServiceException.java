package com.example.demo.exception;

/**
 * Meeting Service Exception
 */
public class SwivelMeetServiceException extends RuntimeException {

    /**
     * Meeting exception with error message
     *
     * @param errorMessage error message
     */
    public SwivelMeetServiceException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * Meeting exception with error message and throwable error
     * @param errorMessage error message
     *
     * @param error error
     */
    public SwivelMeetServiceException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }

}
