/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.telebot;

import com.kurniakue.common.Common;
import com.kurniakue.common.Tool;
import com.kurniakue.data.Customer;
import com.kurniakue.data.Transaction;
import com.pengrad.telegrambot.Replier;
import java.util.List;

/**
 *
 * @author harun1
 */
public class SaldoHandler extends UpdateHandler {

    @Override
    public boolean execute() {
        showSaldo();
        
        return true;
    }

    private void showSaldo() {

        Replier replier = getReplier();

        String telegramUserName = getContext().getUserName();
        String userName = getUserOfTelegram(telegramUserName);
        if (Tool.isBlank(userName)) {
            replier.add(telegramUserName + " tidak ditemukan")
                    .send();
            return;
        }

        List<Transaction> list = new Transaction().getTrxOfThisMonthByCustomer(userName);
        String lastUpdate = "";
        if (!list.isEmpty()) {
            lastUpdate = list.get(list.size() - 1)
                    .getString(Transaction.F.Date);
        }

        long total = 0;
        for (Transaction transaction : list) {
            long amount = transaction.getLong(Transaction.F.Amount);
            int dcflag = transaction.getInt(Transaction.F.DCFlag);
            total += (amount * dcflag);
        }
        
        String saldo = Tool.formatMoney(total);
        
        replier.add("Hai ").add(userName).add(".\n")
                .add("Saldo anda per tanggal ").add(lastUpdate).add(" adalah ")
                .add(saldo)
                .send();

    }

    private String getUserOfTelegram(String telegramUserName) {
        return new Customer()
                .loadByField(Customer.F.TelegramUser, telegramUserName)
                .getString(Customer.F.CustomerName);
    }
}
