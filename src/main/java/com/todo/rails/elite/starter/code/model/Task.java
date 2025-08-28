package com.todo.rails.elite.starter.code.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

// Define the Task entity with appropriate JPA annotations and validation constraints
@Entity
@Table(name = "tasks")
public class Task {

	// Add Validation Error Messages

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "title", unique = true, nullable = false)
    @NotNull(message = "Title cannot be null")
    @NotBlank(message = "Title cannot be blank")
	private String title;

	@Column(name = "description", nullable = false)
    @NotNull(message = "Description cannot be null")
    @NotBlank(message = "Description cannot be blank")
	private String description;

	@Column(name = "completed", nullable = false)
	private boolean completed;

	@Column(name = "due_date", nullable = false)
    @FutureOrPresent(message = "Due date must be in the present or future")
	private LocalDate dueDate;

	public Task() {
	}

	public Task(String title, String description, boolean completed, LocalDate dueDate) {
		this.title = title;
		this.description = description;
		this.completed = completed;
		this.dueDate = dueDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	@Override
	public String toString() {
		return "Task{" +
				"id=" + id +
				", title='" + title + '\'' +
				", description='" + description + '\'' +
				", completed=" + completed +
				", dueDate=" + dueDate +
				'}';
	}
}
