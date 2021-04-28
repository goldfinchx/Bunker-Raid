package com.goldfinch.raid.items.obj;

import com.goldfinch.raid.items.parameters.ItemRarity;
import com.goldfinch.raid.items.parameters.ItemType;
import com.goldfinch.raid.core.Raid;
import com.goldfinch.raid.utils.ItemBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;

public class BunkerItem {

    private final static MongoCollection<Document> itemsCollection =
            Raid.getInstance().getMongo().getCollection("items");

    private BasicDBObject itemQuery;
    @Getter private final Document itemDocument;

    private int _id;
    @Getter private String title;
    @Getter private Material material;
    @Getter private ItemRarity rarity;
    @Getter private ItemType type;

    public BunkerItem(int _id, String title, Material material, ItemRarity rarity, ItemType type) {
        itemDocument = new Document();

        itemDocument.put("_id", _id);
        itemDocument.put("title", title);
        itemDocument.put("material", material.name());
        itemDocument.put("rarity", rarity.name());
        itemDocument.put("type", type.name());

        itemsCollection.insertOne(itemDocument);
        Raid.getInstance().getItems().add(this);
    }

    public BunkerItem(int _id) {
        this._id = _id;

        this.itemQuery = new BasicDBObject();
        this.itemQuery.put("_id", _id);
        this.itemDocument = itemsCollection.find(itemQuery).first();

        this.title = itemDocument.getString("title");
        this.material = Material.getMaterial(itemDocument.getString("material"));
        this.rarity = ItemRarity.valueOf(itemDocument.getString("rarity"));
        this.type = ItemType.valueOf(itemDocument.getString("type"));

        Raid.getInstance().getItems().add(this);
    }

    public static void loadUpItems() {
        MongoCursor cursor = itemsCollection.find().iterator();

        Raid.getInstance().getLogger().log(Level.INFO, "Загрузка предметов.");
        Raid.getInstance().getItems().clear();
        int items = 0;
        while (cursor.hasNext()) {
            Document document = (Document) cursor.next();
            Raid.getInstance().getItems().add(new BunkerItem(document.getInteger("_id")));

            items++;
        }

        Raid.getInstance().getLogger().log(Level.INFO, "Готово. Загруженно " + items + " предметов.");
    }

    public ItemStack getAsItem() {
        ItemStack item = new ItemBuilder(this.material)
                .setDisplayName(rarity.getColor() + title)
                .setAmount(1)
                .build();

        return item;
    }

}
