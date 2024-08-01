package placeholder;

import loopdospru.lvip.config.General;
import loopdospru.lvip.corja.profile.Profile;
import loopdospru.lvip.manager.ProfileManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Holder extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "lvip";
    }

    @Override
    public @NotNull String getAuthor() {
        return "LoopDosPru";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    public String onRequest(Player player, @NotNull String identifier) {
        if (identifier.equalsIgnoreCase("vip_name")) {
            return getVIPName(player);
        } else if (identifier.equalsIgnoreCase("vip_prefix")) {
            return getVIPPrefix(player);
        }
        return null; // Return null if the identifier does not match
    }

    private String getVIPName(Player player) {
        String identifier = getIdentifierFromPlayer(player);
        if (identifier == null) return "?";
        Profile profile = ProfileManager.loadProfile(identifier, General.isUuid());
        if (profile != null && profile.getAtual() != null) {
            return ChatColor.translateAlternateColorCodes('&', profile.getAtual().getNome());
        }
        return "?";
    }

    private String getVIPPrefix(Player player) {
        String identifier = getIdentifierFromPlayer(player);
        if (identifier == null) return "?";
        Profile profile = ProfileManager.loadProfile(identifier, General.isUuid());
        if (profile != null && profile.getAtual() != null) {
            return ChatColor.translateAlternateColorCodes('&', profile.getAtual().getPrefixo());
        }
        return "?";
    }

    private String getIdentifierFromPlayer(Player player) {
        if (General.isUuid()) {
            return String.valueOf(player.getUniqueId());
        } else {
            return player.getName();
        }
    }
}
