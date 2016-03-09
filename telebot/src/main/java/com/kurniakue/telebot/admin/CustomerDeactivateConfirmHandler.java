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
public class CustomerDeactivateConfirmHandler extends UpdateHandler {

    private final Command cmd_No = new Command(this, "/tidak", () -> {
        return cancel();
    });

    private final Command cmd_Yes = new Command(this, "/ya", () -> {
        return deactivateUser();
    });

    public final Command cmd_show = new Command(this, "/show", () -> {
        return show();
    });

    private final Command[] menu = {cmd_No, cmd_Yes};
    
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

    public CustomerDeactivateConfirmHandler() {
        activeCommands = menu;
    }

    @Override
    public boolean execute() {
        return cancel();
    }

    public boolean show() {
        getReplier().keyboard(cmdOf(menu))
                .add("Non-aktifkan " + customerName + " ?")
                .send()
                ;
        
        return true;
    }

    public boolean deactivateUser() {
        customer.put(Customer.F.State, "NA");
        customer.update();
        getReplier()
                .add(customerName + " telah di-non-aktifkan")
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
        
        CustomerDetailHandler handler = getContext().getHandler(CustomerDetailHandler.class);
        return getContext().transferTo(handler, handler.cmd_show);
    }
}
