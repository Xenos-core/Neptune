package dev.lrxh.neptune.game.kit.menu.editor;

import dev.lrxh.neptune.configs.impl.MenusLocale;
import dev.lrxh.neptune.game.kit.Kit;
import dev.lrxh.neptune.game.kit.menu.editor.button.TrimCustomizerButton;
import dev.lrxh.neptune.utils.ItemBuilder;
import dev.lrxh.neptune.utils.menu.Button;
import dev.lrxh.neptune.utils.menu.Filter;
import dev.lrxh.neptune.utils.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemEditorMenu extends Menu implements KitEditingMenu {
    private static final int[] FRAME_SLOTS = {3, 4, 5, 12, 14, 21, 22, 23};

    private final Kit kit;
    private final int loadout;
    private final EquipmentSlot equipmentSlot;
    private final RefillCategory category;

    public ItemEditorMenu(Kit kit, int loadout, EquipmentSlot equipmentSlot, RefillCategory category) {
        super(MenusLocale.KIT_EDITOR_ITEM_EDITOR_TITLE.getString(), 54, Filter.NONE, true);
        this.kit = kit;
        this.loadout = loadout;
        this.equipmentSlot = equipmentSlot;
        this.category = category;
    }

    @Override
    public List<Button> getButtons(Player player) {
        List<Button> buttons = new ArrayList<>();

        for (int slot : FRAME_SLOTS) {
            buttons.add(new Button(slot) {
                @Override
                public ItemStack getItemStack(Player player) {
                    return new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).name(" ").build();
                }
            });
        }

        buttons.add(new Button(13) {
            @Override
            public ItemStack getItemStack(Player player) {
                return player.getInventory().getItem(equipmentSlot).clone();
            }
        });

        buttons.add(new Button(38) {
            @Override
            public void onClick(ClickType type, Player player) {
                ItemStack item = player.getInventory().getItem(equipmentSlot);
                Material swapped = swapTier(item.getType());
                if (swapped != null) {
                    player.getInventory().setItem(equipmentSlot, item.withType(swapped));
                }
            }

            @Override
            public ItemStack getItemStack(Player player) {
                return new ItemBuilder(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
                        .name(MenusLocale.KIT_EDITOR_UPGRADE_NAME.getString())
                        .lore(MenusLocale.KIT_EDITOR_UPGRADE_LORE.getStringList()).build();
            }
        });

        buttons.add(new Button(40) {
            @Override
            public void onClick(ClickType type, Player player) {
                new EnchantEditorMenu(kit, loadout, equipmentSlot, category).open(player);
            }

            @Override
            public ItemStack getItemStack(Player player) {
                return new ItemBuilder(Material.ENCHANTING_TABLE).name(MenusLocale.KIT_EDITOR_ENCHANT_NAME.getString()).build();
            }
        });

        buttons.add(new TrimCustomizerButton(42));

        buttons.add(new Button(53) {
            @Override
            public void onClick(ClickType type, Player player) {
                new KitLayoutMenu(kit, loadout, category).open(player);
            }

            @Override
            public ItemStack getItemStack(Player player) {
                return new ItemBuilder(Material.BARRIER).name(MenusLocale.KIT_EDITOR_BACK_NAME.getString()).build();
            }
        });

        return buttons;
    }

    private static Material swapTier(Material material) {
        String name = material.name();
        if (name.startsWith("DIAMOND_")) return Material.getMaterial(name.replaceFirst("DIAMOND_", "NETHERITE_"));
        if (name.startsWith("NETHERITE_")) return Material.getMaterial(name.replaceFirst("NETHERITE_", "DIAMOND_"));
        return null;
    }
}
