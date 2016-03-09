/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.trxreader.data;

import com.mongodb.DB;
import com.mongodb.MongoClient;

/**
 *
 * @author harun1
 */
public class KurniaKueDb {

    public static final String DB_NAME = "kurniakue";
    private static MongoClient mongoClient;
    private static DB db;

    public static MongoClient getMongoClient() {
        return mongoClient;
    }

    public static DB getDb() {
        return db;
    }

    private KurniaKueDb() {

    }

    public static void init() {
        try {
            mongoClient = new MongoClient();
            db = mongoClient.getDB(DB_NAME);
            initCollections();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private static void initCollections()
    {
        CustomerD.get().init();
        ItemD.get().init();
        TransactionD.get().init();
        PropD.get().init();
    }

    public static void stop() {
        if (mongoClient == null) {
            return;
        }
        mongoClient.close();
    }

    public static void reset() {
        Persistor.get(CustomerD.class).reset();
        Persistor.get(ItemD.class).reset();
    }
}
