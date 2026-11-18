package dev.lrxh.neptune.game.kit.menu.editor;

import dev.lrxh.neptune.API;
import dev.lrxh.neptune.configs.impl.MenusLocale;
import dev.lrxh.neptune.configs.impl.MessagesLocale;
import dev.lrxh.neptune.game.kit.Kit;
import dev.lrxh.neptune.profile.data.KitData;
import dev.lrxh.neptune.utils.menu.Button;
import dev.lrxh.neptune.utils.menu.Filter;
import dev.lrxh.neptune.utils.menu.Menu;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * "Vanilla Kits": the saved kits of a builder kit. Slots 1-3 are free, 4-6 need
 * neptune.kiteditor.slot.<n>.
 */
public class KitSlotsMenu extends Menu {
    private static final int[] SLOTS = {11, 13, 15, 29, 31, 33};
    private static final int FREE_SLOTS = 3;
    private static final int MAX_NAME = 32;

    private final Kit kit;

    public KitSlotsMenu(Kit kit) {
        super(MenusLocale.KIT_EDITOR_SLOTS_TITLE.getString().replace("<kit>", KitEditorItems.strip(kit.getDisplayName())), 54, Filter.NONE, true);
        this.kit = kit;
    }

    public static boolean isUnlocked(Player player, int index) {
        return index < FREE_SLOTS || player.hasPermission("neptune.kiteditor.slot." + (index + 1));
    }

    @Override
    public boolean isClickSound() {
        return false;
    }

    @Override
    public List<Button> getButtons(Player player) {
        List<Button> buttons = new ArrayList<>();
        for (int slot = 0; slot < 54; slot++) buttons.add(KitEditorMenu.pane(slot, Material.GRAY_STAINED_GLASS_PANE));

        for (int i = 0; i < KitData.LOADOUT_SLOTS && i < SLOTS.length; i++) {
            buttons.add(new SlotButton(SLOTS[i], i));
        }

        buttons.add(new Button(49) {
            @Override
            public void onClick(ClickType type, Player player) {
                KitEditorSounds.play(player, KitEditorSounds.BACK);
                new KitEditorMenu(kit.getKitEditorPage()).open(player);
            }

            @Override
            public ItemStack getItemStack(Player player) {
                return KitEditorItems.button(Material.BARRIER, MenusLocale.KIT_EDITOR_BACK_NAME.getString(), List.of());
            }
        });
        return buttons;
    }

    private class SlotButton extends Button {
        private final int index;

        SlotButton(int slot, int index) {
            super(slot);
            this.index = index;
        }

        @Override
        public void onClick(ClickType type, Player player) {
            if (!isUnlocked(player, index)) {
                KitEditorSounds.play(player, KitEditorSounds.LOCKED);
                return;
            }

            KitData kitData = API.getProfile(player).getGameData().get(kit);
            String name = kitData.getLoadoutName(index);

            if (name != null && type == ClickType.SHIFT_RIGHT) {
                KitEditorSounds.play(player, KitEditorSounds.CLICK);
                player.closeInventory();
                MessagesLocale.KIT_EDITOR_RENAME_PROMPT.send(player.getUniqueId());
                ChatPrompt.ask(player, input -> {
                    if (input != null) {
                        String newName = input.length() > MAX_NAME ? input.substring(0, MAX_NAME) : input;
                        kitData.setLoadoutName(index, newName);
                        MessagesLocale.KIT_EDITOR_RENAMED.send(player.getUniqueId(), Placeholder.unparsed("name", newName));
                    }
                    new KitSlotsMenu(kit).open(player);
                });
                return;
            }

            if (name != null && (type == ClickType.DROP || type == ClickType.CONTROL_DROP)) {
                KitEditorSounds.play(player, KitEditorSounds.BACK);
                kitData.setLoadoutName(index, null);
                kitData.setLoadout(index, new ArrayList<>());
                if (kitData.getSelectedLoadout() == index) kitData.setSelectedLoadout(firstKit(kitData));
                MessagesLocale.KIT_EDITOR_DELETED.send(player.getUniqueId(), Placeholder.unparsed("name", KitEditorItems.strip(name)));
                return;
            }

            if (type == ClickType.DROP || type == ClickType.CONTROL_DROP || type == ClickType.SHIFT_RIGHT) return;

            KitEditorSounds.play(player, KitEditorSounds.CLICK);
            KitEditorSession.startBuilder(player, kit, index);
        }

        private int firstKit(KitData kitData) {
            for (int i = 0; i < KitData.LOADOUT_SLOTS; i++) {
                if (kitData.getLoadoutName(i) != null) return i;
            }
            return 0;
        }

        @Override
        public ItemStack getItemStack(Player player) {
            if (!isUnlocked(player, index)) {
                return KitEditorItems.button(Material.RED_STAINED_GLASS_PANE,
                        MenusLocale.KIT_EDITOR_SLOTS_LOCKED_NAME.getString().replace("<slot>", String.valueOf(index + 1)),
                        MenusLocale.KIT_EDITOR_SLOTS_LOCKED_LORE.getStringList());
            }

            KitData kitData = API.getProfile(player).getGameData().get(kit);
            String name = kitData.getLoadoutName(index);
            if (name == null) {
                return KitEditorItems.button(Material.WRITABLE_BOOK, MenusLocale.KIT_EDITOR_SLOTS_EMPTY_NAME.getString(),
                        MenusLocale.KIT_EDITOR_SLOTS_EMPTY_LORE.getStringList());
            }

            int kits = 0;
            for (int i = 0; i < KitData.LOADOUT_SLOTS; i++) if (kitData.getLoadoutName(i) != null) kits++;

            List<String> lore = new ArrayList<>();
            if (kits > 1 && kitData.getSelectedLoadout() == index) lore.add(MenusLocale.KIT_EDITOR_SLOTS_SELECTED_LORE.getString());
            lore.addAll(MenusLocale.KIT_EDITOR_SLOTS_KIT_LORE.getStringList());

            return KitEditorItems.button(Material.ENCHANTED_BOOK,
                    MenusLocale.KIT_EDITOR_SLOTS_KIT_NAME.getString().replace("<name>", name), lore, true);
        }
    }
}
