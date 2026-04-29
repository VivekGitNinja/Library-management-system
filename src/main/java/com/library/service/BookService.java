package com.library.service;

import com.library.exception.BookNotFoundException;
import com.library.exception.InvalidInputException;
import com.library.exception.LibraryException;
import com.library.model.Book;

import java.util.List;

public interface BookService {
    Book addBook(Book book) throws LibraryException;
    Book getBookById(Long id) throws BookNotFoundException, LibraryException;
    Book getBookByCode(String bookCode) throws BookNotFoundException, LibraryException;
    List<Book> getAllBooks() throws LibraryException;
    List<Book> searchBooksByTitle(String keyword) throws LibraryException;
    List<Book> searchBooksByAuthor(String keyword) throws LibraryException;
    List<Book> getAvailableBooks() throws LibraryException;
    boolean updateBook(Book book) throws LibraryException;
    boolean removeBook(Long id) throws BookNotFoundException, LibraryException;
}
