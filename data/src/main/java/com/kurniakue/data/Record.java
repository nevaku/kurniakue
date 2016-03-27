/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.data;

import com.kurniakue.common.Common;
import com.kurniakue.common.EnumField;
import com.kurniakue.common.Tool;
import static com.kurniakue.data.KurniaKueDb.getDbCollection;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bson.Document;

/**
 *
 * @author harun1
 * @param <T>
 */
public class Record<T extends Record> extends Document {
    
    public Record() {
    }

    public Record(Map<String, Object> map) {
        super(map);
    }

    public Record(String key, Object value) {
        super(key, value);
    }

    public Record(EnumField key, Object value) {
        super(key.name(), value);
    }

    public Object put(EnumField key, Object value) {
        return super.put(String.valueOf(key), value);
    }

    public Object get(EnumField key) {
        return super.get(String.valueOf(key));
    }

    public boolean containsKey(EnumField key) {
        return super.containsKey(String.valueOf(key));
    }

    public Object remove(EnumField key) {
        return super.remove(String.valueOf(key));
    }

    public String getString(EnumField key) {
        return String.valueOf(get(key));
    }

    public String getStringNumber(EnumField key) {
        int val = getInt(key);
        return Common.formatNumber(val);
    }

    public String tstr(EnumField key) {
        return Common.tstr(get(key));
    }

    public int getInt(EnumField key) {
        return Common.tint(get(key));
    }

    public long getLong(EnumField key) {
        return Common.tlong(get(key));
    }

    public <T> T getAs(EnumField key) {
        return (T) get(key);
    }

    public <T> List<T> getList(EnumField key, Class<T> clazz) {
        return (List<T>) get(key);
    }

    public <T> T[] getArray(EnumField key, Class<T> clazz) {
        return (T[]) get(key);
    }

    public <K, V> Map<K,V> getMap(EnumField key) {
        return (Map<K,V>) get(key);
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Entry<String, Object> entrySet : entrySet()) {
            sb.append(entrySet.getKey());
            sb.append(": ");
            sb.append(entrySet.getValue());
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }

    public enum F implements EnumField {

        _id
    }

    public MongoCollection<Document> getCollection() {
        return getDbCollection(getClass());
    }

    public Record getFilter() {
        return new Record(getKey(), get(getKey()));
    }

    public EnumField getKey() {
        return F._id;
    }

    public boolean isExists() {
        return (get(F._id) != null);
    }

    public T load(Object keyValue) {
        put(getKey(), keyValue);
        load();
        return (T) this;
    }

    public T loadByField(EnumField field, Object value) {
        Document filter = new Document(field.name(), value);
        load(filter);
        return (T) this;
    }

    public T load() {
        Document filter = new Document(getKey().name(), get(getKey()));
        return load(filter);
    }

    public T load(Document filter) {
        FindIterable<Document> result = getCollection().find(filter);
        for (MongoCursor<Document> iterator = result.iterator(); iterator.hasNext();) {
            Document document = iterator.next();
            putAll(document);
            return (T) this;
        }
        return getNoRecord();
    }

    public T delete(Document filter) {
        getCollection().deleteMany(filter);
        return (T) this;
    }

    public static <T extends Record> List<T> loadList(
            Class<T> recordClass, Document filter, Document sort) {
        return loadList(Tool.neo(recordClass), filter, sort);
    }

    public static <T extends Record> List<T> loadList(
            T dummy, Document filter, Document sort) {
        List<T> list = new ArrayList<>();
        Class<T> clazz = (Class<T>) dummy.getClass();
        
        MongoCollection collection = dummy.getCollection();
        FindIterable<Document> result;
        if (filter == null) {
            result = collection.find();
        } else {
            result = collection.find(filter);
        }
        
        if (sort != null) {
            result = result.sort(sort);
        }
        
        for (MongoCursor<Document> iterator = result.iterator(); iterator.hasNext();) {
            Document document = iterator.next();
            T record = Tool.neo(clazz);
            record.putAll(document);
            list.add(record);
        }
        
        return list;
    }
    
    public T getNoRecord() {
        return null;
    }
    
    public T store() {
        if (get(Record.F._id) == null) {
            return insert();
        } else {
            return update();
        }
    }

    public T insert() {
        getCollection().insertOne(new Document(this));
        return (T) this;
    }

    public T update() {
        getCollection().updateOne(getFilter(), 
                new Document("$set", new Document(this)));
        return (T) this;
    }
}
