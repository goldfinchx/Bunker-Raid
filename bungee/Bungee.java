package com.goldfinch.raid.bungee;

import com.goldfinch.raid.core.Raid;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;

public class Bungee {

    public static void sendPlayer(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);

        player.sendPluginMessage(Raid.getInstance(), "BungeeCord", out.toByteArray());
    }

}
