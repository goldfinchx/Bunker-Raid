package com.goldfinch.raid.core;

import com.goldfinch.raid.bungee.Bungee;
import com.goldfinch.raid.core.parameters.RaidState;
import com.goldfinch.raid.player.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class Manager {

    private static final Raid raid = Raid.getInstance();

    public static void startCountdown() {
        raid.setState(RaidState.COUNTDOWN);

        new BukkitRunnable() {
            int time = 60;

            @Override
            public void run() {
                if (time != 0) {

                    if (time % 10 == 0)
                        sendMessage("§aДо начала игры " + time);
                    else if (time <= 10)
                        sendMessage("§aДо начала игры " + time);

                    time--;

                } else {
                    cancel();
                    startRaid();
                }
            }
        }.runTaskTimerAsynchronously(raid, 0L, 20L);

    }

    public static void startRaid() {
        raid.setState(RaidState.LIVE);
        sendMessage("§aРейд начинается!!!");

        raid.getPlayers().forEach(uuid -> {

            PlayerData playerData = new PlayerData(UUID.fromString(uuid));
            Player player = Bukkit.getPlayer(UUID.fromString(uuid));

            player.getInventory().clear();
            playerData.loadInventory();
        });

        new BukkitRunnable() {
            @Override
            public void run() {
                if (raid.getTime() != 0) {
                    raid.setTime(raid.getTime() - 1);
                    raid.getLeavedPlayers().forEach((uuid, time) -> {
                        if ((int) time != 0) {
                            raid.getLeavedPlayers().remove(uuid);
                            raid.getLeavedPlayers().put(uuid, (int) time-1);
                        } else {
                            raid.getLeavedPlayers().remove(uuid);
                        }

                    });

                    sendMessage("§aДо конца игры осталось -> " + raid.getTime());
                } else {
                    cancel();
                    finishRaid();
                }
            }}.runTaskTimerAsynchronously(raid, 0L,20L);

    }

    public static void finishRaid() {
        raid.setState(RaidState.ENDING);
        sendMessage("§cРейд завершён!");

        Bukkit.getScheduler().runTaskLater(raid, () -> {

            raid.getLeavedPlayers().keySet().forEach(uuid -> {
                PlayerData playerData = new PlayerData(UUID.fromString(uuid));
                playerData.setHasLeaved(true);
            });

            Bukkit.getOnlinePlayers().forEach(player -> {
                player.getInventory().clear();
                Bungee.sendPlayer(player, "Lobby");
                raid.getLeavedPlayers().clear();
                Bukkit.getScheduler().cancelAllTasks();
            });

        }, 10 * 20);
    }

    public static void sendMessage(String message) {
        raid.getPlayers().forEach(uuid -> Bukkit.getPlayer(UUID.fromString(uuid)).sendMessage(message));
    }
}
