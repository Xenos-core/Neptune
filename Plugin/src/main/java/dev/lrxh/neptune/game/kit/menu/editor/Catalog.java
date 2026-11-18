package dev.lrxh.neptune.game.kit.menu.editor;

import dev.lrxh.neptune.configs.impl.MenusLocale;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * The refill tabs of the kit builder, laid out the same as the reference menus.
 * Page 1 has weaponry, utilities and consumables, page 2 has miscellaneous.
 */
@Getter
public enum Catalog {
    WEAPONRY(0, 17, MenusLocale.KIT_EDITOR_BUILDER_WEAPONRY, Material.DIAMOND_SWORD, c -> {
        c.gold(10, KitEditorItems.armor("NETHERITE_HELMET"));
        c.gold(11, KitEditorItems.armor("NETHERITE_CHESTPLATE"));
        c.gold(12, KitEditorItems.armor("NETHERITE_LEGGINGS"));
        c.gold(13, KitEditorItems.armor("NETHERITE_BOOTS"));
        c.gold(14, KitEditorItems.item("SHIELD", 1, "unbreaking", 3, "mending", 1));
        c.gray(15, KitEditorItems.item("ELYTRA", 1, "unbreaking", 3, "mending", 1));
        c.gold(19, KitEditorItems.item("NETHERITE_SWORD", 1, "sharpness", 5, "unbreaking", 3, "mending", 1));
        c.gold(20, KitEditorItems.item("NETHERITE_AXE", 1, "sharpness", 5, "efficiency", 5, "unbreaking", 3, "mending", 1));
        c.gold(21, KitEditorItems.item("TRIDENT", 1, "loyalty", 3, "impaling", 5, "unbreaking", 3, "mending", 1));
        c.gold(22, KitEditorItems.item("MACE", 1, "density", 5, "wind_burst", 1, "unbreaking", 3, "mending", 1));
        c.gold(23, KitEditorItems.item("BOW", 1, "power", 5, "unbreaking", 3, "mending", 1));
        c.gold(24, KitEditorItems.item("CROSSBOW", 1, "quick_charge", 3, "unbreaking", 3, "mending", 1));
        c.gold(28, KitEditorItems.item("NETHERITE_PICKAXE", 1, "efficiency", 5, "unbreaking", 3, "mending", 1));
        c.gold(29, KitEditorItems.item("NETHERITE_SHOVEL", 1, "efficiency", 5, "unbreaking", 3, "mending", 1));
        c.gold(30, KitEditorItems.item("NETHERITE_SPEAR", 1, "unbreaking", 3, "mending", 1));
        c.gray(31, KitEditorItems.item("FIREWORK_ROCKET", 64));
        c.plain(32, KitEditorItems.item("ARROW", 64));
        c.gray(33, KitEditorItems.potion("TIPPED_ARROW", "slow_falling"));
    }),
    UTILITIES(0, 26, MenusLocale.KIT_EDITOR_BUILDER_UTILITIES, Material.ENDER_PEARL, c -> {
        c.add(10, KitEditorItems.item("END_CRYSTAL", 64), null, true);
        c.plain(11, KitEditorItems.item("OBSIDIAN", 64));
        c.plain(12, KitEditorItems.item("ENDER_PEARL", 16));
        c.add(13, KitEditorItems.item("BUCKET", 16), MenusLocale.KIT_EDITOR_BUILDER_TYPE_LORE, true);
        c.plain(14, KitEditorItems.item("OAK_BOAT", 1));
        c.gray(15, KitEditorItems.item("FISHING_ROD", 1, "unbreaking", 3, "mending", 1));
        c.plain(19, KitEditorItems.item("RESPAWN_ANCHOR", 64));
        c.plain(20, KitEditorItems.item("GLOWSTONE", 64));
        c.add(21, KitEditorItems.item("EXPERIENCE_BOTTLE", 64), null, true);
        c.plain(22, KitEditorItems.item("COBWEB", 64));
        c.plain(23, KitEditorItems.item("WIND_CHARGE", 64));
        c.gray(24, KitEditorItems.item("FLINT_AND_STEEL", 1, "unbreaking", 3, "mending", 1));
        c.plain(28, KitEditorItems.item("TNT_MINECART", 1));
        c.plain(29, KitEditorItems.item("TNT", 64));
        c.plain(30, KitEditorItems.item("RAIL", 64));
        c.plain(31, KitEditorItems.item("COBBLESTONE", 64));
        c.plain(32, KitEditorItems.item("SLIME_BLOCK", 64));
        c.plain(33, KitEditorItems.item("TOTEM_OF_UNDYING", 1));
    }),
    CONSUMABLES(0, 35, MenusLocale.KIT_EDITOR_BUILDER_CONSUMABLES, Material.HONEY_BOTTLE, c -> {
        c.plain(10, KitEditorItems.item("GOLDEN_APPLE", 64));
        c.plain(11, KitEditorItems.item("APPLE", 64));
        c.plain(12, KitEditorItems.item("GOLDEN_CARROT", 64));
        c.plain(13, KitEditorItems.item("COOKED_BEEF", 64));
        c.plain(14, KitEditorItems.item("CHORUS_FRUIT", 64));
        c.plain(15, KitEditorItems.item("CAKE", 1));
        c.plain(19, KitEditorItems.item("PUFFERFISH", 64));
        c.plain(20, KitEditorItems.item("SWEET_BERRIES", 64));
        c.plain(21, KitEditorItems.item("ROTTEN_FLESH", 64));
        c.plain(22, KitEditorItems.item("SUSPICIOUS_STEW", 1));
        c.plain(23, KitEditorItems.item("HONEY_BOTTLE", 16));
        c.plain(24, KitEditorItems.item("MILK_BUCKET", 1));
        c.gray(31, KitEditorItems.potion("LINGERING_POTION", "strong_swiftness"));
        c.gray(32, KitEditorItems.potion("SPLASH_POTION", "strong_swiftness"));
        c.gray(33, KitEditorItems.potion("POTION", "strong_swiftness"));
    }),
    MISCELLANEOUS(1, 17, MenusLocale.KIT_EDITOR_BUILDER_MISCELLANEOUS, Material.PAPER, c -> {
        c.gray(10, KitEditorItems.item("OAK_LOG", 64));
        c.plain(11, KitEditorItems.item("HONEY_BLOCK", 64));
        c.gray(12, KitEditorItems.item("ICE", 64));
        c.plain(13, KitEditorItems.item("SPONGE", 64));
        c.plain(14, KitEditorItems.item("SAND", 64));
        c.plain(15, KitEditorItems.item("GRAVEL", 64));
        c.plain(19, KitEditorItems.item("CRAFTING_TABLE", 64));
        c.plain(20, KitEditorItems.item("ANVIL", 64));
        c.gray(21, KitEditorItems.item("ENDER_CHEST", 64));
        c.plain(22, KitEditorItems.item("REDSTONE_BLOCK", 64));
        c.gray(23, KitEditorItems.item("SHULKER_BOX", 1));
        c.plain(24, KitEditorItems.item("POINTED_DRIPSTONE", 64));
        c.plain(28, KitEditorItems.item("SCAFFOLDING", 64));
        c.plain(29, KitEditorItems.item("BREEZE_ROD", 64));
        c.plain(30, KitEditorItems.item("ARMOR_STAND", 16));
        c.gray(31, KitEditorItems.item("SHEARS", 1, "unbreaking", 3, "mending", 1));
    });

    public static final int[] GRID = {10, 11, 12, 13, 14, 15, 19, 20, 21, 22, 23, 24, 28, 29, 30, 31, 32, 33};

    private final int page;
    private final int tabSlot;
    private final MenusLocale name;
    private final Material icon;
    private final Consumer<Builder> filler;
    private Map<Integer, Entry> entries;

    Catalog(int page, int tabSlot, MenusLocale name, Material icon, Consumer<Builder> filler) {
        this.page = page;
        this.tabSlot = tabSlot;
        this.name = name;
        this.icon = icon;
        this.filler = filler;
    }

    public Map<Integer, Entry> getEntries() {
        if (entries == null) {
            Builder builder = new Builder();
            filler.accept(builder);
            entries = builder.entries;
        }
        return entries;
    }

    public static List<Catalog> onPage(int page) {
        List<Catalog> list = new ArrayList<>();
        for (Catalog catalog : values()) {
            if (catalog.page == page) list.add(catalog);
        }
        return list;
    }

    /**
     * An item in the refill. The item is what the player gets, the display is what the menu shows.
     */
    public record Entry(ItemStack item, ItemStack display) {
    }

    public static class Builder {
        private final Map<Integer, Entry> entries = new LinkedHashMap<>();

        void add(int slot, ItemStack item, MenusLocale lore, boolean glint) {
            if (item == null) return;
            ItemStack shown = item.clone();
            shown.setAmount(1);
            ItemStack display = KitEditorItems.display(shown, lore == null ? List.of() : lore.getStringList(), glint);
            entries.put(slot, new Entry(item, display));
        }

        void gold(int slot, ItemStack item) {
            add(slot, item, MenusLocale.KIT_EDITOR_BUILDER_EDITABLE_LORE, false);
        }

        void gray(int slot, ItemStack item) {
            add(slot, item, MenusLocale.KIT_EDITOR_BUILDER_TYPE_LORE, false);
        }

        void plain(int slot, ItemStack item) {
            add(slot, item, null, false);
        }
    }
}
