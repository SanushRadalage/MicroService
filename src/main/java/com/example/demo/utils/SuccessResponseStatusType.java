package com.example.demo.utils;

/**
 * Enum for success
 */
public enum SuccessResponseStatusType {
    CREATE_MEETING(2000, "Meeting created successfully."),
    READ_MEETING(2001, "Return the meeting successfully."),
    READ_SUMMARY(2002, "Return the meeting summary successfully."),
    UPDATE_MEETING(2003, "Meeting was updated successfully."),
    DELETE_MEETING(2004, "Meeting was deleted successfully.");

    private final String message;
    private final int successCode;

    SuccessResponseStatusType(int successCode, String message) {
        this.message = message;
        this.successCode = successCode;
    }

    public String getMessage() {
        return message;
    }

    public int getSuccessCode() {
        return successCode;
    }

    /**
     * Success code covert into string to read display message from success property file
     *
     * @param successCode
     * @return
     */
    public String getCodeString(int successCode) {
        return Integer.toString(successCode);
    }
}
