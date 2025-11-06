package com.example.spocportal.controller;

import com.example.spocportal.model.Activity;
import com.example.spocportal.model.ActivityAssignment;
import com.example.spocportal.model.ActivityStatus;
import com.example.spocportal.service.ActivityService;
<<<<<<< Updated upstream
=======

import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.usermodel.Row;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
>>>>>>> Stashed changes
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
public class ActivityController {
<<<<<<< Updated upstream
    private final ActivityService activityService;
=======

	private final ActivityService service;

	@Autowired
	private ActivityService activityService;
>>>>>>> Stashed changes

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

<<<<<<< Updated upstream
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
=======
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
	public ResponseEntity<?> updateActivityStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
		try {
			String status = body.get("status");
			ActivityStatus newStatus = ActivityStatus.valueOf(status);
			Activity updated = service.updateActivityStatus(id, newStatus);
			if (updated == null)
				return ResponseEntity.notFound().build();
			return ResponseEntity.ok(updated);
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(500).body("Server error: " + ex.getMessage());
		}
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

	@PutMapping("/api/assignments/{id}")
	@ResponseBody
	public ActivityAssignment updateAssignment(@PathVariable Long id, @RequestBody ActivityAssignment payload) {
		return service.updateAssignment(id, payload);
	}

	// ✅ Export to Excel
	@GetMapping("/activities/export")
	public void exportToExcel(HttpServletResponse response) throws IOException {

		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=activities.xlsx");

		List<Activity> list = activityService.getAllActivities();
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Activities");

		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue("ID");
		header.createCell(1).setCellValue("CR Number");
		header.createCell(2).setCellValue("Name");
		header.createCell(3).setCellValue("Status");
		header.createCell(4).setCellValue("Implementation Date");
		header.createCell(5).setCellValue("Created At");
		header.createCell(6).setCellValue("Created By");

		int rowCount = 1;
		for (Activity a : list) {
			Row row = sheet.createRow(rowCount++);

			row.createCell(0).setCellValue(a.getId());
			row.createCell(1).setCellValue(a.getCrNumber());
			row.createCell(2).setCellValue(a.getName());
			row.createCell(3).setCellValue(a.getActivityStatus().name());
			row.createCell(4)
					.setCellValue(a.getImplementationDate() != null ? a.getImplementationDate().toString() : "");
			row.createCell(5).setCellValue(a.getCreatedAt() != null ? a.getCreatedAt().toString() : "");
			row.createCell(6).setCellValue(a.getCreatedBy());
		}

		for (int i = 0; i < 7; i++) {
			sheet.autoSizeColumn(i);
		}

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}

>>>>>>> Stashed changes
}
