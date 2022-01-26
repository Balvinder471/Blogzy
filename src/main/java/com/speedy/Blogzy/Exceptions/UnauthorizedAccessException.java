package com.speedy.Blogzy.Exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedAccessException extends  RuntimeException{
    public UnauthorizedAccessException(String s) {
      log.error("Unauthorized Access Detected : " +s);
    }
}
