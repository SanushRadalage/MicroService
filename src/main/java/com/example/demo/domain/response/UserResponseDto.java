package com.example.demo.domain.response;

import com.example.demo.domain.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * UserResponseDto gets user object map
 */
@Getter
@Setter
public class UserResponseDto implements ResponseDto {

    Map<String, User> users = new HashMap<>();

    @Override
    public String toLogJson() {
        return toJson();
    }
}
