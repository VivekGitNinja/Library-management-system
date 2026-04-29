package com.library.service;

import com.library.exception.LibraryException;
import com.library.model.IssuedBook;

import java.util.List;

public interface LibraryService {
    IssuedBook issueBook(String bookCode, String userCode) throws LibraryException;
    IssuedBook returnBook(String bookCode, String userCode) throws LibraryException;
    List<IssuedBook> getAllIssuedBooks() throws LibraryException;
    List<IssuedBook> getIssuedBooksByUser(String userCode) throws LibraryException;
    List<IssuedBook> getOverdueBooks() throws LibraryException;
}
