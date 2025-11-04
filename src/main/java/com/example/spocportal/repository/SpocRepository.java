package com.example.spocportal.repository;

import com.example.spocportal.model.SpocDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SpocRepository extends JpaRepository<SpocDetails, Long> {
	Optional<SpocDetails> findByTeamNameIgnoreCase(String teamName);
}
