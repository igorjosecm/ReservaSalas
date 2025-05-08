package classes;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final HikariDataSource dataSource;

    static {
        try {
            HikariConfig config = new HikariConfig();

            config.setJdbcUrl("jdbc:postgresql://pg-tads-udesc-bancodedados2-udesc.b.aivencloud.com:20583/BAN2");
            config.setUsername("udesc");
            config.setPassword("MY_PASSWORD");

            config.addDataSourceProperty("ssl", "true");
            config.addDataSourceProperty("sslmode", "require");

            config.setMaximumPoolSize(10);
            config.setConnectionTimeout(30000);

            dataSource = new HikariDataSource(config);

            try (Connection testConn = dataSource.getConnection()) {
                System.out.println("Sucesso na conexão com o servidor!\n");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao tentar estabelecer uma conexão com o servidor:");
            e.printStackTrace();
            throw new RuntimeException("A conexão falhou", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
