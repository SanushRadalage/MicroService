package com.example.demo.domain;

import com.example.demo.exception.SwivelMeetServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Base Dto provides object json mapper
 */
public interface BaseDto {

    /**
     * This method converts object to json string.
     *
     * @return json String
     */
    default String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new SwivelMeetServiceException("Json conversion failed", e);
        }
    }

    /**
     * This method convert object to json string for logging
     * @return json string
     */
    String toLogJson();

}

