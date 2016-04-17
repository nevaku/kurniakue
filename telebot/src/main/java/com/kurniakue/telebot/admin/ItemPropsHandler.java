/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.telebot.admin;

import com.kurniakue.data.Item;
import com.kurniakue.telebot.Command;
import com.kurniakue.telebot.UpdateHandler;
import com.pengrad.telegrambot.Replier;

/**
 *
 * @author harun1
 */
public class ItemPropsHandler extends UpdateHandler {

    public final Command cmd_itemProps = new Command(this, "/props", () -> {
        return showItemProps();
    });
    
    public final Command cmd_items = new Command(this, "/items", () -> {
        return showItems();
    });

    public ItemPropsHandler() {
    }

    @Override
    public boolean execute() {
        if ("/items".equals(getCmd())) {
            return showItems();
        } else {
            setParams(getCmd().substring(1));
            setCmd("/props");
            return showItemProps();
        }
    }
    
    private boolean showItemProps() {
        Replier replier = getReplier();
        String itemNo = getParams()[0];
        replier.add("Property of Item ").add(itemNo);
        
        Item item = new Item().load(itemNo);
        replier.addLine(Item.F.ItemNo).add(": ").add(item.getString(Item.F.ItemNo));
        replier.addLine("/set").add(Item.F.ItemName).add(": ").add(item.getString(Item.F.ItemName));
        replier.addLine("/").add(Item.F.Description).add(": ").add(item.getString(Item.F.Description));
        replier.addLine("/").add(Item.F.Modal).add(": ").add(item.getString(Item.F.Modal));
        replier.addLine("/").add(Item.F.Price).add(": ").add(item.getString(Item.F.Price));
        replier.addLine("/").add(Item.F.State).add(": ").add(item.getString(Item.F.State));
        replier.addLine("/").add(Item.F.ItemType).add(": ").add(item.getString(Item.F.ItemType));
        replier.keyboard(C.Home.cmd, cmd_items.getCmd());
        replier.send();
        
        return true;
    }

    private boolean showItems() {
        ItemsHandler handler = getContext().getHandler(ItemsHandler.class);
        return handler.transferTo(handler.cmd_items);
    }
}
