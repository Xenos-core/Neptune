package dev.lrxh.neptune.game.kit.menu.editor;

import dev.lrxh.neptune.API;
import dev.lrxh.neptune.configs.impl.MenusLocale;
import dev.lrxh.neptune.configs.impl.MessagesLocale;
import dev.lrxh.neptune.game.kit.Kit;
import dev.lrxh.neptune.profile.data.KitData;
import dev.lrxh.neptune.utils.ItemBuilder;
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

public class KitLoadoutsMenu extends Menu {
    private static final int[] SLOTS = {11, 13, 15, 29, 31, 33};
    private static final int FREE_SLOTS = 3;

    private final Kit kit;

    public KitLoadoutsMenu(Kit kit) {
        super(MenusLocale.KIT_EDITOR_SLOTS_TITLE.getString().replace("<kit>", kit.getDisplayName()), 54, Filter.NONE, true);
        this.kit = kit;
    }

    public static boolean isUnlocked(Player player, int index) {
        return index < FREE_SLOTS || player.hasPermission("neptune.kiteditor.slot." + (index + 1));
    }

    @Override
    public List<Button> getButtons(Player player) {
        List<Button> buttons = new ArrayList<>();
        for (int i = 0; i < KitData.LOADOUT_SLOTS; i++) {
            buttons.add(new LoadoutButton(SLOTS[i], i));
        }

        buttons.add(new Button(49) {
            @Override
            public void onClick(ClickType type, Player player) {
                new KitEditorMenu().open(player);
            }

            @Override
            public ItemStack getItemStack(Player player) {
                return new ItemBuilder(Material.BARRIER).name(MenusLocale.KIT_EDITOR_BACK_NAME.getString()).build();
            }
        });
        return buttons;
    }

    private class LoadoutButton extends Button {
        private final int index;

        LoadoutButton(int slot, int index) {
            super(slot);
            this.index = index;
        }

        @Override
        public void onClick(ClickType type, Player player) {
            if (!isUnlocked(player, index)) return;
            KitData kitData = API.getProfile(player).getGameData().get(kit);

            switch (type) {
                case RIGHT, SHIFT_RIGHT -> kitData.setSelectedLoadout(index);
                case DROP, CONTROL_DROP -> {
                    kitData.setLoadout(index, new ArrayList<>());
                    MessagesLocale.KIT_EDITOR_RESET.send(player.getUniqueId(), Placeholder.parsed("kit", kit.getDisplayName()));
                }
                default -> KitEditorSession.start(player, kit, index);
            }
        }

        @Override
        public ItemStack getItemStack(Player player) {
            String name = MenusLocale.KIT_EDITOR_SLOTS_NAME.getString().replace("<slot>", String.valueOf(index + 1));

            if (!isUnlocked(player, index)) {
                return new ItemBuilder(Material.RED_STAINED_GLASS_PANE).name(name)
                        .lore(MenusLocale.KIT_EDITOR_SLOTS_LOCKED_LORE.getStringList()).build();
            }

            boolean selected = API.getProfile(player).getGameData().get(kit).getSelectedLoadout() == index;
            List<String> lore = new ArrayList<>();
            if (selected) lore.add(MenusLocale.KIT_EDITOR_SLOTS_SELECTED_LORE.getString());
            lore.addAll(MenusLocale.KIT_EDITOR_SLOTS_LORE.getStringList());

            return new ItemBuilder(selected ? Material.ENCHANTED_BOOK : Material.WRITABLE_BOOK)
                    .name(name).lore(lore).build();
        }
    }
}
