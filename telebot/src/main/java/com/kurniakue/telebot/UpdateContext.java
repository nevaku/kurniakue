/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.telebot;

import static com.kurniakue.common.Common.*;
import com.kurniakue.common.Tool;
import com.kurniakue.data.Customer;
import com.kurniakue.data.Record;
import com.kurniakue.telebot.UpdateHandler.C;
import com.pengrad.telegrambot.Replier;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author harun1
 */
public class UpdateContext {

    public static final UpdateContext global = new UpdateContext(-1);
    private static final Map<Long, UpdateContext> contextList = new HashMap<>();
    public Record<Record> data = new Record();

    private final long id;

    public long getId() {
        return id;
    }

    private TelegramBot bot;

    public TelegramBot getBot() {
        return bot;
    }

    private Update update;

    public Update getUpdate() {
        return update;
    }

    public Replier replier;

    public Replier getReplier() {
        return replier;
    }

    private Message message;

    public Message getMessage() {
        return message;
    }

    private Chat chat;

    public Chat getChat() {
        return chat;
    }

    private String userName;

    public String getUserName() {
        return userName;
    }
    
    public String memberName;

    public String getMemberName() {
        return memberName;
    }
    
    private List<String> userGroups;

    public List<String> getUserGroups() {
        return userGroups;
    }
    
    private String userMemberId;

    public String getUserMemberId() {
        return userMemberId;
    }
    
    private long userAccountNo;

    public long getUserAccountNo() {
        return userAccountNo;
    }
    
    private String supplierMemberId;

    public String getSupplierMemberId() {
        return supplierMemberId;
    }
    
    private long supplierAccountNo;

    public long getSupplierAccountNo() {
        return supplierAccountNo;
    }
    
    private String supplierName;

    public String getSupplierName() {
        return supplierName;
    }

    private String text;

    public String getText() {
        return text;
    }

    private Map<String, UpdateHandler> registeredHandler;

    public Map<String, UpdateHandler> getRegisteredHandler() {
        return registeredHandler;
    }

    private UpdateHandler activeHandler;

    public UpdateHandler getActiveHandler() {
        return activeHandler;
    }

    public UpdateContext(long id) {
        this.id = id;
    }

    public UpdateContext setInitialInfo(TelegramBot bot, Update update) {
        setInfo(bot, update);
        registeredHandler = UpdateHandler.createRegisteredHandler(update, this);
        activeHandler = registeredHandler.get(C.Help.cmd);
        System.out.println("Active Handler: " + activeHandler.getClass().getSimpleName());

        return this;
    }

    public UpdateContext setInfo(TelegramBot bot, Update update) {
        this.bot = bot;
        this.update = update;
        replier = bot.replyTo(update);
        message = update.message();
        chat = message.chat();
        userName = chat.username();
        userGroups = Arrays.asList(GroupMember.get(userName).split(","));
        if (userGroups == null) {
            userGroups = GROUP_CUSTOMER;
        }
        userMemberId = MemberIdOf.get(userName);
        userAccountNo = Tool.idToNo(userMemberId);
        
        // TODO: should load from member instead of customer
        // should transfer data from customer to member first
        Customer member = new Customer().load(userName);
        memberName = member.getString(Customer.F.CustomerName);
        
        supplierMemberId = DINA_MEMBER_ID;
        supplierAccountNo = Tool.idToNo(supplierMemberId);
        Customer supplierMember = new Customer().load(userName);
        supplierName = supplierMember.getString(Customer.F.CustomerName);
        
        text = message.text();

        return this;
    }
    
    public boolean isMemberOf(String groupMember) {
        return userGroups.contains(groupMember);
    }

    public static UpdateContext get(TelegramBot bot, Update update) {
        if (!UpdateHandler.validateMandatory(update)) {
            return global;
        }

        long id = update.message().chat().id();
        UpdateContext context = contextList.get(id);
        if (context == null) {
            context = new UpdateContext(id).setInitialInfo(bot, update);
            contextList.put(id, context);
            context.start();
        } else {
            context.setInfo(bot, update);
        }

        return context;
    }

    public void handle() {
        if (registeredHandler == null) {
            return;
        }

        if (activeHandler == null) {
            return;
        }

        activeHandler.handle(this);
    }

    public void start() {

    }

    public boolean activate(String id) {
        UpdateHandler handler = registeredHandler.get(id);
        if (handler == null) {
            return false;
        }

        activeHandler = handler;
        System.out.println("Active Handler: " + activeHandler.getClass().getSimpleName());
        handler.handle(this);
        return true;
    }

    public <T extends UpdateHandler> T getHandler(Class<T> handlerClass) {
        UpdateHandler handler = registeredHandler.get(handlerClass.getSimpleName());
        if (handler == null) {
            handler = Tool.neo(handlerClass);
            registeredHandler.put(handlerClass.getSimpleName(), handler);
            handler.setContext(this);
            handler.setReplier(getReplier());
        }

        return (T) handler;
    }

    public boolean transferTo(String id) {
        UpdateHandler handler = registeredHandler.get(id);

        return transferTo(handler);
    }

    public boolean transferTo(Class<? extends UpdateHandler> handlerClass) {
        UpdateHandler handler = getHandler(handlerClass);
        return transferTo(handler);
    }

    public boolean transferTo(UpdateHandler handler) {
        if (handler == null) {
            return false;
        }

        handler.setContext(activeHandler.getContext());
        handler.setReplier(activeHandler.getReplier());
        handler.setParams(activeHandler.getParams());
        handler.setFun(activeHandler.getFun());
        activeHandler = handler;
        System.out.println("Active Handler: " + activeHandler.getClass().getSimpleName());
        return handler.handleCommand();
    }

    public boolean transferTo(UpdateHandler handler, Command command) {
        if (handler == null) {
            return false;
        }

        handler.setContext(activeHandler.getContext());
        handler.setReplier(activeHandler.getReplier());
        handler.setParams(activeHandler.getParams());
        handler.setCmd(command.getCmd());
        handler.setFun(command.getFun());
        activeHandler = handler;
        System.out.println("Active Handler: " + activeHandler.getClass().getSimpleName());
        return command.exec();
    }
}
