package com.kurniakue.data;

import static com.kurniakue.common.Common.CASH;
import com.kurniakue.common.EnumField;
import com.kurniakue.common.Tool;
import com.kurniakue.data.DbProp.N;
import com.mongodb.client.MongoCollection;
import java.util.List;
import java.util.Map;
import org.bson.Document;
import static com.kurniakue.data.KurniaKueDb.getDbCollection;

/**
 *
 * @author harun1
 */
public class Customer extends Record<Customer> implements Comparable<Customer> {


    public enum F implements EnumField {

        CustomerID, CustomerName, Alias, Email, TelegramUser, State;
    }

    public static final String COLLECTION_NAME = "customers";
    public static final Customer NO_CUSTOMER = new Customer();

    public Customer() {

    }

    @Override
    public EnumField getKey() {
        return F.CustomerName;
    }

    public Customer(Map<String, Object> dbobject) {
        putAll(dbobject);
    }

    @Override
    public String toString() {
        return "{" + getString(F.CustomerID) + ", "
                + getString(F.CustomerName) + ", "
                + getString(F.Email) + "}\n";
    }

    @Override
    public int compareTo(Customer record) {
        if (record == null) {
            return 1;
        } else {
            return getString(F.CustomerName).compareTo(record.getString(F.CustomerName));
        }
    }

    @Override
    public Customer getNoRecord() {
        return NO_CUSTOMER;
    }

    @Override
    public MongoCollection<Document> getCollection() {
        return getDbCollection(COLLECTION_NAME);
    }

    public List<Customer> getAllCustomers() {
        Document filter = new Document()
                .append(F.State.name(),
                        new Document("$ne", "NA"));
        Document sort = new Document(F.CustomerName.name(), 1);
        return loadList(this, filter, sort);
    }

    public List<Customer> getCustomerList(String keyword) {
        Document filter = new Document()
                .append(F.CustomerName.name(),
                        new Document("$regex", ".*" + keyword + ".*")
                        .append("$options", "i"))
                .append(F.State.name(),
                        new Document("$ne", "NA"));

        Document sort = new Document(F.CustomerName.name(), 1);

        List<Customer> list = Record.loadList(this, filter, sort);
        return list;
    }

    public List<Transaction> getTransactionList() {
        return new Transaction().getTrxOfThisMonthByCustomer(getString(F.CustomerName));
    }

    public List<Transaction> getTransactionListOfLastMonth() {
        return new Transaction().getTrxOfLastMonthByCustomer(getString(F.CustomerName));
    }

    public List<Transaction> getTransactionListOfMonth(String yearMonth) {
        return new Transaction().getTrxOfMonthByCustomer(
                getString(F.CustomerName),  yearMonth);
    }

    public String getInfoSaldoLastMonth() {
        List<Transaction> trxList = getTransactionListOfLastMonth();
        return getInfoSaldoOfTransaction(trxList);
    }

    public String getInfoSaldo() {
        List<Transaction> trxList = getTransactionList();
        return getInfoSaldoOfTransaction(trxList);
    }

    public String getInfoSaldoOfTransaction(List<Transaction> trxList) {
        String infoSaldo;

        int total = 0;
        for (Transaction transaction : trxList) {
            int amount = transaction.getInt(Transaction.F.Amount);
            int dcflag = transaction.getInt(Transaction.F.DCFlag);
            total += (amount * dcflag);
        }

        if (total < 0) {
            infoSaldo = "Tagihan: " + Tool.formatMoney(-total);
        } else if (total > 0) {
            infoSaldo = "Saldo: " + Tool.formatMoney(total);
        } else {
            infoSaldo = "Tidak ada saldo/tagihan";
        }

        return infoSaldo;
    }

    public Customer loadByCustomerId(String customerId) {
        return super.loadByField(F.CustomerID, customerId);
    }
    
    public void savePayment(int amount, String date) {
        int trxId = DbProp.getAndInc(N.LastTransactionID);
        Transaction transaction = new Transaction();
        transaction.put(Transaction.F.TransactionID, trxId);
        transaction.put(Transaction.F.Date, date);
        
        Item item = new Item();
        item.load(CASH);
        
        transaction.put(Transaction.F.CustomerID, getString(F.CustomerID));
        transaction.put(Transaction.F.CustomerName, getString(F.CustomerName));
        transaction.put(Transaction.F.ItemNo, item.getString(Item.F.ItemNo));
        transaction.put(Transaction.F.ItemName, item.getString(Item.F.ItemName));
        transaction.put(Transaction.F.Price, amount);
        transaction.put(Transaction.F.Count, 1);
        transaction.put(Transaction.F.Amount, amount);
        transaction.put(Transaction.F.DCFlag, 1);

        transaction.insert();
    }
}
