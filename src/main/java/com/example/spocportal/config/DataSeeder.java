//package com.example.spocportal.config;
//
//import com.example.spocportal.model.SpocDetails;
//import com.example.spocportal.repository.SpocRepository;
//import jakarta.annotation.PostConstruct;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Component
//public class DataSeeder {
//	private final SpocRepository repo;
//
//	public DataSeeder(SpocRepository repo) {
//		this.repo = repo;
//	}
//
//	@PostConstruct
//	public void seed() {
//		if (repo.count() > 0)
//			return;
//		List<String> groups = List.of("Billing", "Finance", "Network", "Database", "Linux", "Windows", "Storage",
//				"Wintel", "Security", "DevOps");
//		for (String g : groups) {
//			SpocDetails s = new SpocDetails();
//			s.setTeamName(g);
//			s.setPrimaryName("Primary TBD");
//			s.setPrimaryEmail("primary.tbd@" + g.toLowerCase() + ".example");
//			s.setPrimaryContact("N/A");
//			s.setSecondaryName("Secondary TBD");
//			s.setSecondaryEmail("secondary.tbd@" + g.toLowerCase() + ".example");
//			s.setSecondaryContact("N/A");
//			s.setLastUpdatedBy("system-seed");
//			s.setLastUpdatedTime(LocalDateTime.now());
//			repo.save(s);
//		}
//	}
//}




package com.example.spocportal.config;

import com.example.spocportal.model.SpocDetails;
import com.example.spocportal.repository.SpocRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataSeeder {
    private final SpocRepository spocRepository;

    @Value("${spring.mail.group.emails}")
    private String groupEmails;

    public DataSeeder(SpocRepository spocRepository) {
        this.spocRepository = spocRepository;
    }

    @PostConstruct
    public void seed() {
        if (spocRepository.count() > 0) return;

        List<SpocDetails> items = List.of(
            create("Unix","Unix Primary","unix@yourcompany.com","9999999901","Unix Secondary","unix2@yourcompany.com","9999999902"),
            create("Middleware","MW Primary","middleware@yourcompany.com","9999999903","MW Secondary","middleware2@yourcompany.com","9999999904"),
            create("Database","DB Primary","database@yourcompany.com","9999999905","DB Secondary","database2@yourcompany.com","9999999906"),
            create("Network","Network Primary","network@yourcompany.com","9999999907","Network Secondary","network2@yourcompany.com","9999999908"),
            create("Security","Security Primary","security@yourcompany.com","9999999909","Security Secondary","security2@yourcompany.com","9999999910"),
            create("Backup","Backup Primary","backup@yourcompany.com","9999999911","Backup Secondary","backup2@yourcompany.com","9999999912"),
            create("Monitoring","Monitoring Primary","monitoring@yourcompany.com","9999999913","Monitoring Secondary","monitoring2@yourcompany.com","9999999914"),
            create("Storage","Storage Primary","storage@yourcompany.com","9999999915","Storage Secondary","storage2@yourcompany.com","9999999916"),
            create("Support","Support Primary","support@yourcompany.com","9999999917","Support Secondary","support2@yourcompany.com","9999999918"),
            create("WebLogic","WL Primary","weblogic@yourcompany.com","9999999919","WL Secondary","weblogic2@yourcompany.com","9999999920")
        );

        for (SpocDetails s : items) {
            s.setLastUpdatedBy("system-seed");
            s.setLastUpdatedTime(LocalDateTime.now());
            spocRepository.save(s);
        }
    }

    private SpocDetails create(String team, String pName, String pEmail, String pCont, String sName, String sEmail, String sCont) {
        SpocDetails s = new SpocDetails();
        s.setTeamName(team);
        s.setPrimaryName(pName);
        s.setPrimaryEmail(pEmail);
        s.setPrimaryContact(pCont);
        s.setSecondaryName(sName);
        s.setSecondaryEmail(sEmail);
        s.setSecondaryContact(sCont);
        s.setLastUpdatedBy("seed");
        s.setLastUpdatedTime(LocalDateTime.now());
        return s;
    }
}

