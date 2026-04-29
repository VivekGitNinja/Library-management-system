package com.library;

import com.library.db.DatabaseConnectionPool;
import com.library.db.DatabaseInitializer;
import com.library.ui.ConsoleMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Initializing Library Management System...");

        // Ensure database tables & seed data are initialized
        DatabaseInitializer.initializeDatabase();

        // Register shutdown hook for HikariCP pool
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down application resources...");
            DatabaseConnectionPool.shutdown();
        }));

        // Launch Application Console UI
        ConsoleMenu menu = new ConsoleMenu();
        menu.start();
    }
}
