package com.example.spocportal.service;

import com.example.spocportal.model.SpocDetails;
import com.example.spocportal.repository.SpocRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SpocService {
	private final SpocRepository repo;

	public SpocService(SpocRepository repo) {
		this.repo = repo;
	}

	public List<SpocDetails> findAll() {
		return repo.findAll();
	}

	public Optional<SpocDetails> findById(Long id) {
		return repo.findById(id);
	}

	public SpocDetails save(SpocDetails s) {
		s.setLastUpdatedTime(LocalDateTime.now());
		return repo.save(s);
	}
}
