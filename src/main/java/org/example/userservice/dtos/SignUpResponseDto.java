package org.example.userservice.dtos;

import lombok.Data;
import org.example.userservice.models.User;

@Data
public class SignUpResponseDto {
    private String email;
    private String password;
    private String name;

    public static SignUpResponseDto from(User user) {
        if (user == null)
            return null;
        SignUpResponseDto dto = new SignUpResponseDto();
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setName(user.getName());
        return dto;
    }
}
