package com.example.spocportal.service;

import com.example.spocportal.model.Activity;
import com.example.spocportal.model.ActivityAssignment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String from;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendNotificationForNewActivity(Activity activity, String[] recipients) {
        String subject = "New CR Assigned: " + activity.getCrNumber();
        String body = buildActivityBody(activity);
        sendSimpleMail(recipients, subject, body);
    }

    public void sendReminder(ActivityAssignment assignment, long daysLeft) {
        String[] to = new String[]{assignment.getSpoc().getPrimaryEmail()};
        String subject = "Reminder: Pending SPOC response for " + assignment.getActivity().getCrNumber();
        String body = "CR: " + assignment.getActivity().getCrNumber() + "\n" +
                "Name: " + assignment.getActivity().getName() + "\n" +
                "Implementation Date: " + (assignment.getActivity().getImplementationDate() != null ? assignment.getActivity().getImplementationDate().toString() : "")
                + "\n" +
                "Pending days: " + daysLeft + "\n\n" +
                "Please update the status in SPOC portal.";
        sendSimpleMail(to, subject, body);
    }

    private void sendSimpleMail(String[] to, String subject, String body) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(from);
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(body);
            mailSender.send(msg);
        } catch (Exception ex) {
            // log error
            ex.printStackTrace();
        }
    }

    private String buildActivityBody(Activity activity) {
        String impl = activity.getImplementationDate() != null ? activity.getImplementationDate().toString() : "N/A";
        return "A new change request has been created:\n\n" +
                "CR: " + activity.getCrNumber() + "\n" +
                "Name: " + activity.getName() + "\n" +
                "Implementation Date: " + impl + "\n" +
                "Created By: " + activity.getCreatedBy() + "\n\n" +
                "Please log in to SPOC portal and update your assignment status.";
    }
}
