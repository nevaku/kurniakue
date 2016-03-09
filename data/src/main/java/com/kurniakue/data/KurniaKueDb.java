/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.data;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author harun1
 */
public class KurniaKueDb {

    private static final ThreadLocal<KurniaKueDb> instance
            = new ThreadLocal<>();

    public static KurniaKueDb getData() {
        KurniaKueDb ins = instance.get();
        if (ins == null) {
            ins = new KurniaKueDb();
            instance.set(ins);
            ins.init();
        }

        return ins;
    }

    private static final List<MongoClient> allClients = new ArrayList<>();

    private MongoClient mongoClient;

    public MongoClient getMongoClient() {
        return mongoClient;
    }
    private MongoDatabase db;

    public static MongoDatabase getDb() {
        return getData().db;
    }
    
    public static MongoCollection<Document> getDbCollection(Class collectionClass) {
        return getDb().getCollection(collectionClass.getSimpleName());
    }
    
    public static MongoCollection<Document> getDbCollection(String collectionName) {
        return getDb().getCollection(collectionName);
    }
    
    private static final TheConfig config = new TheConfig();

    public static TheConfig getConfig() {
        return config;
    }

    private KurniaKueDb() {

    }

    public void init() {
        try {
            mongoClient = new MongoClient(config.getProperty("DBIP"), config.getInt("DBPort"));
            if (mongoClient == null) {
                return;
            }
            db = mongoClient.getDatabase(config.getProperty("DBName"));
            allClients.add(mongoClient);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void stopAll() {
        for (MongoClient client : allClients) {
            client.close();
        }
    }
}
