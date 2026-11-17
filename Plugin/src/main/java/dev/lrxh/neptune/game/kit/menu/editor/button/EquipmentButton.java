package dev.lrxh.neptune.game.kit.menu.editor.button;

import dev.lrxh.neptune.Neptune;
import dev.lrxh.neptune.configs.impl.MenusLocale;
import dev.lrxh.neptune.game.kit.Kit;
import dev.lrxh.neptune.game.kit.menu.editor.ItemEditorMenu;
import dev.lrxh.neptune.game.kit.menu.editor.RefillCategory;
import dev.lrxh.neptune.utils.CC;
import dev.lrxh.neptune.utils.ItemBuilder;
import dev.lrxh.neptune.utils.menu.Button;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EquipmentButton extends Button {
    private final EquipmentSlot equipmentSlot;
    private final Kit kit;
    private final int loadout;
    private final RefillCategory category;

    public EquipmentButton(int slot, EquipmentSlot equipmentSlot, Kit kit, int loadout, RefillCategory category) {
        super(slot);
        this.equipmentSlot = equipmentSlot;
        this.kit = kit;
        this.loadout = loadout;
        this.category = category;
    }

    private boolean fits(ItemStack item) {
        if (equipmentSlot == EquipmentSlot.OFF_HAND) return true;
        return item.getType().getEquipmentSlot() == equipmentSlot;
    }

    @Override
    public void onClick(ClickType type, Player player) {
        ItemStack cursor = player.getItemOnCursor();
        ItemStack piece = player.getInventory().getItem(equipmentSlot);
        boolean hasPiece = !piece.getType().isAir();

        if (!cursor.getType().isAir()) {
            if (!fits(cursor)) return;
            player.getInventory().setItem(equipmentSlot, cursor.clone());
            ItemStack old = hasPiece ? piece.clone() : null;
            Bukkit.getScheduler().runTask(Neptune.get(), () -> player.setItemOnCursor(old));
            return;
        }

        if (!hasPiece) return;

        if (type == ClickType.DROP || type == ClickType.CONTROL_DROP) {
            player.getInventory().setItem(equipmentSlot, null);
            return;
        }

        new ItemEditorMenu(kit, loadout, equipmentSlot, category).open(player);
    }

    @Override
    public ItemStack getItemStack(Player player) {
        ItemStack piece = player.getInventory().getItem(equipmentSlot);
        if (piece.getType().isAir()) {
            return new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).name(emptyName()).build();
        }

        ItemStack display = piece.clone();
        ItemMeta meta = display.getItemMeta();
        if (meta == null) return display;

        List<Component> lore = meta.lore() == null ? new ArrayList<>() : new ArrayList<>(meta.lore());
        for (String line : MenusLocale.KIT_EDITOR_ITEM_LORE.getStringList()) {
            lore.add(CC.color(line).decoration(TextDecoration.ITALIC, false));
        }
        meta.lore(lore);
        display.setItemMeta(meta);
        return display;
    }

    private String emptyName() {
        return switch (equipmentSlot) {
            case HEAD -> MenusLocale.CUSTOM_KIT_EDITOR_HELMET_NAME.getString();
            case CHEST -> MenusLocale.CUSTOM_KIT_EDITOR_CHESTPLATE_NAME.getString();
            case LEGS -> MenusLocale.CUSTOM_KIT_EDITOR_LEGGINGS_NAME.getString();
            case FEET -> MenusLocale.CUSTOM_KIT_EDITOR_BOOTS_NAME.getString();
            default -> MenusLocale.CUSTOM_KIT_EDITOR_OFFHAND_NAME.getString();
        };
    }
}
