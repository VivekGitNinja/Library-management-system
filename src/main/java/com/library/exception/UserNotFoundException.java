package com.library.exception;

public class UserNotFoundException extends LibraryException {
    public UserNotFoundException(String identifier) {
        super("Error: User with identifier '" + identifier + "' was not found.");
    }
}
