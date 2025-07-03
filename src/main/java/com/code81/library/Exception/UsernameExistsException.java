package com.code81.library.Exception;

public class UsernameExistsException extends RuntimeException {
    public UsernameExistsException(String username) {
        super("Username already exists: " + username);
    }
}
