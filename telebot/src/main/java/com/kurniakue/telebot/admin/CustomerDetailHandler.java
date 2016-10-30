/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.telebot.admin;

import com.kurniakue.common.Tool;
import com.kurniakue.data.Customer;
import com.kurniakue.telebot.Command;
import com.kurniakue.telebot.UpdateContext;
import com.kurniakue.telebot.UpdateHandler;
import com.kurniakue.telebot.admin.CustomerHandler.CTX;

/**
 *
 * @author harun1
 */
public class CustomerDetailHandler extends UpdateHandler {

    public enum C {

        Show("/show"),
        LinkTele("/link"),
        Edit("/ubah"),
        Delete("/hapus"),
        New("/baru"),
        Back("/balik"),
        Home("/awal"),;
        public String cmd;
        public String fun;

        private C(String command) {
            this.cmd = command;
            if (Tool.isBlank(command)) {
                fun = command;
            } else {
                fun = command.substring(1);
            }
        }
    }

    public final Command cmd_deactivate = new Command(this, "/non\u2796aktifkan", () -> {
        return deactivate();
    });

    public final Command cmd_show = cmd(C.Show);

    public final Command cmd_Transaction = new Command(this, "/rincian", () -> {
        return showTransaction();
    });

    public final Command cmd_Pay = new Command(this, "/bayar", () -> {
        return showPayment();
    });

    //private final C[] customerMenu = {C.LinkTele, C.Edit, C.Delete, C.Home, C.Back, C.New};
    private final Command[] customerDetailMenu = {
        cmd(C.LinkTele), cmd(C.Edit), cmd(C.Delete),
        cmd_deactivate, cmd_Transaction, cmd_Pay,
        cmd(C.Home), cmd(C.Back), cmd(C.New)};

    public static CustomerDetailHandler of(UpdateContext context) {
        return context.getHandler(CustomerDetailHandler.class);
    }

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

    public CustomerDetailHandler() {
        activeCommands = customerDetailMenu;
    }

    @Override
    public boolean execute() {
        if (getParams().length == 0) {
            getContext().open(UpdateHandler.C.Customer.cmd);
        } else if (!getParams()[0].endsWith(";")) {
            return getContext().open(UpdateHandler.C.Customer.cmd);
        }

        return load();
    }

    public boolean load() {
        String[] params = getParams();

        if (params.length == 0) {
            customerName = "";
            customer = Customer.NO_CUSTOMER;
        } else {
            customerName = params[0].substring(0, params[0].length() - 1);
            customer = new Customer().load(customerName);
        }

        if (customer == Customer.NO_CUSTOMER) {
            showNoCustomer(customerName);
            return false;
        }

        return show();
    }

    public boolean show() {
        String state = customer.getString(Customer.F.State);
        if (Tool.isBlank(state)) {
            state = "Active";
        }

        String infoSaldoLastMonth = customer.getInfoSaldoLastMonth();
        String infoSaldo = customer.getInfoSaldo();

        getReplier().addLine("Name: ").add(customer.getString(Customer.F.CustomerName))
                .addLine("CustomerID: ").add(customer.getString(Customer.F.CustomerID))
                .addLine("Alias: ").add(customer.getString(Customer.F.Alias))
                .addLine("Email: ").add(customer.getString(Customer.F.Email))
                .addLine("TelegramUser: ").add(customer.getString(Customer.F.TelegramUser))
                .addLine("State: ").add(state)
                .addLine("Bulan Lalu: ").add(infoSaldoLastMonth)
                .addLine("Bulan Ini: ").add(infoSaldo)
                .keyboard(cmdOf(customerDetailMenu))
                .send();
        return true;
    }

    private void showNoCustomer(String customerName) {
        getReplier().add("Kami tidak menemukan : " + customerName)
                .send();
    }

    public boolean link() {
        CustomerLinkTeleHandler handler
                = getContext().getHandler(CustomerLinkTeleHandler.class);
        handler.setCustomer(customer);
        handler.setCustomerName(customerName);
        if (getParams().length == 0) {
            setParams(new String[]{customerName + ";"});
        } else {
            getParams()[0] = customerName + ";";
        }
        return getContext().open(handler, handler.cmd_linkTeleShow);
    }

    public boolean ubah() {
        getReplier().keyboard(cmdOf(customerDetailMenu));
        sorry();
        return true;
    }

    public boolean hapus() {
        getReplier().keyboard(cmdOf(customerDetailMenu));
        sorry();
        return true;
    }

    public boolean baru() {
        getReplier().keyboard(cmdOf(customerDetailMenu));
        sorry();
        return true;
    }

    public boolean balik() {
        return CustomerHandler.of(getContext()).cmd_loadCurrentCustomerList.run();
    }

    private boolean deactivate() {
        CustomerDeactivateConfirmHandler handler
                = getContext().getHandler(CustomerDeactivateConfirmHandler.class);
        handler.setCustomer(customer);
        handler.setCustomerName(customerName);
        if (getParams().length == 0) {
            setParams(new String[]{customerName + ";"});
        } else {
            getParams()[0] = customerName + ";";
        }
        return getContext().open(handler, handler.cmd_show);
    }

    private boolean showTransaction() {
        getContext().data.put(CTX.Customer, customer);
        CustomerTransactionHandler handler = getContext().getHandler(CustomerTransactionHandler.class);
        return handler.open(handler.cmd_show);
    }

    private boolean showPayment() {
        getContext().data.put(CTX.Customer, customer);
        CustomerPayHandler handler = getContext().getHandler(CustomerPayHandler.class);
        return handler.open(handler.cmd_show);
    }
}
