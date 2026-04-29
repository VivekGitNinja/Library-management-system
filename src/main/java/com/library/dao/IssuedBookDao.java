package com.library.dao;

import com.library.model.IssuedBook;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IssuedBookDao {
    IssuedBook save(IssuedBook issuedBook) throws SQLException;
    IssuedBook save(Connection conn, IssuedBook issuedBook) throws SQLException;
    Optional<IssuedBook> findById(Long id) throws SQLException;
    Optional<IssuedBook> findActiveIssueByBookAndUser(Long bookId, Long userId) throws SQLException;
    Optional<IssuedBook> findActiveIssueByBookId(Long bookId) throws SQLException;
    List<IssuedBook> findAllWithDetails() throws SQLException;
    List<IssuedBook> findByUserId(Long userId) throws SQLException;
    List<IssuedBook> findByStatus(String status) throws SQLException;
    boolean returnBook(Connection conn, Long issueId, LocalDate returnDate, BigDecimal fineAmount) throws SQLException;
    long countActiveIssuedBooks() throws SQLException;
}
