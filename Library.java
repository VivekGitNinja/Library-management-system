import com.library.db.DatabaseInitializer;
import com.library.exception.LibraryException;
import com.library.model.Book;
import com.library.model.IssuedBook;
import com.library.model.User;
import com.library.service.BookService;
import com.library.service.LibraryService;
import com.library.service.UserService;
import com.library.service.impl.BookServiceImpl;
import com.library.service.impl.LibraryServiceImpl;
import com.library.service.impl.UserServiceImpl;

import java.util.List;
import java.util.Scanner;

public class Library {
    private final BookService bookService;
    private final UserService userService;
    private final LibraryService libraryService;

    public Library() {
        DatabaseInitializer.initializeDatabase();
        this.bookService = new BookServiceImpl();
        this.userService = new UserServiceImpl();
        this.libraryService = new LibraryServiceImpl();
    }

    public void issueBook(String bookCode, String userCode) throws LibraryException {
        IssuedBook issue = libraryService.issueBook(bookCode, userCode);
        System.out.println("Success: Book '" + issue.getBookTitle() + "' issued to " + issue.getUserName() + ". Due date: " + issue.getDueDate());
    }

    public void returnBook(String bookCode, String userCode) throws LibraryException {
        IssuedBook ret = libraryService.returnBook(bookCode, userCode);
        System.out.println("Success: Book '" + ret.getBookTitle() + "' returned successfully by " + ret.getUserName());
    }

    public void displayAllBooks() {
        System.out.println("\n========== ALL BOOKS ==========");
        try {
            List<Book> books = bookService.getAllBooks();
            if (books.isEmpty()) {
                System.out.println("No books in library catalog.");
            } else {
                books.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.err.println("Error displaying books: " + e.getMessage());
        }
    }

    public void displayAllStudents() {
        System.out.println("\n========== REGISTERED STUDENTS ==========");
        try {
            List<User> students = userService.getAllUsers();
            if (students.isEmpty()) {
                System.out.println("No students registered.");
            } else {
                students.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.err.println("Error displaying students: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        com.library.Main.main(args);
    }
}
