package loopdospru.lvip.config;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Language {

    private static JavaPlugin plugin;

    // Armazenamento das mensagens
    public static String errorKeyApproved;
    public static String errorKeyNotFound;
    public static String statusUpdate;
    public static List<String> broadcastTitle;
    public static List<String> broadcastMessageActionBar;
    public static List<String> broadcastMessageActived;
    public static List<String> broadcastMessageUpgradeTime;

    public static String profileTitle;

    // Armazenamento dos itens do menu
    public static Map<String, MenuItem> menuItems = new HashMap<>();
    public static int profileSize;

    // Inicializa com o plugin
    public static void initialize(JavaPlugin pluginInstance) {
        if (plugin != null) {
            // Já inicializado
            return;
        }

        plugin = pluginInstance;
        File languageFile = new File(plugin.getDataFolder(), "languages/pt-br.yml");

        if (!languageFile.exists()) {
            plugin.saveResource("languages/pt-br.yml", false); // Copia o arquivo padrão se não existir
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(languageFile);

        // Carrega as mensagens e itens
        loadMessages(config);
        loadMenuItems(config);
    }

    private static void loadMessages(FileConfiguration config) {
        // Carrega as mensagens
        errorKeyApproved = config.getString("Messages.Error.key.0");
        errorKeyNotFound = config.getString("Messages.Error.key.1");
        statusUpdate = config.getString("Messages.StatusUpdate");
        broadcastTitle = config.getStringList("Messages.BroadCast.Title");
        broadcastMessageActionBar = config.getStringList("Messages.BroadCast.Message.ActionBar");
        broadcastMessageActived = config.getStringList("Messages.BroadCast.Message.Actived");
        broadcastMessageUpgradeTime = config.getStringList("Messages.BroadCast.Message.UpgradeTime");
    }

    private static void loadMenuItems(FileConfiguration config) {
        // Carrega itens do menu
        profileTitle = config.getString("Menus.Profile.Title");
        profileSize = config.getInt("Menus.Profile.Size");

        menuItems.put("profileItem", new MenuItem(
                config.getString("Menus.Profile.Items.ProfileItem.Name"),
                getMaterial(config.getString("Menus.Profile.Items.ProfileItem.Material")),
                config.getInt("Menus.Profile.Items.ProfileItem.Data"),
                config.getInt("Menus.Profile.Items.ProfileItem.Slot"),
                config.getStringList("Menus.Profile.Items.ProfileItem.Lore")
        ));

        menuItems.put("config", new MenuItem(
                config.getString("Menus.Profile.Items.Config.Name"),
                getMaterial(config.getString("Menus.Profile.Items.Config.Material")),
                config.getInt("Menus.Profile.Items.Config.Data"),
                config.getInt("Menus.Profile.Items.Config.Slot"),
                config.getStringList("Menus.Profile.Items.Config.Lore")
        ));

        menuItems.put("historic", new MenuItem(
                config.getString("Menus.Profile.Items.Historic.Name"),
                getMaterial(config.getString("Menus.Profile.Items.Historic.Material")),
                config.getInt("Menus.Profile.Items.Historic.Data"),
                config.getInt("Menus.Profile.Items.Historic.Slot"),
                config.getStringList("Menus.Profile.Items.Historic.Lore")
        ));

        menuItems.put("keys", new MenuItem(
                config.getString("Menus.Profile.Items.Keys.Name"),
                getMaterial(config.getString("Menus.Profile.Items.Keys.Material")),
                config.getInt("Menus.Profile.Items.Keys.Data"),
                config.getInt("Menus.Profile.Items.Keys.Slot"),
                config.getStringList("Menus.Profile.Items.Keys.Lore")
        ));
    }

    private static Material getMaterial(String materialName) {
        if (materialName == null || materialName.isEmpty()) {
            Bukkit.getLogger().warning("Nome do material é nulo ou vazio.");
            return Material.AIR; // Retorna um material padrão caso o nome esteja vazio
        }

        try {
            return Material.valueOf(materialName.toUpperCase());
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning("Material não encontrado: " + materialName);
            return Material.AIR; // Retorna um material padrão caso o nome esteja incorreto
        }
    }

    public static class MenuItem {
        private final String name;
        private final Material material;
        private final int data;
        private final int slot;
        private final List<String> lore;

        public MenuItem(String name, Material material, int data, int slot, List<String> lore) {
            this.name = name;
            this.material = material;
            this.data = data;
            this.slot = slot;
            this.lore = lore;
        }

        public String getName() {
            return name;
        }

        public Material getMaterial() {
            return material;
        }

        public int getData() {
            return data;
        }

        public int getSlot() {
            return slot;
        }

        public List<String> getLore() {
            return lore;
        }
    }
}
