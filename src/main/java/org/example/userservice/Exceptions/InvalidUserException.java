package org.example.userservice.Exceptions;

public class InvalidUserException extends Exception {
    public InvalidUserException() {
        super("Invalid User");
    }
}
