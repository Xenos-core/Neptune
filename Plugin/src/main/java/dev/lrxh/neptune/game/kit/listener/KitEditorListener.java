package dev.lrxh.neptune.game.kit.listener;

import dev.lrxh.neptune.API;
import dev.lrxh.neptune.Neptune;
import dev.lrxh.neptune.game.kit.menu.editor.KitEditingMenu;
import dev.lrxh.neptune.game.kit.menu.editor.KitEditorSession;
import dev.lrxh.neptune.profile.data.ProfileState;
import dev.lrxh.neptune.profile.impl.Profile;
import dev.lrxh.neptune.utils.menu.MenuService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.CraftingInventory;


public class KitEditorListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Profile profile = API.getProfile(player);
        if (profile == null)
            return;
        if (!profile.hasState(ProfileState.IN_KIT_EDITOR)) return;

        if (event.getView().getCursor().getType() != Material.AIR) {
            event.getPlayer().getInventory().addItem(event.getView().getCursor());
            event.getView().setCursor(null);
        }

        if (event.getReason() == InventoryCloseEvent.Reason.DISCONNECT) {
            KitEditorSession.finish(player, profile);
            return;
        }

        // moving between the editor menus closes the old one first, only save if no editor menu got opened
        Bukkit.getScheduler().runTask(Neptune.get(), () -> {
            if (!player.isOnline() || !profile.hasState(ProfileState.IN_KIT_EDITOR)) return;
            if (MenuService.get().get(player) instanceof KitEditingMenu) return;
            KitEditorSession.finish(player, profile);
        });
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        Profile profile = API.getProfile(player);
        if (profile == null) return;

        if (profile.hasState(ProfileState.IN_KIT_EDITOR)) {
            if (event.getClickedInventory() instanceof CraftingInventory) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        Profile profile = API.getProfile(player);
        if (profile == null) return;

        if (profile.hasState(ProfileState.IN_KIT_EDITOR)) {
            if (event.getInventory() instanceof CraftingInventory) {
                event.setCancelled(true);
            }
        }
    }
}
