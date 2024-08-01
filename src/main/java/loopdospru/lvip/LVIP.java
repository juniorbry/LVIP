package loopdospru.lvip;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.henryfabio.minecraft.inventoryapi.manager.InventoryManager;
import github.GitHub;
import loopdospru.lvip.commands.PluginCommand;
import loopdospru.lvip.commands.VIPinfo;
import loopdospru.lvip.config.General;
import loopdospru.lvip.config.Language;
import loopdospru.lvip.corja.configs.VIPCONFIG;
import loopdospru.lvip.database.Database;
import loopdospru.lvip.database.enums.DatabaseType;
import loopdospru.lvip.listener.CreateProfile;
import loopdospru.lvip.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import placeholder.Holder;

import java.util.Objects;

public final class LVIP extends JavaPlugin {

    public static LVIP instance;
    public static ProtocolManager protocolManager;

    @Override
    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[LVIP] Plugin is starting...");
        General.initialize(this);
        instance = this;
        // Initialize database
        initializeDatabase();

        // Check for updates
        checkForUpdates();

        // Initialize commands and listeners
        initializeCommands();
        initializeListeners();

        // Display final message
        displayFinalMessage();

        InventoryManager.enable(this);

        VIPCONFIG.enable(this);
        Language.initialize(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[LVIP] Plugin is shutting down...");
    }

    private void initializeDatabase() {
        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "[LVIP - Database] Type > " + General.getDatabaseType() + ". Verifying...");

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) { //
            new Holder().register(); //
        }
        if (DatabaseType.valueOf(General.getDatabaseType().toUpperCase()) != null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[LVIP - Database] " + DatabaseType.valueOf(General.getDatabaseType().toUpperCase()).toString() + " is starting...");
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[LVIP - Database] " + General.getDatabaseType() + " not found. Falling back to SQLITE.");
        }

        // Connect to the database
        new Database();
    }

    private void checkForUpdates() {
        if (GitHub.isTimeToUpdate(this)) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "-------------------------------------------------------------");
            if (General.getLanguage().contains("pt-br")) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Está na hora de atualizar este plugin.");
                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Você pode atualizar através deste link: " + ChatColor.AQUA + "{link}");
            } else {
                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "It's time to update this plugin.");
                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "You can update through this link: " + ChatColor.AQUA + "{link}");
            }
            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "-------------------------------------------------------------");
        } else {
            if (Bukkit.getPluginManager().getPlugin("ProtocolLib") == null) {
                getLogger().warning("[LVIP] ProtocolLib not found! This plugins is required.");
                Bukkit.getPluginManager().disablePlugin(this);
            }
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
                getLogger().warning("[LVIP] PlaceholderAPI not found! This plugin is required.");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }
    }

    private void initializeCommands() {
        // Register commands here
        Objects.requireNonNull(getCommand("lvip")).setExecutor(new PluginCommand());
        getCommand("vipinfo").setExecutor(new VIPinfo());
    }

    private void initializeListeners() {
        // Register event listeners here
        getServer().getPluginManager().registerEvents(new CreateProfile(), this);
    }

    private void displayFinalMessage() {
        String pluginName = "LVIP";
        String version = getDescription().getVersion();
        String author = getDescription().getAuthors().get(0);

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + pluginName);
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Version: " + version);
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Author: " + author);
    }
}
