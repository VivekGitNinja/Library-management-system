package com.library.dao.impl;

import com.library.dao.BookDao;
import com.library.db.DatabaseConnectionPool;
import com.library.exception.DatabaseException;
import com.library.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDaoImpl implements BookDao {

    @Override
    public Book save(Book book) throws SQLException {
        try (Connection conn = DatabaseConnectionPool.getConnection()) {
            return save(conn, book);
        }
    }

    @Override
    public Book save(Connection conn, Book book) throws SQLException {
        String sql = "INSERT INTO books (book_code, title, author, isbn, genre, total_copies, available_copies, is_available) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, book.getBookCode());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setString(4, book.getIsbn());
            pstmt.setString(5, book.getGenre());
            pstmt.setInt(6, book.getTotalCopies());
            pstmt.setInt(7, book.getAvailableCopies());
            pstmt.setBoolean(8, book.getAvailableCopies() > 0);

            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    book.setId(rs.getLong(1));
                }
            }
            return book;
        }
    }

    @Override
    public Optional<Book> findById(Long id) throws SQLException {
        String sql = "SELECT id, book_code, title, author, isbn, genre, total_copies, available_copies, is_available, created_at, updated_at FROM books WHERE id = ?";
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToBook(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Book> findByBookCode(String bookCode) throws SQLException {
        String sql = "SELECT id, book_code, title, author, isbn, genre, total_copies, available_copies, is_available, created_at, updated_at FROM books WHERE book_code = ?";
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bookCode);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToBook(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) throws SQLException {
        String sql = "SELECT id, book_code, title, author, isbn, genre, total_copies, available_copies, is_available, created_at, updated_at FROM books WHERE isbn = ?";
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, isbn);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToBook(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Book> findAll() throws SQLException {
        String sql = "SELECT id, book_code, title, author, isbn, genre, total_copies, available_copies, is_available, created_at, updated_at FROM books ORDER BY id ASC";
        List<Book> books = new ArrayList<>();
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        }
        return books;
    }

    @Override
    public List<Book> searchByTitleLike(String titleKeyword) throws SQLException {
        String sql = "SELECT id, book_code, title, author, isbn, genre, total_copies, available_copies, is_available, created_at, updated_at FROM books WHERE LOWER(title) LIKE ? ORDER BY title ASC";
        List<Book> books = new ArrayList<>();
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + titleKeyword.toLowerCase() + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
        }
        return books;
    }

    @Override
    public List<Book> searchByAuthorLike(String authorKeyword) throws SQLException {
        String sql = "SELECT id, book_code, title, author, isbn, genre, total_copies, available_copies, is_available, created_at, updated_at FROM books WHERE LOWER(author) LIKE ? ORDER BY author ASC";
        List<Book> books = new ArrayList<>();
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + authorKeyword.toLowerCase() + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
        }
        return books;
    }

    @Override
    public List<Book> findAvailableBooks() throws SQLException {
        String sql = "SELECT id, book_code, title, author, isbn, genre, total_copies, available_copies, is_available, created_at, updated_at FROM books WHERE available_copies > 0 ORDER BY title ASC";
        List<Book> books = new ArrayList<>();
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        }
        return books;
    }

    @Override
    public boolean update(Book book) throws SQLException {
        try (Connection conn = DatabaseConnectionPool.getConnection()) {
            return update(conn, book);
        }
    }

    @Override
    public boolean update(Connection conn, Book book) throws SQLException {
        String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, genre = ?, total_copies = ?, available_copies = ?, is_available = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getIsbn());
            pstmt.setString(4, book.getGenre());
            pstmt.setInt(5, book.getTotalCopies());
            pstmt.setInt(6, book.getAvailableCopies());
            pstmt.setBoolean(7, book.getAvailableCopies() > 0);
            pstmt.setLong(8, book.getId());

            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public long countTotalBooks() throws SQLException {
        String sql = "SELECT COUNT(*) FROM books";
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return 0;
    }

    @Override
    public long countAvailableBooks() throws SQLException {
        String sql = "SELECT COUNT(*) FROM books WHERE available_copies > 0";
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return 0;
    }

    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book book = new Book(
                rs.getLong("id"),
                rs.getString("book_code"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("isbn"),
                rs.getString("genre"),
                rs.getInt("total_copies"),
                rs.getInt("available_copies")
        );
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) book.setCreatedAt(createdAt.toLocalDateTime());
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) book.setUpdatedAt(updatedAt.toLocalDateTime());
        return book;
    }
}
