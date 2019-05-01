/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.trxreader;

import com.kurniakue.common.Common;
import com.kurniakue.trxreader.data.Transaction;
import com.kurniakue.trxreader.data.Customer;
import com.kurniakue.trxreader.data.KurniaKueDb;
import com.kurniakue.tool.MailSender;
import static com.kurniakue.common.Common.PrintMode.*;
import com.kurniakue.data.BillInfo;
import com.kurniakue.trxreader.data.CustomerD;
import com.kurniakue.data.DateInfo;
import com.kurniakue.trxreader.data.TransactionD;
import com.kurniakue.trxreader.ui.CustomerDialog;
import com.kurniakue.trxreader.ui.EmailDialog;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author harun1
 */
public class TrxDbReader {

    public static Map<String, Customer> customers = new TreeMap<>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        initParams(args);

        //String filename = Common.csvDir + Common.csvFileName;
        //final URL url = new File(filename).toURI().toURL();
        //Reader in = new FileReader(filename);
        //final Reader reader = new InputStreamReader(new BOMInputStream(url.openStream()), "UTF-8");
        //final CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader());
        //String headerMap = String.valueOf(parser.getHeaderMap());
        //System.out.println(headerMap);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 1, 1);
        
        processBillFromDb(calendar, Common.driveDir);
        KurniaKueDb.stop();
    }

    public static void processBillFromDb(Calendar calendar, String baseDir) {

//        MailSender.initMailSender();
        MailSender.initGmailSender();
        CustomerDialog.init();
        EmailDialog.init();

        //List<Record<TransactionInfo>> records = new ArrayList<>();
        DateInfo dateInfo = DateInfo.getDateInfo(calendar);
        BillInfo billInfo = BillInfo.getBillInfoOf(dateInfo, baseDir);
        System.out.println("DateInfo: " + dateInfo);
        java.util.List<Transaction> transactionList = TransactionD.get().getTransactionsOfTheMonth(dateInfo.tstr(DateInfo.F.LastYearMonth));
        try {
            for (Transaction record : transactionList) {
                Customer customer = getCustomer(record.tstr(Transaction.F.CustomerName));
                customer.addTrx(record);
            }
            System.out.println(Common.TOTAL + customers.size());
            System.out.println(customers);

            outputTrxTo(customers, dateInfo, billInfo);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        finally {
            CustomerDialog.disposeDialog();
            EmailDialog.disposeDialog();
        }
    }

    private static void initParams(String[] args) {
        //Common.printModes = EnumSet.of(Email);
        //Common.printModes = EnumSet.of(Detail);
        Common.printModes = EnumSet.of(Detail, ListCsv);
        //Common.printModes = EnumSet.of(DbUpdate);
        //Common.customersToProcess.add("Agam");
        KurniaKueDb.init();
    }

    public static Customer getCustomer(String customerName) {
        Customer ret = customers.get(customerName);
        if (ret == null) {
            ret = new Customer(customerName);
            customers.put(customerName, ret);
        }
        return ret;
    }

    private static void outputTrxTo(Map<String, Customer> customers, DateInfo dateInfo, BillInfo billInfo) throws IOException {

        Path path = Paths.get(billInfo.getString(BillInfo.F.DetailFileName));
        if (Common.printModes.contains(Detail)) {
            Files.deleteIfExists(path);
        }

        Path pathList = Paths.get(billInfo.getString(BillInfo.F.ListFileName));
        if (Common.printModes.contains(List)) {
            Files.deleteIfExists(pathList);
            Customer.outputPathListHeader(pathList);
        }

        Path pathListCsv = Paths.get(billInfo.getString(BillInfo.F.ListCsvFileName));
        if (Common.printModes.contains(ListCsv)) {
            Files.deleteIfExists(pathListCsv);
            Customer.outputPathListCsvHeader(pathListCsv);
        }

        StringBuilder sbEmailList = new StringBuilder();
        Common.CustNo = 0;
        Set<Map.Entry<String, Customer>> entries = customers.entrySet();
        for (Map.Entry<String, Customer> entry : entries) {
            if (!Common.customersToProcess.isEmpty()
                    && !Common.customersToProcess.contains(entry.getKey())) {
                continue;
            }
            Common.CustNo += 1;
            CustomerD data = entry.getValue().load();

            if (Common.printModes.contains(DbUpdate)) {
                if (entry.getValue().store(data) == null) {
                    return;
                }
            }

            if (Common.printModes.contains(Detail)) {
                entry.getValue().outputTrxTo(path, dateInfo);
            }

            if (Common.printModes.contains(EmailList)) {
                sbEmailList.append(entry.getValue().getEmailAlias());
                sbEmailList.append(";");
            }

            if (Common.printModes.contains(List)) {
                entry.getValue().outputTrxListTo(pathList);
            }

            if (Common.printModes.contains(ListCsv)) {
                entry.getValue().outputTrxListCsvTo(pathListCsv);
            }

            if (Common.printModes.contains(Email)) {
                entry.getValue().sendEmail(dateInfo);
                if (EmailDialog.isCancel()) {
                    return;
                }
            }
        }

        if (Common.printModes.contains(EmailList)) {
            sbEmailList.deleteCharAt(sbEmailList.length() - 1);
            System.out.println(sbEmailList.toString());
        }
    }
}
