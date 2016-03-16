/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.telebot;

import com.kurniakue.common.Tool;
import static com.kurniakue.common.Tool.neo;
import com.kurniakue.telebot.admin.CustomerHandler;
import com.kurniakue.telebot.admin.AdminHelpHandler;
import com.pengrad.telegrambot.Replier;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author harun1
 */
public class UpdateHandler {

    public enum C {

        Help("/petunjuk"),
        Home("/awal"),
        Customer("/pelanggan"),
        Info("/info"),
        Saldo("/saldo"),
        Schedule("/jadwal"),
        Booking("/pemesanan");
        public String cmd;
        public String fun;

        private C(String command) {
            this.cmd = command;
            fun = command.substring(1);
        }
    }
    
    public enum CD {
        Cancel("/batal"),
        OK("/ok"),
        Yes("/ya"),
        No("/tidak"),;
        
        public String cmd;

        private CD(String cmd) {
            this.cmd = cmd;
        }
    }

    private static final Map<String, Class<? extends UpdateHandler>> registeredUserHandlerClass
            = new HashMap<String, Class<? extends UpdateHandler>>() {
                {
                    put(C.Help.cmd, HelpHandler.class);
                    put(C.Info.cmd, InfoHandler.class);
                    put(C.Saldo.cmd, SaldoHandler.class);
                    put(C.Schedule.cmd, JadwalHandler.class);
                    put(C.Booking.cmd, PesanHandler.class);
                }
            };

    private static final Map<String, Class<? extends UpdateHandler>> registeredAdminHandler
            = new HashMap<String, Class<? extends UpdateHandler>>() {
                {
                    put(C.Help.cmd, AdminHelpHandler.class);
                    put(C.Customer.cmd, CustomerHandler.class);
                    put(C.Info.cmd, InfoHandler.class);
                    put(C.Saldo.cmd, SaldoHandler.class);
                    put(C.Schedule.cmd, JadwalHandler.class);
                    put(C.Booking.cmd, PesanHandler.class);
                }
            };

    private static final Map<String, Map<String, Class<? extends UpdateHandler>>> handlerByUser
            = new HashMap<String, Map<String, Class<? extends UpdateHandler>>>() {
                {
                    put("HarunMip", UpdateHandler.registeredAdminHandler);
                }
            };

    public final Command cmd_nihil = new Command(this, "", () -> {
        return true;
    });

    public static Map<String, UpdateHandler> createRegisteredHandler(Update update) {
        if (!validateMandatory(update)) {
            return null;
        }

        Map<String, Class<? extends UpdateHandler>> registeredHandlerClass;

        Message message = update.message();
        Chat chat = message.chat();
        Chat.Type type = chat.type();
        if (type != Chat.Type.Private) {
            return createHandlerListFrom(UpdateHandler.registeredUserHandlerClass);
        }

        String username = chat.username();
        registeredHandlerClass = handlerByUser.get(username);

        if (registeredHandlerClass == null) {
            return createHandlerListFrom(UpdateHandler.registeredUserHandlerClass);
        }

        return createHandlerListFrom(registeredHandlerClass);
    }

    private static Map<String, UpdateHandler> createHandlerListFrom(Map<String, Class<? extends UpdateHandler>> registeredHandlerClass) {
        Map<String, UpdateHandler> handlerList = new HashMap<>();
        registeredHandlerClass.entrySet().stream().forEach((entry) -> {
            handlerList.put(entry.getKey(), neo(entry.getValue()));
        });
        return handlerList;
    }

    private static void clearJunk(Message message) {
        if (message.text() == null) {
            return;
        }

        StringBuilder clean = new StringBuilder(message.text().length());
        String[] keywords = message.text().split(" ");
        for (String keyword : keywords) {
            if (!"".equals(keyword)) {
                clean.append(keyword).append(" ");
            }
        }
        clean.delete(clean.length() - 1, clean.length());
        message.text(clean.toString());
    }

    private static boolean valid(String text) {
        String invalidExp = ".*[^a-zA-Z0-9\\/\\s].*";
        return !text.matches(invalidExp);
    }

    public static boolean validateMandatory(Update update) {
        if (update == null) {
            return false;
        }
        if (update.message() == null) {
            return false;
        }

        return update.message().chat() != null;
    }

    private UpdateContext context;

    public void setContext(UpdateContext context) {
        this.context = context;
    }

    public UpdateContext getContext() {
        return context;
    }

    private Replier replier;

    public void setReplier(Replier replier) {
        this.replier = replier;
    }

    public Replier getReplier() {
        return replier;
    }

    private String cmd;

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getCmd() {
        return cmd;
    }

    private String fun;

    public void setFun(String fun) {
        this.fun = fun;
    }

    public String getFun() {
        return fun;
    }

    private String[] params;

    public void setParams(String[] params) {
        this.params = params;
    }

    public String[] getParams() {
        return params;
    }
    
    protected Command[] activeCommands = null;

    public void setActiveCommands(Command[] activeCommands) {
        this.activeCommands = activeCommands;
    }

    public Command[] getActiveCommands() {
        return activeCommands;
    }

    public void handle(UpdateContext context) {
        this.context = context;
        this.replier = context.getReplier();

        String text = context.getText();
        if (Tool.isBlank(text)) {
            sorry();
            return;
        }
        
        String[] keywords = Tool.parseArguments(text);
        if (keywords[0].startsWith("/")) {
            cmd = keywords[0];
            fun = keywords[0].substring(1);

            if (keywords.length == 1) {
                params = new String[0];
            } else {
                params = Arrays.copyOfRange(keywords, 1, keywords.length - 1);
            }
        } else {
            cmd = "";
            fun = "";
            params = keywords;
        }
        
        if (handleCommand()) {
            return;
        }

        if (getClass() == UpdateHandler.class) {
            sorry();
        }
    }

    public void sorry() {
        replier.add("Maaf, perintah ini sedang dalam pengembangan \n");
        replier.send();
    }

    public boolean handleCommand() {
        if (C.Help.fun.equals(fun)) {
            return petunjuk();
        }
        if (C.Home.fun.equals(fun)) {
            return petunjuk();
        }
        if (Tool.isBlank(fun)) {
            return execute();
        }

        return executeCommand();
    }

    public boolean awal() {
        return petunjuk();
    }
    
    public boolean executeCommand() {
        if (activeCommands != null) {
            getReplier().keyboard(cmdOf(activeCommands));
            return executeActiveCommand();
        }
        
        return callFunction(fun);
    }
    
    public boolean callFunction(String fun) {
        try {
            Method execution = getClass().getMethod(fun);
            Object obj = execution.invoke(this);

            return (obj != null) && (obj == Boolean.TRUE);
        } catch (NoSuchMethodException exc) {
            System.err.println(exc.getClass().getSimpleName() + ": " + exc.getMessage());
            return execute();
        } catch (Exception exc) {
            exc.printStackTrace();
            return execute();
        }
    }
    
    public boolean executeActiveCommand() {
        for (Command command : getActiveCommands()) {
            if (command.getCmd().equals(getCmd())) {
                return command.exec();
            }
        }
        
        return execute();
    }

    public boolean execute() {
        return false;
    }
    
    public boolean transferTo(Command command) {
        return getContext().transferTo(this, command);
    }

    public static String[] cmdOf(Enum[] values) {
        if (values == null || values.length == 0) {
            return new String[0];
        }
        String[] cmds = new String[values.length];
        for (int i = 0; i < cmds.length; i++) {
            String funName = Tool.getMember("cmd").of(values[i]).getString();
            cmds[i] = funName;
        }
        
        return cmds;
    }
    
    public String[] cmdOf(Command[] cmdMenus) {
        String [] cmds = new String[cmdMenus.length];
        for (int i = 0; i < cmds.length; i++) {
            cmds[i] = cmdMenus[i].getCmd();
        }
        
        return cmds;
    }

    public boolean batal() {
        return getContext().transferTo(C.Help.cmd);
    }
    
    public boolean ok() {
        return false;
    }
    
    public boolean ya() {
        return false;
    }
    
    public boolean tidak() {
        return false;
    }
    
    public final Command cmd(Enum enumCommand){
        
        Command cmdobj = new Command(this, enumCommand, () -> {
            return callFunction(fun);
        });
        
        return cmdobj;
    }

    public boolean petunjuk() {
        setCmd(C.Help.cmd);
        setFun(C.Help.fun);
        return getContext().transferTo(C.Help.cmd);
    }
}
