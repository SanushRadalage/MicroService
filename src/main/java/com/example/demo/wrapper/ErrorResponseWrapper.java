package com.example.demo.wrapper;

import com.example.demo.domain.response.ResponseDto;
import com.example.demo.utils.ResponseStatusType;
import lombok.Getter;

/**
 * Wrapper for error response format
 */
@Getter
public class ErrorResponseWrapper extends ResponseWrapper {

    private final int errorCode;

    public ErrorResponseWrapper(ResponseStatusType status, String message, ResponseDto data, String displayMessage, int errorCode) {
        super(status, message, data, displayMessage);
        this.errorCode = errorCode;
    }
}
