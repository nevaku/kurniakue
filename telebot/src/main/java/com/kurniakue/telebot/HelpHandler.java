/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.telebot;

/**
 *
 * @author harun1
 */
public class HelpHandler extends UpdateHandler
{

    @Override
    public boolean execute() {
        return petunjuk();
    }
    
    @Override
    public boolean petunjuk() {
        getReplier().add("Berikut ini daftar perintah yang tersedia. \n");
        getReplier().keyboard(C.Help.cmd, C.Info.cmd, C.Booking.cmd);
        getReplier().send();
        
        return true;
    }
    
    public boolean info() {
        if (!getContext().activate(C.Info.cmd)) {
            return petunjuk();
        }
        return true;
    }
    
    public boolean pemesanan() {
        if (!getContext().activate(C.Booking.cmd)) {
            return petunjuk();
        }
        return true;
    }
    
    @Override
    public boolean batal() {
        return petunjuk();
    }
}
