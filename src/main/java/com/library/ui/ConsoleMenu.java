package com.library.ui;

import com.library.exception.LibraryException;
import com.library.model.Book;
import com.library.model.IssuedBook;
import com.library.model.Role;
import com.library.model.User;
import com.library.service.BookService;
import com.library.service.LibraryService;
import com.library.service.UserService;
import com.library.service.impl.BookServiceImpl;
import com.library.service.impl.LibraryServiceImpl;
import com.library.service.impl.UserServiceImpl;

import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {
    private final BookService bookService;
    private final UserService userService;
    private final LibraryService libraryService;
    private User currentUser;

    public ConsoleMenu() {
        this.bookService = new BookServiceImpl();
        this.userService = new UserServiceImpl();
        this.libraryService = new LibraryServiceImpl();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=================================================");
        System.out.println("  WELCOME TO MYSQL LIBRARY MANAGEMENT SYSTEM     ");
        System.out.println("=================================================");

        while (running) {
            if (currentUser == null) {
                showLoginMenu(scanner);
            } else {
                showMainMenu(scanner);
            }
        }
        scanner.close();
    }

    private void showLoginMenu(Scanner scanner) {
        System.out.println("\n--- AUTHENTICATION MENU ---");
        System.out.println("1. Login to Account");
        System.out.println("2. Register New Student Account");
        System.out.println("3. Quick Guest Access (View Catalog)");
        System.out.println("4. Exit Application");
        System.out.print("Select Option (1-4): ");

        String choice = scanner.nextLine().trim();
        try {
            switch (choice) {
                case "1":
                    System.out.print("Enter Email: ");
                    String email = scanner.nextLine().trim();
                    System.out.print("Enter Password: ");
                    String pass = scanner.nextLine().trim();
                    currentUser = userService.authenticate(email, pass);
                    System.out.println("\nSUCCESS: Logged in as " + currentUser.getName() + " [" + currentUser.getRole() + "]");
                    break;
                case "2":
                    registerUser(scanner, Role.STUDENT);
                    break;
                case "3":
                    displayAllBooks();
                    break;
                case "4":
                    System.out.println("Thank you for using Library Management System. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please enter 1-4.");
            }
        } catch (LibraryException e) {
            System.out.println("AUTHENTICATION FAILURE: " + e.getMessage());
        }
    }

    private void showMainMenu(Scanner scanner) {
        System.out.println("\n=========================================");
        System.out.println("  MAIN MENU | User: " + currentUser.getName() + " (" + currentUser.getRole() + ")");
        System.out.println("=========================================");
        System.out.println("1. View All Books");
        System.out.println("2. Search Books by Title");
        System.out.println("3. Search Books by Author");
        System.out.println("4. Issue Book");
        System.out.println("5. Return Book");
        System.out.println("6. View My Borrowing History");

        if (currentUser.getRole() == Role.ADMIN) {
            System.out.println("7. Add New Book (Admin)");
            System.out.println("8. View All Registered Users (Admin)");
            System.out.println("9. Register Admin/Librarian User (Admin)");
            System.out.println("10. View All System Issued Records (Admin)");
        }

        System.out.println("0. Logout");
        System.out.print("Enter Choice: ");

        String choice = scanner.nextLine().trim();
        try {
            switch (choice) {
                case "1":
                    displayAllBooks();
                    break;
                case "2":
                    System.out.print("Enter Title Keyword: ");
                    String title = scanner.nextLine().trim();
                    List<Book> titleResults = bookService.searchBooksByTitle(title);
                    printBookList(titleResults);
                    break;
                case "3":
                    System.out.print("Enter Author Keyword: ");
                    String author = scanner.nextLine().trim();
                    List<Book> authorResults = bookService.searchBooksByAuthor(author);
                    printBookList(authorResults);
                    break;
                case "4":
                    System.out.print("Enter Book Code (e.g. B001): ");
                    String bCode = scanner.nextLine().trim();
                    String uCode = currentUser.getRole() == Role.ADMIN ?
                            promptUserCode(scanner) : currentUser.getUserCode();
                    IssuedBook issue = libraryService.issueBook(bCode, uCode);
                    System.out.println("\nSUCCESS: " + issue);
                    break;
                case "5":
                    System.out.print("Enter Book Code to Return: ");
                    String retBCode = scanner.nextLine().trim();
                    String retUCode = currentUser.getRole() == Role.ADMIN ?
                            promptUserCode(scanner) : currentUser.getUserCode();
                    IssuedBook retRecord = libraryService.returnBook(retBCode, retUCode);
                    System.out.println("\nSUCCESS: " + retRecord);
                    if (retRecord.getFineAmount() != null && retRecord.getFineAmount().doubleValue() > 0) {
                        System.out.println("NOTE: Overdue Fine Paid: $" + retRecord.getFineAmount());
                    }
                    break;
                case "6":
                    List<IssuedBook> myHistory = libraryService.getIssuedBooksByUser(currentUser.getUserCode());
                    printIssueRecords(myHistory);
                    break;
                case "7":
                    if (currentUser.getRole() == Role.ADMIN) addNewBook(scanner);
                    else System.out.println("Access Denied.");
                    break;
                case "8":
                    if (currentUser.getRole() == Role.ADMIN) {
                        List<User> users = userService.getAllUsers();
                        System.out.println("\n--- ALL REGISTERED USERS ---");
                        users.forEach(System.out::println);
                    } else System.out.println("Access Denied.");
                    break;
                case "9":
                    if (currentUser.getRole() == Role.ADMIN) registerUser(scanner, Role.ADMIN);
                    else System.out.println("Access Denied.");
                    break;
                case "10":
                    if (currentUser.getRole() == Role.ADMIN) {
                        List<IssuedBook> allIssued = libraryService.getAllIssuedBooks();
                        printIssueRecords(allIssued);
                    } else System.out.println("Access Denied.");
                    break;
                case "0":
                    currentUser = null;
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } catch (LibraryException e) {
            System.out.println("OPERATION ERROR: " + e.getMessage());
        }
    }

    private String promptUserCode(Scanner scanner) {
        System.out.print("Enter Student/User Code (e.g. S101): ");
        return scanner.nextLine().trim();
    }

    private void displayAllBooks() throws LibraryException {
        List<Book> books = bookService.getAllBooks();
        System.out.println("\n=================== ALL BOOKS IN CATALOG ===================");
        printBookList(books);
    }

    private void printBookList(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("No matching books found.");
        } else {
            books.forEach(System.out::println);
        }
    }

    private void printIssueRecords(List<IssuedBook> records) {
        if (records.isEmpty()) {
            System.out.println("No borrowing records found.");
        } else {
            System.out.println("\n--- BORROWING RECORDS ---");
            records.forEach(System.out::println);
        }
    }

    private void addNewBook(Scanner scanner) throws LibraryException {
        System.out.print("Enter Unique Book Code (e.g. B006): ");
        String code = scanner.nextLine().trim();
        System.out.print("Enter Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Enter Author: ");
        String author = scanner.nextLine().trim();
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine().trim();
        System.out.print("Enter Genre: ");
        String genre = scanner.nextLine().trim();
        System.out.print("Enter Total Copies: ");
        int copies = Integer.parseInt(scanner.nextLine().trim());

        Book newBook = new Book(null, code, title, author, isbn, genre, copies, copies);
        bookService.addBook(newBook);
        System.out.println("SUCCESS: Book '" + title + "' added to database!");
    }

    private void registerUser(Scanner scanner, Role defaultRole) throws LibraryException {
        System.out.print("Enter User Code (e.g. S104): ");
        String code = scanner.nextLine().trim();
        System.out.print("Enter Full Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter Phone: ");
        String phone = scanner.nextLine().trim();
        System.out.print("Enter Password: ");
        String pass = scanner.nextLine().trim();

        User newUser = new User(code, name, email, phone, null, defaultRole);
        userService.registerUser(newUser, pass);
        System.out.println("SUCCESS: Registered user '" + name + "'!");
    }
}
