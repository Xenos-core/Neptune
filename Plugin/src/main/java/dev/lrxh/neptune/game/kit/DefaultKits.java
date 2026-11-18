package dev.lrxh.neptune.game.kit;

import dev.lrxh.neptune.game.kit.impl.KitRule;
import dev.lrxh.neptune.game.kit.menu.editor.KitEditorItems;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The kits from the reference server, created when the server has no kits yet
 * or with /neptune defaultkits. Contents index: 0-8 hotbar, 9-35 inventory,
 * 36 boots, 37 leggings, 38 chestplate, 39 helmet, 40 offhand.
 */
@UtilityClass
public class DefaultKits {

    public List<Kit> all() {
        List<Kit> kits = new ArrayList<>();
        kits.add(sword());
        kits.add(axe());
        kits.add(mace());
        kits.add(uhc());
        kits.add(netheriteOp());
        kits.add(pot());
        kits.add(smp());
        kits.add(spearMace());
        kits.add(vanilla());
        return kits;
    }

    public Kit sword() {
        ItemStack[] c = new ItemStack[41];
        c[0] = KitEditorItems.item("DIAMOND_SWORD", 1, "sharpness", 5, "unbreaking", 3);
        c[36] = KitEditorItems.armor("DIAMOND_BOOTS");
        c[37] = KitEditorItems.armor("DIAMOND_LEGGINGS");
        c[38] = KitEditorItems.armor("DIAMOND_CHESTPLATE");
        c[39] = KitEditorItems.armor("DIAMOND_HELMET");
        return kit("Sword", Material.DIAMOND_SWORD, 10, c);
    }

    public Kit axe() {
        ItemStack[] c = new ItemStack[41];
        c[0] = KitEditorItems.item("DIAMOND_SWORD", 1, "sharpness", 5, "unbreaking", 3);
        c[1] = KitEditorItems.item("DIAMOND_AXE", 1, "sharpness", 5, "efficiency", 5, "unbreaking", 3);
        c[2] = KitEditorItems.item("BOW", 1, "power", 5, "unbreaking", 3);
        c[3] = KitEditorItems.item("CROSSBOW", 1, "quick_charge", 3, "unbreaking", 3);
        c[8] = KitEditorItems.item("ARROW", 6);
        c[36] = KitEditorItems.armor("DIAMOND_BOOTS");
        c[37] = KitEditorItems.armor("DIAMOND_LEGGINGS");
        c[38] = KitEditorItems.armor("DIAMOND_CHESTPLATE");
        c[39] = KitEditorItems.armor("DIAMOND_HELMET");
        c[40] = KitEditorItems.item("SHIELD", 1, "unbreaking", 3);
        return kit("Axe", Material.DIAMOND_AXE, 11, c);
    }

    public Kit mace() {
        ItemStack[] c = new ItemStack[41];
        c[0] = KitEditorItems.item("NETHERITE_SWORD", 1, "sharpness", 5, "unbreaking", 3, "mending", 1);
        c[1] = KitEditorItems.item("NETHERITE_AXE", 1, "sharpness", 5, "efficiency", 5, "unbreaking", 3, "mending", 1);
        c[2] = KitEditorItems.item("ENDER_PEARL", 16);
        c[3] = KitEditorItems.item("GOLDEN_APPLE", 64);
        c[4] = KitEditorItems.item("WIND_CHARGE", 64);
        c[5] = KitEditorItems.item("ELYTRA", 1, "unbreaking", 3, "mending", 1);
        c[6] = KitEditorItems.item("MACE", 1, "density", 5, "wind_burst", 1, "unbreaking", 3, "mending", 1);
        c[7] = KitEditorItems.item("MACE", 1, "density", 5, "wind_burst", 1, "unbreaking", 3, "mending", 1);
        c[8] = KitEditorItems.item("SHIELD", 1, "unbreaking", 3);
        c[9] = KitEditorItems.item("WIND_CHARGE", 64);
        c[10] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[11] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[12] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[13] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[14] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[15] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[16] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[17] = KitEditorItems.item("SHULKER_BOX", 1);
        c[18] = KitEditorItems.item("ENDER_PEARL", 16);
        c[19] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[20] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[21] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[22] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[23] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[24] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[25] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[26] = KitEditorItems.item("GOLDEN_APPLE", 64);
        c[27] = KitEditorItems.item("ENDER_PEARL", 16);
        c[28] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[29] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[30] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[31] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[32] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[33] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[34] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[35] = KitEditorItems.item("TOTEM_OF_UNDYING", 1);
        c[36] = KitEditorItems.armor("NETHERITE_BOOTS");
        c[37] = KitEditorItems.armor("NETHERITE_LEGGINGS");
        c[38] = KitEditorItems.armor("NETHERITE_CHESTPLATE");
        c[39] = KitEditorItems.armor("NETHERITE_HELMET");
        c[40] = KitEditorItems.item("TOTEM_OF_UNDYING", 1);
        return kit("Mace", Material.MACE, 12, c);
    }

    public Kit uhc() {
        ItemStack[] c = new ItemStack[41];
        c[0] = KitEditorItems.item("DIAMOND_SWORD", 1, "sharpness", 5, "unbreaking", 3);
        c[1] = KitEditorItems.item("DIAMOND_AXE", 1, "sharpness", 5, "efficiency", 5, "unbreaking", 3);
        c[2] = KitEditorItems.named(KitEditorItems.item("GOLDEN_APPLE", 8), "&6Golden Apple &r&7(&r&c❤❤&r&e❤❤&r&7)");
        c[3] = KitEditorItems.named(KitEditorItems.item("HONEY_BOTTLE", 2), "&6Golden Head&r&7 (&r&c❤❤❤❤&r&e❤❤&r&7)");
        c[4] = KitEditorItems.item("OAK_PLANKS", 64);
        c[5] = KitEditorItems.item("WATER_BUCKET", 1);
        c[6] = KitEditorItems.item("LAVA_BUCKET", 1);
        c[7] = KitEditorItems.item("CROSSBOW", 1, "quick_charge", 3, "unbreaking", 3);
        c[8] = KitEditorItems.item("COBWEB", 8);
        c[12] = KitEditorItems.item("WATER_BUCKET", 1);
        c[13] = KitEditorItems.item("WATER_BUCKET", 1);
        c[14] = KitEditorItems.item("WATER_BUCKET", 1);
        c[17] = KitEditorItems.item("ARROW", 10);
        c[22] = KitEditorItems.item("LAVA_BUCKET", 1);
        c[26] = KitEditorItems.item("BOW", 1, "power", 5, "unbreaking", 3);
        c[31] = KitEditorItems.item("DIAMOND_PICKAXE", 1, "efficiency", 5, "unbreaking", 3);
        c[35] = KitEditorItems.item("OAK_PLANKS", 64);
        c[36] = KitEditorItems.armor("DIAMOND_BOOTS");
        c[37] = KitEditorItems.armor("DIAMOND_LEGGINGS");
        c[38] = KitEditorItems.armor("DIAMOND_CHESTPLATE");
        c[39] = KitEditorItems.armor("DIAMOND_HELMET");
        c[40] = KitEditorItems.item("SHIELD", 1, "unbreaking", 3);
        return kit("UHC", Material.GOLDEN_APPLE, 13, c);
    }

    public Kit netheriteOp() {
        ItemStack[] c = new ItemStack[41];
        c[0] = KitEditorItems.item("NETHERITE_SWORD", 1, "sharpness", 5, "unbreaking", 3, "mending", 1);
        c[1] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[2] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[3] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[4] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[5] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[6] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[7] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[8] = KitEditorItems.item("TOTEM_OF_UNDYING", 1);
        c[9] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[10] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[11] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[12] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[13] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[14] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[15] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[16] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[17] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[18] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[19] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[20] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[21] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[22] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[23] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[24] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[25] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[26] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[27] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[28] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[29] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[30] = KitEditorItems.item("TOTEM_OF_UNDYING", 1);
        c[31] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[32] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[33] = KitEditorItems.item("EXPERIENCE_BOTTLE", 64);
        c[34] = KitEditorItems.item("EXPERIENCE_BOTTLE", 64);
        c[35] = KitEditorItems.item("TOTEM_OF_UNDYING", 1);
        c[36] = KitEditorItems.armor("NETHERITE_BOOTS");
        c[37] = KitEditorItems.armor("NETHERITE_LEGGINGS");
        c[38] = KitEditorItems.armor("NETHERITE_CHESTPLATE");
        c[39] = KitEditorItems.armor("NETHERITE_HELMET");
        c[40] = KitEditorItems.item("GOLDEN_APPLE", 64);
        return kit("Netherite OP", Material.NETHERITE_HELMET, 14, c);
    }

    public Kit pot() {
        ItemStack[] c = new ItemStack[41];
        c[0] = KitEditorItems.item("DIAMOND_SWORD", 1, "sharpness", 5, "unbreaking", 3);
        c[1] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[2] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[3] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[4] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[5] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[6] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[7] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[8] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[9] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[10] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[11] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[12] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[13] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[14] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[15] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[16] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[17] = KitEditorItems.potion("SPLASH_POTION", "regeneration");
        c[18] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[19] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[20] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[21] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[22] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[23] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[24] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[25] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[26] = KitEditorItems.potion("SPLASH_POTION", "regeneration");
        c[27] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[28] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[29] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[30] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[31] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[32] = KitEditorItems.potion("SPLASH_POTION", "strong_healing");
        c[33] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[34] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[35] = KitEditorItems.potion("SPLASH_POTION", "regeneration");
        c[36] = KitEditorItems.armor("DIAMOND_BOOTS");
        c[37] = KitEditorItems.armor("DIAMOND_LEGGINGS");
        c[38] = KitEditorItems.armor("DIAMOND_CHESTPLATE");
        c[39] = KitEditorItems.armor("DIAMOND_HELMET");
        c[40] = KitEditorItems.item("COOKED_BEEF", 5);
        return kit("Pot", Material.SPLASH_POTION, 15, c);
    }

    public Kit smp() {
        ItemStack[] c = new ItemStack[41];
        c[0] = KitEditorItems.item("NETHERITE_SWORD", 1, "sharpness", 5, "unbreaking", 3, "mending", 1);
        c[1] = KitEditorItems.item("ENDER_PEARL", 16);
        c[2] = KitEditorItems.item("GOLDEN_APPLE", 64);
        c[3] = KitEditorItems.item("NETHERITE_AXE", 1, "sharpness", 5, "efficiency", 5, "unbreaking", 3, "mending", 1);
        c[4] = KitEditorItems.item("EXPERIENCE_BOTTLE", 64);
        c[5] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[6] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[7] = KitEditorItems.potion("SPLASH_POTION", "long_fire_resistance");
        c[8] = KitEditorItems.item("TOTEM_OF_UNDYING", 1);
        c[9] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[10] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[11] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[12] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[13] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[14] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[15] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[16] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[17] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[18] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[19] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[20] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[21] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[22] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[23] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[24] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[25] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[26] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[27] = KitEditorItems.item("NETHERITE_SWORD", 1, "sharpness", 5, "unbreaking", 3, "mending", 1);
        c[28] = KitEditorItems.item("ENDER_PEARL", 16);
        c[29] = KitEditorItems.item("GOLDEN_APPLE", 64);
        c[30] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[31] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[32] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[33] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[34] = KitEditorItems.potion("SPLASH_POTION", "long_fire_resistance");
        c[35] = KitEditorItems.potion("SPLASH_POTION", "long_fire_resistance");
        c[36] = KitEditorItems.armor("NETHERITE_BOOTS");
        c[37] = KitEditorItems.armor("NETHERITE_LEGGINGS");
        c[38] = KitEditorItems.armor("NETHERITE_CHESTPLATE");
        c[39] = KitEditorItems.armor("NETHERITE_HELMET");
        c[40] = KitEditorItems.item("SHIELD", 1, "unbreaking", 3);
        return kit("SMP", Material.SHIELD, 16, c);
    }

    public Kit spearMace() {
        ItemStack[] c = new ItemStack[41];
        c[0] = KitEditorItems.item("NETHERITE_SWORD", 1, "sharpness", 5, "unbreaking", 3, "mending", 1);
        c[1] = KitEditorItems.item("NETHERITE_AXE", 1, "sharpness", 5, "efficiency", 5, "unbreaking", 3, "mending", 1);
        c[2] = KitEditorItems.item("NETHERITE_SPEAR", 1, "unbreaking", 3, "mending", 1);
        c[3] = KitEditorItems.item("MACE", 1, "density", 5, "wind_burst", 1, "unbreaking", 3, "mending", 1);
        c[4] = KitEditorItems.item("COOKED_BEEF", 64);
        c[5] = KitEditorItems.item("GOLDEN_APPLE", 64);
        c[6] = KitEditorItems.item("ENDER_PEARL", 16);
        c[7] = KitEditorItems.item("WIND_CHARGE", 64);
        c[8] = KitEditorItems.item("SHIELD", 1, "unbreaking", 3);
        c[9] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[10] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[11] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[12] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[13] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[14] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[15] = KitEditorItems.item("ENDER_PEARL", 16);
        c[16] = KitEditorItems.item("TOTEM_OF_UNDYING", 1);
        c[17] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[18] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[19] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[20] = KitEditorItems.potion("SPLASH_POTION", "strong_strength");
        c[21] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[22] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[23] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[24] = KitEditorItems.item("ENDER_PEARL", 16);
        c[25] = KitEditorItems.item("WIND_CHARGE", 64);
        c[26] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[27] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[28] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[29] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[30] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[31] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[32] = KitEditorItems.potion("SPLASH_POTION", "strong_swiftness");
        c[33] = KitEditorItems.item("ENDER_PEARL", 16);
        c[34] = KitEditorItems.item("WIND_CHARGE", 64);
        c[35] = KitEditorItems.item("SHULKER_BOX", 1);
        c[36] = KitEditorItems.armor("NETHERITE_BOOTS");
        c[37] = KitEditorItems.armor("NETHERITE_LEGGINGS");
        c[38] = KitEditorItems.armor("NETHERITE_CHESTPLATE");
        c[39] = KitEditorItems.armor("NETHERITE_HELMET");
        c[40] = KitEditorItems.item("TOTEM_OF_UNDYING", 1);
        return kit("Spear Mace", Material.NETHERITE_SPEAR, 28, c);
    }

    /**
     * Crystal pvp kit, the builder kit: players build their own kits from the refill.
     */
    public Kit vanilla() {
        ItemStack[] c = new ItemStack[41];
        c[0] = KitEditorItems.item("NETHERITE_SWORD", 1, "sharpness", 5, "unbreaking", 3, "mending", 1);
        c[1] = KitEditorItems.item("END_CRYSTAL", 64);
        c[2] = KitEditorItems.item("OBSIDIAN", 64);
        c[3] = KitEditorItems.item("RESPAWN_ANCHOR", 64);
        c[4] = KitEditorItems.item("GLOWSTONE", 64);
        c[5] = KitEditorItems.item("GOLDEN_APPLE", 64);
        c[6] = KitEditorItems.item("ENDER_PEARL", 16);
        c[7] = KitEditorItems.item("TOTEM_OF_UNDYING", 1);
        c[8] = KitEditorItems.item("EXPERIENCE_BOTTLE", 64);
        c[9] = KitEditorItems.item("END_CRYSTAL", 64);
        c[10] = KitEditorItems.item("OBSIDIAN", 64);
        c[11] = KitEditorItems.item("ENDER_PEARL", 16);
        c[12] = KitEditorItems.item("NETHERITE_AXE", 1, "sharpness", 5, "efficiency", 5, "unbreaking", 3, "mending", 1);
        c[13] = KitEditorItems.item("NETHERITE_PICKAXE", 1, "efficiency", 5, "unbreaking", 3, "mending", 1);
        for (int i = 14; i < 36; i++) c[i] = KitEditorItems.item("TOTEM_OF_UNDYING", 1);
        c[36] = KitEditorItems.armor("NETHERITE_BOOTS");
        c[37] = KitEditorItems.armor("NETHERITE_LEGGINGS");
        c[38] = KitEditorItems.armor("NETHERITE_CHESTPLATE");
        c[39] = KitEditorItems.armor("NETHERITE_HELMET");
        c[40] = KitEditorItems.item("TOTEM_OF_UNDYING", 1);

        Kit kit = kit("Vanilla", Material.END_CRYSTAL, 22, c);
        kit.setDisplayName("&dVanilla");
        kit.setKitEditorBuilder(true);
        return kit;
    }

    private Kit kit(String name, Material icon, int editorSlot, ItemStack[] contents) {
        Kit kit = new Kit(name.replace(" ", ""), Arrays.asList(contents), new ItemStack(icon));
        kit.setDisplayName(name);
        kit.getRules().put(KitRule.ALLOW_KIT_EDITOR, true);
        kit.getRules().put(KitRule.DAMAGE, true);
        kit.setKitEditorSlot(editorSlot);
        return kit;
    }
}

