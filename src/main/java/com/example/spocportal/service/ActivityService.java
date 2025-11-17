package com.example.spocportal.service;

import com.example.spocportal.model.*;
import com.example.spocportal.repository.ActivityAssignmentRepository;
import com.example.spocportal.repository.ActivityRepository;
import com.example.spocportal.repository.SpocRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ActivityService {

	private final ActivityRepository activityRepository;
	private final SpocRepository spocRepository;
	private final ActivityAssignmentRepository assignmentRepository;
	private final EmailService emailService;

	public ActivityService(ActivityRepository activityRepository, SpocRepository spocRepository,
			ActivityAssignmentRepository assignmentRepository, EmailService emailService) {
		this.activityRepository = activityRepository;
		this.spocRepository = spocRepository;
		this.assignmentRepository = assignmentRepository;
		this.emailService = emailService;
	}

	public List<Activity> listAll() {
		return activityRepository.findAll();
	}

	public Activity get(Long id) {
		return activityRepository.findById(id).orElse(null);
	}

	@Transactional
	public Activity create(Activity activity, String[] groupEmails) {
		Activity saved = activityRepository.save(activity);

		// create assignment for each SPOC in master
		List<SpocDetails> spocs = spocRepository.findAll();
		for (SpocDetails s : spocs) {
			ActivityAssignment a = new ActivityAssignment();
			a.setActivity(saved);
			a.setSpoc(s);
			a.setStatus(AssignmentStatus.PENDING);
			a.setComments(null);
			a.setUpdatedAt(LocalDateTime.now());
			a.setReminderCount(0);
			assignmentRepository.save(a);
			saved.getAssignments().add(a);
		}

		// send initial notification to provided groupEmails
		if (groupEmails != null && groupEmails.length > 0) {
			emailService.sendNotificationForNewActivity(saved, groupEmails);
		}
		return saved;
	}

	public List<ActivityAssignment> getAssignments(Long activityId) {
		return assignmentRepository.findByActivityId(activityId);
	}

	public ActivityAssignment updateAssignment(Long id, ActivityAssignment update) {
		return assignmentRepository.findById(id).map(existing -> {
			existing.setStatus(update.getStatus());
			existing.setComments(update.getComments());
			existing.setUpdatedAt(LocalDateTime.now());
			return assignmentRepository.save(existing);
		}).orElse(null);
	}

	public List<ActivityAssignment> getPendingAssignments() {
		return assignmentRepository.findByStatus(AssignmentStatus.PENDING);
	}

	public Activity updateActivityStatus(Long id, ActivityStatus status) {
		Activity a = activityRepository.findById(id).orElse(null);
		if (a == null)
			return null;
		a.setActivityStatus(status);
		return activityRepository.save(a);
	}

	public void deleteActivity(Long id) {
		activityRepository.deleteById(id);
	}

	// helper for scheduler - increments reminders and returns days left or elapsed
	public long daysUntilImplementation(ActivityAssignment assignment) {
		LocalDate impl = assignment.getActivity().getImplementationDate();
		if (impl == null)
			return Long.MAX_VALUE;
		return ChronoUnit.DAYS.between(LocalDate.now(), impl); // negative if past
	}

	public void markReminderSent(ActivityAssignment assignment) {
		assignment.setReminderCount((assignment.getReminderCount() == null ? 0 : assignment.getReminderCount()) + 1);
		assignment.setLastReminderAt(LocalDateTime.now());
		assignmentRepository.save(assignment);
	}

	public List<Activity> getAllActivities() {
		return activityRepository.findAll();
	}

	public boolean crNumberExists(String cr) {
		return activityRepository.existsByCrNumber(cr);
	}
}
