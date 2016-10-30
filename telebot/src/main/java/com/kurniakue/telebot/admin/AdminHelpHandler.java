/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.telebot.admin;

import com.kurniakue.common.Tool;
import com.kurniakue.telebot.transaction.TransactionHandler;
import com.kurniakue.telebot.Command;
import com.kurniakue.telebot.HelpHandler;
import java.awt.GraphicsDevice;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import javax.imageio.ImageIO;

/**
 *
 * @author harun1
 */
public class AdminHelpHandler extends HelpHandler {

    private final Command cmd_recapitulation = new Command(this, "/rekapitulasi", () -> {
        return recapitulation();
    });

    private final Command cmd_items = new Command(this, "/items", () -> {
        return showItems();
    });
    
    public final Command cmd_setDate = new Command(this, "/set_Tanggal", () -> {
        return setCurrentDate();
    });
    
    public final Command cmd_transactions = new Command(this, "/transaksi", () -> {
        return get(TransactionHandler.class).cmd_show.run();
    });
    
    public final Command cmd_capture = new Command(this, "/capture", () -> {
        return capture();
    });

    private final Command[] adminMenu = {
        cmd(C.Help), cmd(C.Customer), cmd_items,
        cmd_recapitulation, cmd_setDate, cmd_transactions,
        cmd_capture, cmd_nihil, cmd_nihil
    };

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
        return getContext().open(RecapitulationHandler.class);
    }

    public boolean showItems() {
        ItemsHandler handler = getContext().getHandler(ItemsHandler.class);
        return handler.open(handler.cmd_items);
    }
    
    private boolean setCurrentDate() {
        return getContext().open(DateHandler.class);
    }
    
    private boolean capture() {
        try {
            GraphicsDevice currentDevice = MouseInfo.getPointerInfo().getDevice();
            Rectangle bounds = currentDevice.getDefaultConfiguration().getBounds();
            System.out.println(bounds);
            Robot robot = new Robot(currentDevice);
            BufferedImage capture = robot.createScreenCapture(bounds);
            System.out.println(capture);
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                ImageIO.write(capture, "jpg", baos);
                String fileName = "sc-" + Tool.formatDate(new Date(), "yyyyMMdd-HHmmss") + ".jpg";
                getReplier().sendImg(baos.toByteArray(), fileName);

            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return true;
    }
}
