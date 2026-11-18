package dev.lrxh.neptune.game.kit.menu.editor;

import dev.lrxh.neptune.configs.impl.MenusLocale;
import dev.lrxh.neptune.feature.customkit.enchant.EnchantmentBrowserMenu;
import dev.lrxh.neptune.utils.menu.Button;
import dev.lrxh.neptune.utils.menu.Filter;
import dev.lrxh.neptune.utils.menu.Menu;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EnchantEditorMenu extends Menu implements KitEditingMenu {
    private final KitEditorSession.Session session;
    private final int index;

    public EnchantEditorMenu(KitEditorSession.Session session, int index) {
        super(MenusLocale.KIT_EDITOR_ENCHANT_TITLE.getString(), 54, Filter.NONE, true);
        this.session = session;
        this.index = index;
    }

    @Override
    public boolean isClickSound() {
        return false;
    }

    @Override
    public List<Button> getButtons(Player player) {
        List<Button> buttons = new ArrayList<>();
        for (int slot = 45; slot < 54; slot++) buttons.add(KitEditorMenu.pane(slot, Material.GRAY_STAINED_GLASS_PANE));

        ItemStack item = player.getInventory().getItem(index);
        int slot = 0;
        if (!KitEditorItems.isEmpty(item)) {
            for (Enchantment enchantment : Registry.ENCHANTMENT) {
                if (slot >= 45) break;
                if (!enchantment.canEnchantItem(item)) continue;
                buttons.add(new EnchantButton(slot++, enchantment));
            }
        }

        buttons.add(new Button(53) {
            @Override
            public void onClick(ClickType type, Player player) {
                KitEditorSounds.play(player, KitEditorSounds.BACK);
                new ItemEditorMenu(session, index).open(player);
            }

            @Override
            public ItemStack getItemStack(Player player) {
                return KitEditorItems.button(Material.BARRIER, MenusLocale.KIT_EDITOR_BACK_NAME.getString(), List.of());
            }
        });
        return buttons;
    }

    private class EnchantButton extends Button {
        private final Enchantment enchantment;

        EnchantButton(int slot, Enchantment enchantment) {
            super(slot);
            this.enchantment = enchantment;
        }

        @Override
        public void onClick(ClickType type, Player player) {
            ItemStack item = player.getInventory().getItem(index);
            if (KitEditorItems.isEmpty(item)) return;
            int level = item.getEnchantmentLevel(enchantment);
            KitEditorSounds.play(player, KitEditorSounds.CLICK);

            if (type.isRightClick() || level >= enchantment.getMaxLevel()) {
                item.removeEnchantment(enchantment);
            } else {
                // drop whatever conflicts with it, like protection and blast protection
                for (Enchantment other : new ArrayList<>(item.getEnchantments().keySet())) {
                    if (!other.equals(enchantment) && other.conflictsWith(enchantment)) {
                        item.removeEnchantment(other);
                    }
                }
                item.addUnsafeEnchantment(enchantment, level + 1);
            }
            player.getInventory().setItem(index, item);
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemStack item = player.getInventory().getItem(index);
            int level = item == null ? 0 : item.getEnchantmentLevel(enchantment);
            List<String> lore = new ArrayList<>();
            for (String line : MenusLocale.KIT_EDITOR_ENCHANT_LORE.getStringList()) {
                lore.add(line.replace("<level>", String.valueOf(level))
                        .replace("<max>", String.valueOf(enchantment.getMaxLevel())));
            }
            return KitEditorItems.button(Material.ENCHANTED_BOOK,
                    "&d" + EnchantmentBrowserMenu.format(enchantment.getKey().getKey()), lore, level > 0);
        }
    }
}
