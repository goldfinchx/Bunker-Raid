package com.goldfinch.raid.data;


import com.goldfinch.raid.core.Raid;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;

public class Mongo {

    @Getter private final MongoDatabase db;
    @Getter private final MongoClient mongoClient;

    public Mongo() {
        MongoClientURI mongoClientURI;
        mongoClientURI = new MongoClientURI(Raid.getInstance().getConfig().getString("mongo.url"));

        /*
        String host = BunkerHub.getInstance().getConfig().getString("mongo.host");
        String login = BunkerHub.getInstance().getConfig().getString("mongo.login");
        String dbName = BunkerHub.getInstance().getConfig().getString("mongo.dbName");
        String password = BunkerHub.getInstance().getConfig().getString("mongo.password");
        mongoClientURI = new MongoClientURI("mongodb://" + login + ":" + password + "@" + host + ":" + 27017);

         */

        this.mongoClient = new MongoClient(mongoClientURI);
        db = mongoClient.getDatabase(Raid.getInstance().getConfig().getString("mongo.dbName"));
    }

    public MongoCollection<Document> getCollection(String name) { return db.getCollection(Raid.getInstance().getConfig().getString("mongo.prefix") + name); }
}
