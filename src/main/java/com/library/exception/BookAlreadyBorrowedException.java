package com.library.exception;

public class BookAlreadyBorrowedException extends LibraryException {
    public BookAlreadyBorrowedException(String bookTitle) {
        super("Error: Book '" + bookTitle + "' has no available copies for issue.");
    }
}
