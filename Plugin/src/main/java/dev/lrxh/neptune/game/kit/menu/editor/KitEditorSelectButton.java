package dev.lrxh.neptune.game.kit.menu.editor;

import dev.lrxh.neptune.configs.impl.MenusLocale;
import dev.lrxh.neptune.game.kit.Kit;
import dev.lrxh.neptune.utils.CC;
import dev.lrxh.neptune.utils.menu.Button;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class KitEditorSelectButton extends Button {
    private final Kit kit;

    public KitEditorSelectButton(int slot, Kit kit) {
        super(slot);
        this.kit = kit;
    }

    @Override
    public void onClick(ClickType type, Player player) {
        open(player, kit);
    }

    public static void open(Player player, Kit kit) {
        if (kit.isKitEditorBuilder()) {
            KitEditorSounds.play(player, KitEditorSounds.CLICK);
            new KitSlotsMenu(kit).open(player);
        } else {
            KitEditorSession.startLayout(player, kit);
        }
    }

    @Override
    public ItemStack getItemStack(Player player) {
        ItemStack item = kit.getIcon().clone();
        item.setAmount(1);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        meta.displayName(CC.color(MenusLocale.KIT_EDITOR_SELECT_KIT_NAME.getString().replace("<kit>", kit.getDisplayName())));
        List<Component> lore = new ArrayList<>();
        for (String line : (kit.isKitEditorBuilder() ? MenusLocale.KIT_EDITOR_SELECT_BUILDER_LORE : MenusLocale.KIT_EDITOR_SELECT_LAYOUT_LORE).getStringList()) {
            lore.add(CC.color(line));
        }
        meta.lore(lore);
        if (kit.isKitEditorBuilder()) meta.setEnchantmentGlintOverride(true);
        meta.addItemFlags(ItemFlag.values());
        item.setItemMeta(meta);
        return item;
    }
}
