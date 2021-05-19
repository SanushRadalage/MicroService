package com.example.demo.domain.request;

import com.example.demo.domain.BaseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Data transfer object to get users request
 */
@Getter
@Setter
public class UserRequestDto implements BaseDto {

    private final List<String> userIdList;

    public UserRequestDto(List<String> userIdList) {
        this.userIdList = userIdList;
    }

    @Override
    public String toLogJson() {
        return toJson();
    }
}
