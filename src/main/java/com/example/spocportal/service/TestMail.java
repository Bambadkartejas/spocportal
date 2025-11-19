//package com.example.spocportal.service;
//
//import jakarta.mail.*;
//import jakarta.mail.internet.*;
//import java.util.Properties;
//
//public class TestMail {
//
//    public static void main(String[] args) throws Exception {
//
//        Properties props = new Properties();
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.port", "587");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//
//        Session session = Session.getInstance(props, new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(
//                        "tejasbmabadkar@gmail.com",
//                        "clgqsudsakszhiub"
//                );
//            }
//        });
//
//        Message msg = new MimeMessage(session);
//        msg.setFrom(new InternetAddress("tejasbmabadkar@gmail.com"));
//        msg.setRecipients(Message.RecipientType.TO,
//                InternetAddress.parse("tejasbmabadkar07@gmail.com"));
//        msg.setSubject("Test Mail");
//        msg.setText("This is a test message");
//
//        Transport.send(msg);
//
//        System.out.println("Mail sent successfully!");
//    }
//}
//
