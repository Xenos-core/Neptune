package dev.lrxh.neptune.game.kit.editor.event;

import dev.lrxh.neptune.game.kit.Kit;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import org.bukkit.inventory.ItemStack;

@Getter
public class KitEditorSaveEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Kit kit;
    private final List<ItemStack> newLoadout;

    public KitEditorSaveEvent(Player player, Kit kit, List<ItemStack> newLoadout) {
        this.player = player;
        this.kit = kit;
        this.newLoadout = newLoadout;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
