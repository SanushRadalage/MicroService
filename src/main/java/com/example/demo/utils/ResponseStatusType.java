package com.example.demo.utils;

/**
 * Enum for status
 */
public enum ResponseStatusType {
    SUCCESS("Success"),
    ERROR("Error");

    private final String status;

    ResponseStatusType(String status) {
        this.status = status;
    }
}
