/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.trxreader;

import com.kurniakue.trxreader.data.Transaction;
import com.kurniakue.trxreader.data.Customer;
import com.kurniakue.trxreader.data.KurniaKueDb;
import com.kurniakue.tool.MailSender;
import static com.kurniakue.common.Common.PrintMode.*;
import com.kurniakue.trxreader.data.CustomerD;
import com.kurniakue.trxreader.ui.CustomerDialog;
import com.kurniakue.trxreader.ui.EmailDialog;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;

/**
 *
 * @author harun1
 */
public class Trxreader {

    public static Map<String, Customer> customers = new TreeMap<>();

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
//    public static void main(String[] args) throws Exception {
//        initParams(args);
//        
//        String filename = Common.csvDir + Common.csvFileName;
//        
//        final URL url = new File(filename).toURI().toURL();
//        //Reader in = new FileReader(filename);
//        final Reader reader = new InputStreamReader(new BOMInputStream(url.openStream()), "UTF-8");
//        final CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader());
//        String headerMap = String.valueOf(parser.getHeaderMap());
//        System.out.println(headerMap);
//        
//        processBill(parser, reader);
//    }
//
//    public static void processBill(final CSVParser parser, final Reader reader) throws IOException {
//        KurniaKueDb.init();
//        MailSender.initMailSender();
//        
//        //List<Record<TransactionInfo>> records = new ArrayList<>();
//        try {
//            for (final CSVRecord csvline : parser) {
//                if (csvline.size() <= 1)
//                {
//                    continue;
//                }
//                Transaction record = new Transaction();
//                for (String header : Common.headers) {
//                    Transaction.F field;
//                    if (header.equals("Payee/Payer")){
//                        field = Transaction.F.CustomerName;
//                    }else{
//                        field = Transaction.f(header);
//                    }
//                    record.put(field, csvline.get(header));
//                }
//                //records.add(record);
//                Customer customer = getCustomer(csvline.get("Payee/Payer"));
//                customer.addTrx(record);
//            }
//        System.out.println(Common.TOTAL + customers.size());
//        System.out.println(customers);
//
//        outputTrxTo(customers);
//        
//        } finally {
//            parser.close();
//            reader.close();
//            KurniaKueDb.stop();
//
//            CustomerDialog.disposeDialog();
//            EmailDialog.disposeDialog();
//        }
//    }
//    
//    private static void initParams(String[] args) {
//        //Common.printModes = EnumSet.of(Email);
//        //Common.printModes = EnumSet.of(Detail);
//        Common.printModes = EnumSet.of(Email);
//        //Common.printModes = EnumSet.of(DbUpdate);
//        //Common.customersToProcess.add("XLINK");
//    }
//
//    public static Customer getCustomer(String customerName) {
//        Customer ret = customers.get(customerName);
//        if (ret == null) {
//            ret = new Customer(customerName);
//            customers.put(customerName, ret);
//        }
//        return ret;
//    }
//
//    private static void outputTrxTo(Map<String, Customer> customers) throws IOException {
//            
//        Path path = Paths.get(Common.driveDir + Common.billFileName);
//        if (Common.printModes.contains(Detail)) {
//            Files.deleteIfExists(path);
//        }
//            
//        Path pathList = Paths.get(Common.driveDir + Common.billListFileName);
//        if (Common.printModes.contains(List)) {
//            Files.deleteIfExists(pathList);
//            Customer.outputPathListHeader(pathList);
//        }        
//            
//        Path pathListCsv = Paths.get(Common.driveDir + Common.billListCsvFileName);
//        if (Common.printModes.contains(ListCsv)) {
//            Files.deleteIfExists(pathListCsv);
//            Customer.outputPathListCsvHeader(pathListCsv);
//        }        
//        
//        StringBuilder sbEmailList = new StringBuilder();
//        Common.CustNo = 0;
//        Set<Map.Entry<String, Customer>> entries = customers.entrySet();
//        for (Map.Entry<String, Customer> entry : entries) {
//            if (!Common.customersToProcess.isEmpty() &&
//                    !Common.customersToProcess.contains(entry.getKey())) {
//                continue;
//            }
//            Common.CustNo += 1;
//            CustomerD data = entry.getValue().load();
//            
//            if (Common.printModes.contains(DbUpdate)) {
//                if (entry.getValue().store(data) == null) {
//                    return;
//                }
//            }
//            
//            if (Common.printModes.contains(Detail)) {
//                entry.getValue().outputTrxTo(path);
//            } 
//            
//            if (Common.printModes.contains(EmailList)) {
//                sbEmailList.append(entry.getValue().getEmailAlias());
//                sbEmailList.append(";");
//            }
//            
//            if (Common.printModes.contains(List)) {
//                entry.getValue().outputTrxListTo(pathList);
//            }
//            
//            if (Common.printModes.contains(ListCsv)) {
//                entry.getValue().outputTrxListCsvTo(pathListCsv);
//            } 
//            
//            if (Common.printModes.contains(Email)) {
//                entry.getValue().sendEmail();
//                if (EmailDialog.isCancel())
//                {
//                    return;
//                }
//            }
//        }
//        
//        if (Common.printModes.contains(EmailList)) {
//            sbEmailList.deleteCharAt(sbEmailList.length()-1);
//            System.out.println(sbEmailList.toString());
//        }
//    }
}
