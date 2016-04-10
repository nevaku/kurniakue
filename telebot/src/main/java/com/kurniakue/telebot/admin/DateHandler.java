/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.telebot.admin;

import com.kurniakue.common.Tool;
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
        
        if(params.length > 0) {
            setCurrentDate(params[0]);
        } else {
            getReplier().addLine("Current date is: " + getContext().data.getString(CTX.YearMonthSet));
        }
        
        List<String> keyboards = getAvailableDateSelections();
        keyboards.add(cmd_home.getCmd());
        getReplier().addLine("Silakan").keyboard(keyboards).send();
        return true;
    }
    
    private List<String> getAvailableDateSelections() {
        List<String> availableDateSelections = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        String thisYearMonth = Tool.formatDate(calendar.getTime(), "yyyy-MM");
        calendar.add(Calendar.MONTH, -1);
        String lastYearMonth = Tool.formatDate(calendar.getTime(), "yyyy-MM");
        calendar.add(Calendar.MONTH, 2);
        String nextYearMonth = Tool.formatDate(calendar.getTime(), "yyyy-MM");
        
        availableDateSelections.add(lastYearMonth);
        availableDateSelections.add(thisYearMonth);
        availableDateSelections.add(nextYearMonth);
        
        return availableDateSelections;
    }
    
    private void setCurrentDate(String yearMonth) {
        getContext().data.put(CTX.YearMonthSet, yearMonth);
        getReplier().addLine("Set current date to: "+ yearMonth);
    }
}