package dev.lrxh.neptune.game.kit.menu.editor;

import dev.lrxh.neptune.configs.impl.MenusLocale;
import dev.lrxh.neptune.utils.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * "Trim Customizer" (arms up pottery sherd), locked behind neptune.kiteditor.trims.
 */
public class TrimButton extends Button {
    public static final String PERMISSION = "neptune.kiteditor.trims";

    private final Runnable open;

    public TrimButton(int slot, Runnable open) {
        super(slot);
        this.open = open;
    }

    @Override
    public void onClick(ClickType type, Player player) {
        if (!player.hasPermission(PERMISSION)) {
            KitEditorSounds.play(player, KitEditorSounds.LOCKED);
            return;
        }
        KitEditorSounds.play(player, KitEditorSounds.CLICK);
        open.run();
    }

    @Override
    public ItemStack getItemStack(Player player) {
        boolean unlocked = player.hasPermission(PERMISSION);
        return KitEditorItems.button(Material.ARMS_UP_POTTERY_SHERD, MenusLocale.KIT_EDITOR_TRIM_NAME.getString(),
                unlocked ? MenusLocale.KIT_EDITOR_TRIM_LORE.getStringList() : MenusLocale.KIT_EDITOR_TRIM_LOCKED_LORE.getStringList());
    }
}
