package com.goldfinch.raid.player.data;


import com.goldfinch.raid.core.Raid;
import com.goldfinch.raid.utils.ReflectionUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerData {

    @Getter private final static MongoCollection playersCollection =
            Raid.getInstance().getMongo().getCollection("players");

    @Getter private final UUID uuid;
    @Getter@Setter private int level;
    @Getter@Setter private int exp;
    @Getter@Setter private int balance;
    @Getter@Setter private int kills;
    @Getter@Setter private int deaths;
    @Getter@Setter private long joinDate;
    @Getter@Setter private boolean hasLeaved;

    @Getter private Document inventory;
    @Getter private final Document storage;

    private final BasicDBObject playerQuery = new BasicDBObject();
    @Getter private Document playerDocument;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;

        this.playerQuery.put("_id", uuid.toString());
        this.playerDocument = (Document) playersCollection.find(playerQuery).first();

        this.level = this.playerDocument.getInteger("level");
        this.exp = this.playerDocument.getInteger("exp");
        this.balance = this.playerDocument.getInteger("balance");
        this.kills = this.playerDocument.getInteger("kills");
        this.deaths = this.playerDocument.getInteger("deaths");
        this.joinDate = this.playerDocument.getLong("joinDate");
        this.hasLeaved = this.playerDocument.getBoolean("hasLeaved");

        this.storage = (Document) this.playerDocument.get("storage");
        this.inventory = (Document) this.playerDocument.get("inventory");

        Raid.getInstance().getPlayersData().put(uuid, this);
    }

    public void saveData() {
        this.playerDocument = new Document();
        this.saveInventory();

        this.playerDocument.put("_id", this.uuid.toString());
        this.playerDocument.put("level", this.level);
        this.playerDocument.put("exp", this.exp);
        this.playerDocument.put("balance", this.balance);
        this.playerDocument.put("kills", this.kills);
        this.playerDocument.put("deaths", this.deaths);
        this.playerDocument.put("joinDate", this.joinDate);
        this.playerDocument.put("hasLeaved", this.hasLeaved);

        this.playerDocument.put("storage", this.storage);
        this.playerDocument.put("inventory", this.inventory);

        playersCollection.replaceOne(this.playerQuery, this.playerDocument, new UpdateOptions().upsert(true));
        this.playerDocument = (Document) playersCollection.find(playerQuery).first();
    }

    public void saveInventory() {
        Player player = Bukkit.getPlayer(this.uuid);
        inventory = new Document();

        for (int slot = 0; slot < player.getInventory().getSize(); slot++) {
            if (player.getInventory().getItem(slot) != null) {

                String serializedItem = ReflectionUtils.itemToStringBlob(player.getInventory().getItem(slot));
                this.inventory.put(String.valueOf(slot), serializedItem);
            }
        }
    }

    public void loadInventory() {
        this.inventory.forEach((slot, serializedItem) -> {
            ItemStack deserializedItem = ReflectionUtils.stringBlobToItem((String) serializedItem);
            Bukkit.getPlayer(this.uuid).getInventory().setItem(Integer.parseInt(slot), deserializedItem);
        });
    }
}

