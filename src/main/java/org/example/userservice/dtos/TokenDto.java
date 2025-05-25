package org.example.userservice.dtos;

import lombok.Data;

@Data
public class TokenDto {
    public String tokenValue;
    public String email;
}
