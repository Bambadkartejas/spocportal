package com.example.spocportal.controller;

import com.example.spocportal.model.Activity;
import com.example.spocportal.model.ActivityStatus;
import com.example.spocportal.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class DashboardController {

	@Autowired
	ActivityRepository activityRepo;

	@GetMapping({ "/", "/dashboard" })
	public String dashboard(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "") String filter,
			Model model) {

		Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
		Page<Activity> p;

		if (filter.isEmpty()) {
			p = activityRepo.findAll(pageable);
		} else {
			p = activityRepo.findByActivityStatus(ActivityStatus.valueOf(filter), pageable);
		}

		// ✅ Counts per status
		long pendingCount = activityRepo.countByActivityStatus(ActivityStatus.PENDING);
		long startedCount = activityRepo.countByActivityStatus(ActivityStatus.STARTED);
		long completedCount = activityRepo.countByActivityStatus(ActivityStatus.COMPLETED);
		long rejectedCount = activityRepo.countByActivityStatus(ActivityStatus.REJECTED);
		long totalCount = activityRepo.count();

		model.addAttribute("page", p);
		model.addAttribute("activities", p.getContent());
		model.addAttribute("filter", filter);

		model.addAttribute("pendingCount", pendingCount);
		model.addAttribute("startedCount", startedCount);
		model.addAttribute("completedCount", completedCount);
		model.addAttribute("rejectedCount", rejectedCount);
		model.addAttribute("totalCount", totalCount);

		return "dashboard";
	}
}
