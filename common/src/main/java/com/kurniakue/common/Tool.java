/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.common;

import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author harun1
 */
public class Tool {
    public static boolean isBlank(String string) {
        return string == null || "".equals(string) || "null".equals(string) ;
    }
    
    public static boolean isExists(String string) {
        return string != null && !"".equals(string);
    }

    public static <T> T neo(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }

    public static MemberFinder getMember(String memberName) {
        return new MemberFinder(memberName);
    }

    public static String[] parseArguments(String text) {
        String[] temp = text.split(" ");
        List<String> retlist = new ArrayList<>();
        
        String prev = "";
        for (String string : temp) {
            if (isBlank(prev) && !isBlank(string) 
                    && (!string.startsWith("\"") || string.endsWith("\""))) {
                retlist.add(string.replace("\"", ""));
            } else if (isBlank(prev) && !isBlank(string) && string.startsWith("\"")) {
                prev = string.replace("\"", "");
            } else if (!isBlank(prev) && !isBlank(string) && !string.endsWith("\"")) {
                prev += " " + string;
            } else if (!isBlank(prev) && !isBlank(string) && string.endsWith("\"")) {
                retlist.add(prev + " " + string.replace("\"", ""));
                prev = "";
            }
        }
        
        if (!isBlank(prev)) {
            retlist.add(prev);
        }
        
        return retlist.toArray(new String[0]);
    }

    public static long idToNo(String id) {
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.put(id.getBytes());
        bb.put(new byte[8]);
        bb.rewind();
        return bb.getLong();
    }

    public static String noToId(long no) {
        ByteBuffer bb = ByteBuffer.allocate(Long.BYTES);
        bb.putLong(no);
        return new String( bb.array()).trim();
    }

    public static String formatNumber(long value) {
        String pattern = "#,##0";
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        return myFormatter.format(value);
    }

    public static String formatNumber(long value, String format) {
        DecimalFormat myFormatter = new DecimalFormat(format);
        return myFormatter.format(value);
    }

    public static String tstr(Object obj) {
        if (obj == null) {
            return "";
        } else {
            return String.valueOf(obj);
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

    public static String addDate(String theDate, int delta, int dateComponent) {
        Calendar calendar = Calendar.getInstance();
        Date now = parseDate(theDate, "yyyy-MM-dd");
        calendar.setTime(now);
        do {
            calendar.add(dateComponent, delta);
        } while (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY);
        return formatDate(calendar.getTime(), "yyyy-MM-dd");
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

    public static String addDay(String theDate, int delta) {
        return addDate(theDate, delta, Calendar.DAY_OF_YEAR);
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

    public static String formatMoney(long value) {
        String pattern = "Rp #,##0";
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        return myFormatter.format(value);
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
}
