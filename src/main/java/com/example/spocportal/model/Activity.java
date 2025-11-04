package com.example.spocportal.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Activity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private ActivityStatus activityStatus = ActivityStatus.PENDING;


	// CR number like CR432
	@Column(nullable = false, unique = true)
	private String crNumber;

	private String name;
	private LocalDate implementationDate;
	private String createdBy;
	private LocalDateTime createdAt = LocalDateTime.now();
	private Boolean manualCompleted = false; // admin can mark complete

	@OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ActivityAssignment> assignments = new ArrayList<>();

	public Activity() {
	}

	// getters/setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCrNumber() {
		return crNumber;
	}

	public void setCrNumber(String crNumber) {
		this.crNumber = crNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ActivityStatus getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(ActivityStatus activityStatus) {
		this.activityStatus = activityStatus;
	}

	public LocalDate getImplementationDate() {
		return implementationDate;
	}

	public void setImplementationDate(LocalDate implementationDate) {
		this.implementationDate = implementationDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public List<ActivityAssignment> getAssignments() {
		return assignments;
	}

	public void setAssignments(List<ActivityAssignment> assignments) {
		this.assignments = assignments;
	}

	public Boolean getManualCompleted() {
		return manualCompleted;
	}

	public void setManualCompleted(Boolean manualCompleted) {
		this.manualCompleted = manualCompleted;
	}
}
