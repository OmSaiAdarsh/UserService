package org.example.userservice.dtos;

import lombok.Data;

@Data
public class ErrorDto {
    public String message;
    public int status;
}
