public class ErrorHandling {
    public static class LibraryException extends Exception {
        public LibraryException(String message) {
            super(message);
        }
    }

    public static class BookNotFoundException extends LibraryException {
        public BookNotFoundException(String bookId) {
            super("Error: Book with ID '" + bookId + "' was not found.");
        }
    }

    public static class StudentNotFoundException extends LibraryException {
        public StudentNotFoundException(String studentId) {
            super("Error: Student with ID '" + studentId + "' was not found.");
        }
    }

    public static class BookAlreadyBorrowedException extends LibraryException {
        public BookAlreadyBorrowedException(String title) {
            super("Error: Book '" + title + "' is currently already borrowed.");
        }
    }

    public static class InvalidInputException extends LibraryException {
        public InvalidInputException(String message) {
            super("Invalid Input: " + message);
        }
    }
}
