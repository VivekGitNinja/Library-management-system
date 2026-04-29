package com.library.exception;

public class ErrorHandling {
    public static class LibraryException extends com.library.exception.LibraryException {
        public LibraryException(String message) {
            super(message);
        }
    }

    public static class BookNotFoundException extends com.library.exception.BookNotFoundException {
        public BookNotFoundException(String bookId) {
            super(bookId);
        }
    }

    public static class StudentNotFoundException extends com.library.exception.UserNotFoundException {
        public StudentNotFoundException(String studentId) {
            super(studentId);
        }
    }

    public static class BookAlreadyBorrowedException extends com.library.exception.BookAlreadyBorrowedException {
        public BookAlreadyBorrowedException(String title) {
            super(title);
        }
    }

    public static class InvalidInputException extends com.library.exception.InvalidInputException {
        public InvalidInputException(String message) {
            super(message);
        }
    }
}
