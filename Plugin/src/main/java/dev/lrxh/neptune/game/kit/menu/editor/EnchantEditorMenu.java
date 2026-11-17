package dev.lrxh.neptune.game.kit.menu.editor;

import dev.lrxh.neptune.configs.impl.MenusLocale;
import dev.lrxh.neptune.feature.customkit.enchant.EnchantmentBrowserMenu;
import dev.lrxh.neptune.game.kit.Kit;
import dev.lrxh.neptune.utils.ItemBuilder;
import dev.lrxh.neptune.utils.menu.Button;
import dev.lrxh.neptune.utils.menu.Filter;
import dev.lrxh.neptune.utils.menu.Menu;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EnchantEditorMenu extends Menu implements KitEditingMenu {
    private final Kit kit;
    private final int loadout;
    private final EquipmentSlot equipmentSlot;
    private final RefillCategory category;

    public EnchantEditorMenu(Kit kit, int loadout, EquipmentSlot equipmentSlot, RefillCategory category) {
        super(MenusLocale.KIT_EDITOR_ENCHANT_TITLE.getString(), 54, Filter.NONE, true);
        this.kit = kit;
        this.loadout = loadout;
        this.equipmentSlot = equipmentSlot;
        this.category = category;
    }

    @Override
    public List<Button> getButtons(Player player) {
        List<Button> buttons = new ArrayList<>();
        ItemStack item = player.getInventory().getItem(equipmentSlot);

        int slot = 0;
        for (Enchantment enchantment : Registry.ENCHANTMENT) {
            if (slot >= 45) break;
            if (!enchantment.canEnchantItem(item)) continue;
            buttons.add(new EnchantButton(slot++, enchantment));
        }

        buttons.add(new Button(49) {
            @Override
            public void onClick(ClickType type, Player player) {
                new ItemEditorMenu(kit, loadout, equipmentSlot, category).open(player);
            }

            @Override
            public ItemStack getItemStack(Player player) {
                return new ItemBuilder(Material.BARRIER).name(MenusLocale.KIT_EDITOR_BACK_NAME.getString()).build();
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
            ItemStack item = player.getInventory().getItem(equipmentSlot);
            int level = item.getEnchantmentLevel(enchantment);

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

            player.getInventory().setItem(equipmentSlot, item);
        }

        @Override
        public ItemStack getItemStack(Player player) {
            int level = player.getInventory().getItem(equipmentSlot).getEnchantmentLevel(enchantment);
            List<String> lore = new ArrayList<>();
            for (String line : MenusLocale.KIT_EDITOR_ENCHANT_LORE.getStringList()) {
                lore.add(line.replace("<level>", String.valueOf(level))
                        .replace("<max>", String.valueOf(enchantment.getMaxLevel())));
            }

            ItemBuilder builder = new ItemBuilder(Material.ENCHANTED_BOOK)
                    .name("&d" + EnchantmentBrowserMenu.format(enchantment.getKey().getKey()))
                    .lore(lore);
            return builder.build();
        }
    }
}
