package com.example.spocportal.service;

import com.example.spocportal.model.SpocDetails;
import com.example.spocportal.repository.SpocRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SpocService {

    private final SpocRepository repository;

    public SpocService(SpocRepository repository) {
        this.repository = repository;
    }

    public List<SpocDetails> findAll() {
        return repository.findAll();
    }

    public Optional<SpocDetails> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<SpocDetails> findByTeam(String teamName) {
        return repository.findByTeamNameIgnoreCase(teamName);
    }

    public SpocDetails save(SpocDetails spoc) {
        spoc.setLastUpdatedTime(LocalDateTime.now());
        return repository.save(spoc);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
