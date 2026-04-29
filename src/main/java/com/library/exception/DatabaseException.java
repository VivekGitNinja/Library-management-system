package com.library.exception;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String message, Throwable cause) {
        super("Database Operation Failure: " + message, cause);
    }

    public DatabaseException(String message) {
        super("Database Operation Failure: " + message);
    }
}
