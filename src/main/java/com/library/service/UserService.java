package com.library.service;

import com.library.exception.InvalidInputException;
import com.library.exception.LibraryException;
import com.library.exception.UserNotFoundException;
import com.library.model.User;

import java.util.List;

public interface UserService {
    User registerUser(User user, String rawPassword) throws LibraryException;
    User getUserById(Long id) throws UserNotFoundException, LibraryException;
    User getUserByCode(String userCode) throws UserNotFoundException, LibraryException;
    User authenticate(String email, String rawPassword) throws LibraryException;
    List<User> getAllUsers() throws LibraryException;
    List<User> searchUsersByName(String keyword) throws LibraryException;
    boolean updateUser(User user) throws LibraryException;
    boolean removeUser(Long id) throws UserNotFoundException, LibraryException;
}
