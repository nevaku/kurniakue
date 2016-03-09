/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.trxreader.data;

import static com.kurniakue.common.Common.formatMoney;
import static com.kurniakue.common.Common.formatNumber;
import static com.kurniakue.common.Common.tint;
import static com.kurniakue.common.Common.tstr;
import com.kurniakue.trxreader.data.Transaction.F;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author harun1
 */
public class TransactionD extends Persistor {

    private static final String COLLECTION_NAME = "transactions";
    private static DBCollection transactions;

    public static TransactionD get() {
        return Persistor.get(TransactionD.class);
    }

    public TransactionD() {

    }

    @Override
    public void persist(Record record) {
        BasicDBObject dbobj = getDbObject(record);
        if (record.get("_id") == null) {
            transactions.insert(dbobj);
        } else {
            Record query = new Record();
            query.put("_id", record.get("_id"));
            transactions.update(getDbObject(query), getDbObject(record));
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
            transactions = KurniaKueDb.getDb().getCollection(COLLECTION_NAME);
            transactions.createIndex(new BasicDBObject(F.TransactionID.name(), 1), new BasicDBObject("unique", true));
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
    public boolean exists(String transactionId) {
        return transactions.find(new BasicDBObject(F.TransactionID.name(), transactionId)).hasNext();
    }

    public Transaction delete(int transactionId) {
        DBObject dbobject = transactions.findOne(new BasicDBObject(F.TransactionID.name(), transactionId));
        if (dbobject == null) {
            return new Transaction();
        }

        Transaction result = new Transaction();
        result.putAll(dbobject.toMap());
        transactions.remove(dbobject);
        return result;
    }

    public Transaction find(int transactionId, Transaction result) {
        DBObject dbobject = transactions.findOne(new BasicDBObject(F.TransactionID.name(), transactionId));
        if (dbobject == null) {
            return result;
        }

        result.putAll(dbobject.toMap());
        return result;
    }

    public List<Transaction> getTransactionsByDate(String dateStr) {
        List<Transaction> list = new ArrayList<>();
        if (dateStr == null) {
            return list;
        }
        DBCursor cursor;
        if ("".equals(dateStr)) {
            cursor = transactions.find();
        } else {
            cursor = transactions.find(new BasicDBObject(F.Date.name(), dateStr));
        }
        while (cursor.hasNext()) {
            DBObject dbobject = cursor.next();
            Transaction transaction = new Transaction();
            transaction.putAll(dbobject.toMap());
            list.add(transaction);
        }

        return list;
    }

    public void fixAmount(String dateStr) {
        DBCursor cursor;
        if (dateStr == null || "".equals(dateStr)) {
            cursor = transactions.find();
        } else {
            cursor = transactions.find(new BasicDBObject(F.Date.name(), dateStr));
        }
        while (cursor.hasNext()) {
            DBObject query = cursor.next();
            DBObject update = new BasicDBObject();
            update.putAll(query);
            update.put(F.Amount.name(), tint(query.get(F.Price.name())) * tint(query.get(F.Count.name())));
            transactions.update(query, update);
        }
    }

    public int getAmountOfTheDate(String dateStr) {
        int amount = 0;

        DBCursor cursor;
        if (dateStr == null || "".equals(dateStr)) {
            cursor = transactions.find();
        } else {
            cursor = transactions.find(new BasicDBObject(F.Date.name(), dateStr));
        }
        while (cursor.hasNext()) {
            DBObject query = cursor.next();
            if (tint(query.get(F.DCFlag.name())) < 0) {
                amount += tint(query.get(F.Amount.name()));
            }
        }
        return amount;
    }

    public int getAmountOfTheMonth(String dateStr) {
        int amount = 0;

        DBCursor cursor = transactions.find();

        String monthStr;
        if (dateStr == null || "".equals(dateStr) || dateStr.length() < 10) {
            monthStr = "";
        } else {
            monthStr = dateStr.substring(0, 7);
        }
        while (cursor.hasNext()) {
            DBObject query = cursor.next();
            if (tstr(query.get(F.Date.name())).startsWith(monthStr)) {
                if (tint(query.get(F.DCFlag.name())) < 0) {
                    amount += tint(query.get(F.Amount.name()));
                }
            }
        }
        return amount;
    }

    public void addCreditFlag(String dateStr) {
        DBCursor cursor;
        if (dateStr == null || "".equals(dateStr)) {
            cursor = transactions.find();
        } else {
            cursor = transactions.find(new BasicDBObject(F.Date.name(), dateStr));
        }
        while (cursor.hasNext()) {
            DBObject query = cursor.next();
            DBObject update = new BasicDBObject();
            update.putAll(query);
            update.put(F.DCFlag.name(), -1);
            transactions.update(query, update);
        }
    }

    public List<Transaction> getTransactionsOfTheMonth(String yearMonth) {
        List<Transaction> transactionList = new ArrayList<>();
        //getQuery.put("employeeId", new BasicDBObject("$gt", 2).append("$lt", 5));
        DBCursor cursor = transactions.find(new BasicDBObject("Date",
                new BasicDBObject("$regex", yearMonth + ".*").append("$options", "i")
        )).sort(new BasicDBObject("Date", 1));
        while (cursor.hasNext()) {
            DBObject result = cursor.next();
            Transaction transaction = new Transaction();
            transaction.putAll(result.toMap());
            transactionList.add(transaction);
            System.out.println(
                    transaction.getString(Transaction.F.Date)
                    + transaction.getString(Transaction.F.ItemName)
                    + transaction.getString(Transaction.F.CustomerName)
            );
        }

        return transactionList;
    }

    public void printCustomerAtMonth(String customerName, String yearMonth) {
        DBCursor cursor = transactions.find(
                b("Date", b("$regex", yearMonth + ".*").append("$options", "i"))
                .append("CustomerName", customerName)
        ).sort(b("Date", 1));
        
        int total = 0;
        while (cursor.hasNext()) {
            DBObject result = cursor.next();
            Transaction transaction = new Transaction();
            transaction.putAll(result.toMap());
            
            int count = transaction.getInt(Transaction.F.Count);
            int amount = -transaction.getInt(Transaction.F.Amount);
            int dcflag = transaction.getInt(Transaction.F.DCFlag);
            total += (amount * dcflag);
            String samount;
            if (count > 1)
            {
                samount = count + " x @" + 
                          formatNumber(transaction.getInt(Transaction.F.Price))
                          + " = " + 
                          formatNumber(amount * dcflag);
            }
            else
            {
                samount = formatNumber(amount * dcflag);
            }
            System.out.println(transaction.getString(Transaction.F.Date)
                    + " " + transaction.getString(Transaction.F.ItemName)
                    + ": " + samount 
            );
        }
        
        System.out.println("Total: " + formatMoney(total));
    }

    public void recapitulateCustomerAtMonth(String customerName, String yearMonth) {
        List<DBObject> list = new ArrayList<>();
        list.add(b("$match",
                b("Date", b("$regex", yearMonth + ".*").
                        append("$options", "i"))
                .append("CustomerName", customerName))
        );
        list.add(b("$group",
                b("_id", "$CustomerName").
                append("total",
                        b("$sum", "$Amount")
                      )
                )
        );
        AggregationOutput output = transactions.aggregate(list);
        for (Iterator iterator = output.results().iterator(); iterator.hasNext();) {
            Object next = iterator.next();
            System.out.println(next);
        }
    }

    public static BasicDBObject b(String key, Object value) {
        return new BasicDBObject(key, value);
    }
}