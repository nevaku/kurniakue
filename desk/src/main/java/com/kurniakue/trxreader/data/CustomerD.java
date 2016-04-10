/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.trxreader.data;

import com.kurniakue.common.Tool;
import com.kurniakue.trxreader.data.Customer.F;
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
public class CustomerD extends Persistor {

    private static final String COLLECTION_NAME = "customers";
    private static DBCollection customers;
    
    public static CustomerD get() {
        return Persistor.get(CustomerD.class);
    } 

    public CustomerD() {

    }

    @Override
    public void persist(Record record) {
        BasicDBObject dbobj = getDbObject(record);
        if (record.get("_id") == null) {
            customers.insert(dbobj);
        } else {
            Record query = new Record();
            query.put("_id", record.get("_id"));
            customers.update(getDbObject(query), getDbObject(record));
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
            customers = KurniaKueDb.getDb().getCollection(COLLECTION_NAME);
            customers.createIndex(new BasicDBObject(F.CustomerName.name(), 1), new BasicDBObject("unique", true));
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
    public boolean exists(String customerName) {
        return customers.find(new BasicDBObject(F.CustomerName.name(), customerName)).hasNext();
    }

    public <T extends Record> T find(String customerName, T result) {
        DBCursor cursor = customers.find(new BasicDBObject(F.CustomerName.name(), customerName));
        if (!cursor.hasNext()) {
            return result;
        }
        DBObject dbobject = cursor.next();
        result.putAll(dbobject.toMap());
        return result;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        DBCursor cursor = customers.find();
        while (cursor.hasNext()) {
            DBObject dbobject = cursor.next();
            Customer customer = new Customer(String.valueOf(dbobject.get(F.CustomerName.name())));
            customer.putAll(dbobject.toMap());
            list.add(customer);
        }

        return list;
    }

    public List<StringObj> getAllItemsAsStringObj() {
        List<StringObj> list = new ArrayList<>();
        DBCursor cursor = customers.find();
        while (cursor.hasNext()) {
            DBObject dbobject = cursor.next();
            Item item = new Item(dbobject);
            list.add(new StringObj(item, F.CustomerName));
        }

        return list;
    }

    public String getCustomerIdCounter() {
        List<Customer> allCustomers = getAllCustomers();
        int lastId = 0;
        for (Customer customer : allCustomers) {
            String id = customer.getString(F.CustomerID);
            if (id != null && id.length() >= 5)
            {
                int idc = Tool.tint(id.substring(2));
                if (idc > lastId)
                {
                    lastId = idc;
                }
            }
        }
        
        return lastId + "";
    }
    
    public Customer delete(String customerName) {
        DBObject dbobject = customers.findOne(new BasicDBObject(F.CustomerName.name(), customerName));
        if (dbobject == null) {
            return new Customer();
        }

        Customer result = new Customer();
        result.putAll(dbobject.toMap());
        customers.remove(dbobject);
        return result;
    }

}
