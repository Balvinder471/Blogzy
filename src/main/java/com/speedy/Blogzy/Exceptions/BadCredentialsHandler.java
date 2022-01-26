package com.speedy.Blogzy.Exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@ControllerAdvice
public class BadCredentialsHandler {
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleException(HttpServletRequest request) {
        log.warn("Unauthorized access detected!!!");
        return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials or the token may have been compromised!!, Please send a valid Authorization header with your request");
    }
}

