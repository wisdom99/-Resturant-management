package com.restaurant.management.config.security;

import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class JsonLoginRequest {

    private String userId;
    private String secret;
}
