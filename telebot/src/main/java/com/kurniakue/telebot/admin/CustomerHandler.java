/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.telebot.admin;

import com.kurniakue.common.EnumField;
import com.kurniakue.data.Customer;
import com.kurniakue.telebot.Command;
import com.kurniakue.telebot.UpdateContext;
import com.kurniakue.telebot.UpdateHandler;
import com.pengrad.telegrambot.Replier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author harun1
 */
public class CustomerHandler extends UpdateHandler {
    private static final int maxListCount = 9;
        
    public enum C {

        Back("/balik"),
        Next("/lanjut"),
        Home("/awal");
        public String cmd;
        public String fun;

        private C(String command) {
            this.cmd = command;
            fun = command.substring(1);
        }
    }
    
    public final Command cmd_loadCurrentCustomerList = 
            new Command(this, "/loadCurrentCustomerList", () -> {
                loadCurrentCustomerList();
                return false;
    });

    public static CustomerHandler of(UpdateContext context) {
        return context.getHandler(CustomerHandler.class);
    }
    
    public enum CTX implements EnumField {
        CustomerList,
        CustomerListOffset,
        Customer,
        YearMonthSet,
        Amount;
    }
    
    public List<Customer> getList() {
        List<Customer> list = getContext().data.getAs(CTX.CustomerList);
        if (list == null) {
            list = new ArrayList<>();
            getContext().data.put(CTX.CustomerList, list);
        }
        
        return list;
    }
    
    public int getListOffset() {
        return getContext().data.getInt(CTX.CustomerListOffset);
    }
    
    public CustomerHandler setListOffset(int offset) {
        getContext().data.put(CTX.CustomerListOffset, offset);
        return this;
    }

    @Override
    public boolean execute() {
        pelanggan();
        return true;
    }

    public void pelanggan() {
        String[] params = getParams();
        
        List<Customer> customerList = new ArrayList<>();

        if (params.length == 0) {
            customerList = new Customer().getAllCustomers();
        } else if (params.length == 1 && params[0] != null && params[0].endsWith(";")) {
            getContext().transferTo(CustomerDetailHandler.class);
            return;
        } else if (params.length >= 1) {
            customerList = new Customer().getCustomerList(params[0]);
        }

        if (customerList.size() == 1) {
            CustomerDetailHandler handler = 
                    getContext().getHandler(CustomerDetailHandler.class);
            setFun(CustomerDetailHandler.C.Show.fun);
            handler.setCustomer(customerList.get(0));
            String customerName = customerList.get(0).getString(Customer.F.CustomerName);
            handler.setCustomerName(customerName);
            params[0] = customerName + ";";
            getContext().transferTo(handler);
            return;
        }
        
        getList().clear();
        getList().addAll(customerList);
        setListOffset(0);

        loadCurrentCustomerList();
    }

    public boolean loadCurrentCustomerList() {
        Replier replier = getReplier();
        List<Customer> customerList = getList();
        
        String[] customers;
        
        if (customerList.size() > maxListCount) {
            customers = new String[maxListCount + 3];
            Arrays.fill(customers, "");
            customers[maxListCount] = C.Home.cmd;
            customers[maxListCount + 1] = C.Back.cmd;
            customers[maxListCount + 2] = C.Next.cmd;
        } else {
            int size = customerList.size();
            int mod = (size % 3);
            if (mod > 0) {
                mod = 3 - mod;
            }
            customers = new String[size + mod + 1];
            Arrays.fill(customers, "");
            customers[customers.length - 1] = C.Home.cmd;
        }
        
        replier.add("silakan pilih ")
                .add(getListOffset() + 1).add(" - ")
                .add(getListOffset() + maxListCount)
                .add(" dari " + customerList.size());
        for (int i = 0; i < maxListCount; i++) {
            int index = i + getListOffset();
            if (index >= customerList.size()) {
                break;
            }
            Customer customer = customerList.get(index);
            customers[i] = "\"" + customer.getString(Customer.F.CustomerName) + ";\"";
        }

        replier.keyboard(customers);
        replier.send();
        
        return true;
    }
    
    public boolean balik() {
        if (getListOffset() == 0) {
            return true;
        }
        
        int newOffset = getListOffset() - maxListCount;
        if (newOffset <= 0) {
            newOffset = 0;
        }
        setListOffset(newOffset);
        return loadCurrentCustomerList();
    }
    
    public boolean lanjut() {
        int newOffset = getListOffset() + maxListCount;
        if (newOffset >= getList().size()) {
            loadCurrentCustomerList();
            return true;
        }
        
        setListOffset(newOffset);
        return loadCurrentCustomerList();
    }

//    @Override
//    public Command findCommand(String keyword) {
//        Command command = commandList.get(keyword);
//        if (command == null) {
//            return searchCustomersCommand;
//        }
//        return command;
//    }
//
//    private final Map<String, Command> commandList = new HashMap<String, Command>() {
//        {
//            put("linkToTele", linkToTeleCommand);
//        }
//    };
//
//    private final Command linkToTeleCommand = new Command() {
//
//        @Override
//        public void execute(Replier replier, String... params) {
//            linkToTele(replier, params);
//        }
//    };
//
//    private final Command searchCustomersCommand = new Command() {
//
//        @Override
//        public void execute(Replier replier, String... params) {
//            searchCustomers(replier, params);
//        }
//    };
}
