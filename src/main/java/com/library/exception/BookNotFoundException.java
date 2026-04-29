package com.library.exception;

public class BookNotFoundException extends LibraryException {
    public BookNotFoundException(String identifier) {
        super("Error: Book with identifier '" + identifier + "' was not found.");
    }
}
