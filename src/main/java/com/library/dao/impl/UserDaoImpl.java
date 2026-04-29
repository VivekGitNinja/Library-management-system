package com.library.dao.impl;

import com.library.dao.UserDao;
import com.library.db.DatabaseConnectionPool;
import com.library.model.Role;
import com.library.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {

    @Override
    public User save(User user) throws SQLException {
        try (Connection conn = DatabaseConnectionPool.getConnection()) {
            return save(conn, user);
        }
    }

    @Override
    public User save(Connection conn, User user) throws SQLException {
        String sql = "INSERT INTO users (user_code, name, email, phone, password_hash, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getUserCode());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPhone());
            pstmt.setString(5, user.getPasswordHash());
            pstmt.setString(6, user.getRole().name());

            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getLong(1));
                }
            }
            return user;
        }
    }

    @Override
    public Optional<User> findById(Long id) throws SQLException {
        String sql = "SELECT id, user_code, name, email, phone, password_hash, role, created_at, updated_at FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUserCode(String userCode) throws SQLException {
        String sql = "SELECT id, user_code, name, email, phone, password_hash, role, created_at, updated_at FROM users WHERE user_code = ?";
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userCode);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) throws SQLException {
        String sql = "SELECT id, user_code, name, email, phone, password_hash, role, created_at, updated_at FROM users WHERE LOWER(email) = LOWER(?)";
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> authenticate(String email, String passwordHash) throws SQLException {
        String sql = "SELECT id, user_code, name, email, phone, password_hash, role, created_at, updated_at FROM users WHERE LOWER(email) = LOWER(?) AND password_hash = ?";
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, passwordHash);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() throws SQLException {
        String sql = "SELECT id, user_code, name, email, phone, password_hash, role, created_at, updated_at FROM users ORDER BY id ASC";
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        }
        return users;
    }

    @Override
    public List<User> searchByNameLike(String nameKeyword) throws SQLException {
        String sql = "SELECT id, user_code, name, email, phone, password_hash, role, created_at, updated_at FROM users WHERE LOWER(name) LIKE ? ORDER BY name ASC";
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + nameKeyword.toLowerCase() + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    users.add(mapResultSetToUser(rs));
                }
            }
        }
        return users;
    }

    @Override
    public boolean update(User user) throws SQLException {
        String sql = "UPDATE users SET name = ?, email = ?, phone = ?, role = ? WHERE id = ?";
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPhone());
            pstmt.setString(4, user.getRole().name());
            pstmt.setLong(5, user.getId());
            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public long countTotalUsers() throws SQLException {
        String sql = "SELECT COUNT(*) FROM users";
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return 0;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User(
                rs.getLong("id"),
                rs.getString("user_code"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("password_hash"),
                Role.valueOf(rs.getString("role"))
        );
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) user.setCreatedAt(createdAt.toLocalDateTime());
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) user.setUpdatedAt(updatedAt.toLocalDateTime());
        return user;
    }
}
