package com.example.demo.domain.request;

import com.example.demo.domain.BaseDto;

/**
 * Request Dto can use by any Dto class in the package for field Validations.
 */
abstract class RequestDto implements BaseDto {

    /**
     * This function checks the required fields are available or not.
     * This is accessible to the controller
     * @return
     */
    public abstract boolean isRequiredAvailable();

    /**
     * This function checks if the filed is empty or not
     * @param field
     * @return
     */
    protected boolean isEmpty(String field) {
        return field != null && !field.isEmpty();
    }
}
