package loopdospru.lvip.utils;

import loopdospru.lvip.config.General;

public class Messages {

    private static String getLanguage() {
        return General.getLanguage();
    }

    private static boolean isPortuguese() {
        return getLanguage().contains("pt-br");
    }

    public static String getMessage(String key) {
        if (isPortuguese()) {
            return getPortugueseMessage(key);
        } else {
            return getEnglishMessage(key);
        }
    }

    private static String getPortugueseMessage(String key) {
        switch (key) {
            case "database_connected":
                return "[LVIP Database] Conexão com a base de dados foi estabelecida via HikariCP.";
            case "tables_created":
                return "[LVIP Database] Tabelas criadas ou já existem.";
            case "table_creation_failed":
                return "[LVIP Database] Falha ao criar tabelas.";
            default:
                return "Mensagem não encontrada.";
        }
    }

    private static String getEnglishMessage(String key) {
        switch (key) {
            case "database_connected":
                return "[LVIP Database] Connection to the database has been established via HikariCP.";
            case "tables_created":
                return "[LVIP Database] Tables created or already exist.";
            case "table_creation_failed":
                return "[LVIP Database] Failed to create tables.";
            default:
                return "Message not found.";
        }
    }
}
