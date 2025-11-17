package com.example.spocportal.scheduler;

import com.example.spocportal.model.ActivityAssignment;
import com.example.spocportal.service.ActivityService;
import com.example.spocportal.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
public class ReminderScheduler {

	private final ActivityService activityService;
	private final EmailService emailService;

	@Value("${app.scheduler.zone:Asia/Kolkata}")
	private String zone;

	public ReminderScheduler(ActivityService activityService, EmailService emailService) {
		this.activityService = activityService;
		this.emailService = emailService;
	}

	// runs every day at 10:30 IST
	@Scheduled(cron = "0 30 10 * * *", zone = "${app.scheduler.zone}")
	public void dailyReminders() {
		List<ActivityAssignment> pending = activityService.getPendingAssignments();
		LocalDateTime now = LocalDateTime.now(ZoneId.of(zone));
		for (ActivityAssignment a : pending) {
			// skip if assignment is NOT_REQUIRED or already RECEIVED/COMPLETED ->
			// repository returns only PENDING
			// only send reminders once per day (if lastReminderAt is null or more than 24h)
			if (a.getLastReminderAt() != null) {
				if (a.getLastReminderAt().isAfter(now.minusHours(23))) {
					// already sent today
					continue;
				}
			}

			long daysUntil = activityService.daysUntilImplementation(a); // days left
			// send reminder only if assignment still pending and daysUntil >= -30 (avoid
			// very old ones)
			if (daysUntil >= -30) {
				// send email to primary email if present; fallback to secondary
				emailService.sendReminder(a, daysUntil);
				activityService.markReminderSent(a);
			}
		}
	}
}
