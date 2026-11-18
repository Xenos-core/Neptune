package dev.lrxh.neptune.utils.menu;

import dev.lrxh.neptune.API;
import dev.lrxh.neptune.configs.impl.SoundsLocale;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MenuListener implements Listener {

    @EventHandler
    public void onButtonPress(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        Menu menu = MenuService.get().get(player);
        if (menu == null) return;
        if (menu.handleClick(player, event)) return;

        if (event.getClickedInventory() != null && event.getClickedInventory().equals(player.getOpenInventory().getTopInventory())) {
            Button button = menu.getButton(event.getSlot());

            if (button == null || !button.isMoveAble()) {
                event.setCancelled(true);
            }

            if (button != null) {

                if (menu.isClickSound() && API.getProfile(player).getSettingData().isMenuSound()) {
                    player.playSound(player.getLocation(), SoundsLocale.getSound(SoundsLocale.MENU_BUTTON_CLICK), 1.0f, 1.0f);
                }

                button.onClick(event.getClick(), player);
                if (menu.isUpdateOnClick()) {
                    menu.update(player);
                }
            }
        } else {
            // shift clicks and double clicks would pull items into or out of the menu
            boolean touchesMenu = event.isShiftClick() || event.getAction() == InventoryAction.COLLECT_TO_CURSOR;
            if (!menu.isPlayerInventoryEditable() || touchesMenu) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        Menu menu = MenuService.get().get(player);
        if (menu == null) return;
        if (menu.handleDrag(player, event)) return;

        int topSize = event.getView().getTopInventory().getSize();
        for (int rawSlot : event.getRawSlots()) {
            if (rawSlot < topSize || !menu.isPlayerInventoryEditable()) {
                event.setCancelled(true);
                return;
            }
        }
    }


    @EventHandler
    public void onMenuClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        MenuService.get().remove(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        MenuService.get().remove(event.getPlayer());
    }
}
