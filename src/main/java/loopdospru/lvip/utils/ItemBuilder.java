package loopdospru.lvip.utils;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemBuilder {
    private ItemStack is;

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public ItemBuilder(ItemStack itemStack) {
        this.is = itemStack;
    }

    public ItemBuilder(Material material, int amount) {
        is = new ItemStack(material, amount);
    }

    public ItemBuilder(Material material, int amount, short durability) {
        is = new ItemStack(material, amount);
        setDurability(durability);
    }

    public ItemBuilder clone() {
        return new ItemBuilder(is.clone());
    }

    public ItemBuilder setDurability(short durability) {
        if (is.getType().getMaxDurability() > 0) {
            is.setDurability(durability);
        }
        return this;
    }

    public ItemBuilder setName(String name) {
        ItemMeta meta = is.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            is.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder addUnsafeEnchantment(Enchantment enchantment, int level) {
        is.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        is.removeEnchantment(enchantment);
        return this;
    }

    public ItemBuilder setSkullOwner(String owner) {
        if (is.getType() == Material.SKULL_ITEM) {
            SkullMeta meta = (SkullMeta) is.getItemMeta();
            if (meta != null) {
                meta.setOwner(owner);
                is.setItemMeta(meta);
            }
        }
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        ItemMeta meta = is.getItemMeta();
        if (meta != null) {
            meta.addEnchant(enchantment, level, true);
            is.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
        ItemMeta meta = is.getItemMeta();
        if (meta != null) {
            // Adicionar encantamentos individualmente para compatibilidade com versões antigas
            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                meta.addEnchant(entry.getKey(), entry.getValue(), true);
            }
            is.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder setInfinityDurability() {
        setDurability(Short.MAX_VALUE);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        return setLore(Arrays.asList(lore));
    }

    public ItemBuilder setLore(List<String> lore) {
        ItemMeta meta = is.getItemMeta();
        if (meta != null) {
            meta.setLore(lore);
            is.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder removeLoreLine(String line) {
        ItemMeta meta = is.getItemMeta();
        if (meta != null && meta.hasLore()) {
            List<String> lore = new ArrayList<>(meta.getLore());
            lore.remove(line);
            meta.setLore(lore);
            is.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder removeLoreLine(int index) {
        ItemMeta meta = is.getItemMeta();
        if (meta != null && meta.hasLore()) {
            List<String> lore = new ArrayList<>(meta.getLore());
            if (index >= 0 && index < lore.size()) {
                lore.remove(index);
                meta.setLore(lore);
                is.setItemMeta(meta);
            }
        }
        return this;
    }

    public ItemBuilder addLoreLine(String line) {
        ItemMeta meta = is.getItemMeta();
        if (meta != null) {
            List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
            lore.add(line);
            meta.setLore(lore);
            is.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder addLoreLine(String line, int pos) {
        ItemMeta meta = is.getItemMeta();
        if (meta != null && meta.hasLore()) {
            List<String> lore = new ArrayList<>(meta.getLore());
            if (pos >= 0 && pos < lore.size()) {
                lore.set(pos, line);
                meta.setLore(lore);
                is.setItemMeta(meta);
            }
        }
        return this;
    }

    public ItemBuilder setLeatherArmorColor(Color color) {
        if (is.getType().name().endsWith("_LEGGINGS") || is.getType().name().endsWith("_CHESTPLATE") ||
                is.getType().name().endsWith("_HELMET") || is.getType().name().endsWith("_BOOTS")) {
            LeatherArmorMeta meta = (LeatherArmorMeta) is.getItemMeta();
            if (meta != null) {
                meta.setColor(color);
                is.setItemMeta(meta);
            }
        }
        return this;
    }

    public ItemStack toItemStack() {
        return is;
    }

    private Material getMaterial(String materialName) {
        try {
            return Material.valueOf(materialName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Material.STONE; // Retorno padrão
        }
    }
}
