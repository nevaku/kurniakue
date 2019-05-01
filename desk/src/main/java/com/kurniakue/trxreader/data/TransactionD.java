/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.trxreader.data;

import static com.kurniakue.common.Common.CASH;
import static com.kurniakue.common.Common.RKAP;
import com.kurniakue.common.Tool;
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

    public List<Transaction> getTransactionsNonRkapByDate(String dateStr) {
        List<Transaction> list = new ArrayList<>();
        if (dateStr == null) {
            return list;
        }
        DBCursor cursor;
        if ("".equals(dateStr)) {
            cursor = transactions.find();
        } else {
            cursor = transactions.find(
                    new BasicDBObject(F.Date.name(), dateStr)
                    .append(F.ItemNo.name(), new BasicDBObject("$ne", "RKAP"))
            );
        }
        cursor.sort(new BasicDBObject(F.ItemNo.name(), 1)
                .append(F.TransactionID.name(), 1));

        while (cursor.hasNext()) {
            DBObject dbobject = cursor.next();
            Transaction transaction = new Transaction();
            transaction.putAll(dbobject.toMap());
            list.add(transaction); //filter by itemno and item name RKAP - saldo, fix amount to + instead of -
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
            update.put(F.Amount.name(), Tool.tint(query.get(F.Price.name())) * Tool.tint(query.get(F.Count.name())));
            transactions.update(query, update);
        }
    }

    public int getAmountNonRkapOfTheDate(String dateStr) {
        int amount = 0;

        DBCursor cursor;
        if (dateStr == null || "".equals(dateStr)) {
            cursor = transactions.find();
        } else {
            cursor = transactions.find(new BasicDBObject(F.Date.name(), dateStr)
                    .append(F.ItemNo.name(), new BasicDBObject("$ne", "RKAP")));
        }
        while (cursor.hasNext()) {
            DBObject query = cursor.next();
            if (Tool.tint(query.get(F.DCFlag.name())) < 0) {
                amount += Tool.tint(query.get(F.Amount.name()));
            }
        }
        return amount;
    }

    public int getAmountNonRkapOfTheMonth(String dateStr) {
        int amount = 0;
        String yearMonth;
        if (dateStr == null || "".equals(dateStr) || dateStr.length() < 10) {
            yearMonth = "";
        } else {
            yearMonth = dateStr.substring(0, 7);
        }

        DBCursor cursor = transactions.find(
                new BasicDBObject("Date",
                        new BasicDBObject("$regex", yearMonth + ".*").append("$options", "i"))
                .append(F.ItemNo.name(), new BasicDBObject("$ne", "RKAP")));

        while (cursor.hasNext()) {
            DBObject query = cursor.next();
            if (Tool.tint(query.get(F.DCFlag.name())) < 0) {
                amount += Tool.tint(query.get(F.Amount.name()));
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
            int dcflag = -1;
            if (CASH.equals(update.get(F.ItemNo.name()))) {
                dcflag = 1;
            }
            update.put(F.DCFlag.name(), dcflag);
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
            if (count > 1) {
                samount = count + " x @"
                        + Tool.formatNumber(transaction.getInt(Transaction.F.Price))
                        + " = "
                        + Tool.formatNumber(amount * dcflag);
            } else {
                samount = Tool.formatNumber(amount * dcflag);
            }
            System.out.println(transaction.getString(Transaction.F.Date)
                    + " " + transaction.getString(Transaction.F.ItemName)
                    + ": " + samount
            );
        }

        System.out.println("Total: " + Tool.formatMoney(total));
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

    public void fixRkapField(String dateStr) {
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
            Record record  = Record.r(update);
            if (!RKAP.equals(record.get(F.ItemNo))) {
                continue;
            }
            if (!record.getString(F.ItemName).startsWith("Saldo")) {
                continue;
            }
            
            if (record.getInt(F.Amount) < 0)  {
                update.put(F.Amount.name(), record.getInt(F.Amount) * -1);
            }
            if (record.getInt(F.DCFlag) < 0)  {
                update.put(F.DCFlag.name(), record.getInt(F.DCFlag) * -1);
            }
            
            // uncomment to actually update DB
            // and comment it back to prevent accidental update DB
            // transactions.update(query, update);
        }
    }
}
