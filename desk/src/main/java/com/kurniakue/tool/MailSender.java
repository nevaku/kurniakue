/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.tool;

import com.kurniakue.data.TheConfig;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author harun1
 */
public class MailSender {
    
    private static Properties props;

    private static final TheConfig config = new TheConfig();
    private static String userMail;
    private static String pwdMail;
    private static String fromMe;
    private static String toMe;

    public static void main(String[] args) {
//        initMailSender();
        initGmailSender();
        sendEmail(toMe, "Test from Java Application ", "Assalaamualaikum");
    }
    
    public static void initMailSender() {
        config.load("c:/harun/cfg/mail.conf");
        
        props = new Properties();
        props.put("mail.smtp.host", "zimbra.infolink.co.id");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        
        userMail = config.getProperty("mail.user");
        pwdMail = config.getProperty("mail.pwd");
        fromMe = config.getProperty("mail.from.me");
        toMe = config.getProperty("mail.to.me");
    }
    
    public static void initGmailSender() {
        config.load("c:/harun/cfg/mail.conf");
        
        props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); //TLS
        
        userMail = config.getProperty("mail.user");
        pwdMail = config.getProperty("mail.pwd");
        fromMe = config.getProperty("mail.from.me");
        toMe = config.getProperty("mail.to.me");
    }

    public static void sendEmail(String to, String subject, String content) throws RuntimeException {
        try {
            Session session = Session.getInstance(props, authenticator);

            Message message = new MimeMessage(session);
            InternetAddress[] myEmail = InternetAddress.parse(fromMe);
            message.setFrom(myEmail[0]);
            message.setRecipients(RecipientType.TO, InternetAddress.parse(to));
            message.setRecipients(RecipientType.BCC, myEmail);
            //message.setReplyTo(InternetAddress.parse("harun-1020015@infolink.co.id,kurniakue@gmail.com"));
            message.setSubject(subject);
            message.setText(content);

            System.out.println("Sending email...");
            Transport.send(message);

            System.out.println("Email Sent");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Authenticator authenticator = new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(userMail, pwdMail);
        }
    };
}
