package database;


// You need to add HikariCP dependency to your project
// Add this to pom.xml if using Maven:
// <dependency>
//     <groupId>com.zaxxer</groupId>
//     <artifactId>HikariCP</artifactId>
//     <version>5.0.1</version>
// </dependency>
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class JDBCConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/foot_manager_pro";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final HikariDataSource dataSource;

    static {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(URL);
            config.setUsername(USER);
            config.setPassword(PASSWORD);
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(5);
            config.setIdleTimeout(300000);
            config.setConnectionTimeout(20000);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize connection pool.", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new SQLException("Failed to get database connection from pool.", e);
        }
    }

    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}