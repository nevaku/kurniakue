/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.telebot.admin;

import com.kurniakue.data.Customer;
import com.kurniakue.telebot.Command;
import com.kurniakue.telebot.UpdateHandler;

/**
 *
 * @author harun1
 */
public class CustomerLinkTeleHandler extends UpdateHandler {

    public final Command cmd_linkTeleCancel = new Command(this, "/batal", () -> {
        cancel();
        return false;
    });

    public final Command cmd_linkTeleShow = new Command(this, "/show", () -> {
        show();
        return false;
    });

    public final Command[] linkTeleMenus = {cmd_linkTeleCancel};
    
    private Customer customer;

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }
    
    private String customerName;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    private String telegramUserName;

    public String getTelegramUserName() {
        return telegramUserName;
    }

    public void setTelegramUserName(String telegramUserName) {
        this.telegramUserName = telegramUserName;
    }

    public CustomerLinkTeleHandler() {
        activeCommands = linkTeleMenus;
    }

    @Override
    public boolean execute() {
        return saveLinkTele();
    }

    public boolean show() {
        getReplier().keyboard(cmdOf(linkTeleMenus))
                .add("Enter telegram user name of " + customerName)
                .send()
                ;
        
        return true;
    }

    public boolean saveLinkTele() {
        telegramUserName = getParams()[0];
        customer.put(Customer.F.TelegramUser, telegramUserName);
        customer.update();
        getReplier()
                .add("Telegram user dari " + customerName + " => " + telegramUserName)
                .send()
                ;
        return backToCustomerDetail();
    }
    
    public boolean cancel() {
        return backToCustomerDetail();
    }
    
    public boolean backToCustomerDetail() {
        if (getParams() == null || getParams().length == 0) {
            setParams(new String[]{customerName + ";"});
        } else {
            getParams()[0] = customerName + ";";
        }
        return getContext().transferTo(CustomerDetailHandler.class);
    }
}
