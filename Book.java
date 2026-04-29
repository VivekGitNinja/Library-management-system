import java.io.Serializable;
import java.time.LocalDate;

public class Book implements Serializable {
    private String id;
    private String title;
    private String author;
    private String isbn;
    private String genre;
    private boolean isAvailable;
    private String borrowedByStudentId;
    private LocalDate dueDate;

    public Book(String id, String title, String author, String isbn, String genre) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.genre = genre;
        this.isAvailable = true;
        this.borrowedByStudentId = null;
        this.dueDate = null;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getBorrowedByStudentId() {
        return borrowedByStudentId;
    }

    public void setBorrowedByStudentId(String borrowedByStudentId) {
        this.borrowedByStudentId = borrowedByStudentId;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return String.format("Book[ID='%s', Title='%s', Author='%s', ISBN='%s', Genre='%s', Available=%b, BorrowedBy='%s', DueDate=%s]",
                id, title, author, isbn, genre, isAvailable, 
                borrowedByStudentId != null ? borrowedByStudentId : "None",
                dueDate != null ? dueDate.toString() : "N/A");
    }
}
