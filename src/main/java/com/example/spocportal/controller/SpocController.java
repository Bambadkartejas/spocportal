package com.example.spocportal.controller;

import com.example.spocportal.model.SpocDetails;
import com.example.spocportal.service.SpocService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class SpocController {
	private final SpocService service;

	public SpocController(SpocService service) {
		this.service = service;
	}

	@GetMapping("/spocs")
	public String spocMaster(Model model) {
		List<SpocDetails> list = service.findAll();
		model.addAttribute("spocs", list);
		return "spocs";
	}

	@GetMapping("/api/spocs")
	@ResponseBody
	public List<SpocDetails> apiGetAll() {
		return service.findAll();
	}

	@PutMapping("/api/spocs/{id}")
	@ResponseBody
	public ResponseEntity<SpocDetails> update(@PathVariable Long id, @RequestBody SpocDetails payload) {
		return service.findById(id).map(existing -> {
			existing.setPrimaryName(payload.getPrimaryName());
			existing.setPrimaryEmail(payload.getPrimaryEmail());
			existing.setPrimaryContact(payload.getPrimaryContact());
			existing.setSecondaryName(payload.getSecondaryName());
			existing.setSecondaryEmail(payload.getSecondaryEmail());
			existing.setSecondaryContact(payload.getSecondaryContact());
			existing.setLastUpdatedBy(payload.getLastUpdatedBy());
			SpocDetails saved = service.save(existing);
			return ResponseEntity.ok(saved);
		}).orElse(ResponseEntity.notFound().build());
	}
}
