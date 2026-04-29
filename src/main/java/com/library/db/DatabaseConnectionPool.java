package com.library.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnectionPool {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionPool.class);
    private static HikariDataSource dataSource;
    private static String activeDbType = "mysql";

    static {
        initPool();
    }

    private static void initPool() {
        Properties props = new Properties();
        try (InputStream is = DatabaseConnectionPool.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is != null) {
                props.load(is);
            }
        } catch (Exception e) {
            logger.warn("Could not load db.properties, using defaults.", e);
        }

        String dbType = props.getProperty("db.type", "mysql");
        HikariConfig config = new HikariConfig();

        if ("mysql".equalsIgnoreCase(dbType)) {
            try {
                String driver = props.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");
                String url = props.getProperty("db.url", "jdbc:mysql://localhost:3306/library_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&createDatabaseIfNotExist=true");
                String user = props.getProperty("db.username", "root");
                String pass = props.getProperty("db.password", "root");

                Class.forName(driver);
                config.setDriverClassName(driver);
                config.setJdbcUrl(url);
                config.setUsername(user);
                config.setPassword(pass);
                config.setMaximumPoolSize(Integer.parseInt(props.getProperty("pool.maximumPoolSize", "10")));
                config.setMinimumIdle(Integer.parseInt(props.getProperty("pool.minimumIdle", "2")));
                config.setIdleTimeout(Long.parseLong(props.getProperty("pool.idleTimeout", "30000")));
                config.setConnectionTimeout(Long.parseLong(props.getProperty("pool.connectionTimeout", "5000")));

                dataSource = new HikariDataSource(config);
                // Test connection
                try (Connection conn = dataSource.getConnection()) {
                    activeDbType = "mysql";
                    logger.info("Successfully connected to MySQL database pool!");
                    return;
                }
            } catch (Exception e) {
                logger.warn("MySQL connection failed. Falling back to embedded H2 in-memory database: {}", e.getMessage());
                if (dataSource != null) {
                    dataSource.close();
                }
            }
        }

        // Fallback to H2 in-memory database
        try {
            String h2Driver = props.getProperty("h2.driver", "org.h2.Driver");
            String h2Url = props.getProperty("h2.url", "jdbc:h2:mem:library_db;DB_CLOSE_DELAY=-1;MODE=MySQL");
            String h2User = props.getProperty("h2.username", "sa");
            String h2Pass = props.getProperty("h2.password", "");

            Class.forName(h2Driver);
            HikariConfig h2Config = new HikariConfig();
            h2Config.setDriverClassName(h2Driver);
            h2Config.setJdbcUrl(h2Url);
            h2Config.setUsername(h2User);
            h2Config.setPassword(h2Pass);
            h2Config.setMaximumPoolSize(5);

            dataSource = new HikariDataSource(h2Config);
            activeDbType = "h2";
            logger.info("Successfully initialized H2 embedded database pool.");
        } catch (Exception e) {
            logger.error("Failed to initialize database pool.", e);
            throw new RuntimeException("Could not initialize any database connection.", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null || dataSource.isClosed()) {
            initPool();
        }
        return dataSource.getConnection();
    }

    public static String getActiveDbType() {
        return activeDbType;
    }

    public static void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("Database connection pool shut down.");
        }
    }
}
