package dev.lrxh.neptune.game.kit.menu.editor;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

@UtilityClass
public class KitEditorItems {

    /**
     * Builds an item by its minecraft name, returns null when the server doesn't have it
     * so newer items (spears etc) just get skipped on older versions.
     */
    public ItemStack item(String material, int amount, Object... enchants) {
        Material type = Material.matchMaterial(material);
        if (type == null || !type.isItem()) return null;

        ItemStack item = new ItemStack(type, Math.min(amount, type.getMaxStackSize()));
        for (int i = 0; i + 1 < enchants.length; i += 2) {
            Enchantment enchantment = Registry.ENCHANTMENT.get(NamespacedKey.minecraft((String) enchants[i]));
            if (enchantment == null) continue;
            item.addUnsafeEnchantment(enchantment, (Integer) enchants[i + 1]);
        }
        return item;
    }

    public ItemStack potion(String material, String potion) {
        ItemStack item = item(material, 1);
        if (item == null) return null;

        PotionType type = Registry.POTION.get(NamespacedKey.minecraft(potion));
        if (type == null) return null;

        PotionMeta meta = (PotionMeta) item.getItemMeta();
        meta.setBasePotionType(type);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack armor(String material) {
        if (material.endsWith("_BOOTS")) {
            return item(material, 1, "protection", 4, "feather_falling", 4, "unbreaking", 3, "mending", 1);
        }
        return item(material, 1, "protection", 4, "unbreaking", 3, "mending", 1);
    }
}
