/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.data;

import com.kurniakue.common.Common;
import com.kurniakue.common.EnumField;

/**
 *
 * @author harun1
 */
public class BillInfo extends Record {

    public static BillInfo getBillInfoOf(DateInfo dateInfo) {
        return getBillInfoOf(dateInfo, Common.driveDir);
    }

    public static BillInfo getBillInfoOf(DateInfo dateInfo, String baseDir) {
        BillInfo billInfo = new BillInfo();
        String lastYearMonthMonthFileName = dateInfo.getString(DateInfo.F.LastYearMonthyyyyMM) + "-" + dateInfo.getString(DateInfo.F.LastMonthName);
        String fileName = "/tagihan/" + lastYearMonthMonthFileName + ".txt";
        billInfo.put(BillInfo.F.DetailFileName, baseDir + fileName);
        String listFileName = "/tagihan/" + lastYearMonthMonthFileName + "-list.txt";
        billInfo.put(BillInfo.F.ListFileName, baseDir + listFileName);
        String listCsvFileName = "/tagihan/" + lastYearMonthMonthFileName + "-list.csv";
        billInfo.put(BillInfo.F.ListCsvFileName, baseDir + listCsvFileName);
        return billInfo;
    }

    public enum F implements EnumField {

        DetailFileName,
        ListFileName,
        ListCsvFileName,
    }
}
