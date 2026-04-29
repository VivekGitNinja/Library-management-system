package com.library.dao;

import com.library.model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserDao {
    User save(User user) throws SQLException;
    User save(Connection conn, User user) throws SQLException;
    Optional<User> findById(Long id) throws SQLException;
    Optional<User> findByUserCode(String userCode) throws SQLException;
    Optional<User> findByEmail(String email) throws SQLException;
    Optional<User> authenticate(String email, String passwordHash) throws SQLException;
    List<User> findAll() throws SQLException;
    List<User> searchByNameLike(String nameKeyword) throws SQLException;
    boolean update(User user) throws SQLException;
    boolean deleteById(Long id) throws SQLException;
    long countTotalUsers() throws SQLException;
}
