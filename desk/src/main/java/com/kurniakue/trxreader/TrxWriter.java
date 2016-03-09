/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.trxreader;

import com.kurniakue.trxreader.data.Transaction;
import com.kurniakue.trxreader.data.Customer;
import com.kurniakue.trxreader.data.KurniaKueDb;
import com.kurniakue.trxreader.ui.TransactionDialog;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author harun1
 */
public class TrxWriter {

    public static Map<String, Customer> customers = new TreeMap<>();

    public static void main(String[] args) {
        initParams(args);
        while (showTransactionDialog()) {

        }
        TransactionDialog.disposeDialog();
        KurniaKueDb.stop();
    }

    private static void initParams(String[] args) {
        KurniaKueDb.init();
    }

    private static boolean showTransactionDialog() {
        return TransactionDialog.showDialog();
    }
}
