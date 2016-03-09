/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.telebot.admin;

import static com.kurniakue.common.Common.formatMoney;
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
public class RecapitulationHandler extends UpdateHandler {
    
    private boolean confirm = false;
    
    public final Command cmd_show = new Command(this, "/rekapitulasi", () -> {
        return show();
    });
    
    public final Command cmd_yes = new Command(this, "/ya", () -> {
        confirm = true;
        return calculate();
    });
    
    public final Command cmd_no = new Command(this, "/tidak", () -> {
        return show();
    });
    
    public final Command cmd_bill = new Command(this, "/tagihan", () -> {
        return showBill();
    });
    
    public final Command cmd_deposit = new Command(this, "/deposit", () -> {
        return showDeposit();
    });
    
    public final Command cmd_all = new Command(this, "/gabungan", () -> {
        return showCombination();
    });
    
    public final Command cmd_income = new Command(this, "/pemasukan", () -> {
        return showIncome();
    });
    
    public final Command cmd_expense = new Command(this, "/pengeluaran", () -> {
        return showExpense();
    });
    
    public final Command cmd_balance = new Command(this, "/balance", () -> {
        return showBalance();
    });
    
    public final Command cmd_calculate = new Command(this, "/calculate", () -> {
        return calculate();
    });
    
    private final Command[] rekapMenu = {
        cmd_bill, cmd_deposit, cmd_all,
        cmd_expense, cmd_income, cmd_balance,
        cmd(C.Home), cmd_calculate};
    
    private final Command[] confirmMenu = {cmd_no, cmd_yes};

    public RecapitulationHandler() {
        activeCommands = rekapMenu;
    }

    @Override
    public boolean execute() {
        return show();
    }
    
    private boolean show() {
        getReplier().addLine("Silakan").keyboard(cmdOf(rekapMenu)).send();
        return true;
    }
    
    private boolean showIncome() {
        Calendar calendar = Calendar.getInstance();
        String ThisYearMonth = DateInfo.getDateInfo(calendar)
                .getString(DateInfo.F.ThisYearMonth);

        List<Record> list = new Transaction().getPaymentsOfMonthPerCustomer(ThisYearMonth);
        return showTransactions(list, ShowFlag.Positive);
    }
    
    private boolean showExpense() {
        Calendar calendar = Calendar.getInstance();
        String ThisYearMonth = DateInfo.getDateInfo(calendar)
                .getString(DateInfo.F.ThisYearMonth);

        List<Record> list = new Transaction().getPaymentsOfMonthPerCustomer(ThisYearMonth);
        return showTransactions(list, ShowFlag.Negative);
    }
    
    private boolean showBalance() {
        Calendar calendar = Calendar.getInstance();
        String ThisYearMonth = DateInfo.getDateInfo(calendar)
                .getString(DateInfo.F.ThisYearMonth);

        List<Record> list = new Transaction().getPaymentsOfMonthPerCustomer(ThisYearMonth);
        return showTransactions(list, ShowFlag.All);
    }
    
    private boolean showBill() {
        Calendar calendar = Calendar.getInstance();
        String ThisYearMonth = DateInfo.getDateInfo(calendar)
                .getString(DateInfo.F.ThisYearMonth);

        List<Record> list = new Transaction().getRekapOfMonthPerCustomer(ThisYearMonth);
        return showTransactions(list, ShowFlag.Negative);
    }
    
    private boolean showDeposit() {
        Calendar calendar = Calendar.getInstance();
        String ThisYearMonth = DateInfo.getDateInfo(calendar)
                .getString(DateInfo.F.ThisYearMonth);

        List<Record> list = new Transaction().getRekapOfMonthPerCustomer(ThisYearMonth);
        return showTransactions(list, ShowFlag.Positive);
    }
    
    private boolean showCombination() {
        Calendar calendar = Calendar.getInstance();
        String ThisYearMonth = DateInfo.getDateInfo(calendar)
                .getString(DateInfo.F.ThisYearMonth);

        List<Record> list = new Transaction().getRekapOfMonthPerCustomer(ThisYearMonth);
        return showTransactions(list, ShowFlag.All);
    }
    
    private enum ShowFlag {
        All,
        Negative,
        Positive,
    }
    
    private boolean showTransactions(List<Record> list, ShowFlag showFlag) {
        Replier replier = getReplier();

        long totalPay = 0;
        int count = 0;
        for (Record record : list) {
            long amount = record.getLong(Transaction.V.Rekap);
            if (amount == 0) {
                continue;
            } else if (amount > 0 && showFlag == ShowFlag.Negative) {
                continue;
            } else if (amount < 0 && showFlag == ShowFlag.Positive) {
                continue;
            }
            
            count += 1;
            String strCount = StringUtils.leftPad(count + ". ", 5, "0");
            final String customer = record.getString(Transaction.F._id);
            replier.addLine(strCount).add(customer).add(": ");
            totalPay += amount;
            replier.add(formatMoney(amount));
        }
        replier.addLine("");
        replier.addLine("Total : ").add(formatMoney(totalPay));

        replier.send();
        return true;
    }
    
    private boolean calculate() {
        Replier replier = getReplier();
        
        if (!confirm) {
            replier.addLine("Anda yakin akan menghitung ulang rekapitulasi?")
                    .keyboard(cmdOf(confirmMenu))
                    .send();
            return true;
        }
        
        new Transaction().recapitulate(DateInfo.getDateInfo(Calendar.getInstance()));
        
        replier.add("Rekapitulasi selesai.");
        confirm = false;
        show();
        return true;
    }
}
