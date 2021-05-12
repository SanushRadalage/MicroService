package com.example.demo.wrapper;

import com.example.demo.domain.response.ResponseDto;
import com.example.demo.utils.ResponseStatusType;
import com.example.demo.utils.SuccessResponseStatusType;
import lombok.Getter;

/**
 * Wrapper for success response format
 */
@Getter
public class SuccessResponseWrapper extends ResponseWrapper {
    public SuccessResponseWrapper(ResponseStatusType status, SuccessResponseStatusType successResponseStatusType, ResponseDto responseDto, String displayMessage) {
        super(status, successResponseStatusType.getMessage(), responseDto, displayMessage);
    }
}
