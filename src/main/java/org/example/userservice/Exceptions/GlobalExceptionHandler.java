package org.example.userservice.Exceptions;

import org.example.userservice.dtos.ErrorDto;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.View;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final View error;

    public GlobalExceptionHandler(View error) {
        this.error = error;
    }

    @ExceptionHandler(InvalidUserException.class)
    public ErrorDto exception(InvalidUserException e) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage("Invalid User");
        errorDto.setStatus(404);
        return errorDto;
    }
}
