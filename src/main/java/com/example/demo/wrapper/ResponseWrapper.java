package com.example.demo.wrapper;

import com.example.demo.domain.response.ResponseDto;
import com.example.demo.utils.ResponseStatusType;
import lombok.Getter;

/**
 * Base wrapper class
 */
@Getter
public class ResponseWrapper {

    private final ResponseStatusType status;
    private final String message;
    private final ResponseDto data;
    private final String displayMessage;

    /**
     * @param status
     * @param message
     * @param data
     * @param displayMessage
     */
    public ResponseWrapper(ResponseStatusType status, String message, ResponseDto data, String displayMessage) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.displayMessage = displayMessage;
    }
}
