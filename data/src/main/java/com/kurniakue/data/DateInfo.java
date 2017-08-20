/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.data;

import com.kurniakue.common.Common;
import com.kurniakue.common.EnumField;
import com.kurniakue.common.Tool;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author harun1
 */
public class DateInfo extends Record {

    public static DateInfo getDateInfo(String yearMonthDay) {
        Date date = Tool.parseDate(yearMonthDay, "yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getDateInfo(calendar);
    }

    public static DateInfo getDateInfo(Calendar calendar) {
        DateInfo dateInfo = new DateInfo();
        calendar = (Calendar) calendar.clone();
        dateInfo.put(DateInfo.F.Calendar, calendar);
        dateInfo.put(DateInfo.F.Today, Tool.formatDate(calendar.getTime(), "yyyy-MM-dd"));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        dateInfo.put(DateInfo.F.FirstDayOfThisMonth, Tool.formatDate(calendar.getTime(), "yyyy-MM-dd"));
        dateInfo.put(DateInfo.F.ThisYearMonth, Tool.formatDate(calendar.getTime(), "yyyy-MM"));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        dateInfo.put(DateInfo.F.LastDayOfLastMonth, Tool.formatDate(calendar.getTime(), "yyyy-MM-dd"));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        dateInfo.put(DateInfo.F.FirstDayOfLastMonth, Tool.formatDate(calendar.getTime(), "yyyy-MM-dd"));
        dateInfo.put(DateInfo.F.LastYearMonth, Tool.formatDate(calendar.getTime(), "yyyy-MM"));
        dateInfo.put(DateInfo.F.LastYearMonthyyyyMM, Tool.formatDate(calendar.getTime(), "yyyyMM"));
        String alastMonthName = Common.monthsName[calendar.get(Calendar.MONTH) + 1];
        dateInfo.put(DateInfo.F.LastMonthName, alastMonthName);
        int lastYear = calendar.get(Calendar.YEAR);
        String alastMonthFullName = alastMonthName + " " + lastYear;
        dateInfo.put(DateInfo.F.LastMonthFullName, alastMonthFullName);
        return dateInfo;
    }
    
    public Calendar getCalendar() {
        Calendar calendar = (Calendar) this.getAs(F.Calendar);
        return (Calendar) calendar.clone();
    }

    public enum F implements EnumField {
        Calendar,
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
