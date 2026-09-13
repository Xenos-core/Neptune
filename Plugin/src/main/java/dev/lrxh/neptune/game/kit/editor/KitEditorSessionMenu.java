package dev.lrxh.neptune.game.kit.editor;

import dev.lrxh.neptune.API;
import dev.lrxh.neptune.configs.impl.MenusLocale;
import dev.lrxh.neptune.game.kit.Kit;
import dev.lrxh.neptune.game.kit.editor.event.KitEditorCloseEvent;
import dev.lrxh.neptune.game.kit.editor.event.KitEditorOpenEvent;
import dev.lrxh.neptune.game.kit.editor.event.KitEditorSaveEvent;
import dev.lrxh.neptune.profile.data.GameData;
import dev.lrxh.neptune.profile.data.KitData;
import dev.lrxh.neptune.profile.data.ProfileState;
import dev.lrxh.neptune.profile.impl.Profile;
import dev.lrxh.neptune.utils.CC;
import dev.lrxh.neptune.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class KitEditorSessionMenu {

    private static final int SLOT_HELMET = 2;
    private static final int SLOT_CHESTPLATE = 3;
    private static final int SLOT_LEGGINGS = 4;
    private static final int SLOT_BOOTS = 5;
    private static final int SLOT_OFFHAND = 6;
    private static final int SLOT_EXIT = 8;

    private final Kit kit;

    public KitEditorSessionMenu(Kit kit) {
        this.kit = kit;
    }

    public void open(Player player) {
        Profile profile = API.getProfile(player);
        if (profile == null) return;

        EditorSession session = new EditorSession(player.getUniqueId(), kit);
        profile.setEditorSession(session);

        KitEditorOpenEvent openEvent = new KitEditorOpenEvent(player, kit);
        Bukkit.getPluginManager().callEvent(openEvent);
        if (openEvent.isCancelled()) return;

        String titleStr = MenusLocale.KIT_EDITOR_TITLE.getString()
                .replace("<kit>", stripColor(kit.getDisplayName()));
        Component title = MiniMessage.miniMessage().deserialize(titleStr);

        var inventory = Bukkit.createInventory(player, 54, title);

        ItemStack glass = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").build();
        inventory.setItem(0, glass);
        inventory.setItem(1, glass);
        inventory.setItem(7, glass);
        inventory.setItem(8, new ItemBuilder(Material.BARRIER).name("&cExit").lore("&7Click to save and exit").build());

        inventory.setItem(SLOT_HELMET, new ItemBuilder(Material.DIAMOND_HELMET).name("&7Helmet").build());
        inventory.setItem(SLOT_CHESTPLATE, new ItemBuilder(Material.DIAMOND_CHESTPLATE).name("&7Chestplate").build());
        inventory.setItem(SLOT_LEGGINGS, new ItemBuilder(Material.DIAMOND_LEGGINGS).name("&7Leggings").build());
        inventory.setItem(SLOT_BOOTS, new ItemBuilder(Material.DIAMOND_BOOTS).name("&7Boots").build());
        inventory.setItem(SLOT_OFFHAND, new ItemBuilder(Material.SHIELD).name("&7Offhand").build());

        ItemStack separator = new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).name(" ").build();
        for (int i = 36; i < 45; i++) {
            inventory.setItem(i, separator);
        }

        List<ItemStack> loadout = getLoadout(player);
        List<ItemStack> merged = RefillService.get().mergeWithLoadout(loadout, kit);

        int invIndex = 0;
        for (int i = 0; i < 27 && invIndex < merged.size(); i++) {
            inventory.setItem(9 + i, merged.get(invIndex++));
        }
        for (int i = 0; i < 9 && invIndex < merged.size(); i++) {
            inventory.setItem(45 + i, merged.get(invIndex++));
        }

        if (invIndex < merged.size() && invIndex >= 36) {
            inventory.setItem(SLOT_BOOTS, merged.get(invIndex++));
        }
        if (invIndex < merged.size() && invIndex >= 37) {
            inventory.setItem(SLOT_LEGGINGS, merged.get(invIndex++));
        }
        if (invIndex < merged.size() && invIndex >= 38) {
            inventory.setItem(SLOT_CHESTPLATE, merged.get(invIndex++));
        }
        if (invIndex < merged.size() && invIndex >= 39) {
            inventory.setItem(SLOT_HELMET, merged.get(invIndex++));
        }
        if (invIndex < merged.size() && invIndex >= 40) {
            inventory.setItem(SLOT_OFFHAND, merged.get(invIndex));
        }

        player.openInventory(inventory);
    }

    public void saveAndClose(Player player) {
        Profile profile = API.getProfile(player);
        if (profile == null) return;

        var inventory = player.getOpenInventory().getTopInventory();
        List<ItemStack> loadout = new ArrayList<>();

        for (int i = 9; i < 36; i++) {
            ItemStack item = inventory.getItem(i);
            loadout.add(item);
        }
        for (int i = 45; i < 54; i++) {
            ItemStack item = inventory.getItem(i);
            loadout.add(item);
        }

        ItemStack boots = inventory.getItem(SLOT_BOOTS);
        ItemStack leggings = inventory.getItem(SLOT_LEGGINGS);
        ItemStack chestplate = inventory.getItem(SLOT_CHESTPLATE);
        ItemStack helmet = inventory.getItem(SLOT_HELMET);
        ItemStack offhand = inventory.getItem(SLOT_OFFHAND);

        loadout.add(isPlaceholder(boots) ? null : boots);
        loadout.add(isPlaceholder(leggings) ? null : leggings);
        loadout.add(isPlaceholder(chestplate) ? null : chestplate);
        loadout.add(isPlaceholder(helmet) ? null : helmet);
        loadout.add(isPlaceholder(offhand) ? null : offhand);

        GameData gameData = profile.getGameData();
        KitData kitData = gameData.get(kit);
        kitData.setKitLoadout(loadout);

        Bukkit.getPluginManager().callEvent(new KitEditorSaveEvent(player, kit, loadout));
        Bukkit.getPluginManager().callEvent(new KitEditorCloseEvent(player, kit));

        profile.setEditorSession(null);

        if (profile.getGameData().getParty() == null) {
            profile.setState(ProfileState.IN_LOBBY);
        } else {
            profile.setState(ProfileState.IN_PARTY);
        }

        player.sendMessage(CC.success("Saved loadout for " + kit.getDisplayName()));
    }

    public void cancelAndClose(Player player) {
        Profile profile = API.getProfile(player);
        if (profile == null) return;

        Bukkit.getPluginManager().callEvent(new KitEditorCloseEvent(player, kit));

        profile.setEditorSession(null);

        if (profile.getGameData().getParty() == null) {
            profile.setState(ProfileState.IN_LOBBY);
        } else {
            profile.setState(ProfileState.IN_PARTY);
        }

        player.sendMessage(CC.info("Editor closed without saving"));
    }

    private List<ItemStack> getLoadout(Player player) {
        Profile profile = API.getProfile(player);
        if (profile == null) return new ArrayList<>();

        GameData gameData = profile.getGameData();
        KitData kitData = gameData.get(kit);
        List<ItemStack> loadout = kitData.getKitLoadout();

        if (loadout == null || loadout.isEmpty()) {
            return new ArrayList<>(kit.getItems());
        }
        return new ArrayList<>(loadout);
    }

    private boolean isPlaceholder(ItemStack item) {
        if (item == null) return true;
        if (item.getItemMeta() == null) return false;
        String name = item.getItemMeta().getDisplayName();
        return name.equals("Helmet") || name.equals("Chestplate") || name.equals("Leggings") || name.equals("Boots") || name.equals("Offhand");
    }

    private String stripColor(String input) {
        return input.replaceAll("[&§][0-9a-fk-or]", "").replaceAll("<[^>]+>", "");
    }
}
