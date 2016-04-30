/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.telebot.admin;

import com.kurniakue.data.Item;
import com.kurniakue.telebot.Command;
import com.kurniakue.telebot.UpdateHandler;
import com.kurniakue.telebot.admin.CustomerHandler.CTX;
import com.pengrad.telegrambot.Replier;

/**
 *
 * @author harun1
 */
public class ItemPropsHandler extends UpdateHandler {

    public final Command cmd_itemProps = new Command(this, "/show", () -> {
        return showItemProps();
    });

    public final Command cmd_itemSave = new Command(this, "/save", () -> {
        return saveItemProps();
    });
    
    public final Command cmd_items = new Command(this, "/items", () -> {
        return showItems();
    });
    
    public final Command cmd_cancel = new Command(this, "/cancel", () -> {
        return batal();
    });
    
    private final Command[] propMenu = {
        cmd(C.Home), cmd_itemSave, cmd_items
    };
    
    private final Command[] propSetMenu = {cmd_cancel};

    public ItemPropsHandler() {
        activeCommands = propMenu;
    }

    @Override
    public boolean execute() {
        if ("/items".equals(getCmd())) {
            return showItems();
        } else if (getCmd().startsWith("/set_")) {
            return setItemsProps();
        } else if ("".equals(getCmd())){
            return setProp();
        } else {
            setParams(getCmd().substring(1));
            setCmd("/props");
            return showItemProps();
        }
    }
    
    private boolean showItemProps() {
        Replier replier = getReplier();
        String itemNo;
        Item item;
        if (getParams().length > 0)
        {
            itemNo = getParams()[0];
            item = new Item().load(itemNo);
        }
        else
        {
            item = getContext().data.getAs(CTX.Item);
            if (item == null)
            {
                item = new Item();
            }
            itemNo = item.getString(Item.F.ItemNo);
        }
        
        replier.add("Property of Item ").add(itemNo);        
        if (!item.isExists()) {
            replier.addLine("Item: " + itemNo + " not found");
            replier.send();
            
            showItems();
            return true;
        }
        
        return showItemProps(item);
    }
    
    private boolean showItemProps(Item item) {
        activeCommands = propMenu;
        Replier replier = getReplier();
        getContext().data.put(CTX.Item, item);
        replier.addLine(Item.F.ItemNo).add(": ").add(item.getString(Item.F.ItemNo));
        replier.addLine("/set_").add(Item.F.ItemName).add(": ").add(item.getString(Item.F.ItemName));
        replier.addLine("/set_").add(Item.F.Description).add(": ").add(item.getString(Item.F.Description));
        replier.addLine("/set_").add(Item.F.Modal).add(": ").add(item.getString(Item.F.Modal));
        replier.addLine("/set_").add(Item.F.Price).add(": ").add(item.getString(Item.F.Price));
        replier.addLine("/set_").add(Item.F.State).add(": ").add(item.getString(Item.F.State));
        replier.addLine("/set_").add(Item.F.ItemType).add(": ").add(item.getString(Item.F.ItemType));
        replier.send();        
        
        replier.add("silakan");
        replier.keyboard(cmdOf(activeCommands));
        replier.send();
        
        return true;
    }
    
    private String fieldName = null;

    private boolean setItemsProps() {
        activeCommands = propSetMenu;
        fieldName = getCmd().substring(5);
        Replier replier = getReplier();
        replier.add("Masukkan " + fieldName +": ");
        replier.keyboard(cmdOf(activeCommands));
        replier.send();
        return true;
    }

    private boolean setProp() {
        Item item = getContext().data.getAs(CTX.Item);
        if (item == null) {
            return showItems();
        } else if (null == fieldName) {
            return showItemProps(item);
        }
        
        item.put(fieldName, getContext().getText());
        return showItemProps(item);
    }

    @Override
    public boolean batal() {
        Item item = getContext().data.getAs(CTX.Item);
        if (item == null) {
            return showItems();
        }
        return showItemProps(item);
    }

    private boolean saveItemProps() {
        Item item = getContext().data.getAs(CTX.Item);
        if (item == null) {
            return showItems();
        }
        
        item.update();
        item.load();
        setCmd("/" + item.getString(Item.F.ItemNo));
        
        return showItemProps(item);
    }

    private boolean showItems() {
        ItemsHandler handler = getContext().getHandler(ItemsHandler.class);
        return handler.open(handler.cmd_items);
    }
}
