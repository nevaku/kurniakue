/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.common;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

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
    public static final String templateJumlah = "        Jumlah";
    public static int CustNo = 0;
    public static final String accountsDir = driveDir + "/Accounts";
    public static final String extension = "xhtml";
    public static final String csvDir = driveDir + "/expensemanager/csv";

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

    public enum Argument {

        PrintMode,;
    }

    public static long tlong(Object obj) {
        return tlong(obj, 0);
    }

    public static long tlong(Object obj, long def) {
        if (obj == null) {
            return def;
        } else if (obj instanceof Number) {
            return ((Number) obj).longValue();
        } else {
            try {
                long ret = Long.parseLong(String.valueOf(obj).replace(".00", ""));
                return ret;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return def;
    }

    public static int tint(Object obj) {
        return tint(obj, 0);
    }

    public static int tint(Object obj, int def) {
        if (obj == null) {
            return def;
        } else if (obj instanceof Number) {
            return ((Number) obj).intValue();
        } else {
            try {
                int ret = Integer.parseInt(String.valueOf(obj).replace(".00", ""));
                return ret;
            } catch (Exception e) {

            }
        }
        return def;
    }

    public static String tstr(Object obj) {
        if (obj == null) {
            return "";
        } else {
            return String.valueOf(obj);
        }
    }

    public static String formatMoney(long value) {
        String pattern = "Rp #,##0";
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        return myFormatter.format(value);
    }

    public static String formatNumber(long value) {
        String pattern = "#,##0";
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        return myFormatter.format(value);
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

    public static String addDay(String theDate, int delta) {
        return addDate(theDate, delta, Calendar.DAY_OF_YEAR);
    }

    public static String addDate(String theDate, int delta, int dateComponent) {
        Calendar calendar = Calendar.getInstance();
        Date now = parseDate(theDate, "yyyy-MM-dd");
        calendar.setTime(now);
        do {
            calendar.add(dateComponent, delta);
        } while (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
                || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY);
        return formatDate(calendar.getTime(), "yyyy-MM-dd");
    }

    public static String convertDateFormat(String theDate, String ori, String target) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(ori);
            Date date = formatter.parse(theDate);
            formatter = new SimpleDateFormat(target);
            return formatter.format(date);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Date parseDate(String theDate, String format) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            Date date = formatter.parse(theDate);
            return date;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Date parseDateOrNull(String theDate, String format) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            Date date = formatter.parse(theDate);
            return date;
        } catch (Exception exc) {
            exc.printStackTrace();
            return null;
        }
    }

    public static String formatDate(Date theDate, String format) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return formatter.format(theDate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
