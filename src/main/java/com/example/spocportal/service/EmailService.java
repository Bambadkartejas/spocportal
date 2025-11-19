//package com.example.spocportal.service;
//
//import com.example.spocportal.model.Activity;
//import com.example.spocportal.model.ActivityAssignment;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//import java.time.format.DateTimeFormatter;
//
//@Service
//public class EmailService {
//
//    private final JavaMailSender mailSender;
//
//    @Value("${spring.mail.from}")
//    private String from;
//
//    public EmailService(JavaMailSender mailSender) {
//        this.mailSender = mailSender;
//    }
//
//    public void sendNotificationForNewActivity(Activity activity, String[] recipients) {
//        String subject = "New CR Assigned: " + activity.getCrNumber();
//        String body = buildActivityBody(activity);
//        sendSimpleMail(recipients, subject, body);
//    }
//
//    public void sendReminder(ActivityAssignment assignment, long daysLeft) {
//        String email = assignment.getSpoc().getPrimaryEmail() != null ?
//                assignment.getSpoc().getPrimaryEmail() :
//                assignment.getSpoc().getSecondaryEmail();
//
//        if (email == null) return; // skip if no email
//
//        String subject = "Reminder: Pending SPOC response – " + assignment.getActivity().getCrNumber();
//
//        String body = "CR: " + assignment.getActivity().getCrNumber() + "\n"
//                + "Name: " + assignment.getActivity().getName() + "\n"
//                + "Implementation Date: " + assignment.getActivity().getImplementationDate() + "\n"
//                + "Pending Days: " + daysLeft + "\n\n"
//                + "Please update the status in SPOC Portal.";
//
//        sendSimpleMail(new String[]{email}, subject, body);
//    }
//
//    private void sendSimpleMail(String[] to, String subject, String body) {
//        try {
//            SimpleMailMessage msg = new SimpleMailMessage();
//            msg.setFrom(from);
//            msg.setTo(to);
//            msg.setSubject(subject);
//            msg.setText(body);
//
//            mailSender.send(msg);
//            System.out.println("MAIL SENT TO : " + String.join(",", to));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    private String buildActivityBody(Activity activity) {
//        return "A new change request has been created:\n\n"
//                + "CR: " + activity.getCrNumber() + "\n"
//                + "Name: " + activity.getName() + "\n"
//                + "Implementation Date: " + activity.getImplementationDate() + "\n"
//                + "Created By: " + activity.getCreatedBy() + "\n\n"
//                + "Please log in to SPOC portal and update your assignment.";
//    }
//}
