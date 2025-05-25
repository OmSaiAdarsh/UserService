package org.example.userservice.dtos;

import lombok.Data;

@Data
public class LogOutDto {
    public String email;
    public String tokenValue;
}
