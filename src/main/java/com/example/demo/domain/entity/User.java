package com.example.demo.domain.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * User entity to provide meeting created and invited user objects
 */
@Getter
@Setter
public class User {
    private String userId;
    private String name;
    private String imageUrl;
}
