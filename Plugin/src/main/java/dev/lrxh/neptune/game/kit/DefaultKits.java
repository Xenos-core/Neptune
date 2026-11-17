package dev.lrxh.neptune.game.kit;

import dev.lrxh.neptune.game.kit.impl.KitRule;
import dev.lrxh.neptune.game.kit.menu.editor.KitEditorItems;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

@UtilityClass
public class DefaultKits {

    /**
     * Crystal pvp kit that gets created when the server has no kits yet.
     */
    public Kit vanilla() {
        ItemStack[] contents = new ItemStack[41];
        contents[0] = KitEditorItems.item("NETHERITE_SWORD", 1, "sharpness", 5, "unbreaking", 3, "mending", 1);
        contents[1] = KitEditorItems.item("END_CRYSTAL", 64);
        contents[2] = KitEditorItems.item("OBSIDIAN", 64);
        contents[3] = KitEditorItems.item("RESPAWN_ANCHOR", 64);
        contents[4] = KitEditorItems.item("GLOWSTONE", 64);
        contents[5] = KitEditorItems.item("GOLDEN_APPLE", 64);
        contents[6] = KitEditorItems.item("ENDER_PEARL", 16);
        contents[7] = KitEditorItems.item("TOTEM_OF_UNDYING", 1);
        contents[8] = KitEditorItems.item("EXPERIENCE_BOTTLE", 64);
        contents[9] = KitEditorItems.item("END_CRYSTAL", 64);
        contents[10] = KitEditorItems.item("OBSIDIAN", 64);
        contents[11] = KitEditorItems.item("ENDER_PEARL", 16);
        contents[12] = KitEditorItems.item("NETHERITE_AXE", 1, "sharpness", 5, "efficiency", 5, "unbreaking", 3, "mending", 1);
        contents[13] = KitEditorItems.item("NETHERITE_PICKAXE", 1, "efficiency", 5, "unbreaking", 3, "mending", 1);
        for (int i = 14; i < 36; i++) contents[i] = KitEditorItems.item("TOTEM_OF_UNDYING", 1);
        contents[36] = KitEditorItems.armor("NETHERITE_BOOTS");
        contents[37] = KitEditorItems.armor("NETHERITE_LEGGINGS");
        contents[38] = KitEditorItems.armor("NETHERITE_CHESTPLATE");
        contents[39] = KitEditorItems.armor("NETHERITE_HELMET");
        contents[40] = KitEditorItems.item("TOTEM_OF_UNDYING", 1);

        Kit kit = new Kit("Vanilla", Arrays.asList(contents), new ItemStack(Material.END_CRYSTAL));
        kit.getRules().put(KitRule.ALLOW_KIT_EDITOR, true);
        kit.setKitEditorSlot(22);
        return kit;
    }
}
