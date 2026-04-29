import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BookRepository {
    private final Map<String, Book> books = new HashMap<>();

    public void addBook(Book book) throws ErrorHandling.InvalidInputException {
        if (book == null || book.getId() == null || book.getId().trim().isEmpty()) {
            throw new ErrorHandling.InvalidInputException("Book ID cannot be empty.");
        }
        if (books.containsKey(book.getId())) {
            throw new ErrorHandling.InvalidInputException("Book with ID '" + book.getId() + "' already exists.");
        }
        books.put(book.getId(), book);
    }

    public Book getBookById(String id) throws ErrorHandling.BookNotFoundException {
        Book book = books.get(id);
        if (book == null) {
            throw new ErrorHandling.BookNotFoundException(id);
        }
        return book;
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    public List<Book> searchByTitle(String keyword) {
        String lower = keyword.toLowerCase();
        return books.values().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    public List<Book> searchByAuthor(String author) {
        String lower = author.toLowerCase();
        return books.values().stream()
                .filter(b -> b.getAuthor().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    public List<Book> getAvailableBooks() {
        return books.values().stream()
                .filter(Book::isAvailable)
                .collect(Collectors.toList());
    }

    public boolean removeBook(String id) throws ErrorHandling.BookNotFoundException {
        if (!books.containsKey(id)) {
            throw new ErrorHandling.BookNotFoundException(id);
        }
        books.remove(id);
        return true;
    }
}
