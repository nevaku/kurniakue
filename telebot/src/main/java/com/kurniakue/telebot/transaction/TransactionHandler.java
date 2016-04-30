/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.telebot.transaction;

import com.kurniakue.telebot.Command;
import com.kurniakue.telebot.UpdateHandler;

/**
 *
 * @author harun1
 */
public class TransactionHandler extends UpdateHandler {
    
    private boolean confirm = false;
    
    public final Command cmd_show = new Command(this, "/transaksi", () -> {
        return show();
    });
    
    public final Command cmd_transfer = new Command(this, "/transfer", () -> {
        return get(TransferHandler.class).cmd_show.run();
    });
    
    public final Command cmd_deposit = new Command(this, "/setoran", () -> {
        return get(TransactionHandler.class).cmd_show.run();
    });
    
    public final Command cmd_withdrawal = new Command(this, "/penarikan", () -> {
        return get(TransactionHandler.class).cmd_show.run();
    });
    
    private final Command[] transactionMenu = {
        cmd_transfer, cmd_deposit, cmd_withdrawal,
        cmd_home};

    public TransactionHandler() {
        activeCommands = transactionMenu;
    }

    @Override
    public boolean execute() {
        return show();
    }
    
    private boolean show() {
        activeCommands = transactionMenu;
        getReplier().addLine("Silakan").keyboard(cmdOf(transactionMenu)).send();
        return true;
    }
}
