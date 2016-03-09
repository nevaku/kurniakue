/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.telebot;

import com.kurniakue.common.Tool;
import java.util.concurrent.Callable;

/**
 *
 * @author harun1
 */
public class Command {

    public static final String CMD = "cmd";
    public static final String FUN = "fun";
    
    private final String cmd;

    public String getCmd() {
        return cmd;
    }

    private final String fun;

    public String getFun() {
        return fun;
    }
    private final Callable<Boolean> method;

    private final UpdateHandler handler;

    public UpdateHandler getHandler() {
        return handler;
    }

    public Command(UpdateHandler handler, String cmd, Callable<Boolean> method) {
        this.handler = handler;
        this.cmd = cmd;
        if (Tool.isBlank(cmd)) {
            this.fun = cmd;
        } else {
            this.fun = cmd.substring(1);
        }
        
        this.method = method;
    }

    public Command(UpdateHandler handler, Enum enumCommand, Callable<Boolean> method) {
        this.handler = handler;
        this.cmd = Tool.getMember(CMD).of(enumCommand).getString();
        this.fun = Tool.getMember(FUN).of(enumCommand).getString();
        this.method = method;
    }

    public boolean exec() {
        try {
            return method.call();
        } catch (Exception exc) {
            exc.printStackTrace();
            return false;
        }
    }

    public boolean transfer() {
        return handler.transferTo(this);
    }
}
