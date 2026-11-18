package dev.lrxh.neptune.game.kit.menu.editor;

import dev.lrxh.neptune.API;
import dev.lrxh.neptune.Neptune;
import dev.lrxh.neptune.configs.impl.MenusLocale;
import dev.lrxh.neptune.profile.impl.Profile;
import dev.lrxh.neptune.utils.menu.Button;
import dev.lrxh.neptune.utils.menu.Filter;
import dev.lrxh.neptune.utils.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * "Edit Layout" for normal kits. Row 1: trim, armor, offhand, back.
 * Rows 2-4 are the inventory, row 5 is the separator, row 6 is the hotbar.
 * Items can only be moved around, nothing gets added or removed.
 */
public class KitLayoutMenu extends Menu implements KitEditingMenu {
    static final int TRIM_SLOT = 0;
    static final int OFFHAND_SLOT = 6;
    static final int BACK_SLOT = 8;
    private static final int[] ARMOR_SLOTS = {2, 3, 4, 5};
    private static final int[] ARMOR_INDEX = {39, 38, 37, 36};

    private final KitEditorSession.Session session;

    public KitLayoutMenu(KitEditorSession.Session session) {
        super(MenusLocale.KIT_EDITOR_LAYOUT_TITLE.getString().replace("<kit>", session.getKit().getDisplayName()), 54, Filter.NONE);
        this.session = session;
        session.setLayoutMenu(this);
    }

    @Override
    public boolean isClickSound() {
        return false;
    }

    /**
     * Menu slot to player inventory index, -1 for slots that aren't part of the layout.
     */
    static int toIndex(int slot) {
        if (slot == OFFHAND_SLOT) return 40;
        if (slot >= 9 && slot <= 35) return slot;
        if (slot >= 45 && slot <= 53) return slot - 45;
        return -1;
    }

    @Override
    public List<Button> getButtons(Player player) {
        List<Button> buttons = new ArrayList<>();
        ItemStack[] contents = session.getContents();

        buttons.add(new TrimButton(TRIM_SLOT, () -> {
            sync(player.getOpenInventory().getTopInventory(), null);
            new TrimMenu(TrimMenu.Target.of(session.getContents(), () -> new KitLayoutMenu(session).open(player))).open(player);
        }));
        buttons.add(KitEditorMenu.pane(1, Material.GRAY_STAINED_GLASS_PANE));
        buttons.add(KitEditorMenu.pane(7, Material.GRAY_STAINED_GLASS_PANE));

        for (int i = 0; i < ARMOR_SLOTS.length; i++) {
            ItemStack armor = contents[ARMOR_INDEX[i]];
            buttons.add(new Button(ARMOR_SLOTS[i]) {
                @Override
                public ItemStack getItemStack(Player player) {
                    return armor == null ? new ItemStack(Material.AIR) : armor.clone();
                }
            });
        }

        buttons.add(new Button(BACK_SLOT) {
            @Override
            public void onClick(ClickType type, Player player) {
                KitEditorSounds.play(player, KitEditorSounds.BACK, KitEditorSounds.SAVE);
                sync(player.getOpenInventory().getTopInventory(), player.getItemOnCursor());
                player.setItemOnCursor(null);
                Profile profile = API.getProfile(player);
                KitEditorSession.finish(player, profile);
                new KitEditorMenu(session.getKit().getKitEditorPage()).open(player);
            }

            @Override
            public ItemStack getItemStack(Player player) {
                return KitEditorItems.button(Material.BARRIER, MenusLocale.KIT_EDITOR_BACK_NAME.getString(),
                        MenusLocale.KIT_EDITOR_BACK_SAVE_LORE.getStringList());
            }
        });

        for (int slot = 36; slot <= 44; slot++) {
            buttons.add(new Button(slot) {
                @Override
                public ItemStack getItemStack(Player player) {
                    return KitEditorItems.button(Material.CYAN_STAINED_GLASS_PANE,
                            MenusLocale.KIT_EDITOR_LAYOUT_SEPARATOR_NAME.getString(),
                            MenusLocale.KIT_EDITOR_LAYOUT_SEPARATOR_LORE.getStringList());
                }
            });
        }

        for (int slot = 0; slot < 54; slot++) {
            int index = toIndex(slot);
            if (index < 0) continue;
            ItemStack item = contents[index];
            boolean offhand = slot == OFFHAND_SLOT;
            buttons.add(new Button(slot, true) {
                @Override
                public ItemStack getItemStack(Player player) {
                    if (item == null || item.getType().isAir()) {
                        return offhand ? KitEditorItems.placeholder("Offhand") : new ItemStack(Material.AIR);
                    }
                    return item.clone();
                }
            });
        }

        return buttons;
    }

    @Override
    public boolean handleClick(Player player, InventoryClickEvent event) {
        Inventory top = event.getView().getTopInventory();
        if (event.getClickedInventory() == null || !event.getClickedInventory().equals(top)) {
            event.setCancelled(true);
            return true;
        }

        int slot = event.getSlot();
        ClickType click = event.getClick();

        if (toIndex(slot) < 0) {
            event.setCancelled(true);
            Button button = getButton(slot);
            if (button != null) button.onClick(click, player);
            return true;
        }

        // only plain left / right clicks, shift clicks and number keys would pull items out of the layout
        if (click != ClickType.LEFT && click != ClickType.RIGHT) {
            event.setCancelled(true);
            return true;
        }

        if (slot == OFFHAND_SLOT) {
            ItemStack cursor = event.getCursor();
            if (KitEditorItems.isPlaceholder(event.getCurrentItem())) {
                event.setCancelled(true);
                if (cursor.getType().isAir()) return true;
                Bukkit.getScheduler().runTask(Neptune.get(), () -> {
                    top.setItem(OFFHAND_SLOT, cursor.clone());
                    player.setItemOnCursor(null);
                });
                return true;
            }
            Bukkit.getScheduler().runTask(Neptune.get(), () -> {
                ItemStack now = top.getItem(OFFHAND_SLOT);
                if (now == null || now.getType().isAir()) top.setItem(OFFHAND_SLOT, KitEditorItems.placeholder("Offhand"));
            });
        }
        return true;
    }

    @Override
    public boolean handleDrag(Player player, InventoryDragEvent event) {
        for (int raw : event.getRawSlots()) {
            int index = raw < 54 ? toIndex(raw) : -1;
            if (index < 0 || raw == OFFHAND_SLOT) {
                event.setCancelled(true);
                return true;
            }
        }
        return true;
    }

    /**
     * Reads the menu back into the session, a held item goes into the first free slot.
     */
    public void sync(Inventory top, ItemStack cursor) {
        ItemStack[] contents = session.getContents();
        for (int slot = 0; slot < 54; slot++) {
            int index = toIndex(slot);
            if (index < 0) continue;
            ItemStack item = top.getItem(slot);
            contents[index] = KitEditorItems.isEmpty(item) ? null : item.clone();
        }

        if (cursor == null || cursor.getType().isAir()) return;
        for (int index = 0; index < 36; index++) {
            if (contents[index] == null) {
                contents[index] = cursor.clone();
                return;
            }
        }
    }

    public Inventory getMenuInventory() {
        return getInventory();
    }
}
