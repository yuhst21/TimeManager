//package com.bul.FMSTimeManager.common.email;
//
//import com.bul.FMSTimeManager.models.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class SendEmail {
//    @Autowired
//    private JavaMailSender mailSender;
//
//    public void sendEmailToUser(String to, User u) {
//        // use mailSender here...
//        String from = "vuquanghuy522002@gmail.com";
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom(from);
//        message.setTo(to);
//        message.setSubject("Request from "+u.getFull_name() );
//        message.setText(u.getFull_name()+ " have sent request to you.");
//        mailSender.send(message);
//    }
//    public void sendEmailToUserOnHoliday(String to, User u) {
//        // use mailSender here...
//        String from = "vuquanghuy522002@gmail.com";
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom(from);
//        message.setTo(to);
//        message.setSubject("Request from "+u.getFull_name() );
//        message.setText(u.getFull_name()+ " have sent request to you.");
//        mailSender.send(message);
//    }
//
//
//
//}
