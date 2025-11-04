package com.example.spocportal.service;

import com.example.spocportal.model.*;
import com.example.spocportal.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ActivityService {
	private final ActivityRepository activityRepo;
	private final SpocRepository spocRepo;
	private final ActivityAssignmentRepository assignRepo;

	@Autowired
	private ActivityRepository activityRepository;

	public ActivityService(ActivityRepository activityRepo, SpocRepository spocRepo,
			ActivityAssignmentRepository assignRepo) {
		this.activityRepo = activityRepo;
		this.spocRepo = spocRepo;
		this.assignRepo = assignRepo;
	}

	public List<Activity> listAll() {
		return activityRepo.findAll();
	}

	public Activity get(Long id) {
		return activityRepo.findById(id).orElse(null);
	}

	public boolean crNumberExists(String crNumber) {
		return activityRepo.existsByCrNumber(crNumber);
	}

	// create activity and auto-populate assignments for all spoc groups
	@Transactional
	public Activity create(Activity a) {
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
	    if(activity.getImplementationDate()!=null &&
	       activity.getImplementationDate().isBefore(LocalDate.now()) ||
	       activity.getImplementationDate().isEqual(LocalDate.now())) {
	        if(activity.getActivityStatus() == ActivityStatus.PENDING){
	            activity.setActivityStatus(ActivityStatus.STARTED);
	        }
	    }
	}



	public List<ActivityAssignment> getAssignments(Long activityId) {
		return assignRepo.findByActivityId(activityId);
	}

	// update assignment fields (editable by user and admin)
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
			// after update check auto-complete
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

	public Object getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public Activity getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public void delete(Long id) {
		// TODO Auto-generated method stub

	}

	public void save(Activity activity) {
		// TODO Auto-generated method stub

	}

}
