package loopdospru.lvip.commands;

import loopdospru.lvip.config.General;
import loopdospru.lvip.corja.VIP;
import loopdospru.lvip.corja.VIPsTemplate;
import loopdospru.lvip.corja.configs.VIPCONFIG;
import loopdospru.lvip.corja.profile.Profile;
import loopdospru.lvip.corja.profile.ProfileFactory;
import loopdospru.lvip.corja.profile.ProfilePlayer;
import loopdospru.lvip.corja.profile.ProfileUUID;
import loopdospru.lvip.manager.ProfileManager;
import loopdospru.lvip.utils.VIPUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class VIPAdmin implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(getMessage("command.onlyPlayer"));
            return false;
        }

        Player player = (Player) commandSender;

        if (!player.hasPermission("lvip.admin")) {
            player.sendMessage(getMessage("command.noPermission"));
            return false;
        }

        if (args.length < 2) {
            player.sendMessage(getUsageMessage());
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "give":
                handleGiveCommand(player, args);
                break;
            case "remove":
                handleRemoveCommand(player, args);
                break;
            default:
                player.sendMessage(getUsageMessage());
                break;
        }

        return true;
    }

    private void handleGiveCommand(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage(getUsageMessage());
            return;
        }

        String vipName = args[2];
        VIPsTemplate vip = VIPCONFIG.getTemplate(vipName);

        if (vip == null) {
            player.sendMessage(ChatColor.RED + getMessage("vip.notFound") + getAvailableVIPs());
            return;
        }

        if (args.length < 4) {
            player.sendMessage(ChatColor.YELLOW + getMessage("command.give.usage"));
            return;
        }

        String targetName = args[1];
        VIPUtils.darVIP(vip, targetName);
        player.sendMessage(ChatColor.GREEN + getMessage("vip.given", vip.getNome(), targetName));
    }

    private void handleRemoveCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.YELLOW + getMessage("command.remove.usage"));
            return;
        }

        String targetName = args[1];
        Profile profile = loadProfile(targetName);

        if (profile == null) {
            player.sendMessage(ChatColor.RED + getMessage("profile.notFound", targetName));
            return;
        }

        if (args.length == 3 && args[2].equalsIgnoreCase("-all")) {
            // Remove todos os VIPs
            profile.getVips().clear();
            ProfileManager.saveProfile(profile);
            player.sendMessage(ChatColor.GREEN + getMessage("vip.allRemoved", targetName));
        } else if (args.length == 3) {
            // Remove um VIP específico
            VIPsTemplate vipToRemove = VIPCONFIG.getTemplate(args[2]);
            if (vipToRemove != null) {
                List<VIP> vip = profile.getVips();
                for (VIP vips : vip) {
                    if (!vips.getNome().contains(vipToRemove.getNome())) {
                        vip.add(vips);
                    }
                }
                profile.getVips().clear();
                vip.forEach(v -> profile.getVips().add(v));
                ProfileManager.saveProfile(profile);
                player.sendMessage(ChatColor.GREEN + getMessage("vip.removed", vipToRemove.getNome(), targetName));
            } else {
                player.sendMessage(ChatColor.RED + getMessage("vip.notFound") + getAvailableVIPs());
            }
        } else {
            player.sendMessage(ChatColor.YELLOW + getMessage("command.remove.usage"));
        }
    }

    private Profile loadProfile(String playerName) {
        String identifier = General.isUuid() ? String.valueOf(Bukkit.getOfflinePlayer(playerName).getUniqueId()) : playerName;
        return ProfileManager.loadProfile(identifier, General.isUuid());
    }

    private String getAvailableVIPs() {
        StringBuilder vips = new StringBuilder();
        VIPCONFIG.templates.values().forEach(template -> {
            vips.append(ChatColor.getLastColors(ChatColor.translateAlternateColorCodes('&', template.getPrefixo()) + template.getNome()));
            vips.append(ChatColor.DARK_GRAY).append(", ").append(ChatColor.WHITE);
        });
        return vips.toString();
    }

    private String getUsageMessage() {
        if (General.getLanguage().contains("pt-br")) {
            return ChatColor.YELLOW + "Uso correto dos comandos:" +
                    "\n" + ChatColor.GRAY + "/vipadmin give <player> <vip> - Dá um VIP ao jogador." +
                    "\n" + ChatColor.GRAY + "/vipadmin remove <player> - Remove VIP do jogador." +
                    "\n" + ChatColor.GRAY + "/vipadmin remove <player> -all - Remove todos os VIPs do jogador.";
        } else {
            return ChatColor.YELLOW + "Correct usage of commands:" +
                    "\n" + ChatColor.GRAY + "/vipadmin give <player> <vip> - Grants a VIP to the player." +
                    "\n" + ChatColor.GRAY + "/vipadmin remove <player> - Removes VIP from the player." +
                    "\n" + ChatColor.GRAY + "/vipadmin remove <player> -all - Removes all VIPs from the player.";
        }
    }

    private String getMessage(String key, Object... args) {
        String message;
        switch (key) {
            case "command.onlyPlayer":
                message = General.getLanguage().contains("pt-br") ? "Este comando só pode ser executado por um jogador." : "This command can only be executed by a player.";
                break;
            case "command.noPermission":
                message = General.getLanguage().contains("pt-br") ? "Você não tem permissão para usar este comando." : "You do not have permission to use this command.";
                break;
            case "command.give.usage":
                message = General.getLanguage().contains("pt-br") ? "Uso correto: /vipadmin give <player> <vip>" : "Correct usage: /vipadmin give <player> <vip>";
                break;
            case "command.remove.usage":
                message = General.getLanguage().contains("pt-br") ? "Uso correto: /vipadmin remove <player> [-all]" : "Correct usage: /vipadmin remove <player> [-all]";
                break;
            case "vip.notFound":
                message = General.getLanguage().contains("pt-br") ? "VIP não encontrado. " : "VIP not found. ";
                break;
            case "profile.notFound":
                message = General.getLanguage().contains("pt-br") ? "Erro: Nenhum perfil encontrado para o jogador " + args[0] + "." : "Error: No profile found for player " + args[0] + ".";
                break;
            case "vip.given":
                message = General.getLanguage().contains("pt-br") ? "VIP " + args[0] + " foi dado ao jogador " + args[1] + "." : "VIP " + args[0] + " has been given to player " + args[1] + ".";
                break;
            case "vip.allRemoved":
                message = General.getLanguage().contains("pt-br") ? "Todos os VIPs foram removidos do jogador " + args[0] + "." : "All VIPs have been removed from player " + args[0] + ".";
                break;
            case "vip.removed":
                message = General.getLanguage().contains("pt-br") ? "VIP " + args[0] + " foi removido do jogador " + args[1] + "." : "VIP " + args[0] + " has been removed from player " + args[1] + ".";
                break;
            default:
                message = "Message key not found!";
        }
        return message;
    }
}
