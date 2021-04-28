package com.goldfinch.raid.core;

import com.goldfinch.raid.bungee.Bungee;
import com.goldfinch.raid.commands.TestCommand;
import com.goldfinch.raid.data.Mongo;
import com.goldfinch.raid.core.parameters.RaidState;
import com.goldfinch.raid.player.data.PlayerData;
import com.goldfinch.raid.items.obj.BunkerItem;
import com.goldfinch.raid.player.listener.PlayerListener;
import com.goldfinch.raid.utils.ReflectionUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Raid extends JavaPlugin {

    private static Raid instance;
    public static Raid getInstance() { return instance; }

    @Getter private Mongo mongo;
    private MongoCollection serversCollection;

    @Getter private Document serverDocument = new Document();
    private final BasicDBObject serverQuery = new BasicDBObject();

    @Getter@Setter private int id;
    @Getter@Setter private RaidState state;
    @Getter@Setter private int time;

    @Getter private List<String> players;
    @Getter private final HashMap<UUID, PlayerData> playersData = new HashMap<>();
    @Getter private Document leavedPlayers;

    @Getter private List<BunkerItem> items;

    @Override
    public void onEnable() {
        instance = this;

        // Подключение внутреннего конфига
        this.getConfig().options().copyDefaults();
        this.saveDefaultConfig();

        // Подключение Bungee каналов
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        // Подключение MongoDB
        this.mongo = new Mongo();
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        serversCollection = mongo.getCollection("servers");

        // Загрузка предметов
        items = new ArrayList<>();
        BunkerItem.loadUpItems();

        // Загрузка данных сервера
        this.id = getLastId()+1;
        this.state = RaidState.RECRUITING;
        this.time = Settings.getRaidDuration();
        this.players = new ArrayList();
        this.leavedPlayers = new Document();

        // Загрузка данных сервера в базу данных
        this.serverDocument.put("_id", id);
        this.serverDocument.put("serverName", Settings.getServerName());
        this.serverDocument.put("state", state.name());
        this.serverDocument.put("players", players);
        this.serverDocument.put("leavedPlayers", leavedPlayers);

        this.serverQuery.put("_id", id);
        this.serversCollection.insertOne(serverDocument);

        // Сохранение данных сервера
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            this.serverDocument = new Document();

            this.serverDocument.put("serverName", Settings.getServerName());
            this.serverDocument.put("_id", id);
            this.serverDocument.put("state", state.name());
            this.serverDocument.put("players", players);
            this.serverDocument.put("leavedPlayers", leavedPlayers);

            serversCollection.replaceOne(this.serverQuery, this.serverDocument, new UpdateOptions().upsert(true));
            this.serverDocument = (Document) serversCollection.find(serverQuery).first();
        }, 20*5, 20*5);

        // Регистрация лисенеров
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);


        // Регистрация команд
        ReflectionUtils.registerCommand("test", new TestCommand());

    }

    @Override
    public void onDisable() {
        this.serverQuery.put("_id", id);
        serversCollection.findOneAndDelete(serverQuery);

        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData playerData = getPlayerData(player.getUniqueId());
            playerData.saveData();

            Bungee.sendPlayer(player, "Lobby");
        }
    }

    public int getLastId() {
        MongoCursor cursor = serversCollection.find().iterator();

        int lastId = 0;
        while (cursor.hasNext()) {
            Document document = (Document) cursor.next();

            if (lastId<document.getInteger("_id"))
                lastId = document.getInteger("_id");
        }

        return lastId;
    }

    public static PlayerData getPlayerData(UUID uuid) {
        return new PlayerData(uuid);
    }
}
