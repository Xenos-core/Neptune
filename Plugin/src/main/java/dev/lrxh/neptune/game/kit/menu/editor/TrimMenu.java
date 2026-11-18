package dev.lrxh.neptune.game.kit.menu.editor;

import dev.lrxh.neptune.configs.impl.MenusLocale;
import dev.lrxh.neptune.utils.menu.Button;
import dev.lrxh.neptune.utils.menu.Filter;
import dev.lrxh.neptune.utils.menu.Menu;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.IntFunction;

/**
 * Pick a trim pattern, then a material. Applies to one armor piece (item editor)
 * or to the whole armor set (trim customizer).
 */
public class TrimMenu extends Menu implements KitEditingMenu {
    private static final int[] SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };
    private static final Map<String, String> MATERIAL_ITEMS = Map.ofEntries(
            Map.entry("quartz", "QUARTZ"), Map.entry("iron", "IRON_INGOT"), Map.entry("netherite", "NETHERITE_INGOT"),
            Map.entry("redstone", "REDSTONE"), Map.entry("copper", "COPPER_INGOT"), Map.entry("gold", "GOLD_INGOT"),
            Map.entry("emerald", "EMERALD"), Map.entry("diamond", "DIAMOND"), Map.entry("lapis", "LAPIS_LAZULI"),
            Map.entry("amethyst", "AMETHYST_SHARD"), Map.entry("resin", "RESIN_BRICK")
    );

    private final Target target;
    private final TrimPattern pattern;

    public TrimMenu(Target target) {
        this(target, null);
    }

    private TrimMenu(Target target, TrimPattern pattern) {
        super(pattern == null ? MenusLocale.KIT_EDITOR_TRIM_PATTERN_TITLE.getString() : MenusLocale.KIT_EDITOR_TRIM_MATERIAL_TITLE.getString(),
                54, Filter.NONE);
        this.target = target;
        this.pattern = pattern;
    }

    @Override
    public boolean isClickSound() {
        return false;
    }

    @Override
    public List<Button> getButtons(Player player) {
        List<Button> buttons = new ArrayList<>();
        for (int slot = 0; slot < 54; slot++) buttons.add(KitEditorMenu.pane(slot, Material.GRAY_STAINED_GLASS_PANE));

        int i = 0;
        if (pattern == null) {
            for (TrimPattern trimPattern : Registry.TRIM_PATTERN) {
                if (i >= SLOTS.length) break;
                String key = trimPattern.getKey().getKey();
                Material icon = Material.matchMaterial(key + "_armor_trim_smithing_template");
                if (icon == null) icon = Material.PAPER;
                Material shown = icon;
                buttons.add(new Button(SLOTS[i++]) {
                    @Override
                    public void onClick(ClickType type, Player player) {
                        KitEditorSounds.play(player, KitEditorSounds.CLICK);
                        new TrimMenu(target, trimPattern).open(player);
                    }

                    @Override
                    public ItemStack getItemStack(Player player) {
                        return KitEditorItems.button(shown, "&d" + pretty(key), List.of());
                    }
                });
            }
        } else {
            for (TrimMaterial trimMaterial : Registry.TRIM_MATERIAL) {
                if (i >= SLOTS.length) break;
                String key = trimMaterial.getKey().getKey();
                Material icon = Material.matchMaterial(MATERIAL_ITEMS.getOrDefault(key, key));
                if (icon == null) icon = Material.PAPER;
                Material shown = icon;
                buttons.add(new Button(SLOTS[i++]) {
                    @Override
                    public void onClick(ClickType type, Player player) {
                        KitEditorSounds.play(player, KitEditorSounds.CLICK);
                        target.apply(new ArmorTrim(trimMaterial, pattern));
                        target.back.run();
                    }

                    @Override
                    public ItemStack getItemStack(Player player) {
                        return KitEditorItems.button(shown, "&d" + pretty(key), List.of());
                    }
                });
            }
        }

        buttons.add(new Button(49) {
            @Override
            public void onClick(ClickType type, Player player) {
                KitEditorSounds.play(player, KitEditorSounds.BACK);
                target.apply(null);
                target.back.run();
            }

            @Override
            public ItemStack getItemStack(Player player) {
                return KitEditorItems.button(Material.LAVA_BUCKET, MenusLocale.KIT_EDITOR_TRIM_REMOVE_NAME.getString(), List.of());
            }
        });

        buttons.add(new Button(53) {
            @Override
            public void onClick(ClickType type, Player player) {
                KitEditorSounds.play(player, KitEditorSounds.BACK);
                if (pattern == null) target.back.run();
                else new TrimMenu(target).open(player);
            }

            @Override
            public ItemStack getItemStack(Player player) {
                return KitEditorItems.button(Material.BARRIER, MenusLocale.KIT_EDITOR_BACK_NAME.getString(), List.of());
            }
        });
        return buttons;
    }

    private static String pretty(String key) {
        StringBuilder builder = new StringBuilder();
        for (String word : key.split("_")) {
            if (word.isEmpty()) continue;
            if (!builder.isEmpty()) builder.append(' ');
            builder.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
        }
        return builder.toString();
    }

    /**
     * Where the trimmed armor lives: the layout copy or the player's own inventory.
     */
    public record Target(IntFunction<ItemStack> getter, BiConsumer<Integer, ItemStack> setter, int[] indexes, Runnable back) {
        private static final int[] ARMOR = {39, 38, 37, 36};

        public static Target of(ItemStack[] contents, Runnable back) {
            return new Target(i -> contents[i], (i, item) -> contents[i] = item, ARMOR, back);
        }

        public static Target player(PlayerInventory inventory, int[] indexes, Runnable back) {
            return new Target(inventory::getItem, inventory::setItem, indexes, back);
        }

        public static Target armor(PlayerInventory inventory, Runnable back) {
            return player(inventory, ARMOR, back);
        }

        void apply(ArmorTrim trim) {
            for (int index : indexes) {
                ItemStack item = getter.apply(index);
                if (item == null || !(item.getItemMeta() instanceof ArmorMeta meta)) continue;
                meta.setTrim(trim);
                ItemStack trimmed = item.clone();
                trimmed.setItemMeta(meta);
                setter.accept(index, trimmed);
            }
        }
    }
}
