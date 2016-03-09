/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.telebot;

import com.kurniakue.data.DbProp;
import com.kurniakue.data.KurniaKueDb;
import com.kurniakue.data.TheConfig;
//import static com.kurniakue.telebot.UpdateHandler.getHandler;
//import static com.kurniakue.telebot.UpdateHandler.getRegisteredHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
//import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import com.kurniakue.data.DbProp.N;

/**
 *
 * @author harun1
 */
@SuppressWarnings("CallToPrintStackTrace")
public class Main {

    private static final AtomicBoolean started = new AtomicBoolean(false);

    private static TelegramBot bot;
    private static TheConfig config;

    public static void main(String[] args) throws Exception {
        started.set(true);
        config = KurniaKueDb.getConfig();
        config.load("c:/harun/cfg/telebot.conf");
        startBotting();
        waitForExit();
        disconnectDb();
    }

    private static void startBotting() {
        Thread botListenerThread = new Thread(()
                -> {
                    listenForUpdates();
                });

        botListenerThread.start();

        Thread botHandlerThread = new Thread(()
                -> {
                    try {
                        handleUpdates();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        botHandlerThread.start();
    }

    private static void listenForUpdates() {
        String token = config.getProperty("TelebotToken");
        bot = TelegramBotAdapter.build(token);
        int offset = DbProp.getPropInt(N.offset);
        while (started.get()) {
            GetUpdatesResponse updatesResponse;
            try {
                updatesResponse = bot.getUpdates(offset, config.getInt("UpdatePeriod"));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }

            try {

                if (updatesResponse == null || !updatesResponse.isOk()) {
                    System.out.println("Failed");
                    continue;
                }

                List<Update> updates = updatesResponse.updates();
                System.out.println("Number of updates: " + updates.size());

                for (Update update : updates) {
                    offset = update.updateId() + 1;
                    DbProp.setProp(N.offset, offset);
                    System.out.println(update);
                    System.out.println("--------------------------------------------------");
                    System.out.println("--------------------------------------------------");
                    System.out.println("--------------------------------------------------");
                    enqueue(update);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        updateQueue.offer(STOP_HANDLE);
    }

    @SuppressWarnings("CallToPrintStackTrace")
    private static void waitForExit() {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        System.out.println("=====================================================");
        System.out.println("=====================================================");
        System.out.println("================ Press enter to exit ================");
        System.out.println("=====================================================");
        System.out.println("=====================================================");
        try {
            String name = reader.readLine();
            System.out.println("Stopping services " + name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        started.set(false);
    }

    private static void disconnectDb() {
        KurniaKueDb.stopAll();
    }

    private static final BlockingQueue<Update> updateQueue
            = new LinkedBlockingQueue<>();

    private static final Update STOP_HANDLE = new Update();

    private static void enqueue(Update update) {
        updateQueue.offer(update);
    }

    private static void handleUpdates() throws InterruptedException {
        while (started.get()) {
            Update update = updateQueue.poll(1, TimeUnit.HOURS);
            if (update == STOP_HANDLE) {
                return;
            }

            try {
                UpdateContext.get(bot, update).handle();
//                Map<String, UpdateHandler> registeredHandler
//                        = getRegisteredHandler(update);
//                UpdateHandler handler = getHandler(update, registeredHandler);
//                if (handler == null) {
//                    System.err.println("No handler found for " + update);
//                    continue;
//                }
//                handler.handle(bot, update);
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
    }
}
