package loopdospru.lvip.database.enums;

public enum DatabaseType {

    MYSQL,
    SQLITE;

    public static DatabaseType value(String type) {
        if (type.equalsIgnoreCase("mysql")) {
            return MYSQL;
        }else if (type.equalsIgnoreCase("sqlite")) {
            return SQLITE;
        }
        return null;
    }
}
