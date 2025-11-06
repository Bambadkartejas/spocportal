package com.example.spocportal.service;

<<<<<<< Updated upstream
import com.example.spocportal.model.Activity;
import com.example.spocportal.model.ActivityAssignment;
import com.example.spocportal.model.SpocDetails;
import com.example.spocportal.repository.ActivityAssignmentRepository;
import com.example.spocportal.repository.ActivityRepository;
import com.example.spocportal.repository.SpocRepository;
=======
import com.example.spocportal.model.*;
import com.example.spocportal.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
>>>>>>> Stashed changes
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ActivityService {
<<<<<<< Updated upstream
    private final ActivityRepository activityRepository;
    private final SpocRepository spocRepository;
    private final ActivityAssignmentRepository assignmentRepository;
=======

	private final ActivityRepository activityRepo;
	private final SpocRepository spocRepo;
	private final ActivityAssignmentRepository assignRepo;
>>>>>>> Stashed changes

    public ActivityService(ActivityRepository activityRepository, SpocRepository spocRepository, ActivityAssignmentRepository assignmentRepository) {
        this.activityRepository = activityRepository;
        this.spocRepository = spocRepository;
        this.assignmentRepository = assignmentRepository;
    }

    public List<Activity> listAll() { return activityRepository.findAll(); }

<<<<<<< Updated upstream
    public Activity get(Long id) { return activityRepository.findById(id).orElse(null); }
=======
	public List<Activity> getAllActivities() {
		return activityRepository.findAll();
	}

	public List<Activity> listAll() {
		return activityRepo.findAll();
	}
>>>>>>> Stashed changes

    @Transactional
    public Activity create(Activity activity) {
        // save activity first
        Activity saved = activityRepository.save(activity);

        // auto-populate assignments for every SPOC in master
        List<SpocDetails> spocs = spocRepository.findAll();
        for (SpocDetails s : spocs) {
            ActivityAssignment a = new ActivityAssignment();
            a.setActivity(saved);
            a.setSpoc(s);
            a.setStatus("Pending");
            a.setComments("");
            assignmentRepository.save(a);
            saved.getAssignments().add(a);
        }
        return saved;
    }

<<<<<<< Updated upstream
    public List<ActivityAssignment> getAssignments(Long activityId) { return assignmentRepository.findByActivityId(activityId); }

    public ActivityAssignment updateAssignment(Long id, ActivityAssignment update) {
        return assignmentRepository.findById(id).map(existing -> {
            existing.setStatus(update.getStatus());
            existing.setComments(update.getComments());
            existing.setUpdatedAt(java.time.LocalDateTime.now());
            return assignmentRepository.save(existing);
        }).orElse(null);
    }
=======
	@Transactional
	public Activity create(Activity a) {
		a.evaluateCrossMidnight(); // ✅ Perform logic here
		Activity saved = activityRepo.save(a);

		List<SpocDetails> spocs = spocRepo.findAll();
		for (SpocDetails s : spocs) {
			ActivityAssignment asg = new ActivityAssignment();
			asg.setActivity(saved);
			asg.setSpoc(s);
			asg.setStatus(AssignmentStatus.PENDING);
			assignRepo.save(asg);

			saved.getAssignments().add(asg);
		}
		return saved;
	}

	public Activity updateActivityStatus(Long id, ActivityStatus status) {
		return activityRepository.findById(id).map(a -> {
			a.setActivityStatus(status);
			return activityRepository.save(a);
		}).orElse(null);
	}

	public void refreshStatus(Activity activity) {
		if (activity.getImplementationDate() != null && (activity.getImplementationDate().isBefore(LocalDate.now())
				|| activity.getImplementationDate().isEqual(LocalDate.now()))) {

			if (activity.getActivityStatus() == ActivityStatus.PENDING) {
				activity.setActivityStatus(ActivityStatus.STARTED);
			}
		}
	}

	public List<ActivityAssignment> getAssignments(Long activityId) {
		return assignRepo.findByActivityId(activityId);
	}

	@Transactional
	public ActivityAssignment updateAssignment(Long id, ActivityAssignment payload) {
		return assignRepo.findById(id).map(existing -> {
			existing.setOverridePrimaryName(payload.getOverridePrimaryName());
			existing.setOverridePrimaryEmail(payload.getOverridePrimaryEmail());
			existing.setOverridePrimaryContact(payload.getOverridePrimaryContact());
			existing.setOverrideSecondaryName(payload.getOverrideSecondaryName());
			existing.setOverrideSecondaryEmail(payload.getOverrideSecondaryEmail());
			existing.setOverrideSecondaryContact(payload.getOverrideSecondaryContact());

			if (payload.getComments() != null)
				existing.setComments(payload.getComments());
			if (payload.getStatus() != null)
				existing.setStatus(payload.getStatus());

			existing.setUpdatedAt(java.time.LocalDateTime.now());

			ActivityAssignment saved = assignRepo.save(existing);
			checkAndAutoCompleteActivity(existing.getActivity());
			return saved;
		}).orElse(null);
	}

	private void checkAndAutoCompleteActivity(Activity activity) {
		List<ActivityAssignment> assigns = assignRepo.findByActivity(activity);

		boolean allDone = assigns.stream().allMatch(
				a -> a.getStatus() == AssignmentStatus.RECEIVED || a.getStatus() == AssignmentStatus.COMPLETED);

		if (allDone && !Boolean.TRUE.equals(activity.getManualCompleted())) {
			activity.setManualCompleted(true);
			activityRepo.save(activity);
		}
	}

	public void deleteActivity(Long id) {
		activityRepo.deleteById(id);
	}

>>>>>>> Stashed changes
}
