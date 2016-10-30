/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.telebot.transaction;

import com.kurniakue.common.Common;
import com.kurniakue.common.Tool;
import com.kurniakue.data.Customer;
import com.kurniakue.data.DbProp;
import com.kurniakue.data.DbProp.N;
import com.kurniakue.data.Item;
import com.kurniakue.data.Transaction;
import com.kurniakue.data.TrxAccount;
import com.kurniakue.telebot.Command;
import com.kurniakue.telebot.Step;
import com.kurniakue.telebot.UpdateHandler;
import java.util.Date;
import java.util.List;

/**
 *
 * @author harun1
 */
public class TransferHandler extends UpdateHandler {

    private Transaction transaction = new Transaction();

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

    public final Step step_Source = new Step(this, "Masukkan ID sumber", () -> {
        return enterSource();
    });

    public final Step step_Destination = new Step(this, "Masukkan ID tujuan", () -> {
        return enterDestination();
    });

    public final Step step_Amount = new Step(this, "Jumlah yang akan ditransfer", () -> {
        return enterAmount();
    });

    public final Step step_Date = new Step(this, "Tanggal ", () -> {
        return enterDate();
    });

    public final Step step_Confirm = new Step(this, "Anda yakin? (ketik ya)", () -> {
        return enterConfirm();
    });

    private final Step[] steps = {
        step_Source, step_Destination,
        step_Amount, step_Date, step_Confirm
    };

    private int currentStepIndex = 0;
    private Step currentStep = null;

    @Override
    public boolean execute() {
        return currentStep.exec();
    }

    private boolean show() {
        activeCommands = transferMenu;
        currentStepIndex = -1;
        currentStep = step_Source;
        getReplier().addLine("HARUN_MEMBER_ID = \"HS005\"");
        getReplier().addLine("DINA_MEMBER_ID = \"DR001\"");
        getReplier().send();
        init();
        return showNextStep();
    }
    
    private void init() {
        transaction = new Transaction();
    }

    private boolean enterSource() {
        String sourceId = getContext().getText();
        long accountNo = Tool.idToNo(sourceId);
        Customer customer = new Customer().loadByCustomerId(sourceId);
        TrxAccount trxAccount = new TrxAccount();
        trxAccount.put(TrxAccount.F.AccountNo, accountNo);
        final String sourceName = customer.getString(Customer.F.CustomerName);
        trxAccount.put(TrxAccount.F.AccountName, sourceName);
        trxAccount.put(TrxAccount.F.Amount, 0);
        trxAccount.put(TrxAccount.F.DCFlag, -1);
        transaction.getTrxAccounts().add(trxAccount);
        
        transaction.put(Transaction.F.Description, "Transfer from " + sourceName);

        showNextStep();
        return true;
    }

    private boolean enterDestination() {
        String destinationId = getContext().getText();
        long accountNo = Tool.idToNo(destinationId);
        Customer customer = new Customer().loadByCustomerId(destinationId);
        TrxAccount trxAccount = new TrxAccount();
        trxAccount.put(TrxAccount.F.AccountNo, accountNo);
        final String destinationName = customer.getString(Customer.F.CustomerName);
        trxAccount.put(TrxAccount.F.AccountName, destinationName);
        trxAccount.put(TrxAccount.F.Amount, 0);
        trxAccount.put(TrxAccount.F.DCFlag, 1);
        transaction.getTrxAccounts().add(trxAccount);
        
        transaction.put(Transaction.F.Description,
                transaction.getString(Transaction.F.Description) 
                + " to " + destinationName);

        showNextStep();
        return true;
    }

    private boolean enterAmount() {
        long amount = Tool.tlong(getContext().getText());
        transaction.put(Transaction.F.Amount, amount);
        transaction.put(Transaction.F.DCFlag, amount >= 0 ? 1 : -1);
        transaction.put(Transaction.F.Price, amount);

        List<TrxAccount> trxAccounts = transaction.getTrxAccounts();
        for (TrxAccount trxAccount : trxAccounts) {
            trxAccount.put(TrxAccount.F.Amount, amount);
        }

        showNextStep();
        return true;
    }

    private boolean enterDate() {
        Date now = new Date();
        String text = getContext().getText();
        String strDate;
        if ("now".equalsIgnoreCase(text)) {
            strDate = Tool.formatDate(now, "yyyy-MM-dd");
        } else if (Tool.parseDateOrNull(text, "yyyy-MM-dd") != null) {
            strDate = text;
        } else {
            strDate = Tool.formatDate(now, "yyyy-MM-dd");
        }

        transaction.put(Transaction.F.Date, strDate);

        showNextStep();
        return true;
    }

    private boolean enterConfirm() {
        String text = getContext().getText();

        if (!"ya".equals(text)) {
            cmd_cancel.run();
        }

        transaction.put(Transaction.F.Count, 1);
        String customerId = getContext().getUserMemberId();
        String customerName = getContext().getMemberName();
        transaction.put(Transaction.F.CustomerID, customerId);
        transaction.put(Transaction.F.CustomerName, customerName);
        
        String itemNo = Common.TRAN;
        Item item = new Item().load(itemNo);
        transaction.put(Transaction.F.ItemNo, itemNo);
        transaction.put(Transaction.F.ItemName, item.getString(Item.F.ItemName));
        
        int trxId = DbProp.getAndInc(N.LastTransactionID);
        transaction.put(Transaction.F.TransactionID, trxId);
        transaction.store();

        getReplier().addLine("Transfer behasil...");
        return get(TransactionHandler.class).cmd_show.run();
    }

    private boolean showNextStep() {
        currentStepIndex += 1;
        currentStep = steps[currentStepIndex];
        getReplier().addLine(currentStep.text);
        getReplier().keyboard(cmdOf(transferMenu)).send();
        return true;
    }
}
