/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.telebot.admin;

import com.kurniakue.common.Tool;
import com.kurniakue.data.DateInfo;
import com.kurniakue.data.Record;
import com.kurniakue.data.Transaction;
import com.kurniakue.telebot.Command;
import com.kurniakue.telebot.UpdateHandler;
import com.pengrad.telegrambot.Replier;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author harun1
 */
@Deprecated
public class BalanceHandler extends UpdateHandler {

    public final Command cmd_balance = new Command(this, "/balance", () -> {
        return showBalance();
    });

    public BalanceHandler() {
    }

    @Override
    public boolean execute() {
        return showBalance();
    }

    private boolean showBalance() {
        Calendar calendar = Calendar.getInstance();
        String ThisYearMonth = DateInfo.getDateInfo(calendar)
                .getString(DateInfo.F.ThisYearMonth);

        List<Record> list = new Transaction().getRekapOfMonthPerCustomer(ThisYearMonth);
        Replier replier = getReplier();

        long totalPay = 0;
        long totalBill = 0;
        int count = 0;
        for (Record record : list) {
            long amount = record.getLong(Transaction.V.Rekap);
            if (amount == 0) {
                continue;
            }
            count += 1;
            String strCount = StringUtils.leftPad(count + ". ", 5, "0");
            final String customer = record.getString(Transaction.F._id);
            replier.addLine(strCount).add(customer).add(": ");

            if (amount < 0) {
                totalBill -= amount;
                replier.add(Tool.formatMoney(-amount));
            } else if (amount > 0) {
                totalPay += amount;
                replier.add(Tool.formatMoney(amount)).add(" \uD83D\uDC4C");
            }
        }
        replier.addLine("");
        replier.addLine("Total Tagihan : ").add(Tool.formatMoney(totalBill));
        replier.addLine("Total Saldo   : ").add(Tool.formatMoney(totalPay));

        replier.send();

        return petunjuk();
    }
}
