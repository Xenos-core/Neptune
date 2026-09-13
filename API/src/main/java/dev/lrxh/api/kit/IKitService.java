package dev.lrxh.api.kit;

import dev.lrxh.api.arena.IArena;
import org.bukkit.entity.Player;

import java.util.LinkedHashSet;
import java.util.Optional;

public interface IKitService {
    LinkedHashSet<IKit> getAllKits();

    IKit getKitByName(String name);

    IKit getKitByDisplay(String displayName);

    void removeArena(IArena arena);

    boolean addKit(IKit kit);

    boolean isInEditor(Player player);

    Optional<IKit> getEditorSessionKit(Player player);
}
