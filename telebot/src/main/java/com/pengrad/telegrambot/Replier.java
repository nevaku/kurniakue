/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pengrad.telegrambot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import java.util.List;

/**
 *
 * @author harun1
 */
public class Replier {

    private final TelegramBot bot;
    private final Update update;
    private StringBuilder reply = new StringBuilder(1000);
    private ReplyKeyboardMarkup replyMarkup = null;

    public Replier(TelegramBot bot, Update update) {
        this.bot = bot;
        this.update = update;
    }
    
    private static final int MAX_LENGTH = 3000;

    public Replier add(Object text) {
        if (reply.length() + String.valueOf(text).length() > MAX_LENGTH)
        {
            send();
            reply = new StringBuilder(1000);
        }
        reply.append(text);
        return this;
    }
    
    public Replier addLine(String text) {
        if (reply.length() + String.valueOf(text).length() > MAX_LENGTH)
        {
            send();
            reply = new StringBuilder(1000);
        }
        else
        {
            reply.append("\n");
        }
        reply.append(text);
        return this;
    }
    
    public void reset() {
        reply = new StringBuilder(1000);
        replyMarkup = null;
    }

    public Replier send() {
        if (update.message() == null) {
            return this;
        }
        if (update.message().chat() == null) {
            return this;
        }
        if (replyMarkup == null) {
            bot.sendMessage(update.message().chat().id(), reply.toString());
        } else {
            bot.sendMessage(update.message().chat().id(), reply.toString(), 
                    ParseMode.Markdown, Boolean.FALSE, 0, replyMarkup);
        }
        reset();
        return this;
    }

    public Replier keyboard(List<String> menuList) {
        return keyboard(menuList.toArray(new String[menuList.size()]));
    }

    public Replier keyboard(String... menuList) {
        final int col = 3;

        int rowSize = (menuList.length / col);
        if (menuList.length % col > 0) {
            rowSize += 1;
        }
        String[][] keyboardMenu = new String[rowSize][col];
        for (int i = 0; i < menuList.length; i++) {
            String menu = menuList[i];
            keyboardMenu[i / col][i % col] = menu;
        }
        
        for (int i = 0; i < col; i++) {
            if (keyboardMenu[rowSize-1][i] == null) {
                keyboardMenu[rowSize-1][i] = "";
            }
        }
        replyMarkup = new ReplyKeyboardMarkup(keyboardMenu, true, true, false);
        return this;
    }
}
