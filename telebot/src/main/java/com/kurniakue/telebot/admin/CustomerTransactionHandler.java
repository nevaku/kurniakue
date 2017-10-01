/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.telebot.admin;

import com.kurniakue.data.Customer;
import com.kurniakue.data.Customer.F;
import com.kurniakue.data.DateInfo;
import com.kurniakue.data.Transaction;
import com.kurniakue.telebot.Command;
import com.kurniakue.telebot.UpdateContext;
import com.kurniakue.telebot.UpdateHandler;
import com.kurniakue.telebot.admin.CustomerHandler.CTX;
import java.util.Calendar;
import java.util.List;
import com.kurniakue.common.Tool;

/**
 *
 * @author harun1
 */
public class CustomerTransactionHandler extends UpdateHandler {

    private final Command cmd_home = new Command(this, "/awal", () -> {
        return petunjuk();
    });

    private final Command cmd_back = new Command(this, "/balik", () -> {
        return backToCustomerDetail();
    });

    public final Command cmd_show = new Command(this, "/show", () -> {
        return show();
    });

    private final Command[] menu = {cmd_home, cmd_back, cmd_nihil};

    public Customer getCustomer() {
        return getContext().data.getAs(CTX.Customer);
    }

    public String getCustomerName() {
        return getCustomer().getString(F.CustomerName);
    }

    public CustomerTransactionHandler() {
        activeCommands = menu;
    }
    
    public String[] getKeyboards() {
        return getContext().data.getAs(CTX.YearMonthSet);
    }

    @Override
    public void handle(UpdateContext context) {
        String text = context.getText();
        if (text.startsWith("/")) {
            super.handle(context);
            return;
        }
        
        String[] keyboards = getKeyboards();
        for (String keyboard : keyboards) {
            if (keyboard.equals(text)) {
                showTransactionOfMonth(text);
                return;
            }
        }
    }

    @Override
    public boolean execute() {
        return cancel();
    }

    public boolean show() {
        List<Transaction> trxList = getCustomer().getTransactionListOfLastMonth();
        return showTransaction(trxList);
    }

    public boolean showTransactionOfMonth(String yearMonth) {
        List<Transaction> trxList = getCustomer()
                .getTransactionListOfMonth(yearMonth);
        return showTransaction(trxList);
    }

    private boolean showTransaction(List<Transaction> trxList) {

        getReplier()
                .add("Transaksi dari ").add(getCustomerName());

        int total = 0;
        for (Transaction transaction : trxList) {
            int count = transaction.getInt(Transaction.F.Count);
            int amount = transaction.getInt(Transaction.F.Amount);
            int dcflag = transaction.getInt(Transaction.F.DCFlag);
            total += (amount * dcflag);
            String samount;
            if (count > 1) {
                samount = count + " x @"
                        + Tool.formatNumber(transaction.getInt(Transaction.F.Price))
                        + " = "
                        + Tool.formatNumber(amount);
            } else {
                samount = Tool.formatNumber(amount);
            }

            getReplier()
                    .addLine(transaction.getString(Transaction.F.Date))
                    .add(" ").add(transaction.getString(Transaction.F.ItemName))
                    .add(": ").add(samount);
            
            if (dcflag > 0) {
                getReplier().add(" \uD83D\uDC4C");
            }
        }
        
        
        String infoSaldo;
        if (total < 0) {
            infoSaldo = "Tagihan: " + Tool.formatMoney(-total);
        } else if (total > 0) {
            infoSaldo = "Saldo: " + Tool.formatMoney(total);
        } else {
            infoSaldo = "Tidak ada saldo/tagihan";
        }
        
        getReplier().addLine(infoSaldo);
        buildKeyboard();
        getReplier().send();
        
        return true;
    }
    
    private void buildKeyboard() {
        Calendar calendar = getContext().getCurrentCalendar();
        String thisYearMonth
                = DateInfo.getDateInfo(calendar)
                .getString(DateInfo.F.ThisYearMonth);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        String lastYearMonth
                = DateInfo.getDateInfo(calendar)
                .getString(DateInfo.F.ThisYearMonth);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        String beforeLastYearMonth
                = DateInfo.getDateInfo(calendar)
                .getString(DateInfo.F.ThisYearMonth);
        
        String[] keyboards = {beforeLastYearMonth, lastYearMonth, thisYearMonth,
                cmd_home.getCmd(), cmd_back.getCmd()};
        getContext().data.put(CTX.YearMonthSet, keyboards);
        getReplier().keyboard(keyboards);
    }
    
    public boolean cancel() {
        return backToCustomerDetail();
    }
    
    public boolean backToCustomerDetail() {
        if (getParams() == null || getParams().length == 0) {
            setParams(new String[]{getCustomerName() + ";"});
        } else {
            getParams()[0] = getCustomerName() + ";";
        }
        
        CustomerDetailHandler handler = getContext()
                .getHandler(CustomerDetailHandler.class);
        return getContext().open(handler, handler.cmd_show);
    }
}
