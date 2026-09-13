package dev.lrxh.neptune.game.kit.editor;

import dev.lrxh.neptune.API;
import dev.lrxh.neptune.profile.impl.Profile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class KitEditorSessionListener implements Listener {

    private static final int SLOT_HELMET = 2;
    private static final int SLOT_CHESTPLATE = 3;
    private static final int SLOT_LEGGINGS = 4;
    private static final int SLOT_BOOTS = 5;
    private static final int SLOT_OFFHAND = 6;
    private static final int SLOT_EXIT = 8;

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        Profile profile = API.getProfile(player);
        if (profile == null) return;
        if (profile.getEditorSession() == null) return;

        if (!isEditorInventory(event.getInventory())) return;

        int slot = event.getRawSlot();

        if (slot == 0 || slot == 1 || slot == 7) {
            event.setCancelled(true);
            return;
        }

        if (slot == SLOT_EXIT) {
            event.setCancelled(true);
            new KitEditorSessionMenu(profile.getEditorSession().getKit()).saveAndClose(player);
            player.closeInventory();
            return;
        }

        if (slot >= 36 && slot < 45) {
            event.setCancelled(true);
            return;
        }

        if (slot == SLOT_HELMET || slot == SLOT_CHESTPLATE || slot == SLOT_LEGGINGS || slot == SLOT_BOOTS || slot == SLOT_OFFHAND) {
            return;
        }

        if (slot >= 9 && slot < 36) {
            return;
        }

        if (slot >= 45 && slot < 54) {
            return;
        }

        if (event.getClick().isShiftClick() && slot >= 54) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;

        Profile profile = API.getProfile(player);
        if (profile == null) return;
        if (profile.getEditorSession() == null) return;

        new KitEditorSessionMenu(profile.getEditorSession().getKit()).saveAndClose(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = API.getProfile(player);
        if (profile == null) return;
        if (profile.getEditorSession() == null) return;

        new KitEditorSessionMenu(profile.getEditorSession().getKit()).saveAndClose(player);
    }

    private boolean isEditorInventory(org.bukkit.inventory.Inventory inventory) {
        return inventory != null && inventory.getSize() == 54;
    }
}
