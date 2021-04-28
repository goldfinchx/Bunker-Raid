package com.goldfinch.raid.utils;

import com.goldfinch.raid.core.Raid;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;

public class PlayerUtils {

    public static void removeAllPotionEffects(Player player) {
        for (PotionEffect potionEffect : player.getActivePotionEffects())
            player.removePotionEffect(potionEffect.getType());

    }

    public static boolean isInventoryHaveEmptySlots(Player player) {
        Inventory inventory = player.getInventory();

        return inventory.firstEmpty() != -1;
    }

    public static void hideAllPlayers(Player player) {
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> player.hidePlayer(Raid.getInstance(), onlinePlayer));
    }

    public static void hidePlayerFromOthers(Player player) {
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.hidePlayer(Raid.getInstance(), player));
    }

    public static void showAllPlayers(Player player) {
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> player.showPlayer(Raid.getInstance(), onlinePlayer));
    }

    public static void showPlayerForOthers(Player player) {
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.showPlayer(Raid.getInstance(), player));
    }
}
