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
public class TransferHandler extends UpdateHandler {

    private boolean confirm = false;

    public final Command cmd_show = new Command(this, "/show", () -> {
        return show();
    });
    
    public final Command cmd_cancel = new Command(this, "/batal", () -> {
        return get(TransactionHandler.class).cmd_show.run();
    });

    private final Command[] transferMenu = {
        cmd_home, cmd_cancel};

    public TransferHandler() {
        activeCommands = transferMenu;
    }

    public enum Step {
        Source("Masukkan No. Rekening sumber"),
        Destination("Masukkan No. Rekening tujuan"),
        Amount("Masukkan jumlah"),
        Confirm("Anda yakin? (ketik ya)");
        public final String text;

        private Step(String text) {
            this.text = text;
        }
    }
    
    private int currentStepIndex = 0;

    @Override
    public boolean execute() {
        currentStepIndex += 1;
        if (currentStepIndex == Step.values().length) {
            return transfer();
        }
        return enterValueOnStep();
    }

    private boolean show() {
        activeCommands = transferMenu;
        currentStepIndex = 0;
        return enterValueOnStep();
    }
    
    private boolean transfer() {
        getReplier().addLine("Transfer berhasil").send();
        return get(TransactionHandler.class).cmd_show.run();
    }
    
    private boolean enterValueOnStep() {
        getReplier().addLine(Step.values()[currentStepIndex].text)
                .keyboard(cmdOf(transferMenu)).send();
        return true;
    }
}
