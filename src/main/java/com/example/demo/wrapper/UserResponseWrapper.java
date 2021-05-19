package com.example.demo.wrapper;

import com.example.demo.domain.BaseDto;
import com.example.demo.domain.response.UserResponseDto;
import com.example.demo.utils.ResponseStatusType;
import lombok.Getter;
import lombok.Setter;

/**
 * Wrapper for get response from external user API
 */
@Getter
@Setter
public class UserResponseWrapper implements BaseDto {

    private ResponseStatusType status;
    private String message;
    private String displayMessage;
    private UserResponseDto data;

    @Override
    public String toLogJson() {
        return null;
    }
}
