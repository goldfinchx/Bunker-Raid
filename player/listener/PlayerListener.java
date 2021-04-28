package com.goldfinch.raid.player.listener;

import com.goldfinch.raid.core.Manager;
import com.goldfinch.raid.core.Raid;
import com.goldfinch.raid.core.Settings;
import com.goldfinch.raid.core.parameters.RaidState;
import com.goldfinch.raid.player.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        PlayerData playerData = new PlayerData(player.getUniqueId());

        player.getInventory().clear();
        Raid.getInstance().getPlayers().add(player.getUniqueId().toString());

        if (Raid.getInstance().getState().isJoinable()) {
            Manager.sendMessage(ChatColor.GREEN + "[" + Raid.getInstance().getPlayers().size() + "/" + Settings.getMaximumPlayers() + "]" + " Игрок " + player.getDisplayName() + " вошёл в игру!");

            if (Raid.getInstance().getPlayers().size() >= Settings.getRequiredPlayers())
                Manager.startCountdown();

        } else {
            if (Raid.getInstance().getLeavedPlayers().containsKey(player.getUniqueId().toString())) {
                Raid.getInstance().getLeavedPlayers().remove(player.getUniqueId().toString());
                playerData.loadInventory();
                playerData.setHasLeaved(false);
            }

        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        PlayerData bunkerPlayer = Raid.getPlayerData(player.getUniqueId());

        Raid.getInstance().getPlayers().remove(player.getUniqueId().toString());

        // если игрок вышел, то мы его не добавляем в ливд плеерс
        if (!Raid.getInstance().getState().isJoinable())
            Raid.getInstance().getLeavedPlayers().put(player.getUniqueId().toString(), Settings.getTimeToRejoin());

        if (Raid.getInstance().getState().equals(RaidState.COUNTDOWN) && Raid.getInstance().getPlayers().size() < Settings.getRequiredPlayers()) {
            Raid.getInstance().setState(RaidState.RECRUITING);
            Bukkit.getScheduler().cancelAllTasks();
        }


        // добавляем в список вышедших, чтобы он, если что, успел войти
        // создаем его клон и в таймере отсчитываем время и если не успеет, то киляем.
        // также, нужно сделать так, что если игрок вышел из рейда и зашел обратно в лобби, если кидает сразу на рейд
        // во время же рейда в лобби выйти нельзя

        bunkerPlayer.saveData();
    }
}
