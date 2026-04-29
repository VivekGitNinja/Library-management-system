import com.library.exception.BookNotFoundException;
import com.library.exception.ErrorHandling;
import com.library.exception.InvalidInputException;
import com.library.model.Book;
import com.library.service.BookService;
import com.library.service.impl.BookServiceImpl;

import java.util.List;

public class BookRepository {
    private final BookService bookService = new BookServiceImpl();

    public void addBook(Book book) throws ErrorHandling.InvalidInputException {
        try {
            bookService.addBook(book);
        } catch (InvalidInputException e) {
            throw new ErrorHandling.InvalidInputException(e.getMessage());
        } catch (Exception e) {
            throw new ErrorHandling.InvalidInputException("Database error: " + e.getMessage());
        }
    }

    public Book getBookById(String id) throws ErrorHandling.BookNotFoundException {
        try {
            return bookService.getBookByCode(id);
        } catch (BookNotFoundException e) {
            throw new ErrorHandling.BookNotFoundException(id);
        } catch (Exception e) {
            throw new ErrorHandling.BookNotFoundException(id);
        }
    }

    public List<Book> getAllBooks() {
        try {
            return bookService.getAllBooks();
        } catch (Exception e) {
            return List.of();
        }
    }

    public List<Book> searchByTitle(String keyword) {
        try {
            return bookService.searchBooksByTitle(keyword);
        } catch (Exception e) {
            return List.of();
        }
    }

    public List<Book> getAvailableBooks() {
        try {
            return bookService.getAvailableBooks();
        } catch (Exception e) {
            return List.of();
        }
    }
}
