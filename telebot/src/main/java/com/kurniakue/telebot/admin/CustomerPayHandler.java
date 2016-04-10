/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.telebot.admin;

import com.kurniakue.data.Customer;
import com.kurniakue.data.Customer.F;
import com.kurniakue.data.DateInfo;
import com.kurniakue.telebot.Command;
import com.kurniakue.telebot.UpdateContext;
import com.kurniakue.telebot.UpdateHandler;
import com.kurniakue.telebot.admin.CustomerHandler.CTX;
import java.util.Calendar;
import com.kurniakue.common.Tool;

/**
 *
 * @author harun1
 */
public class CustomerPayHandler extends UpdateHandler {

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

    public int getAmount() {
        return getContext().data.getInt(CTX.Amount);
    }

    public CustomerPayHandler() {
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

        validateAndPay(text);
    }

    @Override
    public boolean execute() {
        return petunjuk();
    }

    public boolean show() {
        getReplier()
                .addLine("Masukkan nilai pembayarannya (tanpa titik dan koma)")
                .keyboard(cmdOf(menu))
                .send();
        return true;
    }

    private boolean validateAndPay(String text) {
        String[] parts = text.split(" ");

        int amount = Tool.tint(parts[0]);
        if (amount <= 0) {
            getReplier()
                    .addLine("Nilai  yang anda masukkan: ").add(parts[0])
                    .add(" masih belum benar")
                    .send();
            return show();
        }

        String date = DateInfo.getDateInfo(Calendar.getInstance())
                    .getString(DateInfo.F.Today);

        if (parts.length > 1) {
            if (parts[1].length() < 10) {
                date = date.substring(0, date.length() - parts[1].length())
                        + parts[1];
            }
            
            if (Tool.parseDate(date, "yyyy-MM-dd") == null) {
                getReplier()
                        .addLine("Tanggal yang anda masukkan: ").add(parts[0])
                        .add(" masih belum benar")
                        .send();
                return show();
            }
        }

        getCustomer().savePayment(amount, date);

        getReplier()
                .addLine("Pembayaran/setoran dari ").add(getCustomerName())
                .addLine("Senilai ").add(Tool.formatNumber(amount))
                .addLine("Tanggal ").add(date)
                .addLine("Telah berhasil");

        getReplier().send();
        backToCustomerDetail();

        return true;
    }

    public boolean backToCustomerDetail() {
        if (getParams() == null || getParams().length == 0) {
            setParams(new String[]{getCustomerName() + ";"});
        } else {
            getParams()[0] = getCustomerName() + ";";
        }

        CustomerDetailHandler handler = getContext()
                .getHandler(CustomerDetailHandler.class);
        return getContext().transferTo(handler, handler.cmd_show);
    }
}
