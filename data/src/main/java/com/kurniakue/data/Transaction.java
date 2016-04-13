package com.kurniakue.data;

import static com.kurniakue.common.Common.*;
import com.kurniakue.common.EnumField;
import com.kurniakue.common.Tool;
import com.kurniakue.data.DbProp.N;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.bson.Document;
import static com.kurniakue.data.KurniaKueDb.getDbCollection;
import com.mongodb.client.result.UpdateResult;

/**
 *
 * @author harun1
 */
public class Transaction extends Record<Transaction> implements Comparable<Transaction> {

    public enum F implements EnumField {

        _id,
        TransactionID, Date, Category, Subcategory, Description,
        CustomerID, CustomerName,
        ItemNo, ItemName, Price,
        Count, Amount, DCFlag,
        TrxAccounts;
    }

    public enum V implements EnumField {

        Rekap
    }

    public static final String COLLECTION_NAME = "transactions";

    public Transaction() {

    }

    @Override
    public EnumField getKey() {
        return F.TransactionID;
    }

    public Transaction(Map<String, Object> dbobject) {
        putAll(dbobject);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(1000);
        sb.append("{").append(getString(F.TransactionID)).append(", ");
        sb.append(getString(F.Date)).append(", ");
        sb.append(getString(F.CustomerID)).append(", ");
        sb.append(getString(F.CustomerName)).append(", ");
        sb.append(getString(F.ItemNo)).append(", ");
        sb.append(getString(F.ItemName)).append(", ");
        sb.append(getString(F.Amount)).append(", ");
        
        List<TrxAccount> trxAccounts_ = getTrxAccounts();
        if (trxAccounts_ != null) {
            sb.append("[");
            for (TrxAccount trxAccount : trxAccounts_) {
                sb.append(trxAccount.toString()).append(", ");
            }
            sb.append("]");
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public int compareTo(Transaction record) {
        if (record == null) {
            return 1;
        } else {
            return getString(F.TransactionID).compareTo(record.getString(F.TransactionID));
        }
    }

    @Override
    public Transaction getNoRecord() {
        return null;
    }

    @Override
    public MongoCollection<Document> getCollection() {
        return getDbCollection(COLLECTION_NAME);
    }

    public List<Transaction> getTrxOfThisMonthByCustomer(String customerName) {
        String thisYearMonth
                = DateInfo.getDateInfo(Calendar.getInstance())
                .getString(DateInfo.F.ThisYearMonth);
        return getTrxOfMonthByCustomer(customerName, thisYearMonth);
    }

    public List<Transaction> getTrxOfLastMonthByCustomer(String customerName) {
        String lastYearMonth
                = DateInfo.getDateInfo(Calendar.getInstance())
                .getString(DateInfo.F.LastYearMonth);
        return getTrxOfMonthByCustomer(customerName, lastYearMonth);
    }

    public List<Transaction> getTrxOfMonthByCustomer(
            String customerName, String yearMonth) {

        Document filter = new Document()
                .append(F.Date.name(),
                        new Document("$regex", yearMonth + ".*")
                        .append("$options", "i"))
                .append(F.CustomerName.name(), customerName);

        Document sort = new Document(F.Date.name(), 1);

        List<Transaction> list = Record.loadList(this, filter, sort);
        return list;
    }

    public List<Record> getRekapOfMonthPerCustomer(String yearMonth) {
        Document filter = new Document()
                .append(F.Date.name(),
                        new Document("$regex", yearMonth + ".*")
                        .append("$options", "i"));

        Document group = new Document()
                .append(F._id.name(), "$CustomerName")
                .append(V.Rekap.name(), new Document("$sum",
                        new Document("$multiply", asList("$Amount", "$DCFlag"))));

        AggregateIterable<Document> iterable = getCollection()
                .aggregate(asList(
                        new Document("$match", filter),
                        new Document("$group", group)));

        List<Record> list = new ArrayList<>();
        for (Document document : iterable) {
            list.add(new Record(document));
        }

        Collections.sort(list,
                (Record trx1, Record trx2) -> trx1.getString(F._id).compareTo(
                trx2.getString(F._id)));

        return list;
    }

    public List<Record> getPaymentsOfMonthPerCustomer(String yearMonth) {
        Document filter = new Document()
                .append(F.Date.name(),
                        new Document("$regex", yearMonth + ".*")
                        .append("$options", "i"))
                .append(F.ItemNo.name(), CASH);

        Document group = new Document()
                .append(F._id.name(), "$CustomerName")
                .append(V.Rekap.name(), new Document("$sum",
                        new Document("$multiply", asList("$Amount", "$DCFlag"))));

        AggregateIterable<Document> iterable = getCollection()
                .aggregate(asList(
                        new Document("$match", filter),
                        new Document("$group", group)));

        List<Record> list = new ArrayList<>();
        for (Document document : iterable) {
            list.add(new Record(document));
        }

        Collections.sort(list,
                (Record trx1, Record trx2) -> trx1.getString(F._id).compareTo(
                trx2.getString(F._id)));

        return list;
    }

    public void recapitulate(DateInfo dateInfo) {
        List<Record> list = getRekapOfMonthPerCustomer(dateInfo.getString(DateInfo.F.LastYearMonth));
        for (Record record : list) {
            new Transaction().deleteRekapOf(
                    record.getString(F._id), dateInfo.getString(DateInfo.F.ThisYearMonth));
            long amount = record.getLong(V.Rekap);
            if (amount == 0) {
                continue;
            }

            Customer customer = new Customer().load(record.getString(F._id));
            Item item = new Item().load(RKAP);

            int dcFlag;
            String itemName;
            String lastYearMonth = dateInfo.getString(DateInfo.F.LastYearMonth);
            if (amount < 0) {
                dcFlag = -1;
                amount = -amount;
                itemName = "Tagihan bulan " + lastYearMonth;
            } else {
                dcFlag = 1;
                itemName = "Saldo bulan " + lastYearMonth;
            }

            Transaction trx = new Transaction();
            int trxId = DbProp.getAndInc(N.LastTransactionID);
            trx.put(F.TransactionID, trxId);

            String date = dateInfo.getString(DateInfo.F.ThisYearMonth) + "-01";
            trx.put(F.Date, date);

            trx.put(F.CustomerID, customer.getString(Customer.F.CustomerID));
            trx.put(F.CustomerName, customer.getString(Customer.F.CustomerName));
            trx.put(F.ItemNo, item.getString(Item.F.ItemNo));
            trx.put(F.ItemName, itemName);
            trx.put(F.Price, amount);
            trx.put(F.Count, 1);
            trx.put(F.Amount, amount);
            trx.put(F.DCFlag, dcFlag);
            trx.store();
        }
    }

    public Transaction deleteRekapOf(String customerName, String yearMonth) {
        Document filter = new Document()
                .append(F.Date.name(),
                        new Document("$regex", yearMonth + ".*")
                        .append("$options", "i"))
                .append(F.CustomerName.name(), customerName)
                .append(F.ItemNo.name(), RKAP);

        return delete(filter);
    }
    
    private List<TrxAccount> trxAccounts;
    
    public List<TrxAccount> getTrxAccounts() {
        if (trxAccounts != null) {
            return trxAccounts;
        }
        
        trxAccounts = new ArrayList<>();
        
        List<Document> trxdocs = (List<Document>) get(F.TrxAccounts);
        if (trxdocs == null) {
            return trxAccounts;
        }
        
        for (Document trxdoc : trxdocs) {
            TrxAccount trxAccount = new TrxAccount();
            trxAccount.putAll(trxdoc);
            trxAccounts.add(trxAccount);
        }
        
        return trxAccounts;
    }

    /**
     * Add account involved in each transaction. Add new Fields: Accounts in
     * transactions record in Accounts field there is list of Accounts in each
     * account, there are fields: AccountNo, Amount, Flag Flag: Debit/Credit Sum
     * of (Amount*Flag) of Accounts should equal to zero.
     *
     * @param dateInfo
     * @param userAccountNo
     * @param userAccountName
     * @param supplierAccountNo
     * @param supplierAccountName
     */
    public static void upgradeTrx_addAccounts(DateInfo dateInfo,
            long userAccountNo, String userAccountName,
            long supplierAccountNo, String supplierAccountName) {

        if (userAccountNo == 0) {
            return;
        }

        String yearMonth = dateInfo.getString(DateInfo.F.ThisYearMonth);
        Document filter = new Document()
                .append(F.Date.name(),
                        new Document("$regex", yearMonth + ".*")
                        .append("$options", "i"));
        Document sort = new Document(F.Date.name(), 1);

        List<Transaction> list = Transaction.loadList(new Transaction(), filter, sort);
        TrxAccount trxaccount;
        

        for (Transaction transaction : list) {
            List<TrxAccount> trxAccounts = transaction.getTrxAccounts();
            trxAccounts.clear();

            if (CASH.equals(transaction.getString(F.ItemNo))) {
                // add customer acccount as credit
                // Customer- (kurang uang)
                trxaccount = new TrxAccount();
                long accountNo = Tool.idToNo(transaction.getString(F.CustomerID));
                trxaccount.put(TrxAccount.F.AccountNo, accountNo);
                trxaccount.put(TrxAccount.F.AccountName, transaction.getString(F.CustomerName));
                trxaccount.put(TrxAccount.F.Amount, transaction.getLong(F.Amount));
                trxaccount.put(TrxAccount.F.DCFlag, CREDIT);
                trxAccounts.add(trxaccount);

                // add user acccount as debbit
                // User+ (dapat uang)
                trxaccount = new TrxAccount();
                trxaccount.put(TrxAccount.F.AccountNo, userAccountNo);
                trxaccount.put(TrxAccount.F.AccountName, userAccountName);
                trxaccount.put(TrxAccount.F.Amount, transaction.getLong(F.Amount));
                trxaccount.put(TrxAccount.F.DCFlag, DEBBIT);
                trxAccounts.add(trxaccount);
            } else {
                // add suppier acccount as credit
                // Supplier- (kurang barang)
                trxaccount = new TrxAccount();
                trxaccount.put(TrxAccount.F.AccountNo, supplierAccountNo);
                trxaccount.put(TrxAccount.F.AccountName, supplierAccountName);
                trxaccount.put(TrxAccount.F.Amount, transaction.getLong(F.Amount));
                trxaccount.put(TrxAccount.F.DCFlag, CREDIT);
                trxAccounts.add(trxaccount);

                // add Customer acccount as debbit
                // Customer+ (dapat barang)
                trxaccount = new TrxAccount();
                long accountNo = Tool.idToNo(transaction.getString(F.CustomerID));
                trxaccount.put(TrxAccount.F.AccountNo, accountNo);
                trxaccount.put(TrxAccount.F.AccountName, transaction.getString(F.CustomerName));
                trxaccount.put(TrxAccount.F.Amount, transaction.getLong(F.Amount));
                trxaccount.put(TrxAccount.F.DCFlag, DEBBIT);
                trxAccounts.add(trxaccount);
            }
            transaction.saveTrxAccounts(trxAccounts);
        }
    }

    private void saveTrxAccounts(List<TrxAccount> trxAccounts) {
        UpdateResult result = getCollection().updateOne(getFilter(), 
                new Document("$set", new Document()
                        .append(F.TrxAccounts.name(), trxAccounts)
                ));
        
        if (result.getMatchedCount() == 0 || result.getModifiedCount() > 0) {
            System.out.println(result.toString());
            System.out.println(this);
        }
    }

    public List<Transaction> showRekapOfAccount(String yearMonth, long accountNo) {
        Document filter = new Document()
                .append(F.Date.name(),
                        new Document("$regex", yearMonth + ".*")
                        .append("$options", "i"))
                .append(F.TrxAccounts.name(),
                        new Document("$elemMatch", 
                                new Document(TrxAccount.F.AccountNo.name(), accountNo)));
        
        long balance = 0;
        long debit = 0;
        long credit = 0;
        
        List<Transaction> retlist = new ArrayList<>();
        
        Transaction trx;
        
        List<Transaction> list = loadList(this, filter, null);
        for (Transaction transaction : list) {
            List<TrxAccount> curTrxAccounts = transaction.getTrxAccounts();
            for (TrxAccount trxAccount : curTrxAccounts) {
                if (trxAccount.getLong(TrxAccount.F.AccountNo) == accountNo)
                {
                    long amount = trxAccount.getLong(TrxAccount.F.Amount);
                    int dcflag = trxAccount.getInt(TrxAccount.F.DCFlag);
                    if (dcflag == 1)
                    {
                        debit += amount;                        
                    }
                    else {
                        credit += amount;
                    }
                    
                    balance += (amount * dcflag);
                    
                    String remarks = transaction.getString(F.Date)
                            + " - " + transaction.getString(F.ItemName)
                            + " - " + transaction.getString(F.CustomerName);
                    trx = new Transaction();
                    trx.put(Transaction.V.Rekap, amount * dcflag);
                    trx.put(Transaction.F._id, remarks);
                    trx.put(F.DCFlag, 1);
                    retlist.add(trx);
                }
            }
        }

        return retlist;
    }
}