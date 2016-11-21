/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.common;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author harun1
 */
public class Common {

    public static final String[] monthsName = {"",
        "Januari", "Pebruari", "Maret", "April", "Mei", "Juni",
        "Juli", "Agustus", "September", "Oktober", "Nopember", "Desember",
        "Januari"
    };

    public static final String templateName = "Nama                        ";
    public static final String templateNo = "No. ";
    public static final String driveDir = "C:\\harun\\KurniaKue\\tagihan";
    public static final String TOTAL = "Total: ";
    public static final String SUBCAT_BLANKS = "                         ";
    public static final String HORZ_LINE = "----------------------------------------------------------------------\n";
    public static final int SUBCAT_LENGTH = 28;
    public static final DefaultEnumField _id = new DefaultEnumField("_id");
    public static final String CASH = "CASH";
    public static final String RKAP = "RKAP";
    public static final String TRAN = "TRAN";
    public static final String templateJumlah = "        Jumlah";
    public static int CustNo = 0;
    public static final String accountsDir = driveDir + "/Accounts";
    public static final String extension = "xhtml";
    public static final String csvDir = driveDir + "/expensemanager/csv";
    /**
     * make it 6 words as CREDIT
     */
    public static int DEBBIT = +1;
    public static int CREDIT = -1;

    public enum PrintMode {

        List,
        Detail,
        Email,
        DbUpdate,
        EmailList,
        ListCsv,
    }

    public static EnumSet<PrintMode> printModes = EnumSet.allOf(PrintMode.class);
    public static List<String> customersToProcess = new ArrayList<>();
    
    /**
     * 
     * @deprecated should be in DB
     */
    @Deprecated
    public static final Map<String, String> GroupMember = new HashMap<String, String>() {
        {
            put("HarunMip", "Admin");
            put("DinaKS", "Admin,Supplier");
            put("TakDikenal", "Customer");
        }
    };
    
    /**
     * 
     * @deprecated should be placed to DBProp
     */
    @Deprecated
    public static final List<String> GROUP_CUSTOMER = Collections.singletonList("Customer");
    
    public static final String HARUN_MEMBER_ID = "HS005";
    public static final String DINA_MEMBER_ID = "DR001";
    /**
     * 
     * @deprecated should be in DBs
     */
    @Deprecated
    public static final Map<String, String> MemberIdOf = new HashMap<String, String>() {
        {
            put("HarunMip", HARUN_MEMBER_ID);
            put("DinaKS", DINA_MEMBER_ID);
        }
    };

    public enum Argument {

        PrintMode,;
    }


    public static void outputSbToFile(StringBuilder sb, Path pathList) {
        byte[] data;
        data = sb.toString().getBytes();
        try {
            Files.write(pathList, data, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
