package com.example.spocportal.service;

import com.example.spocportal.model.Activity;
import com.example.spocportal.model.ActivityAssignment;
import com.example.spocportal.model.SpocDetails;
import com.example.spocportal.repository.ActivityAssignmentRepository;
import com.example.spocportal.repository.ActivityRepository;
import com.example.spocportal.repository.SpocRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final SpocRepository spocRepository;
    private final ActivityAssignmentRepository assignmentRepository;

    public ActivityService(ActivityRepository activityRepository, SpocRepository spocRepository, ActivityAssignmentRepository assignmentRepository) {
        this.activityRepository = activityRepository;
        this.spocRepository = spocRepository;
        this.assignmentRepository = assignmentRepository;
    }

    public List<Activity> listAll() { return activityRepository.findAll(); }

    public Activity get(Long id) { return activityRepository.findById(id).orElse(null); }

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

    public List<ActivityAssignment> getAssignments(Long activityId) { return assignmentRepository.findByActivityId(activityId); }

    public ActivityAssignment updateAssignment(Long id, ActivityAssignment update) {
        return assignmentRepository.findById(id).map(existing -> {
            existing.setStatus(update.getStatus());
            existing.setComments(update.getComments());
            existing.setUpdatedAt(java.time.LocalDateTime.now());
            return assignmentRepository.save(existing);
        }).orElse(null);
    }
}
