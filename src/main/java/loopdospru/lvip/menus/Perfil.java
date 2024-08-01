package loopdospru.lvip.menus;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.simple.SimpleInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import com.henryfabio.minecraft.inventoryapi.viewer.configuration.ViewerConfiguration;
import com.henryfabio.minecraft.inventoryapi.viewer.impl.simple.SimpleViewer;
import loopdospru.lvip.config.General;
import loopdospru.lvip.config.Language;
import loopdospru.lvip.corja.profile.Profile;
import loopdospru.lvip.manager.ProfileManager;
import loopdospru.lvip.utils.ItemBuilder;
import loopdospru.lvip.utils.Tempo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

public class Perfil extends SimpleInventory {

    public Perfil() {
        super("lvip.profile", Language.profileTitle, Language.profileSize);
        configuration(configuration -> {
            configuration.secondUpdate(1); // Definir o tempo de atualização do inventário
        });
    }

    @Override
    protected void configureInventory(Viewer viewer, InventoryEditor editor) {
        Player viewerPlayer = viewer.getPlayer();
        Language.MenuItem menuItem = Language.menuItems.get("profileItem");
        InventoryItem perfil = InventoryItem.of(new ItemBuilder(new ItemStack(menuItem.getMaterial(), 1,(short)menuItem.getData()))
                .setName(ChatColor.translateAlternateColorCodes('&', menuItem.getName()))
                .setLore(translateLore(menuItem.getLore(), viewerPlayer))
                .toItemStack());
        editor.setItem(menuItem.getSlot(), perfil);
    }

    private List<String> translateLore(List<String> lore, Player player) {
        List<String> nova = new ArrayList<>();
        String identificador = (General.isUuid() ? String.valueOf(player.getUniqueId()) : player.getName());
        Profile profile = ProfileManager.loadProfile(identificador, General.isUuid());

        if (profile == null) {
            // Logar um erro ou tratar a ausência do perfil de forma adequada
            Bukkit.getLogger().warning("Perfil não encontrado para o identificador: " + identificador);
            // Retorne uma lista de lore padrão ou uma mensagem de erro
            return nova;
        }

        String timezone = General.getTimeZone();
        for (String line : lore) {
            nova.add(ChatColor.translateAlternateColorCodes('&', line
                    .replace("@tempo", Tempo.restam(profile.getAtual().getData(), profile.getAtual().getTempo(), timezone))
                    .replace("@vip", profile.getAtual().getPrefixo())));
        }
        return nova;
    }
}
