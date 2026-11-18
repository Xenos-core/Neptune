package dev.lrxh.neptune.game.kit.menu.editor;

import dev.lrxh.neptune.configs.impl.MenusLocale;
import dev.lrxh.neptune.game.kit.Kit;
import dev.lrxh.neptune.game.kit.KitService;
import dev.lrxh.neptune.game.kit.impl.KitRule;
import dev.lrxh.neptune.utils.menu.Button;
import dev.lrxh.neptune.utils.menu.Filter;
import dev.lrxh.neptune.utils.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * "Select Kit": page 0 holds the main kits (blue), page 1 the subtiers (green).
 */
public class KitEditorMenu extends Menu {
    private static final int[] INNER_ROW = {19, 20, 21, 23, 24, 25};

    private final int page;

    public KitEditorMenu() {
        this(0);
    }

    public KitEditorMenu(int page) {
        super(page == 0 ? MenusLocale.KIT_EDITOR_SELECT_TITLE.getString() : MenusLocale.KIT_EDITOR_SELECT_SUBTIER_TITLE.getString(),
                MenusLocale.KIT_EDITOR_SELECT_SIZE.getInt(), Filter.NONE);
        this.page = page;
    }

    @Override
    public boolean isClickSound() {
        return false;
    }

    @Override
    public List<Button> getButtons(Player player) {
        List<Button> buttons = new ArrayList<>();
        Set<Integer> used = new HashSet<>();

        for (Kit kit : KitService.get().kits) {
            if (kit.is(KitRule.HIDDEN) || !kit.is(KitRule.ALLOW_KIT_EDITOR)) continue;
            if (kit.getKitEditorPage() != page) continue;
            if (!used.add(kit.getKitEditorSlot())) continue;
            buttons.add(new KitEditorSelectButton(kit.getKitEditorSlot(), kit));
        }

        int customSlot = MenusLocale.KIT_EDITOR_SELECT_CUSTOM_SLOT.getInt();
        if (page == 0 && MenusLocale.KIT_EDITOR_SELECT_CUSTOM_ENABLED.getBoolean() && used.add(customSlot)) {
            buttons.add(new Button(customSlot) {
                @Override
                public void onClick(ClickType type, Player player) {
                    KitEditorSounds.play(player, KitEditorSounds.CLICK);
                    player.closeInventory();
                    player.performCommand("customkit");
                }

                @Override
                public ItemStack getItemStack(Player player) {
                    return KitEditorItems.button(Material.BOOK, MenusLocale.KIT_EDITOR_SELECT_CUSTOM_NAME.getString(),
                            MenusLocale.KIT_EDITOR_SELECT_CUSTOM_LORE.getStringList());
                }
            });
        }

        int pagesSlot = MenusLocale.KIT_EDITOR_SELECT_PAGES_SLOT.getInt();
        if (used.add(pagesSlot)) {
            buttons.add(new Button(pagesSlot) {
                @Override
                public void onClick(ClickType type, Player player) {
                    KitEditorSounds.play(player, KitEditorSounds.PAGE);
                    new KitEditorMenu(page == 0 ? 1 : 0).open(player);
                }

                @Override
                public ItemStack getItemStack(Player player) {
                    return KitEditorItems.button(page == 0 ? Material.MUSIC_DISC_WAIT : Material.MUSIC_DISC_FAR,
                            MenusLocale.KIT_EDITOR_SELECT_PAGES_NAME.getString(),
                            page == 0 ? MenusLocale.KIT_EDITOR_SELECT_PAGES_MAIN_LORE.getStringList()
                                    : MenusLocale.KIT_EDITOR_SELECT_PAGES_SUBTIER_LORE.getStringList());
                }
            });
        }

        Material border = page == 0 ? Material.BLUE_STAINED_GLASS_PANE : Material.GREEN_STAINED_GLASS_PANE;
        Material inner = page == 0 ? Material.CYAN_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE;
        int size = getSize();
        int rows = size / 9;
        for (int slot = 0; slot < size; slot++) {
            if (used.contains(slot)) continue;
            int row = slot / 9, col = slot % 9;
            boolean isBorder = row == 0 || row == rows - 1 || col == 0 || col == 8;
            boolean isInner = false;
            for (int innerSlot : INNER_ROW) if (innerSlot == slot) isInner = true;
            if (isBorder || isInner) buttons.add(pane(slot, isBorder ? border : inner));
        }
        return buttons;
    }

    static Button pane(int slot, Material material) {
        return new Button(slot) {
            @Override
            public ItemStack getItemStack(Player player) {
                return KitEditorItems.pane(material);
            }
        };
    }
}
