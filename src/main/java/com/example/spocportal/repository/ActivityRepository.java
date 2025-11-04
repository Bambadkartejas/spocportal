package com.example.spocportal.repository;

import com.example.spocportal.model.Activity;
import com.example.spocportal.model.ActivityStatus;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
	boolean existsByCrNumber(String crNumber);
	 List<Activity> findByActivityStatus(ActivityStatus status);
}

