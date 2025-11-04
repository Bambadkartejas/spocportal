package com.example.spocportal.controller;

import com.example.spocportal.model.Activity;
import com.example.spocportal.model.ActivityAssignment;
import com.example.spocportal.service.ActivityService;
import com.example.spocportal.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
public class ActivityController {
	private final ActivityService service;
	
	@Autowired
	private ActivityService activityService;

	public ActivityController(ActivityService service) {
		this.service = service;
	}

	@GetMapping("/activities")
	public String activitiesPage(Model model) {
		model.addAttribute("activities", service.listAll());
		return "activities";
	}

	@GetMapping("/admin/activities/create")
	public String createPage() {
		return "activity-create";
	}

	@PostMapping("/api/activities")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody Activity payload) {
		if (payload.getImplementationDate() == null)
			payload.setImplementationDate(LocalDate.now());
		if (payload.getCrNumber() == null || payload.getCrNumber().isBlank())
			return ResponseEntity.badRequest().body("CR Number required");
		if (service.crNumberExists(payload.getCrNumber()))
			return ResponseEntity.badRequest().body("CR Number already exists");
		Activity created = service.create(payload);
		return ResponseEntity.ok(created);
	}

	@GetMapping("/activities/{id}")
	public String details(@PathVariable Long id, Model model) {
		Activity a = service.get(id);
		if (a == null)
			return "redirect:/activities";
		List<ActivityAssignment> assigns = service.getAssignments(id);
		model.addAttribute("activity", a);
		model.addAttribute("assignments", assigns);
		return "activity-details";
	}
	@PutMapping("/api/activities/{id}/status")
	@ResponseBody
	public ResponseEntity<?> updateActivityStatus(@PathVariable Long id, @RequestBody Map<String,String> body) {
	    try {
	        String status = body.get("status");
	        ActivityStatus newStatus = ActivityStatus.valueOf(status);
	        Activity updated = service.updateActivityStatus(id, newStatus);
	        if(updated == null) return ResponseEntity.notFound().build();
	        return ResponseEntity.ok(updated);
	    } catch(Exception ex) {
	        ex.printStackTrace();
	        return ResponseEntity.status(500).body("Server error: " + ex.getMessage());
	    }
	}
	
	@PutMapping("/activities/{id}/status")
	@ResponseBody
	public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam String status) {
	    ActivityStatus st = ActivityStatus.valueOf(status.toUpperCase());
	    Activity a = activityService.updateActivityStatus(id, st);
	    if(a == null) return ResponseEntity.notFound().build();
	    return ResponseEntity.ok("Updated");
	}



	@GetMapping("/api/activities/{id}/assignments")
	@ResponseBody
	public List<ActivityAssignment> assignments(@PathVariable Long id) {
		return service.getAssignments(id);
	}

	@DeleteMapping("/api/activities/{id}")
	@ResponseBody
	public ResponseEntity<?> deleteActivity(@PathVariable Long id) {
		service.deleteActivity(id);
		return ResponseEntity.noContent().build();
	}

	// update assignment via ActivityService
//	@PutMapping("/api/assignments/{id}")
//	@ResponseBody
//	public ResponseEntity<?> updateAssignment(@PathVariable Long id, @RequestBody ActivityAssignment payload) {
//		ActivityAssignment updated = service.updateAssignment(id, payload);
//		if (updated == null)
//			return ResponseEntity.notFound().build();
//		return ResponseEntity.ok(updated);
//	}

	// update assignment via ActivityService
//	@PutMapping("/api/assignments/{id}")
//	@ResponseBody
//	public ResponseEntity<?> updateAssignment(@PathVariable Long id, @RequestBody ActivityAssignment payload) {
//	    try {
//	        ActivityAssignment updated = service.updateAssignment(id, payload);
//	        if (updated == null) return ResponseEntity.notFound().build();
//	        return ResponseEntity.ok(updated);
//	    } catch (Exception ex) {
//	        // log stacktrace server-side, but return message so UI gets helpful info
//	        ex.printStackTrace();
//	        return ResponseEntity.status(500).body("Server error: " + ex.getMessage());
//	    }
//	}

	@PutMapping("/api/assignments/{id}")
	@ResponseBody
	public ActivityAssignment updateAssignment(@PathVariable Long id, @RequestBody ActivityAssignment payload) {
		return service.updateAssignment(id, payload);
	}
	
}
