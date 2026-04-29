package com.library.dao;

import com.library.model.Book;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BookDao {
    Book save(Book book) throws SQLException;
    Book save(Connection conn, Book book) throws SQLException;
    Optional<Book> findById(Long id) throws SQLException;
    Optional<Book> findByBookCode(String bookCode) throws SQLException;
    Optional<Book> findByIsbn(String isbn) throws SQLException;
    List<Book> findAll() throws SQLException;
    List<Book> searchByTitleLike(String titleKeyword) throws SQLException;
    List<Book> searchByAuthorLike(String authorKeyword) throws SQLException;
    List<Book> findAvailableBooks() throws SQLException;
    boolean update(Book book) throws SQLException;
    boolean update(Connection conn, Book book) throws SQLException;
    boolean deleteById(Long id) throws SQLException;
    long countTotalBooks() throws SQLException;
    long countAvailableBooks() throws SQLException;
}
