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
import com.kurniakue.telebot.admin.CustomerHandler.CTX;
import com.pengrad.telegrambot.Replier;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author harun1
 */
public class ItemsHandler extends UpdateHandler {

    public final Command cmd_items = new Command(this, "/items", () -> {
        return showItems();
    });

    public ItemsHandler() {
    }

    @Override
    public boolean execute() {
        if ("/items".equals(getCmd())) {
            return showItems();
        } else {
            return showItemProps();
        }
    }
    
    private boolean showItems() {
        getContext().data.put(CTX.Item, null);
        List<Item> list = new Item().getProductList();
        Replier replier = getReplier();
        
        int count = 0;
        for (Item item : list) {
            count += 1;
            String strCount = StringUtils.leftPad(count + ". ", 5, "0");
            final String itemName = item.getString(Item.F.ItemName);
            replier.addLine(strCount)
                    .add("/").add(item.getString(Item.F.ItemNo)).add("-")
                    .add(itemName).add(": ");
            long price = item.getLong(Item.F.Price);
            replier.add(Tool.formatMoney(price));
        }
        
        replier.keyboard(C.Home.cmd);
        replier.send();
        
        return true;
    }

    private boolean showItemProps() {
        setParams(getCmd().substring(1));
        ItemPropsHandler handler = getContext().getHandler(ItemPropsHandler.class);
        return handler.open(handler.cmd_itemProps);
    }
}
