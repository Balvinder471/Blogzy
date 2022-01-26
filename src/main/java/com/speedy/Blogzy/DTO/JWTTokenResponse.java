package com.speedy.Blogzy.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JWTTokenResponse {
    private Long timeStamp;
    private String token;
    private String message;
}
