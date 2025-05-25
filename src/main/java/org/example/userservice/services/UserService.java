package org.example.userservice.services;

import org.example.userservice.models.Token;
import org.example.userservice.models.User;

public interface UserService {
    public Token login(String username, String password);
    public User register(String name, String email, String password);
    public User validateToken(String token);
    public boolean logout(String email, String tokenValue);
    public boolean validateTokenUpdated(String token, String email);

}
