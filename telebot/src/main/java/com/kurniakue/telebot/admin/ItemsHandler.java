/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.telebot.admin;

import com.kurniakue.common.Tool;
import com.kurniakue.data.Item;
import com.kurniakue.telebot.Command;
import com.kurniakue.telebot.UpdateHandler;
import com.pengrad.telegrambot.Replier;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author harun1
 */
public class ItemsHandler extends UpdateHandler {

    public final Command cmd_items = new Command(this, "/barang", () -> {
        return showItems();
    });

    public ItemsHandler() {
    }

    @Override
    public boolean execute() {
        return showItems();
    }
    
    private boolean showItems() {
        List<Item> list = new Item().getAllItems();
        Replier replier = getReplier();
        
        int count = 0;
        for (Item item : list) {
            count += 1;
            String strCount = StringUtils.leftPad(count + ". ", 5, "0");
            final String itemName = item.getString(Item.F.ItemName);
            replier.addLine(strCount)
                    .add(item.getString(Item.F.ItemNo)).add("-")
                    .add(itemName).add(": ");
            long price = item.getLong(Item.F.Price);
            replier.add(Tool.formatMoney(price));
        }
        
        replier.send();
        
        return petunjuk();
    }
}
