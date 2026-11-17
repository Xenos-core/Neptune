package dev.lrxh.neptune.game.kit.menu.editor;

import dev.lrxh.neptune.API;
import dev.lrxh.neptune.configs.impl.MessagesLocale;
import dev.lrxh.neptune.game.kit.Kit;
import dev.lrxh.neptune.profile.data.GameData;
import dev.lrxh.neptune.profile.data.ProfileState;
import dev.lrxh.neptune.profile.impl.Profile;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

@UtilityClass
public class KitEditorSession {

    public void start(Player player, Kit kit, int loadout) {
        Profile profile = API.getProfile(player);
        if (profile == null || !profile.hasState(ProfileState.IN_LOBBY, ProfileState.IN_PARTY)) return;

        GameData gameData = profile.getGameData();
        gameData.setKitEditor(kit);
        gameData.setKitEditorLoadout(loadout);
        profile.setState(ProfileState.IN_KIT_EDITOR);

        List<ItemStack> layout = gameData.get(kit).getLoadout(loadout);
        if (layout.isEmpty()) layout = kit.getItems();

        player.getInventory().clear();
        player.getInventory().setContents(layout.toArray(new ItemStack[0]));
        player.updateInventory();

        MessagesLocale.KIT_EDITOR_START.send(player.getUniqueId(), Placeholder.parsed("kit", kit.getDisplayName()));
        new KitLayoutMenu(kit, loadout, RefillCategory.WEAPONRY).open(player);
    }

    public void finish(Player player, Profile profile) {
        GameData gameData = profile.getGameData();
        Kit kit = gameData.getKitEditor();
        if (kit != null) {
            gameData.get(kit).setLoadout(gameData.getKitEditorLoadout(), Arrays.asList(player.getInventory().getContents()));
            MessagesLocale.KIT_EDITOR_STOP.send(player.getUniqueId(), Placeholder.parsed("kit", kit.getDisplayName()));
        }

        profile.setState(gameData.getParty() == null ? ProfileState.IN_LOBBY : ProfileState.IN_PARTY);
    }
}
