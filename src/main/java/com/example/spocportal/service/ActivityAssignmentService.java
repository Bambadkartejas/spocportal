package com.example.spocportal.service;

import com.example.spocportal.model.ActivityAssignment;
import com.example.spocportal.repository.ActivityAssignmentRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ActivityAssignmentService {

	private final ActivityAssignmentRepository assignmentRepo;

	public ActivityAssignmentService(ActivityAssignmentRepository assignmentRepo) {
		this.assignmentRepo = assignmentRepo;
	}

	public ActivityAssignment getById(Long id) {
		return assignmentRepo.findById(id).orElse(null);
	}

	public List<ActivityAssignment> getByActivityId(Long activityId) {
		return assignmentRepo.findByActivityId(activityId);
	}

	// ✅ PASTE THIS METHOD HERE
	public ActivityAssignment updateAssignment(Long id, ActivityAssignment p) {
		ActivityAssignment a = assignmentRepo.findById(id).orElse(null);
		if (a == null)
			return null;

		a.setOverridePrimaryName(p.getOverridePrimaryName());
		a.setOverridePrimaryEmail(p.getOverridePrimaryEmail());
		a.setOverridePrimaryContact(p.getOverridePrimaryContact());

		a.setOverrideSecondaryName(p.getOverrideSecondaryName());
		a.setOverrideSecondaryEmail(p.getOverrideSecondaryEmail());
		a.setOverrideSecondaryContact(p.getOverrideSecondaryContact());

		a.setComments(p.getComments());
		a.setStatus(p.getStatus());

		return assignmentRepo.save(a);
	}
}
