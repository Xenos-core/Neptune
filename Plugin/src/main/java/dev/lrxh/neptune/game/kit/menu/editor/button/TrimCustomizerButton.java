package dev.lrxh.neptune.game.kit.menu.editor.button;

import dev.lrxh.neptune.configs.impl.MenusLocale;
import dev.lrxh.neptune.feature.cosmetics.menu.armorTrims.ArmorTrimsMenu;
import dev.lrxh.neptune.utils.ItemBuilder;
import dev.lrxh.neptune.utils.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class TrimCustomizerButton extends Button {
    public static final String PERMISSION = "neptune.kiteditor.trims";

    public TrimCustomizerButton(int slot) {
        super(slot);
    }

    @Override
    public void onClick(ClickType type, Player player) {
        if (!player.hasPermission(PERMISSION)) return;
        // leaving the editor saves the layout first
        new ArmorTrimsMenu().open(player);
    }

    @Override
    public ItemStack getItemStack(Player player) {
        boolean unlocked = player.hasPermission(PERMISSION);
        return new ItemBuilder(Material.ARMS_UP_POTTERY_SHERD).name(MenusLocale.KIT_EDITOR_TRIM_NAME.getString())
                .lore(unlocked ? MenusLocale.KIT_EDITOR_TRIM_LORE.getStringList() : MenusLocale.KIT_EDITOR_TRIM_LOCKED_LORE.getStringList())
                .build();
    }
}
