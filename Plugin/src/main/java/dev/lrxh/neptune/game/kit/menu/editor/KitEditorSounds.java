package dev.lrxh.neptune.game.kit.menu.editor;

import lombok.experimental.UtilityClass;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

/**
 * The sounds the kit editor menus play, same ones as the reference server.
 */
@UtilityClass
public class KitEditorSounds {
    public final String CLICK = "block.wooden_button.click_on";
    public final String BACK = "block.cherry_wood_button.click_off";
    public final String SAVE = "block.deadbush.idle";
    public final String LOCKED = "block.vault.close_shutter";
    public final String TAB = "block.wooden_pressure_plate.click_on";
    public final String PAGE = "item.book.page_turn";

    public void play(Player player, String... sounds) {
        for (String sound : sounds) {
            player.playSound(player.getLocation(), "minecraft:" + sound, SoundCategory.MASTER, 1.0f, 1.0f);
        }
    }
}
