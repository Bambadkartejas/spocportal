package com.example.spocportal.repository;

import com.example.spocportal.model.Activity;
<<<<<<< Updated upstream
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
=======
import com.example.spocportal.model.ActivityStatus;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    boolean existsByCrNumber(String crNumber);

    long countByActivityStatus(ActivityStatus status);

    Page<Activity> findByActivityStatus(ActivityStatus status, Pageable pageable);
>>>>>>> Stashed changes
}

