package dev.lrxh.neptune.game.kit.menu.editor;

import dev.lrxh.neptune.Neptune;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Waits for the next chat message of a player (used for renaming kits and items).
 * The callback gets null when the player types cancel.
 */
public class ChatPrompt implements Listener {
    private static final Map<UUID, Consumer<String>> PROMPTS = new ConcurrentHashMap<>();

    public static void ask(Player player, Consumer<String> callback) {
        PROMPTS.put(player.getUniqueId(), callback);
    }

    public static boolean isWaiting(Player player) {
        return PROMPTS.containsKey(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncChatEvent event) {
        Consumer<String> callback = PROMPTS.remove(event.getPlayer().getUniqueId());
        if (callback == null) return;

        event.setCancelled(true);
        String input = PlainTextComponentSerializer.plainText().serialize(event.message()).trim();
        String result = input.isEmpty() || input.equalsIgnoreCase("cancel") ? null : input;
        Bukkit.getScheduler().runTask(Neptune.get(), () -> {
            if (event.getPlayer().isOnline()) callback.accept(result);
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        PROMPTS.remove(event.getPlayer().getUniqueId());
    }
}
