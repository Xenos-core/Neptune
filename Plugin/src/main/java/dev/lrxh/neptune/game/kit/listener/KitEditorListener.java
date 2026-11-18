package dev.lrxh.neptune.game.kit.listener;

import dev.lrxh.neptune.API;
import dev.lrxh.neptune.Neptune;
import dev.lrxh.neptune.game.kit.menu.editor.ChatPrompt;
import dev.lrxh.neptune.game.kit.menu.editor.KitEditingMenu;
import dev.lrxh.neptune.game.kit.menu.editor.KitEditorSession;
import dev.lrxh.neptune.game.kit.menu.editor.KitLayoutMenu;
import dev.lrxh.neptune.profile.data.ProfileState;
import dev.lrxh.neptune.profile.impl.Profile;
import dev.lrxh.neptune.utils.menu.MenuService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;


public class KitEditorListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Profile profile = API.getProfile(player);
        if (profile == null) return;
        if (!profile.hasState(ProfileState.IN_KIT_EDITOR)) return;

        KitEditorSession.Session session = KitEditorSession.get(player);
        ItemStack cursor = event.getView().getCursor();
        boolean holding = !cursor.getType().isAir();

        if (session != null && session.getMode() == KitEditorSession.Mode.LAYOUT) {
            KitLayoutMenu layoutMenu = session.getLayoutMenu();
            if (layoutMenu != null && event.getInventory().equals(layoutMenu.getMenuInventory())) {
                layoutMenu.sync(event.getInventory(), holding ? cursor : null);
                if (holding) event.getView().setCursor(null);
            }
        } else if (holding) {
            player.getInventory().addItem(cursor);
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
            if (ChatPrompt.isWaiting(player)) return;
            KitEditorSession.finish(player, profile);
        });
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = API.getProfile(player);
        if (profile == null || KitEditorSession.get(player) == null) return;
        if (profile.hasState(ProfileState.IN_KIT_EDITOR)) KitEditorSession.finish(player, profile);
        else KitEditorSession.clear(player);
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
