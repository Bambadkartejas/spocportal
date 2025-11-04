package com.example.spocportal.config;

import com.example.spocportal.model.SpocDetails;
import com.example.spocportal.repository.SpocRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataSeeder {
	private final SpocRepository repo;

	public DataSeeder(SpocRepository repo) {
		this.repo = repo;
	}

	@PostConstruct
	public void seed() {
		if (repo.count() > 0)
			return;
		List<String> groups = List.of("Billing", "Finance", "Network", "Database", "Linux", "Windows", "Storage",
				"Wintel", "Security", "DevOps");
		for (String g : groups) {
			SpocDetails s = new SpocDetails();
			s.setTeamName(g);
			s.setPrimaryName("Primary TBD");
			s.setPrimaryEmail("primary.tbd@" + g.toLowerCase() + ".example");
			s.setPrimaryContact("N/A");
			s.setSecondaryName("Secondary TBD");
			s.setSecondaryEmail("secondary.tbd@" + g.toLowerCase() + ".example");
			s.setSecondaryContact("N/A");
			s.setLastUpdatedBy("system-seed");
			s.setLastUpdatedTime(LocalDateTime.now());
			repo.save(s);
		}
	}
}
