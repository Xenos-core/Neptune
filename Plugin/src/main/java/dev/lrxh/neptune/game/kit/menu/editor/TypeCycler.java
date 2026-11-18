package dev.lrxh.neptune.game.kit.menu.editor;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * What the "Change Type" button in the item editor does: walks an item through its variants,
 * like diamond to netherite, water bucket to lava bucket, splash to lingering potion.
 */
@UtilityClass
public class TypeCycler {
    private final List<String> ARMOR_TIERS = List.of("LEATHER", "CHAINMAIL", "COPPER", "IRON", "GOLDEN", "DIAMOND", "NETHERITE");
    private final List<String> TOOL_TIERS = List.of("WOODEN", "STONE", "COPPER", "IRON", "GOLDEN", "DIAMOND", "NETHERITE");
    private final List<String> ARMOR_PIECES = List.of("HELMET", "CHESTPLATE", "LEGGINGS", "BOOTS");
    private final List<String> TOOLS = List.of("SWORD", "AXE", "PICKAXE", "SHOVEL", "HOE", "SPEAR");
    private final List<String> SKIPPED_POTIONS = List.of("water", "mundane", "thick", "awkward");

    private final List<List<Material>> GROUPS = new ArrayList<>();

    static {
        group("BUCKET", "WATER_BUCKET", "LAVA_BUCKET", "POWDER_SNOW_BUCKET");
        group("POTION", "SPLASH_POTION", "LINGERING_POTION");
        group("ARROW", "SPECTRAL_ARROW", "TIPPED_ARROW");
        group("GOLDEN_APPLE", "ENCHANTED_GOLDEN_APPLE");
        group("ICE", "PACKED_ICE", "BLUE_ICE");
        group("SAND", "RED_SAND");
        group("ANVIL", "CHIPPED_ANVIL", "DAMAGED_ANVIL");
        group("ENDER_CHEST", "CHEST", "TRAPPED_CHEST", "BARREL");
        group("COBBLESTONE", "STONE", "DEEPSLATE", "COBBLED_DEEPSLATE", "NETHERRACK", "END_STONE");
        group("OBSIDIAN", "CRYING_OBSIDIAN");
        group("FIREWORK_ROCKET", "FIRE_CHARGE");
        group("FLINT_AND_STEEL", "FIRE_CHARGE");
        group("FISHING_ROD", "CARROT_ON_A_STICK");
        group("SHEARS", "BRUSH");
        groupBySuffix("_LOG", "STRIPPED_");
        groupBySuffix("_BOAT", "_CHEST_BOAT");
        groupShulkers();
    }

    private void group(String... names) {
        List<Material> list = new ArrayList<>();
        for (String name : names) {
            Material material = Material.matchMaterial(name);
            if (material != null && material.isItem()) list.add(material);
        }
        if (list.size() > 1) GROUPS.add(list);
    }

    private void groupBySuffix(String suffix, String excluded) {
        List<Material> list = new ArrayList<>();
        for (Material material : Material.values()) {
            if (material.isLegacy() || !material.isItem()) continue;
            String name = material.name();
            if (name.endsWith(suffix) && !name.contains(excluded)) list.add(material);
        }
        if (list.size() > 1) GROUPS.add(list);
    }

    private void groupShulkers() {
        List<Material> list = new ArrayList<>();
        list.add(Material.SHULKER_BOX);
        for (Material material : Material.values()) {
            if (!material.isLegacy() && material.name().endsWith("_SHULKER_BOX")) list.add(material);
        }
        GROUPS.add(list);
    }

    public boolean isPotion(ItemStack item) {
        return item != null && item.getItemMeta() instanceof PotionMeta;
    }

    public boolean canChange(ItemStack item) {
        if (item == null || item.getType().isAir()) return false;
        return next(item.getType(), true) != null || isPotion(item);
    }

    public ItemStack change(ItemStack item, boolean forward) {
        Material next = next(item.getType(), forward);
        if (next == null) return item;
        ItemStack changed = item.withType(next);
        changed.setAmount(Math.min(item.getAmount(), next.getMaxStackSize()));
        return changed;
    }

    public ItemStack nextEffect(ItemStack item) {
        if (!(item.getItemMeta() instanceof PotionMeta meta)) return item;

        List<PotionType> types = new ArrayList<>();
        for (PotionType type : Registry.POTION) {
            if (!SKIPPED_POTIONS.contains(type.getKey().getKey())) types.add(type);
        }
        if (types.isEmpty()) return item;

        int index = types.indexOf(meta.getBasePotionType());
        meta.setBasePotionType(types.get((index + 1) % types.size()));
        ItemStack changed = item.clone();
        changed.setItemMeta(meta);
        return changed;
    }

    private Material next(Material material, boolean forward) {
        String name = material.name();

        if (name.equals("TURTLE_HELMET")) {
            return tier("HELMET", ARMOR_TIERS, forward ? 0 : ARMOR_TIERS.size() - 1, forward, true);
        }

        int split = name.indexOf('_');
        if (split > 0) {
            String prefix = name.substring(0, split);
            String suffix = name.substring(split + 1);

            if (ARMOR_PIECES.contains(suffix) && ARMOR_TIERS.contains(prefix)) {
                return tier(suffix, ARMOR_TIERS, ARMOR_TIERS.indexOf(prefix) + (forward ? 1 : -1), forward, suffix.equals("HELMET"));
            }
            if (TOOLS.contains(suffix) && TOOL_TIERS.contains(prefix)) {
                return tier(suffix, TOOL_TIERS, TOOL_TIERS.indexOf(prefix) + (forward ? 1 : -1), forward, false);
            }
        }

        for (List<Material> group : GROUPS) {
            int index = group.indexOf(material);
            if (index < 0) continue;
            int size = group.size();
            return group.get(Math.floorMod(index + (forward ? 1 : -1), size));
        }
        return null;
    }

    /**
     * Finds the next tier that exists on this server, turtle shell sits after netherite for helmets.
     */
    private Material tier(String piece, List<String> tiers, int start, boolean forward, boolean turtle) {
        List<Material> options = new ArrayList<>();
        for (String tier : tiers) {
            Material material = Material.matchMaterial(tier + "_" + piece);
            options.add(material != null && material.isItem() ? material : null);
        }
        if (turtle) options.add(Material.TURTLE_HELMET);

        int size = options.size();
        int index = Math.floorMod(start, size);
        for (int i = 0; i < size; i++) {
            Material option = options.get(index);
            if (option != null) return option;
            index = Math.floorMod(index + (forward ? 1 : -1), size);
        }
        return null;
    }

    public static List<String> names(List<Material> materials) {
        return Arrays.stream(materials.toArray(new Material[0])).map(Enum::name).toList();
    }
}
