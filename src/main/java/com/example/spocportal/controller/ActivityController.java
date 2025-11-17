package com.example.spocportal.controller;

import com.example.spocportal.model.Activity;
import com.example.spocportal.model.ActivityAssignment;
import com.example.spocportal.model.ActivityStatus;
import com.example.spocportal.service.ActivityService;

import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.usermodel.Row;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
public class ActivityController {

	private final ActivityService service;
	
	@Autowired
    private Environment env; 

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

//	@PostMapping("/api/activities")
//	@ResponseBody
//	public ResponseEntity<?> create(@RequestBody Activity payload) {
//		if (payload.getImplementationDate() == null)
//			payload.setImplementationDate(LocalDate.now());
//		if (payload.getCrNumber() == null || payload.getCrNumber().isBlank())
//			return ResponseEntity.badRequest().body("CR Number required");
//		if (service.crNumberExists(payload.getCrNumber()))
//			return ResponseEntity.badRequest().body("CR Number already exists");
//		Activity created = service.create(payload);
//		return ResponseEntity.ok(created);
//	}
	
	@PostMapping("/api/activities")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody Activity payload) {
	    if (payload.getImplementationDate() == null)
	        payload.setImplementationDate(LocalDate.now());
	    if (payload.getCrNumber() == null || payload.getCrNumber().isBlank())
	        return ResponseEntity.badRequest().body("CR Number required");
	    if (service.crNumberExists(payload.getCrNumber()))
	        return ResponseEntity.badRequest().body("CR Number already exists");

	    String groupCsv = env.getProperty("spring.mail.group.emails", "");
	    String[] groupEmails = groupCsv.isBlank() ? new String[0] : groupCsv.split("\\s*,\\s*");

	    Activity created = service.create(payload, groupEmails);
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
	
	@Autowired
	private JavaMailSender mailSender;

	@GetMapping("/test-mail")
	public String sendTestMail() {
	    SimpleMailMessage msg = new SimpleMailMessage();
	    msg.setTo("tejasbmabadkar@gmail.com");
	    msg.setSubject("Test Mail");
	    msg.setText("Hello! This is a Spring Boot local test email.");

	    mailSender.send(msg);
	    return "Mail Sent!";
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

}
