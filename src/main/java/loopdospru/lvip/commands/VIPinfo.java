package loopdospru.lvip.commands;

import loopdospru.lvip.menus.Perfil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VIPinfo implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            new Perfil().init().openInventory((((Player) commandSender).getPlayer()));
        }
        return false;
    }
}
