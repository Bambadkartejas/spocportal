package com.example.spocportal.controller;

import com.example.spocportal.model.Activity;
import com.example.spocportal.model.ActivityAssignment;
import com.example.spocportal.service.ActivityService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ActivityController {
    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) { this.activityService = activityService; }

    // UI pages
    @GetMapping("/activities")
    public String activitiesPage(Model model) {
        model.addAttribute("activities", activityService.listAll());
        return "activities";
    }

    @GetMapping("/activities/{id}")
    public String activityDetails(@PathVariable Long id, Model model) {
        model.addAttribute("activity", activityService.get(id));
        model.addAttribute("assignments", activityService.getAssignments(id));
        return "activity-details";
    }

    // REST
    @GetMapping("/api/activities")
    @ResponseBody
    public List<Activity> list() { return activityService.listAll(); }

    @PostMapping("/api/activities")
    @ResponseBody
    public ResponseEntity<Activity> create(@RequestBody Activity payload) {
        if (payload.getActivityDate() == null) payload.setActivityDate(LocalDate.now());
        Activity created = activityService.create(payload);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/api/activities/{id}/assignments")
    @ResponseBody
    public List<ActivityAssignment> assignments(@PathVariable Long id) { return activityService.getAssignments(id); }

    @PutMapping("/api/assignments/{id}")
    @ResponseBody
    public ResponseEntity<ActivityAssignment> updateAssignment(@PathVariable Long id, @RequestBody ActivityAssignment payload) {
        ActivityAssignment updated = activityService.updateAssignment(id, payload);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }
}
