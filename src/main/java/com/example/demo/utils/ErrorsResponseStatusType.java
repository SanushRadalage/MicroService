package com.example.demo.utils;

/**
 * Enum for errors
 */
public enum ErrorsResponseStatusType {
    FIELDS_MISSING(4000, "Required fields are missing."),
    INVALID_DATETIME(4003, "Invalid dateTime."),
    LIMIT_EXCEED(4004, "Invitees limit exceeded."),
    INVALID_TIMEZONE(4005, "Invalid time zone."),
    INVALID_TIME_DURATION(4006, "Invalid time duration."),
    INVALID_MEETING(4007, "Invalid meeting."),
    INTERNAL_SERVERERROR(5000, "Internal server error.");

    private final String message;
    private final int errorCode;

    ErrorsResponseStatusType(int errorCode, String message) {
        this.message = message;
        this.errorCode = errorCode;
    }

    /**
     * Error code covert into string to read display message from error property file
     *
     * @param errorCode
     * @return errorCode as string
     */
    public static String getCodeString(int errorCode) {
        return Integer.toString(errorCode);
    }


    public String getMessage() {
        return message;
    }


    public int getErrorCode() {
        return errorCode;
    }
}