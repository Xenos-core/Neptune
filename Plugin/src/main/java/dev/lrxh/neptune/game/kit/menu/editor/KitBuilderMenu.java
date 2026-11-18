package dev.lrxh.neptune.game.kit.menu.editor;

import dev.lrxh.neptune.API;
import dev.lrxh.neptune.Neptune;
import dev.lrxh.neptune.configs.impl.MenusLocale;
import dev.lrxh.neptune.utils.menu.Button;
import dev.lrxh.neptune.utils.menu.Filter;
import dev.lrxh.neptune.utils.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;

/**
 * "Kit Editor" for builder kits. The refill sits on top, the player's own inventory is the kit,
 * the bottom row holds the trim customizer, the armor, the offhand and the back button.
 */
public class KitBuilderMenu extends Menu implements KitEditingMenu {
    private static final int PAGE_SLOT = 8;
    private static final int TRIM_SLOT = 45;
    private static final int[] ARMOR_SLOTS = {47, 48, 49, 50};
    private static final int[] ARMOR_INDEX = {39, 38, 37, 36};
    private static final String[] ARMOR_NAMES = {"Helmet", "Chestplate", "Leggings", "Boots"};
    private static final EquipmentSlot[] ARMOR_EQUIPMENT = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    private static final int OFFHAND_SLOT = 51;
    private static final int OFFHAND_INDEX = 40;
    private static final int BACK_SLOT = 53;

    private final KitEditorSession.Session session;

    public KitBuilderMenu(KitEditorSession.Session session) {
        super(54, Filter.NONE);
        this.session = session;
    }

    @Override
    public net.kyori.adventure.text.Component getTitle(Player player) {
        return dev.lrxh.neptune.utils.CC.color(MenusLocale.KIT_EDITOR_BUILDER_TITLE.getString().replace("<name>", loadoutName(player)));
    }

    private String loadoutName(Player player) {
        String name = API.getProfile(player).getGameData().get(session.getKit()).getLoadoutName(session.getLoadout());
        return name == null ? MenusLocale.KIT_EDITOR_SLOTS_DEFAULT_NAME.getString().replace("<slot>", String.valueOf(session.getLoadout() + 1)) : name;
    }

    @Override
    public boolean isClickSound() {
        return false;
    }

    @Override
    public List<Button> getButtons(Player player) {
        List<Button> buttons = new ArrayList<>();
        for (int slot = 0; slot < 54; slot++) buttons.add(KitEditorMenu.pane(slot, Material.GRAY_STAINED_GLASS_PANE));

        Catalog category = session.getCategory();
        int page = category.getPage();

        for (int slot : Catalog.GRID) {
            Catalog.Entry entry = category.getEntries().get(slot);
            if (entry == null) continue;
            buttons.add(display(slot, entry.display()));
        }

        for (Catalog tab : Catalog.onPage(page)) {
            boolean selected = tab == category;
            buttons.add(display(tab.getTabSlot(), KitEditorItems.button(tab.getIcon(), tab.getName().getString(), List.of(), selected)));
        }

        boolean hasNext = !Catalog.onPage(page + 1).isEmpty();
        buttons.add(display(PAGE_SLOT, KitEditorItems.button(Material.BLACK_BANNER,
                hasNext ? MenusLocale.KIT_EDITOR_BUILDER_NEXT_PAGE.getString() : MenusLocale.KIT_EDITOR_BUILDER_PREVIOUS_PAGE.getString(), List.of())));

        buttons.add(new TrimButton(TRIM_SLOT, () -> new TrimMenu(TrimMenu.Target.armor(player.getInventory(),
                () -> new KitBuilderMenu(session).open(player))).open(player)));

        PlayerInventory inventory = player.getInventory();
        for (int i = 0; i < ARMOR_SLOTS.length; i++) {
            ItemStack armor = inventory.getItem(ARMOR_INDEX[i]);
            buttons.add(display(ARMOR_SLOTS[i], KitEditorItems.isEmpty(armor)
                    ? KitEditorItems.placeholder(ARMOR_NAMES[i])
                    : KitEditorItems.display(armor, MenusLocale.KIT_EDITOR_BUILDER_ARMOR_LORE.getStringList(), false)));
        }

        ItemStack offhand = inventory.getItem(OFFHAND_INDEX);
        buttons.add(display(OFFHAND_SLOT, KitEditorItems.isEmpty(offhand)
                ? KitEditorItems.placeholder("Offhand")
                : KitEditorItems.display(offhand, MenusLocale.KIT_EDITOR_BUILDER_OFFHAND_LORE.getStringList(), false)));

        buttons.add(display(BACK_SLOT, KitEditorItems.button(Material.BARRIER, MenusLocale.KIT_EDITOR_BACK_NAME.getString(),
                MenusLocale.KIT_EDITOR_BACK_SAVE_LORE.getStringList())));
        return buttons;
    }

    private static Button display(int slot, ItemStack item) {
        return new Button(slot) {
            @Override
            public ItemStack getItemStack(Player player) {
                return item;
            }
        };
    }

    @Override
    public boolean handleClick(Player player, InventoryClickEvent event) {
        Inventory clicked = event.getClickedInventory();
        ClickType click = event.getClick();
        PlayerInventory inventory = player.getInventory();

        // clicking outside the window throws the held item away
        if (clicked == null) {
            event.setCancelled(true);
            later(player, () -> player.setItemOnCursor(null));
            return true;
        }

        if (!clicked.equals(event.getView().getTopInventory())) {
            int index = event.getSlot();
            ItemStack current = event.getCurrentItem();
            switch (click) {
                case SHIFT_LEFT, SHIFT_RIGHT -> {
                    event.setCancelled(true);
                    if (!KitEditorItems.isEmpty(current)) {
                        KitEditorSounds.play(player, KitEditorSounds.CLICK);
                        new ItemEditorMenu(session, index).open(player);
                    }
                }
                case DROP, CONTROL_DROP -> {
                    event.setCancelled(true);
                    later(player, () -> inventory.setItem(index, null));
                }
                case DOUBLE_CLICK -> event.setCancelled(true);
                default -> {
                }
            }
            return true;
        }

        event.setCancelled(true);
        int slot = event.getSlot();
        ItemStack cursor = event.getCursor().clone();
        boolean holding = !cursor.getType().isAir();
        Catalog category = session.getCategory();

        Catalog.Entry entry = category.getEntries().get(slot);
        if (entry != null && isGrid(slot)) {
            if (holding) {
                later(player, () -> player.setItemOnCursor(null));
                return true;
            }
            ItemStack give = entry.item().clone();
            if (click.isShiftClick()) {
                inventory.addItem(give);
                return true;
            }
            if (click.isRightClick()) give.setAmount(1);
            later(player, () -> player.setItemOnCursor(give));
            return true;
        }

        for (Catalog tab : Catalog.onPage(category.getPage())) {
            if (tab.getTabSlot() == slot && tab != category) {
                KitEditorSounds.play(player, KitEditorSounds.TAB);
                session.setCategory(tab);
                update(player);
                return true;
            }
        }

        if (slot == PAGE_SLOT) {
            int page = category.getPage();
            List<Catalog> next = Catalog.onPage(page + 1);
            List<Catalog> target = next.isEmpty() ? Catalog.onPage(Math.max(0, page - 1)) : next;
            if (!target.isEmpty()) {
                KitEditorSounds.play(player, KitEditorSounds.TAB);
                session.setCategory(target.getFirst());
                update(player);
            }
            return true;
        }

        if (slot == TRIM_SLOT) {
            Button button = getButton(slot);
            if (button != null) button.onClick(click, player);
            return true;
        }

        for (int i = 0; i < ARMOR_SLOTS.length; i++) {
            if (ARMOR_SLOTS[i] != slot) continue;
            int index = ARMOR_INDEX[i];
            ItemStack armor = inventory.getItem(index);

            if (holding) {
                if (cursor.getType().getEquipmentSlot() != ARMOR_EQUIPMENT[i]) return true;
                KitEditorSounds.play(player, KitEditorSounds.CLICK);
                ItemStack put = cursor.clone();
                put.setAmount(1);
                ItemStack rest = cursor.clone();
                rest.setAmount(cursor.getAmount() - 1);
                later(player, () -> {
                    inventory.setItem(index, put);
                    if (rest.getAmount() > 0) player.setItemOnCursor(rest);
                    else player.setItemOnCursor(KitEditorItems.isEmpty(armor) ? null : armor);
                });
                return true;
            }
            if (KitEditorItems.isEmpty(armor)) return true;
            if (click == ClickType.DROP || click == ClickType.CONTROL_DROP) {
                later(player, () -> inventory.setItem(index, null));
                return true;
            }
            KitEditorSounds.play(player, KitEditorSounds.CLICK);
            new ItemEditorMenu(session, index).open(player);
            return true;
        }

        if (slot == OFFHAND_SLOT) {
            ItemStack offhand = inventory.getItem(OFFHAND_INDEX);
            if (holding) {
                KitEditorSounds.play(player, KitEditorSounds.CLICK);
                later(player, () -> {
                    inventory.setItem(OFFHAND_INDEX, cursor);
                    player.setItemOnCursor(KitEditorItems.isEmpty(offhand) ? null : offhand);
                });
                return true;
            }
            if (KitEditorItems.isEmpty(offhand)) return true;
            switch (click) {
                case SHIFT_LEFT, SHIFT_RIGHT -> {
                    KitEditorSounds.play(player, KitEditorSounds.CLICK);
                    new ItemEditorMenu(session, OFFHAND_INDEX).open(player);
                }
                case RIGHT -> later(player, () -> player.setItemOnCursor(offhand.clone()));
                case DROP, CONTROL_DROP -> later(player, () -> inventory.setItem(OFFHAND_INDEX, null));
                case LEFT -> later(player, () -> {
                    inventory.setItem(OFFHAND_INDEX, null);
                    player.setItemOnCursor(offhand);
                });
                default -> {
                }
            }
            return true;
        }

        if (slot == BACK_SLOT) {
            KitEditorSounds.play(player, KitEditorSounds.BACK, KitEditorSounds.SAVE);
            if (holding) {
                player.setItemOnCursor(null);
                inventory.addItem(cursor);
            }
            KitEditorSession.finish(player, API.getProfile(player));
            new KitSlotsMenu(session.getKit()).open(player);
            return true;
        }
        return true;
    }

    @Override
    public boolean handleDrag(Player player, InventoryDragEvent event) {
        int topSize = event.getView().getTopInventory().getSize();
        for (int raw : event.getRawSlots()) {
            if (raw < topSize) {
                event.setCancelled(true);
                return true;
            }
        }
        return true;
    }

    private static boolean isGrid(int slot) {
        for (int grid : Catalog.GRID) if (grid == slot) return true;
        return false;
    }

    /**
     * The click event resets the cursor and slots, so changes happen a tick later.
     */
    private void later(Player player, Runnable runnable) {
        Bukkit.getScheduler().runTask(Neptune.get(), () -> {
            if (!player.isOnline() || player.getOpenInventory().getTopInventory() != getInventory()) return;
            runnable.run();
            update(player);
            player.updateInventory();
        });
    }
}
