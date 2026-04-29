package com.library.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class User implements Serializable {
    private Long id;
    private String userCode;
    private String name;
    private String email;
    private String phone;
    private String passwordHash;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User() {
    }

    public User(Long id, String userCode, String name, String email, String phone, String passwordHash, Role role) {
        this.id = id;
        this.userCode = userCode;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.passwordHash = passwordHash;
        this.role = role != null ? role : Role.STUDENT;
    }

    public User(String userCode, String name, String email, String phone, String passwordHash, Role role) {
        this(null, userCode, name, email, phone, passwordHash, role);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return String.format("User[ID=%d, Code='%s', Name='%s', Email='%s', Phone='%s', Role=%s]",
                id, userCode, name, email, phone, role);
    }
}
