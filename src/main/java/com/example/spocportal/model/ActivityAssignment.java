package com.example.spocportal.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ActivityAssignment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "activity_id")
	private Activity activity;

	@ManyToOne
	@JoinColumn(name = "spoc_id")
	private SpocDetails spoc;

	private String status; // Pending / Confirmed / NACK
	private String comments;
	private LocalDateTime updatedAt = LocalDateTime.now();

	// getters and setters
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
}
