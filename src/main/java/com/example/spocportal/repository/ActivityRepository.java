package com.example.spocportal.repository;

import com.example.spocportal.model.Activity;
import com.example.spocportal.model.ActivityStatus;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    boolean existsByCrNumber(String crNumber);

    long countByActivityStatus(ActivityStatus status);

    Page<Activity> findByActivityStatus(ActivityStatus status, Pageable pageable);
    

    // find activities which are not COMPLETED (useful to limit reminders)
    List<Activity> findByActivityStatusNot(ActivityStatus status);
    
    List<Activity> findByImplementationDateBetween(LocalDate from, LocalDate to);
}

