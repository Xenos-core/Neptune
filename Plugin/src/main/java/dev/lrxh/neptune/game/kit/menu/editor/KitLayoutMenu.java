package dev.lrxh.neptune.game.kit.menu.editor;

import dev.lrxh.neptune.Neptune;
import dev.lrxh.neptune.configs.impl.MenusLocale;
import dev.lrxh.neptune.game.kit.Kit;
import dev.lrxh.neptune.game.kit.menu.editor.button.EquipmentButton;
import dev.lrxh.neptune.game.kit.menu.editor.button.TrimCustomizerButton;
import dev.lrxh.neptune.utils.ItemBuilder;
import dev.lrxh.neptune.utils.menu.Button;
import dev.lrxh.neptune.utils.menu.Filter;
import dev.lrxh.neptune.utils.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class KitLayoutMenu extends Menu implements KitEditingMenu {
    private static final int[] REFILL_SLOTS = {
            9, 10, 11, 12, 13, 14, 15, 16,
            18, 19, 20, 21, 22, 23, 24, 25,
            27, 28, 29, 30, 31, 32, 33, 34,
            36, 37, 38, 39, 40, 41, 42, 43
    };
    private static final int[] CATEGORY_SLOTS = {17, 26, 35};

    private final Kit kit;
    private final int loadout;
    private RefillCategory category;

    public KitLayoutMenu(Kit kit, int loadout, RefillCategory category) {
        super(MenusLocale.KIT_EDITOR_EDIT_TITLE.getString().replace("<slot>", String.valueOf(loadout + 1)), 54, Filter.NONE, true);
        this.kit = kit;
        this.loadout = loadout;
        this.category = category;
    }

    @Override
    public boolean isPlayerInventoryEditable() {
        return true;
    }

    @Override
    public List<Button> getButtons(Player player) {
        List<Button> buttons = new ArrayList<>();

        List<ItemStack> items = category.getItems();
        for (int i = 0; i < REFILL_SLOTS.length; i++) {
            // empty slots still get a button so switching tabs clears the old items
            buttons.add(new RefillButton(REFILL_SLOTS[i], i < items.size() ? items.get(i) : null));
        }

        RefillCategory[] categories = RefillCategory.values();
        for (int i = 0; i < categories.length && i < CATEGORY_SLOTS.length; i++) {
            buttons.add(new CategoryButton(CATEGORY_SLOTS[i], categories[i]));
        }

        buttons.add(new TrimCustomizerButton(45));
        buttons.add(new EquipmentButton(47, EquipmentSlot.HEAD, kit, loadout, category));
        buttons.add(new EquipmentButton(48, EquipmentSlot.CHEST, kit, loadout, category));
        buttons.add(new EquipmentButton(49, EquipmentSlot.LEGS, kit, loadout, category));
        buttons.add(new EquipmentButton(50, EquipmentSlot.FEET, kit, loadout, category));
        buttons.add(new EquipmentButton(51, EquipmentSlot.OFF_HAND, kit, loadout, category));

        buttons.add(new Button(53) {
            @Override
            public void onClick(ClickType type, Player player) {
                player.closeInventory();
            }

            @Override
            public ItemStack getItemStack(Player player) {
                return new ItemBuilder(Material.BARRIER).name(MenusLocale.KIT_EDITOR_SAVE_NAME.getString())
                        .lore(MenusLocale.KIT_EDITOR_SAVE_LORE.getStringList()).build();
            }
        });

        return buttons;
    }

    private static class RefillButton extends Button {
        private final ItemStack item;

        RefillButton(int slot, ItemStack item) {
            super(slot);
            this.item = item;
        }

        @Override
        public void onClick(ClickType type, Player player) {
            if (item == null) return;
            if (type.isShiftClick()) {
                player.getInventory().addItem(item.clone());
                return;
            }

            // clicking the refill while holding something throws it away
            boolean holding = !player.getItemOnCursor().getType().isAir();
            ItemStack give = item.clone();
            if (type.isRightClick()) give.setAmount(1);

            // the click event resets the cursor, so set it a tick later
            Bukkit.getScheduler().runTask(Neptune.get(), () -> player.setItemOnCursor(holding ? null : give));
        }

        @Override
        public ItemStack getItemStack(Player player) {
            return item == null ? new ItemStack(Material.AIR) : item.clone();
        }
    }

    private class CategoryButton extends Button {
        private final RefillCategory refill;

        CategoryButton(int slot, RefillCategory refill) {
            super(slot);
            this.refill = refill;
        }

        @Override
        public void onClick(ClickType type, Player player) {
            category = refill;
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemBuilder builder = new ItemBuilder(refill.getIcon()).name(refill.getName().getString());
            if (refill == category) builder.addEnchantedGlow();
            return builder.build();
        }
    }
}
