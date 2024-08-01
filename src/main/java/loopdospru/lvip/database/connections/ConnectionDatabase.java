package loopdospru.lvip.database.connections;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionDatabase {

    private HikariDataSource hikari;

    public void connect(String HOST, String port, String username, String password, String database, String type) {
        HikariConfig config = new HikariConfig();
        if (type.equalsIgnoreCase("mysql")) {
            config.setJdbcUrl("jdbc:mysql://" + HOST + ":" + port + "/" + database);
            config.setUsername(username);
            config.setPassword(password);
        } else {
            File dbFile = new File("plugins/LPayments/" + database + ".db");
            config.setJdbcUrl("jdbc:sqlite:" + dbFile.getAbsolutePath());
            config.setDriverClassName("org.sqlite.JDBC");
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        }

        config.setMaximumPoolSize(10);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setValidationTimeout(5000);

        if (type.equalsIgnoreCase("sqlite")) {
            config.setConnectionTestQuery("SELECT 1");
        }

        hikari = new HikariDataSource(config);
        System.out.println("Database connected");
        createTablesIfDoesntExist();
    }

    public HikariDataSource getHikari() {
        return hikari;
    }

    private void createTablesIfDoesntExist() {
        String createClientsTable = "CREATE TABLE IF NOT EXISTS clients (" +
                "player_name TEXT PRIMARY KEY," +
                "json_data TEXT NOT NULL" +
                ");";
        try (Connection connection = hikari.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(createClientsTable);
            System.out.println("Tables created");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Table creation failed");
        }
    }
}
