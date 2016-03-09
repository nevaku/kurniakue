/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.trxreader.data;

import com.kurniakue.common.EnumField;
import com.kurniakue.trxreader.data.Prop.F;
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
public class PropD extends Persistor {

    private static final String COLLECTION_NAME = "props";
    private static DBCollection props;

    public PropD() {

    }
    
    public static PropD get() {
        return Persistor.get(PropD.class);
    } 

    @Override
    public void persist(Record record) {
        BasicDBObject dbobj = getDbObject(record);
        if (record.get("_id") == null) {
            props.insert(dbobj);
        } else {
            Record query = new Record();
            query.put("_id", record.get("_id"));
            props.update(getDbObject(query), getDbObject(record));
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
            props = KurniaKueDb.getDb().getCollection(COLLECTION_NAME);
            props.createIndex(new BasicDBObject(F.PropertyName.name(), 1), new BasicDBObject("unique", true));
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
    public boolean exists(String propertyName) {
        return props.find(new BasicDBObject(F.PropertyName.name(), propertyName)).hasNext();
    }

    public Prop find(EnumField property, Prop result) {
        return find(property.name(), result);
    }

    public Prop find(String propertyName, Prop result) {
        
        DBCursor cursor = props.find(new BasicDBObject(F.PropertyName.name(), propertyName));
        if (!cursor.hasNext()) {
            return result;
        }
        DBObject dbobject = cursor.next();
        result.putAll(dbobject.toMap());
        return result;
    }

    public List<Prop> getAllProps() {
        List<Prop> list = new ArrayList<>();
        DBCursor cursor = props.find();
        while (cursor.hasNext()) {
            DBObject dbobject = cursor.next();
            Prop prop = new Prop(dbobject);
            list.add(prop);
        }

        return list;
    }

    public List<StringObj> getAllPropsAsStringObj() {
        List<StringObj> list = new ArrayList<>();
        DBCursor cursor = props.find();
        while (cursor.hasNext()) {
            DBObject dbobject = cursor.next();
            Prop prop = new Prop(dbobject);
            list.add(new StringObj(prop, F.PropertyName));
        }

        return list;
    }

}
