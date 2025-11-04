package com.example.spocportal.controller;

import com.example.spocportal.model.Activity;
import com.example.spocportal.service.ActivityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {
	private final ActivityService activityService;

	public DashboardController(ActivityService activityService) {
		this.activityService = activityService;
	}

	@GetMapping({ "/", "/dashboard" })
	public String dashboard(Model model) {
		List<Activity> activities = activityService.listAll();
		model.addAttribute("activities", activities);
		// counts and pending will be computed in template using assignments or in
		// controller as needed.
		return "dashboard";
	}
}
