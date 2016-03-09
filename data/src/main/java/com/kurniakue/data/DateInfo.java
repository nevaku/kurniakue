/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.data;

import com.kurniakue.common.Common;
import com.kurniakue.common.EnumField;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author harun1
 */
public class DateInfo extends Record {

    public static DateInfo getDateInfo(String yearMonthDay) {
        Date date = Common.parseDate(yearMonthDay, "yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getDateInfo(calendar);
    }

    public static DateInfo getDateInfo(Calendar calendar) {
        DateInfo dateInfo = new DateInfo();
        dateInfo.put(DateInfo.F.Today, Common.formatDate(calendar.getTime(), "yyyy-MM-dd"));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        dateInfo.put(DateInfo.F.FirstDayOfThisMonth, Common.formatDate(calendar.getTime(), "yyyy-MM-dd"));
        dateInfo.put(DateInfo.F.ThisYearMonth, Common.formatDate(calendar.getTime(), "yyyy-MM"));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        dateInfo.put(DateInfo.F.LastDayOfLastMonth, Common.formatDate(calendar.getTime(), "yyyy-MM-dd"));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        dateInfo.put(DateInfo.F.FirstDayOfLastMonth, Common.formatDate(calendar.getTime(), "yyyy-MM-dd"));
        dateInfo.put(DateInfo.F.LastYearMonth, Common.formatDate(calendar.getTime(), "yyyy-MM"));
        dateInfo.put(DateInfo.F.LastYearMonthyyyyMM, Common.formatDate(calendar.getTime(), "yyyyMM"));
        String alastMonthName = Common.monthsName[calendar.get(Calendar.MONTH) + 1];
        dateInfo.put(DateInfo.F.LastMonthName, alastMonthName);
        int lastYear = calendar.get(Calendar.YEAR);
        String alastMonthFullName = alastMonthName + " " + lastYear;
        dateInfo.put(DateInfo.F.LastMonthFullName, alastMonthFullName);
        return dateInfo;
    }

    public enum F implements EnumField {

        Today,
        FirstDayOfThisMonth,
        FirstDayOfLastMonth,
        LastDayOfLastMonth,
        ThisYearMonth,
        LastYearMonth,
        LastYearMonthyyyyMM,
        LastMonthName,
        LastMonthFullName,
    }
}
