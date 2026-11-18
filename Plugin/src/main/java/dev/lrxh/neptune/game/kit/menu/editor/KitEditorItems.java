package dev.lrxh.neptune.game.kit.menu.editor;

import dev.lrxh.neptune.Neptune;
import dev.lrxh.neptune.configs.impl.MenusLocale;
import dev.lrxh.neptune.utils.CC;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class KitEditorItems {
    private NamespacedKey placeholderKey;

    /**
     * Builds an item by its minecraft name, returns null when the server doesn't have it
     * so newer items (spears etc) just get skipped on older versions.
     */
    public ItemStack item(String material, int amount, Object... enchants) {
        Material type = Material.matchMaterial(material);
        if (type == null || !type.isItem()) return null;

        ItemStack item = new ItemStack(type, Math.max(1, Math.min(amount, type.getMaxStackSize())));
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
        if (type == null) return item;

        PotionMeta meta = (PotionMeta) item.getItemMeta();
        meta.setBasePotionType(type);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack armor(String material) {
        boolean netherite = material.startsWith("NETHERITE");
        if (material.endsWith("_BOOTS")) {
            return netherite
                    ? item(material, 1, "protection", 4, "feather_falling", 4, "unbreaking", 3, "mending", 1)
                    : item(material, 1, "protection", 4, "feather_falling", 4, "unbreaking", 3);
        }
        return netherite
                ? item(material, 1, "protection", 4, "unbreaking", 3, "mending", 1)
                : item(material, 1, "protection", 4, "unbreaking", 3);
    }

    public ItemStack named(ItemStack item, String name) {
        if (item == null) return null;
        ItemMeta meta = item.getItemMeta();
        meta.displayName(CC.color(name));
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Copy of the item with the given lore, enchantments and the rest stay visible.
     */
    public ItemStack display(ItemStack item, List<String> lore, boolean glint) {
        if (item == null || item.getType().isAir()) return new ItemStack(Material.AIR);
        ItemStack display = item.clone();
        ItemMeta meta = display.getItemMeta();
        if (meta == null) return display;

        List<Component> lines = new ArrayList<>();
        List<Component> existing = meta.lore();
        if (existing != null) lines.addAll(existing);
        for (String line : lore) lines.add(CC.color(line));
        if (!lines.isEmpty()) meta.lore(lines);
        if (glint) meta.setEnchantmentGlintOverride(true);
        display.setItemMeta(meta);
        return display;
    }

    public ItemStack button(Material material, String name, List<String> lore, boolean glint) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(CC.color(name));
        if (lore != null && !lore.isEmpty()) {
            List<Component> lines = new ArrayList<>();
            for (String line : lore) lines.add(CC.color(line));
            meta.lore(lines);
        }
        if (glint) meta.setEnchantmentGlintOverride(true);
        meta.addItemFlags(org.bukkit.inventory.ItemFlag.values());
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack button(Material material, String name, List<String> lore) {
        return button(material, name, lore, false);
    }

    public ItemStack pane(Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.empty());
        meta.setHideTooltip(true);
        item.setItemMeta(meta);
        return item;
    }

    private NamespacedKey placeholderKey() {
        if (placeholderKey == null) placeholderKey = new NamespacedKey(Neptune.get(), "kit_editor_placeholder");
        return placeholderKey;
    }

    /**
     * Item frame that marks an empty armor / offhand slot.
     */
    public ItemStack placeholder(String slotName) {
        ItemStack item = button(Material.ITEM_FRAME,
                MenusLocale.KIT_EDITOR_EMPTY_SLOT_NAME.getString().replace("<slot>", slotName),
                MenusLocale.KIT_EDITOR_EMPTY_SLOT_LORE.getStringList());
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(placeholderKey(), PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    public boolean isPlaceholder(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer().has(placeholderKey(), PersistentDataType.BYTE);
    }

    public boolean isEmpty(ItemStack item) {
        return item == null || item.getType().isAir() || isPlaceholder(item);
    }

    public boolean isArmor(ItemStack item) {
        if (item == null) return false;
        String name = item.getType().name();
        return name.endsWith("_HELMET") || name.endsWith("_CHESTPLATE") || name.endsWith("_LEGGINGS") || name.endsWith("_BOOTS");
    }

    public String strip(String text) {
        return text == null ? "" : text.replaceAll("(?i)&#[0-9a-f]{6}|&[0-9a-fk-or]|<[^>]+>", "");
    }

    public ItemStack[] copy(List<ItemStack> items, int size) {
        ItemStack[] contents = new ItemStack[size];
        if (items == null) return contents;
        for (int i = 0; i < Math.min(size, items.size()); i++) {
            ItemStack item = items.get(i);
            contents[i] = item == null ? null : item.clone();
        }
        return contents;
    }
}
