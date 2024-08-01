package loopdospru.lvip.database;

import com.zaxxer.hikari.HikariDataSource;
import loopdospru.lvip.config.General;
import loopdospru.lvip.database.connections.ConnectionDatabase;
import loopdospru.lvip.database.connections.ConnectionDatabase;
import loopdospru.lvip.database.enums.DatabaseType;

public class Database {

    private final String HOST;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private static String type;
    public static HikariDataSource hikari;

    public Database() {
        String[] ip = General.getDatabaseIP().split(":");
        HOST = ip[0];
        port = Integer.parseInt(ip[1]);
        database = General.getDatabaseName();
        username = General.getDatabaseUser();
        password = General.getDatabasePassword();
        enable();
    }

    private void enable() {
        switch (DatabaseType.valueOf(General.getDatabaseType().toUpperCase())) {
            case MYSQL:
                type = "mysql";
                break;
            case SQLITE:
                type = "sqlite";
                break;
            default:
                type = "sqlite";
        }
        connect();
    }

    public void connect() {
        ConnectionDatabase connection = new ConnectionDatabase();
        connection.connect(HOST, String.valueOf(port), username, password, database, type);
        hikari = connection.getHikari(); // Atribui a variável hikari
    }

    public boolean isConnected() {
        return hikari != null && !hikari.isClosed();
    }

    public HikariDataSource getHikari() {
        return hikari;
    }

    public void disconnect() {
        if (isConnected()) {
            hikari.close();
        }
    }
}
