package com.goldfinch.raid.core.mechanics.looting.obj;

import com.goldfinch.raid.items.obj.BunkerItem;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.util.EulerAngle;

public class Loot {

    private final BunkerItem item;
    private final Location location;

    public Loot(BunkerItem item, Location location) {
        this.item = item;
        this.location = location;
    }

    public void spawn() {
        location.subtract(0, 1.4, 0);
        ArmorStand lootDoll = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

        lootDoll.setVisible(false);
        lootDoll.setGravity(false);
        lootDoll.setInvulnerable(true);
        lootDoll.setRemoveWhenFarAway(false);

        lootDoll.setRightArmPose(new EulerAngle(Math.toRadians(230), Math.toRadians(0), Math.toRadians(90)));
        lootDoll.setItemInHand(this.item.getAsItem());
    }

}
