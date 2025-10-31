package com.example.spocportal.repository;

import com.example.spocportal.model.ActivityAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ActivityAssignmentRepository extends JpaRepository<ActivityAssignment, Long> {
    List<ActivityAssignment> findByActivityId(Long activityId);
}