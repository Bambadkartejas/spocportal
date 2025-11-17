package com.example.spocportal.repository;

import com.example.spocportal.model.Activity;
import com.example.spocportal.model.ActivityAssignment;
import com.example.spocportal.model.AssignmentStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ActivityAssignmentRepository extends JpaRepository<ActivityAssignment, Long> {
	List<ActivityAssignment> findByActivityId(Long activityId);

	List<ActivityAssignment> findByActivity(Activity activity);
	  List<ActivityAssignment> findByStatus(AssignmentStatus status);
}

