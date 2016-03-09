/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.telebot;

import com.pengrad.telegrambot.Replier;

/**
 *
 * @author harun1
 */
public class InfoHandler extends UpdateHandler
{

    @Override
    public boolean execute() {
        showMoreInfo(getReplier());
        return true;
    }

    private void showMoreInfo(Replier replier)
    {
        replier.add("Mau lihat info apa? \n");
        String[] infoList = {"/saldo", "/jadwal"};
        for (String info : infoList)
        {
            replier.add(info).add("\n");
        }
        
        replier.keyboard(infoList);
        replier.send();
    }
}
