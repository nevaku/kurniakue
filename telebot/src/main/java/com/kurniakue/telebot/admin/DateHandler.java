/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.telebot.admin;

import com.kurniakue.common.Tool;
import com.kurniakue.data.DateInfo;
import com.kurniakue.telebot.Command;
import com.kurniakue.telebot.UpdateHandler;
import com.kurniakue.telebot.admin.CustomerHandler.CTX;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author harun1
 */
public class DateHandler extends UpdateHandler {

    public final Command cmd_show = new Command(this, "/set_Date", () -> {
        return show();
    });

    private final Command cmd_home = new Command(this, "/awal", () -> {
        return awal();
    });

    public DateHandler() {

    }

    @Override
    public boolean execute() {
        return show();
    }

    private boolean show() {
        String[] params = getParams();

        if (params.length > 0) {
            setCurrentDate(params[0]);
        } else {
            Calendar calendar = getContext().getCurrentCalendar();
            getReplier().addLine("Current date is: " + DateInfo.getDateInfo(
                    calendar).getString(DateInfo.F.Today));
        }

        List<String> keyboards = getAvailableDateSelections();
        keyboards.add(cmd_home.getCmd());
        getReplier().addLine("Silakan").keyboard(keyboards).send();
        return true;
    }

    private List<String> getAvailableDateSelections() {
        List<String> availableDateSelections = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        
        calendar.add(Calendar.MONTH, -4);
        String yearMonth = Tool.formatDate(calendar.getTime(), "yyyy-MM-dd");
        availableDateSelections.add(yearMonth);
        
        calendar.add(Calendar.MONTH, 1);
        yearMonth = Tool.formatDate(calendar.getTime(), "yyyy-MM-dd");
        availableDateSelections.add(yearMonth);
        
        calendar.add(Calendar.MONTH, 1);
        yearMonth = Tool.formatDate(calendar.getTime(), "yyyy-MM-dd");
        availableDateSelections.add(yearMonth);
        
        calendar.add(Calendar.MONTH, 1);
        yearMonth = Tool.formatDate(calendar.getTime(), "yyyy-MM-dd");
        availableDateSelections.add(yearMonth);
        
        calendar.add(Calendar.MONTH, 1);
        yearMonth = Tool.formatDate(calendar.getTime(), "yyyy-MM-dd");
        availableDateSelections.add(yearMonth);
        
        calendar.add(Calendar.MONTH, 1);
        yearMonth = Tool.formatDate(calendar.getTime(), "yyyy-MM-dd");
        availableDateSelections.add(yearMonth);

        return availableDateSelections;
    }

    private void setCurrentDate(String today) {
        DateInfo dateInfo = DateInfo.getDateInfo(today);
        getContext().setCurrentCalendar(dateInfo.getCalendar());
        getContext().data.put(CTX.YearMonthSet, dateInfo.getString(DateInfo.F.ThisYearMonth));
        getReplier().addLine("Set current date to: " + dateInfo.getString(DateInfo.F.Today));
    }
}
