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
    
    public final Command cmd_show = new Command(this, "/show", () -> {
        showMoreInfo();
        return true;
    });
    
    public final Command cmd_saldo = new Command(this, "/saldo", () -> {
        return showSaldo();
    });
    
    public final Command cmd_jadwal = new Command(this, "/jadwal", () -> {
        return showJadwal();
    });
    
    private final Command[] infoMenu = {
        cmd_saldo, cmd_jadwal, cmd_nihil};

    public InfoHandler() {
         activeCommands = infoMenu;
    }

    @Override
    public boolean execute() {
        showMoreInfo();
        return true;
    }

    private void showMoreInfo()
    {
        activeCommands = rekapMenu;
        getReplier().addLine("Silakan").keyboard(cmdOf(rekapMenu)).send();
        return true;
        Replier replier = getReplier();
        replier.add("Mau lihat info apa? \n");
        String[] infoList = {"/saldo", "/jadwal"};
        
        replier.keyboard(infoList);
        replier.send();
    }
    
    private boolean showSaldo()
    {
        return true;
    }
    
    private boolean showJadwal()
    {
        return true;
    }
}
