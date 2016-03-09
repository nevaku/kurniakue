package com.kurniakue.trxreader.data;

import com.kurniakue.data.DateInfo;
import com.kurniakue.tool.MailSender;
import com.kurniakue.common.Common;
import com.kurniakue.common.EnumField;
import com.kurniakue.trxreader.ui.CustomerDialog;
import com.kurniakue.trxreader.ui.EmailDialog;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author harun1
 */
public class Customer extends Record implements Comparable<Customer> {

    public enum F implements EnumField {

        CustomerID, CustomerName, Alias, Email,;
    }

    public String id = "";
    //public String name;
    //public String email;
    //public String alias;
    public final List<Record> transactions = new ArrayList<>();
    public long total = 0;
    public long totalDebit = 0;
    public long totalCredit = 0;
    //private final Record saldoRecord = new Record();
    //private int saldo = 0;

    public Customer() {
        
    }

    public Customer(String name) {
        put(F.CustomerName, name);
        //createSaldo();
    }

//    private void createSaldo() {
//        saldoRecord.put(Saldo.F.CustomerName, getString(F.CustomerName));
//        saldoRecord.put(Saldo.F.Date, Common.lastYearMonth + "-01");
//        saldoRecord.put(Saldo.F.Amount, saldo);
//    }
//
//    private void addSaldo(Transaction transaction) {
//        saldo += Common.tlong(transaction.get(Transaction.F.Amount));
//        saldoRecord.put(Saldo.F.Amount, saldo);
//    }

    @Override
    public String toString() {
        return "{" + getString(F.CustomerName) + ": " + id + ":" + transactions.toString() + "}\n";
    }

    public void outputTrxTo(Path path, DateInfo dateInfo) {
        if (total == 0) {
            return;
        }
        StringBuilder sb = buildBilling(dateInfo);
        byte[] data = sb.toString().getBytes();
        try {
            Files.write(path, data, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public StringBuilder buildBilling(DateInfo dateInfo) {
        StringBuilder sb = new StringBuilder(1000);
        sb.append("Salam ").append(getString(F.Alias));
        sb.append(" (").append(getString(F.CustomerID)).append(")").append(",\n\n");
        sb.append(Common.HORZ_LINE);
        String strTotal = getTotalString();
        String sumTitle = strTotal.contains("-") ? "Saldo: " : "Tagihan: ";
        if (strTotal.contains("-")) {
            strTotal = strTotal.replace("-", " ");
        }
        sb.append("              ").append(sumTitle).append(
                Common.SUBCAT_BLANKS.substring(0, Common.SUBCAT_LENGTH - sumTitle.length()));
        sb.append(strTotal).append("\n");
        sb.append(Common.HORZ_LINE);
        for (Record transaction : transactions) {
            String dateStr = transaction.getString(Transaction.F.Date);
            dateStr = Common.convertDateFormat(dateStr, "yyyy-MM-dd", "dd-MM-yyyy");
            sb.append(dateStr).append("    ");
            for (int i = dateStr.length(); i < 10; i++) {
                sb.append(" ");
            }
            long amount = transaction.getInt(Transaction.F.Amount)
                    * transaction.getInt(Transaction.F.DCFlag);
            String item = transaction.tstr(Transaction.F.ItemNo)
                    + "-" + transaction.tstr(Transaction.F.ItemName);
            if (amount > 0) {
                item = "Bayar, trims :)";
            }
            item = (item + Common.SUBCAT_BLANKS).substring(0, Common.SUBCAT_LENGTH);
            sb.append(item);
            String strAmount = Common.SUBCAT_BLANKS + Common.formatMoney(-amount);
            int more = strAmount.length() - Common.SUBCAT_LENGTH;
            strAmount = strAmount.substring(more);
            sb.append(strAmount).append("\n");
        }
        sb.append(Common.HORZ_LINE).append("\n");
        sb.append("Untuk Pembayaran Tunai silakan Hubungi: \n");
        sb.append("-- Harun di ext. 217\n\n");
        sb.append("Untuk Pembayaran Via Transfer \n");
        sb.append("-- Bank Tujuan    : BCA \n");
        sb.append("-- No. Rekenening : 2741497699 \n");
        sb.append("-- Nama           : Harun Al Rasyid \n");
        sb.append("-- Berita         : Tagihan ");
        sb.append(dateInfo.getString(DateInfo.F.LastMonthFullName)).append(" \n");
        sb.append("\n");
        sb.append("Untuk Kritik/Saran/Pesanan Silakan Hubungi \n");
        sb.append("Dina/Harun \n");
        sb.append("HP/WA/Telegram: +62 896 5297 3311, +62 896 5297 3300 \n");
        sb.append("PIN BBM: 7CF5 4549, 7424 B588 \n");
        sb.append("email: kurniakue@gmail.com;harun-1020015@infolink.co.id \n");
        sb.append("\n");
        sb.append("Trims,\n");
        sb.append("\n");
        sb.append("Harun\n");
        sb.append(Common.HORZ_LINE).append("\n\n\n");
        return sb;
    }

    public static void outputPathListHeader(Path pathList) {
        StringBuilder sb = new StringBuilder(1000);
        sb.append("-----------------------------------------------------\n");
        sb.append(Common.templateNo).append(Common.templateName).append(Common.templateJumlah).append("\n");
        sb.append("-----------------------------------------------------\n");
        Common.outputSbToFile(sb, pathList);
    }

    public void outputTrxListTo(Path pathList) {
        if (total == 0) {
            return;
        }
        StringBuilder sb = new StringBuilder(1000);
        String strNo = "" + Common.CustNo;
        fill(sb, " ", Common.templateNo.length() - strNo.length() - 1);
        sb.append(strNo);
        sb.append(" ");
        sb.append(getString(F.CustomerName));
        fill(sb, " ", Common.templateName.length() - getString(F.CustomerName).length());
        sb.append(getTotalString());
        sb.append("\n");
        Common.outputSbToFile(sb, pathList);
    }

    public static void outputPathListCsvHeader(Path pathListCsv) {
        StringBuilder sb = new StringBuilder(1000);
        sb.append(Common.templateNo.trim());
        sb.append(",");
        sb.append(Common.templateName.trim());
        sb.append(",");
        sb.append("Kredit");
        sb.append(",");
        sb.append("Debit");
        sb.append("\n");
        Common.outputSbToFile(sb, pathListCsv);
    }

    public void outputTrxListCsvTo(Path pathListCsv) {
        StringBuilder sb = new StringBuilder(1000);
        String strNo = "" + Common.CustNo;
        sb.append(strNo);
        sb.append(",");
        sb.append(getString(F.CustomerName));
        sb.append(",");
        sb.append(-totalCredit);
        sb.append(",");
        sb.append(totalDebit);
        sb.append("\n");
        Common.outputSbToFile(sb, pathListCsv);
    }

    private String getTotalString() {
        String strTotal = Common.SUBCAT_BLANKS + Common.formatMoney(-total);
        int more = strTotal.length() - Common.SUBCAT_LENGTH;
        strTotal = strTotal.substring(more);
        return strTotal;
    }

    public void addTrx(Transaction transaction) {
        long trxAmount = transaction.getInt(Transaction.F.Amount) 
                * transaction.getInt(Transaction.F.DCFlag);
        transactions.add(transaction);
        total += trxAmount;
        if (trxAmount < 0) {
            totalCredit += trxAmount;
        } else {
            totalDebit += trxAmount;
        }
    }

    @Override
    public int compareTo(Customer customer) {
        if (customer == null) {
            return 1;
        } else {
            return getString(F.CustomerName).compareTo(customer.getString(F.CustomerName));
        }
    }

    private void fill(StringBuilder sb, String charToFill, int num) {
        for (int i = 0; i < num; i++) {
            sb.append(charToFill);
        }
    }

    public CustomerD load() {
        CustomerD data = Persistor.get(CustomerD.class);
        data.find(getString(F.CustomerName), this);

        CustomerDialog.get().clear();
        CustomerDialog.get().setCustomerName(getString(F.CustomerName));
        CustomerDialog.get().setEmail(getString(F.Email));
        CustomerDialog.get().setAlias(getString(F.Alias));
        return data;
    }

    public String store(CustomerD data) {
        if (CustomerDialog.showDialog()) {
            put(F.CustomerName, getString(F.CustomerName));
            put(F.Email, CustomerDialog.get().getEmail());
            put(F.Alias, CustomerDialog.get().getAlias());
            data.persist(this);
        } else {
            return null;
        }

        id = this.getString(F.Email);
        return id;
    }

    public void sendEmail(DateInfo dateInfo) {
        if (total == 0) {
            return;
        }
        StringBuilder sb = buildBilling(dateInfo);
        String billing = sb.toString();
        System.out.println(billing);
        String emailAddress = getEmailAlias() + "\n";
        String subject = "Tagihan " + dateInfo.getString(DateInfo.F.LastMonthFullName) + "\n";
        if (!EmailDialog.showDialog(emailAddress, subject, billing)) {
            return;
        }
        MailSender.sendEmail(emailAddress, subject, billing);
    }

    public String getEmailAlias() {
        return getString(F.Alias) + " <" + getString(F.Email) + ">";
    }
}
