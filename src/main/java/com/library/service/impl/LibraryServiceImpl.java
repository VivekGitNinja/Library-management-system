package com.library.service.impl;

import com.library.dao.BookDao;
import com.library.dao.IssuedBookDao;
import com.library.dao.UserDao;
import com.library.dao.impl.BookDaoImpl;
import com.library.dao.impl.IssuedBookDaoImpl;
import com.library.dao.impl.UserDaoImpl;
import com.library.db.DatabaseConnectionPool;
import com.library.exception.*;
import com.library.model.Book;
import com.library.model.IssuedBook;
import com.library.model.User;
import com.library.service.LibraryService;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class LibraryServiceImpl implements LibraryService {
    private final BookDao bookDao;
    private final UserDao userDao;
    private final IssuedBookDao issuedBookDao;
    private static final BigDecimal DAILY_FINE_RATE = new BigDecimal("5.00");

    public LibraryServiceImpl() {
        this.bookDao = new BookDaoImpl();
        this.userDao = new UserDaoImpl();
        this.issuedBookDao = new IssuedBookDaoImpl();
    }

    public LibraryServiceImpl(BookDao bookDao, UserDao userDao, IssuedBookDao issuedBookDao) {
        this.bookDao = bookDao;
        this.userDao = userDao;
        this.issuedBookDao = issuedBookDao;
    }

    @Override
    public IssuedBook issueBook(String bookCode, String userCode) throws LibraryException {
        if (bookCode == null || bookCode.trim().isEmpty() || userCode == null || userCode.trim().isEmpty()) {
            throw new InvalidInputException("Book Code and User Code cannot be empty.");
        }

        Connection conn = null;
        try {
            conn = DatabaseConnectionPool.getConnection();
            conn.setAutoCommit(false);

            Book book = bookDao.findByBookCode(bookCode.trim())
                    .orElseThrow(() -> new BookNotFoundException(bookCode));
            User user = userDao.findByUserCode(userCode.trim())
                    .orElseThrow(() -> new UserNotFoundException(userCode));

            if (book.getAvailableCopies() <= 0) {
                throw new BookAlreadyBorrowedException(book.getTitle());
            }

            if (issuedBookDao.findActiveIssueByBookAndUser(book.getId(), user.getId()).isPresent()) {
                throw new InvalidInputException("User '" + user.getName() + "' already holds an active issue for book '" + book.getTitle() + "'.");
            }

            // Decrement available copies
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            bookDao.update(conn, book);

            // Insert issued_books record
            LocalDate now = LocalDate.now();
            LocalDate dueDate = now.plusDays(14);
            IssuedBook issueRecord = new IssuedBook(null, book.getId(), user.getId(), now, dueDate);
            issueRecord.setBookTitle(book.getTitle());
            issueRecord.setUserName(user.getName());

            IssuedBook savedRecord = issuedBookDao.save(conn, issueRecord);

            conn.commit();
            return savedRecord;

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    // Suppress
                }
            }
            if (e instanceof LibraryException) {
                throw (LibraryException) e;
            }
            throw new LibraryException("Transaction failed issuing book: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    // Suppress
                }
            }
        }
    }

    @Override
    public IssuedBook returnBook(String bookCode, String userCode) throws LibraryException {
        if (bookCode == null || bookCode.trim().isEmpty() || userCode == null || userCode.trim().isEmpty()) {
            throw new InvalidInputException("Book Code and User Code cannot be empty.");
        }

        Connection conn = null;
        try {
            conn = DatabaseConnectionPool.getConnection();
            conn.setAutoCommit(false);

            Book book = bookDao.findByBookCode(bookCode.trim())
                    .orElseThrow(() -> new BookNotFoundException(bookCode));
            User user = userDao.findByUserCode(userCode.trim())
                    .orElseThrow(() -> new UserNotFoundException(userCode));

            IssuedBook activeIssue = issuedBookDao.findActiveIssueByBookAndUser(book.getId(), user.getId())
                    .orElseThrow(() -> new InvalidInputException("No active issue found for Book '" + book.getTitle() + "' and User '" + user.getName() + "'."));

            LocalDate today = LocalDate.now();
            BigDecimal fine = BigDecimal.ZERO;
            if (today.isAfter(activeIssue.getDueDate())) {
                long overdueDays = ChronoUnit.DAYS.between(activeIssue.getDueDate(), today);
                fine = DAILY_FINE_RATE.multiply(BigDecimal.valueOf(overdueDays));
            }

            // Update issue status
            issuedBookDao.returnBook(conn, activeIssue.getId(), today, fine);

            // Increment available copies
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookDao.update(conn, book);

            activeIssue.setReturnDate(today);
            activeIssue.setFineAmount(fine);
            activeIssue.setStatus("RETURNED");

            conn.commit();
            return activeIssue;

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    // Suppress
                }
            }
            if (e instanceof LibraryException) {
                throw (LibraryException) e;
            }
            throw new LibraryException("Transaction failed returning book: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    // Suppress
                }
            }
        }
    }

    @Override
    public List<IssuedBook> getAllIssuedBooks() throws LibraryException {
        try {
            return issuedBookDao.findAllWithDetails();
        } catch (SQLException e) {
            throw new LibraryException("Database error retrieving issued books: " + e.getMessage(), e);
        }
    }

    @Override
    public List<IssuedBook> getIssuedBooksByUser(String userCode) throws LibraryException {
        try {
            User user = userDao.findByUserCode(userCode.trim())
                    .orElseThrow(() -> new UserNotFoundException(userCode));
            return issuedBookDao.findByUserId(user.getId());
        } catch (SQLException e) {
            throw new LibraryException("Database error retrieving user borrowing history: " + e.getMessage(), e);
        }
    }

    @Override
    public List<IssuedBook> getOverdueBooks() throws LibraryException {
        try {
            return issuedBookDao.findByStatus("OVERDUE");
        } catch (SQLException e) {
            throw new LibraryException("Database error retrieving overdue books: " + e.getMessage(), e);
        }
    }
}
