-- ========================================================
-- SAMPLE DATA: Library Management System
-- ========================================================

USE library_db;

-- --------------------------------------------------------
-- Populate users (Admins & Students)
-- Passwords hashed via SHA-256 for demo (admin123 / student123)
-- --------------------------------------------------------
INSERT INTO users (user_code, name, email, phone, password_hash, role) VALUES
('A001', 'System Administrator', 'admin@library.com', '9998887770', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'ADMIN'),
('S101', 'Vivek Verma', 'vivek@example.com', '9876543210', 'cd93560731f416d860d5b00c3c54433d7373f738f71295b955743dd132049d5c', 'STUDENT'),
('S102', 'Rahul Sharma', 'rahul@example.com', '9876543211', 'cd93560731f416d860d5b00c3c54433d7373f738f71295b955743dd132049d5c', 'STUDENT'),
('S103', 'Ananya Roy', 'ananya@example.com', '9876543212', 'cd93560731f416d860d5b00c3c54433d7373f738f71295b955743dd132049d5c', 'STUDENT')
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- --------------------------------------------------------
-- Populate books catalog
-- --------------------------------------------------------
INSERT INTO books (book_code, title, author, isbn, genre, total_copies, available_copies) VALUES
('B001', 'Clean Code', 'Robert C. Martin', '9780132350884', 'Software Engineering', 5, 5),
('B002', 'The Pragmatic Programmer', 'Andrew Hunt', '9780201616224', 'Software Engineering', 3, 3),
('B003', 'Design Patterns', 'Erich Gamma', '9780201633610', 'Computer Science', 4, 4),
('B004', 'Introduction to Algorithms', 'Thomas H. Cormen', '9780262033848', 'Computer Science', 2, 2),
('B005', 'Artificial Intelligence', 'Stuart Russell', '9780134610993', 'Artificial Intelligence', 3, 3)
ON DUPLICATE KEY UPDATE title=VALUES(title);

-- --------------------------------------------------------
-- Sample Issued Books
-- --------------------------------------------------------
INSERT INTO issued_books (book_id, user_id, issue_date, due_date, return_date, fine_amount, status) VALUES
(1, 2, CURDATE() - INTERVAL 10 DAY, CURDATE() + INTERVAL 4 DAY, NULL, 0.00, 'ISSUED'),
(3, 3, CURDATE() - INTERVAL 20 DAY, CURDATE() - INTERVAL 6 DAY, NULL, 30.00, 'OVERDUE')
ON DUPLICATE KEY UPDATE status=VALUES(status);
