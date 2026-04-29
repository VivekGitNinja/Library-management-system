package com.library.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class IssuedBook implements Serializable {
    private Long id;
    private Long bookId;
    private Long userId;
    private String bookTitle;
    private String userName;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private BigDecimal fineAmount;
    private String status; // ISSUED, RETURNED, OVERDUE, LOST
    private LocalDateTime createdAt;

    public IssuedBook() {
        this.fineAmount = BigDecimal.ZERO;
        this.status = "ISSUED";
    }

    public IssuedBook(Long id, Long bookId, Long userId, LocalDate issueDate, LocalDate dueDate) {
        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.fineAmount = BigDecimal.ZERO;
        this.status = "ISSUED";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public BigDecimal getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(BigDecimal fineAmount) {
        this.fineAmount = fineAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return String.format("IssuedBook[RecordID=%d, BookTitle='%s', User='%s', IssueDate=%s, DueDate=%s, Fine=$%.2f, Status=%s]",
                id, bookTitle != null ? bookTitle : "Book#" + bookId,
                userName != null ? userName : "User#" + userId,
                issueDate, dueDate, fineAmount != null ? fineAmount : BigDecimal.ZERO, status);
    }
}
