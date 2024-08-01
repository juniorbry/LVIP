package loopdospru.lvip.commands;

import github.GitHub;
import loopdospru.lvip.LVIP;
import loopdospru.lvip.config.General;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PluginCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (!player.hasPermission("lvip.admin")) {
                return true;
            }

            if (args.length == 0) {
                showHelp(player);
            } else {
                switch (args[0].toLowerCase()) {
                    case "update":
                        handleUpdate(player);
                        break;
                    case "reload":
                        handleReload(player);
                        break;
                    default:
                        player.sendMessage(getMessage("invalid_command"));
                        break;
                }
            }
        } else {
            // Handle commands for console
            if (args.length == 0) {
                showHelpConsole();
            } else {
                switch (args[0].toLowerCase()) {
                    case "update":
                        handleUpdateConsole();
                        break;
                    case "reload":
                        handleReloadConsole();
                        break;
                    default:
                        commandSender.sendMessage(getMessage("invalid_command_console"));
                        break;
                }
            }
        }
        return true;
    }

    private void showHelp(Player player) {
        player.sendMessage("");
        player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + " LVIP " + ChatColor.YELLOW + "v" + LVIP.getPlugin(LVIP.class).getDescription().getVersion());
        player.sendMessage(ChatColor.AQUA + "https://github.com/juniorbry/LVIP");
        player.sendMessage("");
        player.sendMessage(ChatColor.GRAY + "/lvip update - " + getMessage("update_description"));
        player.sendMessage(ChatColor.GRAY + "/lvip reload - " + getMessage("reload_description"));
        player.sendMessage("");
    }

    private void showHelpConsole() {
        System.out.println("");
        System.out.println("LVIP v" + LVIP.getPlugin(LVIP.class).getDescription().getVersion());
        System.out.println("https://github.com/juniorbry/LVIP");
        System.out.println("");
        System.out.println("/lvip update - " + getMessage("update_description_console"));
        System.out.println("/lvip reload - " + getMessage("reload_description_console"));
        System.out.println("");
    }

    private void handleUpdate(Player player) {
        if (GitHub.isTimeToUpdate(LVIP.getPlugin(LVIP.class))) {
            if (General.getLanguage().contains("pt-br")) {
                player.sendMessage(ChatColor.GREEN + "Iniciando o processo de atualização...");
            } else {
                player.sendMessage(ChatColor.GREEN + "Starting the update process...");
            }
            GitHub.update(LVIP.getPlugin(LVIP.class));
        } else {
            player.sendMessage(ChatColor.RED + getMessage("no_update_available"));
        }
    }

    private void handleReload(Player player) {
        General.initialize(LVIP.getPlugin(LVIP.class));
        player.sendMessage(ChatColor.GREEN + getMessage("reload_success"));
    }

    private void handleUpdateConsole() {
        if (GitHub.isTimeToUpdate(LVIP.getPlugin(LVIP.class))) {
            System.out.println("Starting the update process...");
            GitHub.update(LVIP.getPlugin(LVIP.class));
        } else {
            System.out.println(ChatColor.RED + "No update available.");
        }
    }

    private void handleReloadConsole() {
        General.initialize(LVIP.getPlugin(LVIP.class));
        System.out.println(ChatColor.GREEN + "Plugin reloaded successfully.");
    }

    private String getMessage(String key) {
        if (General.getLanguage().contains("pt-br")) {
            switch (key) {
                case "update_description":
                    return "Atualiza o plugin para a versão mais recente.";
                case "reload_description":
                    return "Recarrega a configuração do plugin.";
                case "invalid_command":
                    return ChatColor.RED + "Comando inválido. Use /lvip para ajuda.";
                case "no_update_available":
                    return ChatColor.RED + "Nenhuma atualização disponível no momento.";
                case "reload_success":
                    return ChatColor.GREEN + "Configuração do plugin recarregada com sucesso.";
                case "update_description_console":
                    return "Atualiza o plugin para a versão mais recente.";
                case "reload_description_console":
                    return "Recarrega a configuração do plugin.";
                case "invalid_command_console":
                    return ChatColor.RED + "Comando inválido. Use /lvip para ajuda.";
                case "has_no_permission":
                    return ChatColor.RED + "Você não possui tal permissão necessária.";
                default:
                    return "";
            }
        } else {
            switch (key) {
                case "update_description":
                    return "Updates the plugin to the latest version.";
                case "reload_description":
                    return "Reloads the plugin configuration.";
                case "invalid_command":
                    return ChatColor.RED + "Invalid command. Use /lvip for help.";
                case "no_update_available":
                    return ChatColor.RED + "No update available at the moment.";
                case "reload_success":
                    return ChatColor.GREEN + "Plugin configuration reloaded successfully.";
                case "update_description_console":
                    return "Updates the plugin to the latest version.";
                case "reload_description_console":
                    return "Reloads the plugin configuration.";
                case "invalid_command_console":
                    return ChatColor.RED + "Invalid command. Use /lvip for help.";
                case "has_no_permission":
                    return ChatColor.RED + "You don't have permission.";
                default:
                    return "";
            }
        }
    }
}
