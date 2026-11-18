package dev.lrxh.neptune.game.kit.menu.editor;

import dev.lrxh.neptune.API;
import dev.lrxh.neptune.configs.impl.MenusLocale;
import dev.lrxh.neptune.configs.impl.MessagesLocale;
import dev.lrxh.neptune.game.kit.Kit;
import dev.lrxh.neptune.profile.data.GameData;
import dev.lrxh.neptune.profile.data.KitData;
import dev.lrxh.neptune.profile.data.ProfileState;
import dev.lrxh.neptune.profile.impl.Profile;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks what a player is editing.
 * Layout mode: a normal kit, the layout lives in the menu (Edit Layout).
 * Builder mode: a builder kit (Vanilla), the kit lives in the player's own inventory (Kit Editor).
 */
@UtilityClass
public class KitEditorSession {
    public enum Mode {LAYOUT, BUILDER}

    private final Map<UUID, Session> sessions = new ConcurrentHashMap<>();

    @Getter
    @Setter
    public static class Session {
        private final Kit kit;
        private final int loadout;
        private final Mode mode;
        // layout mode only, 0-35 inventory, 36-39 armor (boots first), 40 offhand
        private final ItemStack[] contents;
        private Catalog category = Catalog.WEAPONRY;
        private KitLayoutMenu layoutMenu;

        Session(Kit kit, int loadout, Mode mode, ItemStack[] contents) {
            this.kit = kit;
            this.loadout = loadout;
            this.mode = mode;
            this.contents = contents;
        }
    }

    public Session get(Player player) {
        return sessions.get(player.getUniqueId());
    }

    private Profile enter(Player player) {
        Profile profile = API.getProfile(player);
        if (profile == null || !profile.hasState(ProfileState.IN_LOBBY, ProfileState.IN_PARTY)) return null;
        return profile;
    }

    public void startLayout(Player player, Kit kit) {
        Profile profile = enter(player);
        if (profile == null) return;

        GameData gameData = profile.getGameData();
        List<ItemStack> layout = gameData.get(kit).getLoadout(0);
        if (layout.isEmpty()) layout = kit.getItems();

        Session session = new Session(kit, 0, Mode.LAYOUT, KitEditorItems.copy(layout, 41));
        sessions.put(player.getUniqueId(), session);

        gameData.setKitEditor(kit);
        gameData.setKitEditorLoadout(0);
        profile.setState(ProfileState.IN_KIT_EDITOR);

        KitEditorSounds.play(player, KitEditorSounds.CLICK, KitEditorSounds.SAVE);
        new KitLayoutMenu(session).open(player);
    }

    public void startBuilder(Player player, Kit kit, int loadout) {
        Profile profile = enter(player);
        if (profile == null) return;

        GameData gameData = profile.getGameData();
        KitData kitData = gameData.get(kit);

        // creating a new kit starts from the kit's default items
        if (kitData.getLoadoutName(loadout) == null) {
            kitData.setLoadoutName(loadout, MenusLocale.KIT_EDITOR_SLOTS_DEFAULT_NAME.getString()
                    .replace("<slot>", String.valueOf(loadout + 1)));
            kitData.setLoadout(loadout, kit.getItems());
        }

        List<ItemStack> layout = kitData.getLoadout(loadout);
        if (layout.isEmpty()) layout = kit.getItems();
        ItemStack[] contents = KitEditorItems.copy(layout, 41);

        sessions.put(player.getUniqueId(), new Session(kit, loadout, Mode.BUILDER, null));

        gameData.setKitEditor(kit);
        gameData.setKitEditorLoadout(loadout);
        profile.setState(ProfileState.IN_KIT_EDITOR);

        player.getInventory().setContents(contents);
        player.updateInventory();

        MessagesLocale.KIT_EDITOR_START.send(player.getUniqueId(), Placeholder.parsed("kit", kit.getDisplayName()));
        new KitBuilderMenu(get(player)).open(player);
    }

    public void finish(Player player, Profile profile) {
        Session session = sessions.remove(player.getUniqueId());
        GameData gameData = profile.getGameData();

        if (session != null) {
            KitData kitData = gameData.get(session.getKit());
            if (session.getMode() == Mode.LAYOUT) {
                kitData.setLoadout(0, Arrays.asList(session.getContents()));
            } else {
                kitData.setLoadout(session.getLoadout(), Arrays.asList(player.getInventory().getContents()));
                kitData.setSelectedLoadout(session.getLoadout());
            }
            MessagesLocale.KIT_EDITOR_STOP.send(player.getUniqueId(), Placeholder.parsed("kit", session.getKit().getDisplayName()));
        }

        gameData.setKitEditor(null);
        profile.setState(gameData.getParty() == null ? ProfileState.IN_LOBBY : ProfileState.IN_PARTY);
    }

    public void clear(Player player) {
        sessions.remove(player.getUniqueId());
    }
}
