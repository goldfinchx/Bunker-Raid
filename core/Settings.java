package com.goldfinch.raid.core;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Settings {
    private static final FileConfiguration config = Raid.getInstance().getConfig();

    public static int getRaidDuration() {
        return config.getInt("raid-duration");
    }

    public static int getRequiredPlayers() {
        return config.getInt("required-players");
    }

    public static int getMaximumPlayers() {
        return config.getInt("maximum-players");
    }

    public static int getTimeToRejoin() {
        return config.getInt("rejoin-time");
    }

    public static String getMap() {
        return config.getString("map");
    }

    public static String getServerName() {
        return config.getString("server-name");
    }

    public static List<Location> getExitPoints(String mapName) {
        List<Location> exits = new ArrayList<>();

        config.getConfigurationSection("maps." + mapName + ".exit-points").getKeys(false).forEach(point -> {
            World world = Bukkit.getWorld(config.getString("maps." + mapName + ".exit-points." + point + ".world"));
            double x = config.getDouble("maps." + mapName + ".exit-points." + point + ".x");
            double y = config.getDouble("maps." + mapName + ".exit-points." + point + ".y");
            double z = config.getDouble("maps." + mapName + ".exit-points." + point + ".z");
            int yaw = config.getInt("maps." + mapName + ".exit-points." + point + ".yaw");
            int pitch = config.getInt("maps." + mapName + ".exit-points." + point + ".pitch");

            exits.add(new Location(world, x, y, z, yaw, pitch));
        });

        return exits;
    }

    public static List<Location> getSpawnPoints(String mapName) {
        List<Location> spawnPoints = new ArrayList<>();

        config.getConfigurationSection("maps." + mapName + ".spawn-points").getKeys(false).forEach(point -> {
            World world = Bukkit.getWorld(config.getString("maps." + mapName + ".spawn-points." + point + ".world"));
            double x = config.getDouble("maps." + mapName + ".spawn-points." + point + ".x");
            double y = config.getDouble("maps." + mapName + ".spawn-points." + point + ".y");
            double z = config.getDouble("maps." + mapName + ".spawn-points." + point + ".z");
            int yaw = config.getInt("maps." + mapName + ".spawn-points." + point + ".yaw");
            int pitch = config.getInt("maps." + mapName + ".spawn-points." + point + ".pitch");

            spawnPoints.add(new Location(world, x, y, z, yaw, pitch));
        });

        return spawnPoints;
    }
}
