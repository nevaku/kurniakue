/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.telebot.admin;

import com.kurniakue.telebot.Command;
import com.kurniakue.telebot.UpdateHandler;
import com.pengrad.telegrambot.Replier;

/**
 *
 * @author harun1
 */
public class PropHandler extends UpdateHandler {

    public final Command cmd_setProp = new Command(this, "/enter", () -> {
        return enterProp();
    });
    
    public final Command cmd_ok = new Command(this, "/ok", () -> {
        return ok();
    });
    
    public final Command cmd_cancel = new Command(this, "/cancel", () -> {
        return batal();
    });

    public PropHandler() {
    }

    @Override
    public boolean execute() {
        return enterProp();
    }
    
    private boolean enterProp() {
        Replier replier = getReplier();
        String objContextName = getParams()[0];
        String propName = getParams()[1];
        
        replier.add("Enter value for ").add(propName);
        replier.keyboard(C.Home.cmd, cmd_ok.getCmd(), cmd_cancel.getCmd());
        replier.send();
        
        return true;
    }

    @Override
    public boolean batal() {
        return super.batal(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean ok() {
        return super.ok(); //To change body of generated methods, choose Tools | Templates.
    }
}
