package dev.lrxh.neptune.game.kit.editor.event;

import dev.lrxh.neptune.game.kit.Kit;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class KitEditorCloseEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Kit kit;

    public KitEditorCloseEvent(Player player, Kit kit) {
        this.player = player;
        this.kit = kit;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
