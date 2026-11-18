package dev.lrxh.neptune.game.kit.menu.editor;

import dev.lrxh.neptune.configs.impl.MenusLocale;
import dev.lrxh.neptune.configs.impl.MessagesLocale;
import dev.lrxh.neptune.utils.CC;
import dev.lrxh.neptune.utils.menu.Button;
import dev.lrxh.neptune.utils.menu.Filter;
import dev.lrxh.neptune.utils.menu.Menu;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * "Item Editor": the item framed by glass, then trim, enchant and change type, back in the corner.
 * Works on one slot of the player's inventory (0-35, 36-39 armor, 40 offhand).
 */
public class ItemEditorMenu extends Menu implements KitEditingMenu {
    private static final int[] FRAME_SLOTS = {3, 4, 5, 12, 14, 21, 22, 23};
    private static final int MAX_NAME = 48;

    private final KitEditorSession.Session session;
    private final int index;

    public ItemEditorMenu(KitEditorSession.Session session, int index) {
        super(MenusLocale.KIT_EDITOR_ITEM_EDITOR_TITLE.getString(), 54, Filter.NONE, true);
        this.session = session;
        this.index = index;
    }

    @Override
    public boolean isClickSound() {
        return false;
    }

    private ItemStack item(Player player) {
        return player.getInventory().getItem(index);
    }

    @Override
    public List<Button> getButtons(Player player) {
        List<Button> buttons = new ArrayList<>();
        for (int slot = 0; slot < 54; slot++) buttons.add(KitEditorMenu.pane(slot, Material.GRAY_STAINED_GLASS_PANE));
        for (int slot : FRAME_SLOTS) buttons.add(KitEditorMenu.pane(slot, Material.GLASS_PANE));

        buttons.add(new Button(13) {
            @Override
            public void onClick(ClickType type, Player player) {
                if (!type.isShiftClick() || KitEditorItems.isEmpty(item(player))) return;
                KitEditorSounds.play(player, KitEditorSounds.CLICK);
                player.closeInventory();
                MessagesLocale.KIT_EDITOR_RENAME_PROMPT.send(player.getUniqueId());
                ChatPrompt.ask(player, input -> {
                    ItemStack item = item(player);
                    if (input != null && !KitEditorItems.isEmpty(item)) {
                        String name = input.length() > MAX_NAME ? input.substring(0, MAX_NAME) : input;
                        ItemMeta meta = item.getItemMeta();
                        meta.displayName(CC.color(name));
                        item.setItemMeta(meta);
                        player.getInventory().setItem(index, item);
                        MessagesLocale.KIT_EDITOR_RENAMED.send(player.getUniqueId(), Placeholder.unparsed("name", KitEditorItems.strip(name)));
                    }
                    if (KitEditorSession.get(player) == session) new ItemEditorMenu(session, index).open(player);
                });
            }

            @Override
            public ItemStack getItemStack(Player player) {
                ItemStack item = item(player);
                if (KitEditorItems.isEmpty(item)) return new ItemStack(Material.AIR);
                return KitEditorItems.display(item, MenusLocale.KIT_EDITOR_ITEM_EDITOR_ITEM_LORE.getStringList(), false);
            }
        });

        buttons.add(new Button(38) {
            @Override
            public void onClick(ClickType type, Player player) {
                if (!player.hasPermission(TrimButton.PERMISSION)) {
                    KitEditorSounds.play(player, KitEditorSounds.LOCKED);
                    return;
                }
                if (!KitEditorItems.isArmor(item(player))) return;
                KitEditorSounds.play(player, KitEditorSounds.CLICK);
                new TrimMenu(TrimMenu.Target.player(player.getInventory(), new int[]{index},
                        () -> new ItemEditorMenu(session, index).open(player))).open(player);
            }

            @Override
            public ItemStack getItemStack(Player player) {
                boolean unlocked = player.hasPermission(TrimButton.PERMISSION);
                return KitEditorItems.button(Material.NETHER_STAR, MenusLocale.KIT_EDITOR_ITEM_EDITOR_TRIM_NAME.getString(),
                        unlocked ? List.of() : MenusLocale.KIT_EDITOR_ITEM_EDITOR_TRIM_LOCKED_LORE.getStringList());
            }
        });

        buttons.add(new Button(40) {
            @Override
            public void onClick(ClickType type, Player player) {
                if (KitEditorItems.isEmpty(item(player))) return;
                KitEditorSounds.play(player, KitEditorSounds.CLICK);
                new EnchantEditorMenu(session, index).open(player);
            }

            @Override
            public ItemStack getItemStack(Player player) {
                return KitEditorItems.button(Material.ENCHANTING_TABLE, MenusLocale.KIT_EDITOR_ITEM_EDITOR_ENCHANT_NAME.getString(), List.of());
            }
        });

        buttons.add(new Button(42) {
            @Override
            public void onClick(ClickType type, Player player) {
                ItemStack item = item(player);
                if (KitEditorItems.isEmpty(item) || !TypeCycler.canChange(item)) return;
                KitEditorSounds.play(player, KitEditorSounds.CLICK);

                ItemStack changed;
                if (TypeCycler.isPotion(item)) {
                    changed = type.isRightClick() ? TypeCycler.nextEffect(item) : TypeCycler.change(item, true);
                } else {
                    changed = TypeCycler.change(item, !type.isRightClick());
                }
                player.getInventory().setItem(index, changed);
            }

            @Override
            public ItemStack getItemStack(Player player) {
                boolean potion = TypeCycler.isPotion(item(player));
                return KitEditorItems.button(Material.NETHERITE_SCRAP, MenusLocale.KIT_EDITOR_ITEM_EDITOR_TYPE_NAME.getString(),
                        potion ? MenusLocale.KIT_EDITOR_ITEM_EDITOR_POTION_TYPE_LORE.getStringList() : List.of());
            }
        });

        buttons.add(new Button(53) {
            @Override
            public void onClick(ClickType type, Player player) {
                KitEditorSounds.play(player, KitEditorSounds.BACK, KitEditorSounds.SAVE);
                new KitBuilderMenu(session).open(player);
            }

            @Override
            public ItemStack getItemStack(Player player) {
                return KitEditorItems.button(Material.BARRIER, MenusLocale.KIT_EDITOR_BACK_NAME.getString(), List.of());
            }
        });
        return buttons;
    }
}
