package loopdospru.lvip.corja.configs;

import loopdospru.lvip.LVIP;
import loopdospru.lvip.corja.VIPsTemplate;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VIPCONFIG {

    public static Map<String, VIPsTemplate> templates = new HashMap<>();
    private static File configFile;

    public static void enable(JavaPlugin plugin) {
        // Define the file path for the vips.yml
        configFile = new File(plugin.getDataFolder(), "vips.yml");

        // Check if the file exists, and copy it if it doesn't
        if (!configFile.exists()) {
            copyDefaultConfig(plugin);
        }

        // Load the templates from the file
        loadTemplates();
    }

    private static void copyDefaultConfig(JavaPlugin plugin) {
        // Create the file if it doesn't exist
        plugin.saveResource("vips.yml", false);
    }

    private static void loadTemplates() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        // Load VIP templates from the file
        if (config.contains("TEMPLATES")) {
            for (String key : config.getConfigurationSection("TEMPLATES").getKeys(false)) {
                String name = config.getString("TEMPLATES." + key + ".Name");
                String prefix = config.getString("TEMPLATES." + key + ".Prefix");
                String defaultTime = config.getString("TEMPLATES." + key + ".Default-Time");
                List<String> commands = config.getStringList("TEMPLATES." + key + ".Commands");

                VIPsTemplate template = new VIPsTemplate(name, prefix, defaultTime, commands);
                templates.put(key, template);
            }
        }
    }

    public static VIPsTemplate carregar(String nome, String prefixo, String tempo, List<String> comandos) {
        return new VIPsTemplate(nome, prefixo, tempo, comandos);
    }

    public static VIPsTemplate getTemplate(String key) {
        return templates.get(key);
    }
}
