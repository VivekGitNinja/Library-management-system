package com.library.exception;

public class InvalidInputException extends LibraryException {
    public InvalidInputException(String message) {
        super("Invalid Input: " + message);
    }
}
