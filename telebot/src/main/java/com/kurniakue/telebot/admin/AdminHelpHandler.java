/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.telebot.admin;

import com.kurniakue.telebot.Command;
import com.kurniakue.telebot.HelpHandler;

/**
 *
 * @author harun1
 */
public class AdminHelpHandler extends HelpHandler {

    private final Command cmd_recapitulation = new Command(this, "/rekapitulasi", () -> {
        return recapitulation();
    });

    private final Command cmd_items = new Command(this, "/item", () -> {
        return showItems();
    });

    private final Command[] adminMenu = {
        cmd(C.Help), cmd(C.Customer), cmd_items,
        cmd_recapitulation, cmd(C.Booking)};

    public AdminHelpHandler() {
        activeCommands = adminMenu;
    }

    @Override
    public boolean awal() {
        return petunjuk();
    }

    @Override
    public boolean petunjuk() {
        getReplier().add("Berikut ini daftar perintah yang tersedia. \n");
        getReplier().keyboard(cmdOf(activeCommands));
        getReplier().send();

        return true;
    }

    public boolean pelanggan() {
        if (!getContext().activate(C.Customer.cmd)) {
            return petunjuk();
        }
        return true;
    }

    public boolean recapitulation() {
        return getContext().transferTo(RecapitulationHandler.class);
    }

    public boolean showItems() {
        ItemsHandler handler = getContext().getHandler(ItemsHandler.class);
        return handler.transferTo(handler.cmd_items);
    }
}