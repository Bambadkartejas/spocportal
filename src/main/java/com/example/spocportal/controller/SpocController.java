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

    // serve UI
    @GetMapping({"/", "/spocs"})
    public String index(Model model) {
        List<SpocDetails> list = service.findAll();
        model.addAttribute("spocs", list);
        return "index"; // thymeleaf template
    }

    // REST endpoints
    @GetMapping("/api/spocs")
    @ResponseBody
    public List<SpocDetails> getAll() {
        return service.findAll();
    }

    @GetMapping("/api/spocs/{team}")
    @ResponseBody
    public ResponseEntity<SpocDetails> getByTeam(@PathVariable String team) {
        return service.findByTeam(team)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api/spocs")
    @ResponseBody
    public ResponseEntity<SpocDetails> create(@RequestBody SpocDetails spoc) {
        SpocDetails saved = service.save(spoc);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/api/spocs/{id}")
    @ResponseBody
    public ResponseEntity<SpocDetails> update(@PathVariable Long id, @RequestBody SpocDetails spoc) {
        return service.findById(id).map(existing -> {
            existing.setTeamName(spoc.getTeamName());
            existing.setSpocName(spoc.getSpocName());
            existing.setEmail(spoc.getEmail());
            existing.setContactNumber(spoc.getContactNumber());
            existing.setBackupName(spoc.getBackupName());
            existing.setLastUpdatedBy(spoc.getLastUpdatedBy());
            SpocDetails updated = service.save(existing);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/api/spocs/{id}")
    @ResponseBody
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
