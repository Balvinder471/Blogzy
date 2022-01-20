package com.speedy.Blogzy.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionDto {
    private int status;
    private String message;
    private Long timeStamp;
}
