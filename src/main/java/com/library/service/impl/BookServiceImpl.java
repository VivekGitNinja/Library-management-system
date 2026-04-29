package com.library.service.impl;

import com.library.dao.BookDao;
import com.library.dao.impl.BookDaoImpl;
import com.library.exception.BookNotFoundException;
import com.library.exception.InvalidInputException;
import com.library.exception.LibraryException;
import com.library.model.Book;
import com.library.service.BookService;

import java.sql.SQLException;
import java.util.List;

public class BookServiceImpl implements BookService {
    private final BookDao bookDao;

    public BookServiceImpl() {
        this.bookDao = new BookDaoImpl();
    }

    public BookServiceImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public Book addBook(Book book) throws LibraryException {
        if (book == null || book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new InvalidInputException("Book title cannot be empty.");
        }
        if (book.getBookCode() == null || book.getBookCode().trim().isEmpty()) {
            throw new InvalidInputException("Book code cannot be empty.");
        }
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new InvalidInputException("Book ISBN cannot be empty.");
        }
        try {
            if (bookDao.findByBookCode(book.getBookCode()).isPresent()) {
                throw new InvalidInputException("Book with code '" + book.getBookCode() + "' already exists.");
            }
            if (bookDao.findByIsbn(book.getIsbn()).isPresent()) {
                throw new InvalidInputException("Book with ISBN '" + book.getIsbn() + "' already exists.");
            }
            return bookDao.save(book);
        } catch (SQLException e) {
            throw new LibraryException("Database error adding book: " + e.getMessage(), e);
        }
    }

    @Override
    public Book getBookById(Long id) throws BookNotFoundException, LibraryException {
        try {
            return bookDao.findById(id).orElseThrow(() -> new BookNotFoundException(String.valueOf(id)));
        } catch (SQLException e) {
            throw new LibraryException("Database error retrieving book: " + e.getMessage(), e);
        }
    }

    @Override
    public Book getBookByCode(String bookCode) throws BookNotFoundException, LibraryException {
        try {
            return bookDao.findByBookCode(bookCode).orElseThrow(() -> new BookNotFoundException(bookCode));
        } catch (SQLException e) {
            throw new LibraryException("Database error retrieving book by code: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Book> getAllBooks() throws LibraryException {
        try {
            return bookDao.findAll();
        } catch (SQLException e) {
            throw new LibraryException("Database error listing all books: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Book> searchBooksByTitle(String keyword) throws LibraryException {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllBooks();
        }
        try {
            return bookDao.searchByTitleLike(keyword.trim());
        } catch (SQLException e) {
            throw new LibraryException("Database error searching books: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Book> searchBooksByAuthor(String keyword) throws LibraryException {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllBooks();
        }
        try {
            return bookDao.searchByAuthorLike(keyword.trim());
        } catch (SQLException e) {
            throw new LibraryException("Database error searching books by author: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Book> getAvailableBooks() throws LibraryException {
        try {
            return bookDao.findAvailableBooks();
        } catch (SQLException e) {
            throw new LibraryException("Database error fetching available books: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updateBook(Book book) throws LibraryException {
        if (book == null || book.getId() == null) {
            throw new InvalidInputException("Cannot update book without valid ID.");
        }
        try {
            return bookDao.update(book);
        } catch (SQLException e) {
            throw new LibraryException("Database error updating book: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean removeBook(Long id) throws BookNotFoundException, LibraryException {
        try {
            if (!bookDao.findById(id).isPresent()) {
                throw new BookNotFoundException(String.valueOf(id));
            }
            return bookDao.deleteById(id);
        } catch (SQLException e) {
            throw new LibraryException("Database error deleting book: " + e.getMessage(), e);
        }
    }
}
