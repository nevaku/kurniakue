/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.telebot;

import com.pengrad.telegrambot.Replier;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author harun1
 */
public class JadwalHandler extends UpdateHandler
{

    @Override
    public boolean execute() {
        showInfoJadwal();
        return true;
    }

    private void showInfoJadwal()
    {
        Replier replier = getReplier();
        replier.add("Jadwal penjualan kue minggu ini\n");
        replier.add(getFoodSchedule());
        replier.send();
    }

    private String getFoodSchedule()
    {
        Calendar calendar = Calendar.getInstance();
        int dow = calendar.get(Calendar.DAY_OF_WEEK);
        int delta;
        if (dow == Calendar.SUNDAY)
        {
            delta = 1;
        }
        else if (dow == Calendar.SATURDAY)
        {
            delta = 2;
        }
        else
        {
            delta = Calendar.MONDAY - dow;
        }
        calendar.add(Calendar.DAY_OF_YEAR, delta);
        Date start = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 5);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        
        StringBuilder foodSchedule = new StringBuilder(1000);
        String[] foods = getFoodOfThisWeek(start);
                
        Date day = start;
        calendar.setTime(day);
        for (String food : foods)
        {
            foodSchedule.append(df.format(day)).append(" - ").append(food).append("\n");
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            day = calendar.getTime();
        }
        
        return foodSchedule.toString();
    }

    private String[] getFoodOfThisWeek(Date start) {
        String[] foods = {" - ", " - ", " - ", " - ", " - "};
        return foods;
    }
}
