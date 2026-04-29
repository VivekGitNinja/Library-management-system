package com.library.service.impl;

import com.library.dao.UserDao;
import com.library.dao.impl.UserDaoImpl;
import com.library.exception.InvalidInputException;
import com.library.exception.LibraryException;
import com.library.exception.UserNotFoundException;
import com.library.model.User;
import com.library.service.UserService;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl() {
        this.userDao = new UserDaoImpl();
    }

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User registerUser(User user, String rawPassword) throws LibraryException {
        if (user == null || user.getName() == null || user.getName().trim().isEmpty()) {
            throw new InvalidInputException("User name cannot be empty.");
        }
        if (user.getUserCode() == null || user.getUserCode().trim().isEmpty()) {
            throw new InvalidInputException("User code cannot be empty.");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new InvalidInputException("Email cannot be empty.");
        }

        try {
            if (userDao.findByUserCode(user.getUserCode()).isPresent()) {
                throw new InvalidInputException("User code '" + user.getUserCode() + "' already registered.");
            }
            if (userDao.findByEmail(user.getEmail()).isPresent()) {
                throw new InvalidInputException("Email '" + user.getEmail() + "' already registered.");
            }
            user.setPasswordHash(hashPassword(rawPassword));
            return userDao.save(user);
        } catch (SQLException e) {
            throw new LibraryException("Database error registering user: " + e.getMessage(), e);
        }
    }

    @Override
    public User getUserById(Long id) throws UserNotFoundException, LibraryException {
        try {
            return userDao.findById(id).orElseThrow(() -> new UserNotFoundException(String.valueOf(id)));
        } catch (SQLException e) {
            throw new LibraryException("Database error retrieving user: " + e.getMessage(), e);
        }
    }

    @Override
    public User getUserByCode(String userCode) throws UserNotFoundException, LibraryException {
        try {
            return userDao.findByUserCode(userCode).orElseThrow(() -> new UserNotFoundException(userCode));
        } catch (SQLException e) {
            throw new LibraryException("Database error retrieving user by code: " + e.getMessage(), e);
        }
    }

    @Override
    public User authenticate(String email, String rawPassword) throws LibraryException {
        if (email == null || email.trim().isEmpty() || rawPassword == null) {
            throw new InvalidInputException("Email and password are required.");
        }
        try {
            String passHash = hashPassword(rawPassword);
            return userDao.authenticate(email.trim(), passHash)
                    .orElseThrow(() -> new InvalidInputException("Invalid email or password."));
        } catch (SQLException e) {
            throw new LibraryException("Database error during authentication: " + e.getMessage(), e);
        }
    }

    @Override
    public List<User> getAllUsers() throws LibraryException {
        try {
            return userDao.findAll();
        } catch (SQLException e) {
            throw new LibraryException("Database error retrieving all users: " + e.getMessage(), e);
        }
    }

    @Override
    public List<User> searchUsersByName(String keyword) throws LibraryException {
        try {
            return userDao.searchByNameLike(keyword != null ? keyword.trim() : "");
        } catch (SQLException e) {
            throw new LibraryException("Database error searching users: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updateUser(User user) throws LibraryException {
        if (user == null || user.getId() == null) {
            throw new InvalidInputException("User ID is required for update.");
        }
        try {
            return userDao.update(user);
        } catch (SQLException e) {
            throw new LibraryException("Database error updating user: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean removeUser(Long id) throws UserNotFoundException, LibraryException {
        try {
            if (!userDao.findById(id).isPresent()) {
                throw new UserNotFoundException(String.valueOf(id));
            }
            return userDao.deleteById(id);
        } catch (SQLException e) {
            throw new LibraryException("Database error removing user: " + e.getMessage(), e);
        }
    }

    private String hashPassword(String rawPassword) {
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            rawPassword = "defaultPassword123";
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            return String.valueOf(rawPassword.hashCode());
        }
    }
}
