package com.library.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    public static void initializeDatabase() {
        try (Connection conn = DatabaseConnectionPool.getConnection();
             Statement stmt = conn.createStatement()) {

            // Create users table
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_code VARCHAR(20) NOT NULL UNIQUE, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "email VARCHAR(100) NOT NULL UNIQUE, " +
                    "phone VARCHAR(20) NOT NULL, " +
                    "password_hash VARCHAR(255) NOT NULL, " +
                    "role VARCHAR(20) NOT NULL DEFAULT 'STUDENT', " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")");

            // Create books table
            stmt.execute("CREATE TABLE IF NOT EXISTS books (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "book_code VARCHAR(20) NOT NULL UNIQUE, " +
                    "title VARCHAR(200) NOT NULL, " +
                    "author VARCHAR(150) NOT NULL, " +
                    "isbn VARCHAR(20) NOT NULL UNIQUE, " +
                    "genre VARCHAR(50) NOT NULL, " +
                    "total_copies INT NOT NULL DEFAULT 1, " +
                    "available_copies INT NOT NULL DEFAULT 1, " +
                    "is_available BOOLEAN DEFAULT TRUE, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")");

            // Create issued_books table
            stmt.execute("CREATE TABLE IF NOT EXISTS issued_books (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "book_id BIGINT NOT NULL, " +
                    "user_id BIGINT NOT NULL, " +
                    "issue_date DATE NOT NULL, " +
                    "due_date DATE NOT NULL, " +
                    "return_date DATE NULL, " +
                    "fine_amount DECIMAL(10, 2) NOT NULL DEFAULT 0.00, " +
                    "status VARCHAR(20) NOT NULL DEFAULT 'ISSUED', " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (book_id) REFERENCES books(id), " +
                    "FOREIGN KEY (user_id) REFERENCES users(id)" +
                    ")");

            // Seed initial data if tables are empty
            seedInitialData(conn, stmt);

            logger.info("Database initialized successfully.");
        } catch (Exception e) {
            logger.error("Error initializing database schema: {}", e.getMessage(), e);
        }
    }

    private static void seedInitialData(Connection conn, Statement stmt) throws Exception {
        // Check if users empty
        var rsUsers = stmt.executeQuery("SELECT COUNT(*) FROM users");
        if (rsUsers.next() && rsUsers.getInt(1) == 0) {
            stmt.execute("INSERT INTO users (user_code, name, email, phone, password_hash, role) VALUES " +
                    "('A001', 'System Admin', 'admin@library.com', '9998887770', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'ADMIN'), " +
                    "('S101', 'Vivek Verma', 'vivek@example.com', '9876543210', 'cd93560731f416d860d5b00c3c54433d7373f738f71295b955743dd132049d5c', 'STUDENT'), " +
                    "('S102', 'Rahul Sharma', 'rahul@example.com', '9876543211', 'cd93560731f416d860d5b00c3c54433d7373f738f71295b955743dd132049d5c', 'STUDENT'), " +
                    "('S103', 'Ananya Roy', 'ananya@example.com', '9876543212', 'cd93560731f416d860d5b00c3c54433d7373f738f71295b955743dd132049d5c', 'STUDENT')");
        }

        // Check if books empty
        var rsBooks = stmt.executeQuery("SELECT COUNT(*) FROM books");
        if (rsBooks.next() && rsBooks.getInt(1) == 0) {
            stmt.execute("INSERT INTO books (book_code, title, author, isbn, genre, total_copies, available_copies, is_available) VALUES " +
                    "('B001', 'Clean Code', 'Robert C. Martin', '9780132350884', 'Software Engineering', 5, 5, TRUE), " +
                    "('B002', 'The Pragmatic Programmer', 'Andrew Hunt', '9780201616224', 'Software Engineering', 3, 3, TRUE), " +
                    "('B003', 'Design Patterns', 'Erich Gamma', '9780201633610', 'Computer Science', 4, 4, TRUE), " +
                    "('B004', 'Introduction to Algorithms', 'Thomas H. Cormen', '9780262033848', 'Computer Science', 2, 2, TRUE), " +
                    "('B005', 'Artificial Intelligence', 'Stuart Russell', '9780134610993', 'Artificial Intelligence', 3, 3, TRUE)");
        }
    }
}
