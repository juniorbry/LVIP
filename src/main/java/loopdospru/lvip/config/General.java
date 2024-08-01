package loopdospru.lvip.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class General {

    private static FileConfiguration config;
    private static JavaPlugin plugin;

    // Armazena os valores carregados em variáveis estáticas
    private static String databaseType;
    private static String databaseName;
    private static String databaseIP;
    private static String databaseUser;
    private static String databasePassword;
    private static String language;
    private static boolean titleEnabled;
    private static boolean fireworkEnabled;
    private static boolean alwaysUpdateEnabled;
    private static boolean warnEnabled;
    private static String timeZone;
    private static boolean uuid;

    // Inicialize com o plugin
    public static void initialize(JavaPlugin pluginInstance) {
        if (plugin != null) {
            // Já inicializado
            return;
        }

        plugin = pluginInstance;
        File configFile = new File(plugin.getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            plugin.saveDefaultConfig(); // Salva o arquivo padrão se não existir
        }

        config = YamlConfiguration.loadConfiguration(configFile);

        // Carrega as configurações uma única vez
        loadConfigurations();
    }

    // Carrega as configurações na memória
    private static void loadConfigurations() {
        databaseType = config.getString("Database.Type");
        databaseName = config.getString("Database.Name");
        databaseIP = config.getString("Database.IP");
        databaseUser = config.getString("Database.User");
        databasePassword = config.getString("Database.Password");
        language = config.getString("General.Language");
        titleEnabled = config.getBoolean("General.Title");
        fireworkEnabled = config.getBoolean("General.Firework");
        alwaysUpdateEnabled = config.getBoolean("options.always-update");
        warnEnabled = config.getBoolean("options.warn");
        timeZone = config.getString("options.time-zone");
        uuid = config.getBoolean("General.utils-uuid");
    }

    // Métodos estáticos para acessar as configurações
    public static String getDatabaseType() {
        return databaseType;
    }

    public static String getDatabaseName() {
        return databaseName;
    }

    public static String getDatabaseIP() {
        return databaseIP;
    }

    public static String getDatabaseUser() {
        return databaseUser;
    }

    public static String getDatabasePassword() {
        return databasePassword;
    }

    public static String getLanguage() {
        return language;
    }

    public static boolean isTitleEnabled() {
        return titleEnabled;
    }

    public static boolean isFireworkEnabled() {
        return fireworkEnabled;
    }

    public static boolean isAlwaysUpdateEnabled() {
        return alwaysUpdateEnabled;
    }

    public static boolean isWarnEnabled() {
        return warnEnabled;
    }

    public static String getTimeZone() {
        return timeZone;
    }

    // Salva configurações de volta ao arquivo
    public static void saveConfig() {
        try {
            config.save(new File(plugin.getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isUuid() {
        return uuid;
    }
}
