package dev.lrxh.neptune.game.kit.editor.command;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import dev.lrxh.neptune.game.kit.Kit;
import dev.lrxh.neptune.game.kit.KitService;
import dev.lrxh.neptune.game.kit.editor.RefillService;
import dev.lrxh.neptune.utils.CC;
import org.bukkit.entity.Player;

public class RefillCommand {

    @Command(name = "save", desc = "", usage = "<kit>")
    @Require("neptune.admin")
    public void save(@Sender Player player, Kit kit) {
        RefillService.get().saveRefillItems(player, kit);
        player.sendMessage(CC.color("&aSaved refill items for kit &f" + kit.getDisplayName()));
    }

    @Command(name = "clear", desc = "", usage = "<kit>")
    @Require("neptune.admin")
    public void clear(@Sender Player player, Kit kit) {
        RefillService.get().clearRefillItems(kit);
        player.sendMessage(CC.color("&aCleared refill items for kit &f" + kit.getDisplayName()));
    }

    @Command(name = "view", desc = "", usage = "<kit>")
    @Require("neptune.admin")
    public void view(@Sender Player player, Kit kit) {
        var items = RefillService.get().getRefillItems(kit);
        if (items.isEmpty()) {
            player.sendMessage(CC.color("&cNo refill items set for kit &f" + kit.getDisplayName()));
            return;
        }
        player.sendMessage(CC.color("&eRefill items for &f" + kit.getDisplayName() + "&e:"));
        for (int i = 0; i < items.size(); i++) {
            var item = items.get(i);
            if (item != null) {
                player.sendMessage(CC.color("  &7" + i + ": &f" + item.getType() + " &7x" + item.getAmount()));
            }
        }
    }
}
