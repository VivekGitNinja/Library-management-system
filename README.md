# Library Management System

A Java-based Library Management System designed to handle book inventories, student registrations, book issuances, returns, search operations, and error handling.

## Features

- **Book Inventory Management**: Add, view, search, and manage books by title, author, or genre.
- **Student Registration**: Manage student details and track borrowed books.
- **Issue & Return System**: Issue books with due date tracking (default 14 days) and handle returns seamlessly.
- **Robust Exception Handling**: Custom exceptions for missing books, unavailable books, invalid student IDs, and invalid inputs.
- **Interactive Command-Line Interface (CLI)**: User-friendly console interface for easy navigation.

## Architecture

- `Book.java`: Data model representing books and availability state.
- `Student.java`: Data model representing registered students and their borrowed book IDs.
- `BookRepository.java`: Repository pattern for managing book collection and search features.
- `StudentRepository.java`: Repository pattern for student management.
- `ErrorHandling.java`: Contains custom domain-specific exception classes.
- `Library.java`: Core application entry point and interactive CLI menu handler.

## Prerequisites

- Java Development Kit (JDK 8 or higher)

## How to Build and Run

### 1. Compile the Source Code
```bash
javac *.java
```

### 2. Run the Application
```bash
java Library
```

---
Developed by Vivek Verma
