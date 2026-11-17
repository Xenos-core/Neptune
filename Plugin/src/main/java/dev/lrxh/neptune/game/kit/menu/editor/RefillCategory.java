package dev.lrxh.neptune.game.kit.menu.editor;

import dev.lrxh.neptune.configs.impl.MenusLocale;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@Getter
public enum RefillCategory {
    WEAPONRY(MenusLocale.KIT_EDITOR_WEAPONRY_NAME, Material.DIAMOND_SWORD, () -> Arrays.asList(
            KitEditorItems.armor("NETHERITE_HELMET"),
            KitEditorItems.armor("NETHERITE_CHESTPLATE"),
            KitEditorItems.armor("NETHERITE_LEGGINGS"),
            KitEditorItems.armor("NETHERITE_BOOTS"),
            KitEditorItems.item("SHIELD", 1, "unbreaking", 3, "mending", 1),
            KitEditorItems.item("ELYTRA", 1, "unbreaking", 3, "mending", 1),
            KitEditorItems.item("NETHERITE_SWORD", 1, "sharpness", 5, "unbreaking", 3, "mending", 1),
            KitEditorItems.item("NETHERITE_AXE", 1, "sharpness", 5, "efficiency", 5, "unbreaking", 3, "mending", 1),
            KitEditorItems.item("TRIDENT", 1, "loyalty", 3, "impaling", 5, "unbreaking", 3, "mending", 1),
            KitEditorItems.item("MACE", 1, "density", 5, "wind_burst", 1, "unbreaking", 3, "mending", 1),
            KitEditorItems.item("BOW", 1, "power", 5, "unbreaking", 3, "mending", 1),
            KitEditorItems.item("CROSSBOW", 1, "quick_charge", 3, "piercing", 4, "unbreaking", 3, "mending", 1),
            KitEditorItems.item("NETHERITE_PICKAXE", 1, "efficiency", 5, "unbreaking", 3, "mending", 1),
            KitEditorItems.item("NETHERITE_SHOVEL", 1, "efficiency", 5, "unbreaking", 3, "mending", 1),
            KitEditorItems.item("NETHERITE_SPEAR", 1, "unbreaking", 3, "mending", 1),
            KitEditorItems.item("FIREWORK_ROCKET", 64),
            KitEditorItems.item("ARROW", 64),
            KitEditorItems.item("SPECTRAL_ARROW", 64)
    )),
    UTILITIES(MenusLocale.KIT_EDITOR_UTILITIES_NAME, Material.ENDER_PEARL, () -> Arrays.asList(
            KitEditorItems.item("END_CRYSTAL", 64),
            KitEditorItems.item("OBSIDIAN", 64),
            KitEditorItems.item("ENDER_PEARL", 16),
            KitEditorItems.item("WATER_BUCKET", 1),
            KitEditorItems.item("LAVA_BUCKET", 1),
            KitEditorItems.item("FISHING_ROD", 1, "unbreaking", 3, "mending", 1),
            KitEditorItems.item("RESPAWN_ANCHOR", 64),
            KitEditorItems.item("GLOWSTONE", 64),
            KitEditorItems.item("EXPERIENCE_BOTTLE", 64),
            KitEditorItems.item("COBWEB", 64),
            KitEditorItems.item("WIND_CHARGE", 64),
            KitEditorItems.item("CRYING_OBSIDIAN", 64),
            KitEditorItems.item("TNT", 64),
            KitEditorItems.item("TNT_MINECART", 1),
            KitEditorItems.item("RAIL", 64),
            KitEditorItems.item("FLINT_AND_STEEL", 1),
            KitEditorItems.item("LADDER", 64),
            KitEditorItems.item("COBBLESTONE", 64),
            KitEditorItems.item("SLIME_BLOCK", 64),
            KitEditorItems.item("TOTEM_OF_UNDYING", 1)
    )),
    CONSUMABLES(MenusLocale.KIT_EDITOR_CONSUMABLES_NAME, Material.HONEY_BOTTLE, () -> Arrays.asList(
            KitEditorItems.item("GOLDEN_APPLE", 64),
            KitEditorItems.item("ENCHANTED_GOLDEN_APPLE", 64),
            KitEditorItems.item("GOLDEN_CARROT", 64),
            KitEditorItems.item("COOKED_BEEF", 64),
            KitEditorItems.item("CHORUS_FRUIT", 64),
            KitEditorItems.item("CAKE", 1),
            KitEditorItems.item("SWEET_BERRIES", 64),
            KitEditorItems.item("COOKED_PORKCHOP", 64),
            KitEditorItems.item("MUSHROOM_STEW", 1),
            KitEditorItems.item("HONEY_BOTTLE", 16),
            KitEditorItems.item("MILK_BUCKET", 1),
            KitEditorItems.potion("POTION", "strong_strength"),
            KitEditorItems.potion("POTION", "strong_swiftness"),
            KitEditorItems.potion("POTION", "long_fire_resistance"),
            KitEditorItems.potion("POTION", "long_slow_falling"),
            KitEditorItems.potion("POTION", "strong_turtle_master"),
            KitEditorItems.potion("SPLASH_POTION", "strong_strength"),
            KitEditorItems.potion("SPLASH_POTION", "strong_swiftness"),
            KitEditorItems.potion("SPLASH_POTION", "strong_healing")
    ));

    private final MenusLocale name;
    private final Material icon;
    private final Supplier<List<ItemStack>> supplier;
    private List<ItemStack> items;

    RefillCategory(MenusLocale name, Material icon, Supplier<List<ItemStack>> supplier) {
        this.name = name;
        this.icon = icon;
        this.supplier = supplier;
    }

    public List<ItemStack> getItems() {
        if (items == null) {
            items = new ArrayList<>(supplier.get().stream().filter(Objects::nonNull).toList());
        }
        return items;
    }
}
