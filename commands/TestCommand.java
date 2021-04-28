package com.goldfinch.raid.commands;

import com.goldfinch.raid.items.obj.BunkerItem;
import com.goldfinch.raid.core.mechanics.looting.obj.Loot;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand extends Command {
    public TestCommand() {
        super("test");
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args) {
        Player player = (Player) sender;

        new Loot(new BunkerItem(1), player.getLocation()).spawn();

        return false;
    }
}
