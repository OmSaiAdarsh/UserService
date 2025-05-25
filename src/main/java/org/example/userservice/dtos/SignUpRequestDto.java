package org.example.userservice.dtos;

import lombok.Data;

@Data
public class SignUpRequestDto {
    public String name;
    public String email;
    public String password;

}
