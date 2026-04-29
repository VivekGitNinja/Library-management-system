package com.library.dao.impl;

import com.library.dao.IssuedBookDao;
import com.library.db.DatabaseConnectionPool;
import com.library.model.IssuedBook;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IssuedBookDaoImpl implements IssuedBookDao {

    @Override
    public IssuedBook save(IssuedBook issuedBook) throws SQLException {
        try (Connection conn = DatabaseConnectionPool.getConnection()) {
            return save(conn, issuedBook);
        }
    }

    @Override
    public IssuedBook save(Connection conn, IssuedBook issuedBook) throws SQLException {
        String sql = "INSERT INTO issued_books (book_id, user_id, issue_date, due_date, fine_amount, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, issuedBook.getBookId());
            pstmt.setLong(2, issuedBook.getUserId());
            pstmt.setDate(3, Date.valueOf(issuedBook.getIssueDate()));
            pstmt.setDate(4, Date.valueOf(issuedBook.getDueDate()));
            pstmt.setBigDecimal(5, issuedBook.getFineAmount() != null ? issuedBook.getFineAmount() : BigDecimal.ZERO);
            pstmt.setString(6, issuedBook.getStatus());

            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    issuedBook.setId(rs.getLong(1));
                }
            }
            return issuedBook;
        }
    }

    @Override
    public Optional<IssuedBook> findById(Long id) throws SQLException {
        String sql = "SELECT ib.id, ib.book_id, ib.user_id, ib.issue_date, ib.due_date, ib.return_date, ib.fine_amount, ib.status, ib.created_at, " +
                     "b.title AS book_title, u.name AS user_name " +
                     "FROM issued_books ib " +
                     "JOIN books b ON ib.book_id = b.id " +
                     "JOIN users u ON ib.user_id = u.id " +
                     "WHERE ib.id = ?";
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToIssuedBook(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<IssuedBook> findActiveIssueByBookAndUser(Long bookId, Long userId) throws SQLException {
        String sql = "SELECT ib.id, ib.book_id, ib.user_id, ib.issue_date, ib.due_date, ib.return_date, ib.fine_amount, ib.status, ib.created_at, " +
                     "b.title AS book_title, u.name AS user_name " +
                     "FROM issued_books ib " +
                     "JOIN books b ON ib.book_id = b.id " +
                     "JOIN users u ON ib.user_id = u.id " +
                     "WHERE ib.book_id = ? AND ib.user_id = ? AND ib.status = 'ISSUED'";
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, bookId);
            pstmt.setLong(2, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToIssuedBook(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<IssuedBook> findActiveIssueByBookId(Long bookId) throws SQLException {
        String sql = "SELECT ib.id, ib.book_id, ib.user_id, ib.issue_date, ib.due_date, ib.return_date, ib.fine_amount, ib.status, ib.created_at, " +
                     "b.title AS book_title, u.name AS user_name " +
                     "FROM issued_books ib " +
                     "JOIN books b ON ib.book_id = b.id " +
                     "JOIN users u ON ib.user_id = u.id " +
                     "WHERE ib.book_id = ? AND ib.status = 'ISSUED'";
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, bookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToIssuedBook(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<IssuedBook> findAllWithDetails() throws SQLException {
        String sql = "SELECT ib.id, ib.book_id, ib.user_id, ib.issue_date, ib.due_date, ib.return_date, ib.fine_amount, ib.status, ib.created_at, " +
                     "b.title AS book_title, u.name AS user_name " +
                     "FROM issued_books ib " +
                     "JOIN books b ON ib.book_id = b.id " +
                     "JOIN users u ON ib.user_id = u.id " +
                     "ORDER BY ib.id DESC";
        List<IssuedBook> list = new ArrayList<>();
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToIssuedBook(rs));
            }
        }
        return list;
    }

    @Override
    public List<IssuedBook> findByUserId(Long userId) throws SQLException {
        String sql = "SELECT ib.id, ib.book_id, ib.user_id, ib.issue_date, ib.due_date, ib.return_date, ib.fine_amount, ib.status, ib.created_at, " +
                     "b.title AS book_title, u.name AS user_name " +
                     "FROM issued_books ib " +
                     "JOIN books b ON ib.book_id = b.id " +
                     "JOIN users u ON ib.user_id = u.id " +
                     "WHERE ib.user_id = ? " +
                     "ORDER BY ib.id DESC";
        List<IssuedBook> list = new ArrayList<>();
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToIssuedBook(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<IssuedBook> findByStatus(String status) throws SQLException {
        String sql = "SELECT ib.id, ib.book_id, ib.user_id, ib.issue_date, ib.due_date, ib.return_date, ib.fine_amount, ib.status, ib.created_at, " +
                     "b.title AS book_title, u.name AS user_name " +
                     "FROM issued_books ib " +
                     "JOIN books b ON ib.book_id = b.id " +
                     "JOIN users u ON ib.user_id = u.id " +
                     "WHERE ib.status = ? " +
                     "ORDER BY ib.id DESC";
        List<IssuedBook> list = new ArrayList<>();
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToIssuedBook(rs));
                }
            }
        }
        return list;
    }

    @Override
    public boolean returnBook(Connection conn, Long issueId, LocalDate returnDate, BigDecimal fineAmount) throws SQLException {
        String sql = "UPDATE issued_books SET return_date = ?, fine_amount = ?, status = 'RETURNED' WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(returnDate));
            pstmt.setBigDecimal(2, fineAmount != null ? fineAmount : BigDecimal.ZERO);
            pstmt.setLong(3, issueId);
            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public long countActiveIssuedBooks() throws SQLException {
        String sql = "SELECT COUNT(*) FROM issued_books WHERE status = 'ISSUED'";
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return 0;
    }

    private IssuedBook mapResultSetToIssuedBook(ResultSet rs) throws SQLException {
        IssuedBook ib = new IssuedBook();
        ib.setId(rs.getLong("id"));
        ib.setBookId(rs.getLong("book_id"));
        ib.setUserId(rs.getLong("user_id"));
        ib.setBookTitle(rs.getString("book_title"));
        ib.setUserName(rs.getString("user_name"));

        Date issueDate = rs.getDate("issue_date");
        if (issueDate != null) ib.setIssueDate(issueDate.toLocalDate());
        Date dueDate = rs.getDate("due_date");
        if (dueDate != null) ib.setDueDate(dueDate.toLocalDate());
        Date returnDate = rs.getDate("return_date");
        if (returnDate != null) ib.setReturnDate(returnDate.toLocalDate());

        ib.setFineAmount(rs.getBigDecimal("fine_amount"));
        ib.setStatus(rs.getString("status"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) ib.setCreatedAt(createdAt.toLocalDateTime());
        return ib;
    }
}
