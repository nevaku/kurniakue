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
public class Step {

    public final String text;
    
    private final Callable<Boolean> method;

    private final UpdateHandler handler;

    public UpdateHandler getHandler() {
        return handler;
    }

    public Step(UpdateHandler handler, String text, Callable<Boolean> method) {
        this.handler = handler;
        this.text = text;
        
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
}
