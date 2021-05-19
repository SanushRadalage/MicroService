package com.example.demo.service;

import com.example.demo.domain.request.UserRequestDto;
import com.example.demo.domain.response.UserResponseDto;
import com.example.demo.exception.InvalidUserException;
import com.example.demo.exception.SwivelMeetServiceException;
import com.example.demo.wrapper.UserResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Optional;

/**
 * User service handle external get user API
 */
@Slf4j
@Service
public class UserService {

    private static final String FAILED_LOG_MESSAGE = "Read users by id was failed.";
    private static final String INVALID_USER_RESPONSE = "Invalid user response.";
    private final String userServiceUrl;
    private final RestTemplate restTemplate;
    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserService(@Value("${userServiceUrl}") final String userServiceUrl, RestTemplate restTemplate) {
        this.userServiceUrl = userServiceUrl;
        this.restTemplate = restTemplate;
    }

    public UserResponseDto getUserList(UserRequestDto userRequestDto) {
        HttpEntity<UserRequestDto> entity = new HttpEntity<>(userRequestDto, null);


        String requestBody = entity.getBody() == null ? "" : entity.getBody().toLogJson();

        log.debug("Read users by Id list. url: {}, requestBody: {}",
                userServiceUrl, requestBody);

        try {
            ResponseEntity<UserResponseWrapper> result =
                    restTemplate.exchange(userServiceUrl, HttpMethod.POST, entity, UserResponseWrapper.class);
            String responseBody = Objects.requireNonNull(result.getBody()).getData().toLogJson();
            log.debug("Read users by Id list successfully. statusCode: {}, body: {}",
                    result.getStatusCode(), responseBody);

            Optional<UserResponseDto> optionalUserResponseDto = Optional.ofNullable(result.getBody().getData());

            if (optionalUserResponseDto.isPresent()) {
                return optionalUserResponseDto.get();
            } else {
                throw new InvalidUserException(INVALID_USER_RESPONSE);
            }

        } catch (HttpClientErrorException e) {
            log.error(FAILED_LOG_MESSAGE, e);
            throw new SwivelMeetServiceException(e.getMessage());
        }

    }
}
