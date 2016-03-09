/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.trxreader.data;

import com.kurniakue.trxreader.data.Item.F;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author harun1
 */
public class ItemD extends Persistor {

    private static final String COLLECTION_NAME = "items";
    private static DBCollection items;

    public ItemD() {

    }
    
    public static ItemD get() {
        return Persistor.get(ItemD.class);
    } 

    @Override
    public void persist(Record record) {
        BasicDBObject dbobj = getDbObject(record);
        if (record.get("_id") == null) {
            items.insert(dbobj);
        } else {
            Record query = new Record();
            query.put("_id", record.get("_id"));
            items.update(getDbObject(query), getDbObject(record));
        }
    }

    private BasicDBObject getDbObject(Record record) {
        BasicDBObject ret = new BasicDBObject(record.size());
        Set<Map.Entry<String, Object>> entries = record.entrySet();
        entries.stream().forEach((entry) -> {
            ret.put(entry.getKey(), entry.getValue());
        });
        return ret;
    }

    @Override
    public void init() {
        try {
            items = KurniaKueDb.getDb().getCollection(COLLECTION_NAME);
            items.createIndex(new BasicDBObject(F.ItemNo.name(), 1), new BasicDBObject("unique", true));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {
    }

    @Override
    public void reset() {
        Set<String> collectionNames = KurniaKueDb.getDb().getCollectionNames();
        if (collectionNames.contains(COLLECTION_NAME)) {
            DBCollection collection = KurniaKueDb.getDb().getCollection(COLLECTION_NAME);
            collection.drop();
        }
    }

    @Override
    public boolean exists(String itemNo) {
        return items.find(new BasicDBObject(F.ItemNo.name(), itemNo)).hasNext();
    }

    public Item find(String itemNo, Item result) {
        DBCursor cursor = items.find(new BasicDBObject(F.ItemNo.name(), itemNo));
        if (!cursor.hasNext()) {
            return result;
        }
        DBObject dbobject = cursor.next();
        result.putAll(dbobject.toMap());
        return result;
    }

    public List<Item> getAllItems() {
        List<Item> list = new ArrayList<>();
        DBCursor cursor = items.find();
        while (cursor.hasNext()) {
            DBObject dbobject = cursor.next();
            Item item = new Item(dbobject);
            list.add(item);
        }

        return list;
    }

    public List<StringObj> getAllItemsAsStringObj() {
        List<StringObj> list = new ArrayList<>();
        DBCursor cursor = items.find();
        while (cursor.hasNext()) {
            DBObject dbobject = cursor.next();
            Item item = new Item(dbobject);
            list.add(new StringObj(item, F.ItemNo));
        }

        return list;
    }

}
