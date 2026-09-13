package dev.lrxh.neptune.game.kit.editor;

import dev.lrxh.neptune.game.kit.Kit;
import dev.lrxh.neptune.game.kit.KitService;
import dev.lrxh.neptune.utils.ItemUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RefillService {

    private static RefillService instance;

    public static RefillService get() {
        if (instance == null) {
            instance = new RefillService();
        }
        return instance;
    }

    public void saveRefillItems(Player player, Kit kit) {
        List<ItemStack> items = new ArrayList<>();
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                items.add(item.clone());
            }
        }
        kit.setRefillItems(items);
        KitService.get().save();
    }

    public void clearRefillItems(Kit kit) {
        kit.setRefillItems(new ArrayList<>());
        KitService.get().save();
    }

    public List<ItemStack> getRefillItems(Kit kit) {
        return kit.getRefillItems() != null ? kit.getRefillItems() : new ArrayList<>();
    }

    public List<ItemStack> mergeWithLoadout(List<ItemStack> loadout, Kit kit) {
        List<ItemStack> merged = new ArrayList<>(loadout);
        List<ItemStack> refill = getRefillItems(kit);

        for (ItemStack refillItem : refill) {
            if (refillItem == null) continue;
            boolean found = false;
            for (int i = 0; i < merged.size(); i++) {
                ItemStack loadoutItem = merged.get(i);
                if (loadoutItem != null && loadoutItem.isSimilar(refillItem)) {
                    int total = loadoutItem.getAmount() + refillItem.getAmount();
                    if (total <= loadoutItem.getMaxStackSize()) {
                        loadoutItem.setAmount(total);
                    } else {
                        loadoutItem.setAmount(loadoutItem.getMaxStackSize());
                        ItemStack extra = refillItem.clone();
                        extra.setAmount(total - loadoutItem.getMaxStackSize());
                        merged.add(extra);
                    }
                    found = true;
                    break;
                }
            }
            if (!found) {
                merged.add(refillItem.clone());
            }
        }

        return merged;
    }
}
