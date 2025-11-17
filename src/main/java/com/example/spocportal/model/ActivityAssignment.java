package com.example.spocportal.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ActivityAssignment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// link to activity
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="activity_id")
	private Activity activity;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="spoc_id")
    private SpocDetails spoc; 

    // reminder fields
    private Integer reminderCount = 0;
    private LocalDateTime lastReminderAt;


	@Enumerated(EnumType.STRING)
	private AssignmentStatus status = AssignmentStatus.PENDING;

	// override fields for this particular CR (editable)
	private String overridePrimaryName;
	private String overridePrimaryEmail;
	private String overridePrimaryContact;

	private String overrideSecondaryName;
	private String overrideSecondaryEmail;
	private String overrideSecondaryContact;

	@Column(length = 2000)
	private String comments;

	private LocalDateTime updatedAt = LocalDateTime.now();

	public ActivityAssignment() {
	}

	
	public Integer getReminderCount() {
		return reminderCount;
	}


	public void setReminderCount(Integer reminderCount) {
		this.reminderCount = reminderCount;
	}


	public LocalDateTime getLastReminderAt() {
		return lastReminderAt;
	}


	public void setLastReminderAt(LocalDateTime lastReminderAt) {
		this.lastReminderAt = lastReminderAt;
	}


	// getters/setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public SpocDetails getSpoc() {
		return spoc;
	}

	public void setSpoc(SpocDetails spoc) {
		this.spoc = spoc;
	}

	public AssignmentStatus getStatus() {
		return status;
	}

	public void setStatus(AssignmentStatus status) {
		this.status = status;
	}

	public String getOverridePrimaryName() {
		return overridePrimaryName;
	}

	public void setOverridePrimaryName(String overridePrimaryName) {
		this.overridePrimaryName = overridePrimaryName;
	}

	public String getOverridePrimaryEmail() {
		return overridePrimaryEmail;
	}

	public void setOverridePrimaryEmail(String overridePrimaryEmail) {
		this.overridePrimaryEmail = overridePrimaryEmail;
	}

	public String getOverridePrimaryContact() {
		return overridePrimaryContact;
	}

	public void setOverridePrimaryContact(String overridePrimaryContact) {
		this.overridePrimaryContact = overridePrimaryContact;
	}

	public String getOverrideSecondaryName() {
		return overrideSecondaryName;
	}

	public void setOverrideSecondaryName(String overrideSecondaryName) {
		this.overrideSecondaryName = overrideSecondaryName;
	}

	public String getOverrideSecondaryEmail() {
		return overrideSecondaryEmail;
	}

	public void setOverrideSecondaryEmail(String overrideSecondaryEmail) {
		this.overrideSecondaryEmail = overrideSecondaryEmail;
	}

	public String getOverrideSecondaryContact() {
		return overrideSecondaryContact;
	}

	public void setOverrideSecondaryContact(String overrideSecondaryContact) {
		this.overrideSecondaryContact = overrideSecondaryContact;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	// helper getters to display (prefer override else spoc master)
	@Transient
//	public String getDisplayPrimaryName() {
//		if (overridePrimaryName != null && !overridePrimaryName.isBlank())
//			return overridePrimaryName;
//		return spoc != null ? spoc.getPrimaryName() : "";
//	}
	public String getDisplayPrimaryName() {
	    return overridePrimaryName != null ? overridePrimaryName : spoc.getPrimaryName();
	}


	@Transient
	public String getDisplayPrimaryEmail() {
		if (overridePrimaryEmail != null && !overridePrimaryEmail.isBlank())
			return overridePrimaryEmail;
		return spoc != null ? spoc.getPrimaryEmail() : "";
	}

	@Transient
	public String getDisplayPrimaryContact() {
		if (overridePrimaryContact != null && !overridePrimaryContact.isBlank())
			return overridePrimaryContact;
		return spoc != null ? spoc.getPrimaryContact() : "";
	}
	

	@Transient
	public String getDisplaySecondaryName() {
		if (overrideSecondaryName != null && !overrideSecondaryName.isBlank())
			return overrideSecondaryName;
		return spoc != null ? spoc.getSecondaryName() : "";
	}

	@Transient
	public String getDisplaySecondaryEmail() {
		if (overrideSecondaryEmail != null && !overrideSecondaryEmail.isBlank())
			return overrideSecondaryEmail;
		return spoc != null ? spoc.getSecondaryEmail() : "";
	}

	@Transient
	public String getDisplaySecondaryContact() {
		if (overrideSecondaryContact != null && !overrideSecondaryContact.isBlank())
			return overrideSecondaryContact;
		return spoc != null ? spoc.getSecondaryContact() : "";
	}

	// aging simple: days left from activity implementation date
	@Transient
	public long getDaysDelta() {
		if (activity == null || activity.getImplementationDate() == null)
			return 0L;
		LocalDate today = LocalDate.now();
		return ChronoUnit.DAYS.between(today, activity.getImplementationDate());
	}

	@Transient
	public String getAging() {
		if (status != AssignmentStatus.PENDING)
			return status.name();
		long days = getDaysDelta();
		if (days < 0)
			return "RED";
		if (days <= 3)
			return "AMBER";
		return "AMBER";
	}
}
