import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Library {
    private final BookRepository bookRepo;
    private final StudentRepository studentRepo;

    public Library() {
        this.bookRepo = new BookRepository();
        this.studentRepo = new StudentRepository();
        initializeSampleData();
    }

    private void initializeSampleData() {
        try {
            bookRepo.addBook(new Book("B001", "Clean Code", "Robert C. Martin", "9780132350884", "Software Engineering"));
            bookRepo.addBook(new Book("B002", "The Pragmatic Programmer", "Andrew Hunt", "9780201616224", "Software Engineering"));
            bookRepo.addBook(new Book("B003", "Design Patterns", "Erich Gamma", "9780201633610", "Computer Science"));
            bookRepo.addBook(new Book("B004", "Introduction to Algorithms", "Thomas H. Cormen", "9780262033848", "Computer Science"));
            bookRepo.addBook(new Book("B005", "Artificial Intelligence", "Stuart Russell", "9780134610993", "Artificial Intelligence"));

            studentRepo.addStudent(new Student("S101", "Vivek Verma", "vivek@example.com", "9876543210"));
            studentRepo.addStudent(new Student("S102", "Rahul Sharma", "rahul@example.com", "9876543211"));
            studentRepo.addStudent(new Student("S103", "Ananya Roy", "ananya@example.com", "9876543212"));
        } catch (ErrorHandling.InvalidInputException e) {
            System.err.println("Error initializing sample data: " + e.getMessage());
        }
    }

    public void issueBook(String bookId, String studentId) throws ErrorHandling.LibraryException {
        Book book = bookRepo.getBookById(bookId);
        Student student = studentRepo.getStudentById(studentId);

        if (!book.isAvailable()) {
            throw new ErrorHandling.BookAlreadyBorrowedException(book.getTitle());
        }

        book.setAvailable(false);
        book.setBorrowedByStudentId(studentId);
        book.setDueDate(LocalDate.now().plusDays(14));
        student.addBorrowedBook(bookId);

        System.out.println("Success: Book '" + book.getTitle() + "' has been issued to " + student.getName() + ". Due date: " + book.getDueDate());
    }

    public void returnBook(String bookId) throws ErrorHandling.LibraryException {
        Book book = bookRepo.getBookById(bookId);
        if (book.isAvailable() || book.getBorrowedByStudentId() == null) {
            throw new ErrorHandling.InvalidInputException("Book '" + book.getTitle() + "' is not currently borrowed.");
        }

        Student student = studentRepo.getStudentById(book.getBorrowedByStudentId());
        student.removeBorrowedBook(bookId);

        book.setAvailable(true);
        book.setBorrowedByStudentId(null);
        book.setDueDate(null);

        System.out.println("Success: Book '" + book.getTitle() + "' returned successfully by " + student.getName());
    }

    public void displayAllBooks() {
        System.out.println("\n========== ALL BOOKS ==========");
        List<Book> books = bookRepo.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books in library catalog.");
        } else {
            books.forEach(System.out::println);
        }
    }

    public void displayAllStudents() {
        System.out.println("\n========== REGISTERED STUDENTS ==========");
        List<Student> students = studentRepo.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students registered.");
        } else {
            students.forEach(System.out::println);
        }
    }

    public static void main(String[] args) {
        Library library = new Library();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=========================================");
        System.out.println("  WELCOME TO LIBRARY MANAGEMENT SYSTEM   ");
        System.out.println("=========================================");

        while (running) {
            System.out.println("\nSelect an option:");
            System.out.println("1. View All Books");
            System.out.println("2. View All Students");
            System.out.println("3. Issue Book");
            System.out.println("4. Return Book");
            System.out.println("5. Add New Book");
            System.out.println("6. Register New Student");
            System.out.println("7. Search Book by Title");
            System.out.println("8. Exit");
            System.out.print("Enter choice (1-8): ");

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        library.displayAllBooks();
                        break;
                    case "2":
                        library.displayAllStudents();
                        break;
                    case "3":
                        System.out.print("Enter Book ID: ");
                        String bId = scanner.nextLine().trim();
                        System.out.print("Enter Student ID: ");
                        String sId = scanner.nextLine().trim();
                        library.issueBook(bId, sId);
                        break;
                    case "4":
                        System.out.print("Enter Book ID to return: ");
                        String returnBId = scanner.nextLine().trim();
                        library.returnBook(returnBId);
                        break;
                    case "5":
                        System.out.print("Enter Book ID: ");
                        String id = scanner.nextLine().trim();
                        System.out.print("Enter Title: ");
                        String title = scanner.nextLine().trim();
                        System.out.print("Enter Author: ");
                        String author = scanner.nextLine().trim();
                        System.out.print("Enter ISBN: ");
                        String isbn = scanner.nextLine().trim();
                        System.out.print("Enter Genre: ");
                        String genre = scanner.nextLine().trim();
                        library.bookRepo.addBook(new Book(id, title, author, isbn, genre));
                        System.out.println("Success: Book added successfully.");
                        break;
                    case "6":
                        System.out.print("Enter Student ID: ");
                        String studId = scanner.nextLine().trim();
                        System.out.print("Enter Name: ");
                        String name = scanner.nextLine().trim();
                        System.out.print("Enter Email: ");
                        String email = scanner.nextLine().trim();
                        System.out.print("Enter Phone: ");
                        String phone = scanner.nextLine().trim();
                        library.studentRepo.addStudent(new Student(studId, name, email, phone));
                        System.out.println("Success: Student registered successfully.");
                        break;
                    case "7":
                        System.out.print("Enter title search keyword: ");
                        String keyword = scanner.nextLine().trim();
                        List<Book> found = library.bookRepo.searchByTitle(keyword);
                        System.out.println("\nSearch Results (" + found.size() + " books found):");
                        found.forEach(System.out::println);
                        break;
                    case "8":
                        running = false;
                        System.out.println("Exiting Library Management System. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid option. Please enter a number between 1 and 8.");
                }
            } catch (ErrorHandling.LibraryException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
        scanner.close();
    }
}
