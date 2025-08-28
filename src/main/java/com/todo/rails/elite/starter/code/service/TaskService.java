package com.todo.rails.elite.starter.code.service;

import com.todo.rails.elite.starter.code.model.Task;
import com.todo.rails.elite.starter.code.repository.TaskRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// Service Layer: TaskService. Implement business logic for managing tasks, including validation and error handling.
@Service
public class TaskService {
	private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
	private final TaskRepository taskRepository;

	@Autowired
	public TaskService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

    // This method adds a new task to the repository if it does not already exist.
	public Task addTask(@NotNull(message = "Task cannot be null") Task task) throws RuntimeException {
		if (taskRepository.findByTitle(task.getTitle()).isPresent()) {
			logger.error("Task already exists: {}", task.getTitle());
			throw new RuntimeException("Task already exists");
		}
		return taskRepository.save(task);
	}

    // This method retrieves a task by its ID, throwing an exception if not found.
	public Task getTaskById(@NotNull(message = "Id cannot be null") Long id) throws RuntimeException {
		return taskRepository.findById(id)
				.orElseThrow(
						() -> {
							logger.error("Task not found with id: {}", id);
							return new RuntimeException("Task not found");
						}
				);
	}

    // This method retrieves a task by its title, throwing an exception if not found.
	public Task getTaskByTitle(
			@NotNull(message = "Title cannot be null")
			@NotBlank(message = "Title cannot be blank")
			String title
	) throws RuntimeException {
		return taskRepository.findByTitle(title)
				.orElseThrow(
						() -> {
							logger.error("Task not found with title: {}", title);
							return new RuntimeException("Task not found");
						}
				);
	}

    // This method retrieves all tasks from the repository.
	public List<Task> getAllTasks() {
		if (taskRepository.findAll().isEmpty()) {
			return List.of();
		}
		return taskRepository.findAll();
	}

    // This method updates an existing task's details, throwing an exception if the task does not exist.
	public Task updateTask(@NotNull(message = "Task cannot be null") Task task) throws RuntimeException {
		Optional<Task> existingTask = taskRepository.findByTitle(task.getTitle());
		if (existingTask.isEmpty()) {
			logger.error("Task not found for update: {}", task.getTitle());
			throw new RuntimeException("Task not found");
		}
		Task taskToUpdate = existingTask.get();
		taskToUpdate.setTitle(task.getTitle());
		taskToUpdate.setDescription(task.getDescription());
		taskToUpdate.setCompleted(task.isCompleted());
		taskToUpdate.setDueDate(task.getDueDate());
		return taskRepository.save(taskToUpdate);
	}

    // This method deletes a task from the repository, throwing an exception if the task does not exist.
	public void deleteTask(@NotNull(message = "Task cannot be null") Task task) throws RuntimeException {
		Optional<Task> existingTask = taskRepository.findByTitle(task.getTitle());
		if (existingTask.isEmpty()) {
			logger.error("Task not found for deletion: {}", task.getTitle());
			throw new RuntimeException("Task not found");
		}
		taskRepository.delete(task);
	}

    // This method retrieves all pending (not completed) tasks from the repository.
	public List<Task> getPendingTasks() {
		List<Task> allTasks = getAllTasks();
		if (allTasks.isEmpty()) {
			return List.of();
		}
		return allTasks.stream()
				.filter(task -> !task.isCompleted())
				.toList();
	}

    // This method retrieves all completed tasks from the repository.
	public List<Task> getCompletedTasks() {
		List<Task> allTasks = getAllTasks();
		if (allTasks.isEmpty()) {
			return List.of();
		}
		return allTasks.stream()
				.filter(Task::isCompleted)
				.toList();
	}

    // This method retrieves all tasks that are due today and not yet completed.
	public List<Task> getTodayTasks() {
		List<Task> allTasks = getAllTasks();
		if (allTasks.isEmpty()) {
			return List.of();
		}
		return allTasks.stream()
				.filter(
						task -> !task.isCompleted()
				)
				.filter(
						task -> task.getDueDate()
								.isEqual(LocalDate.now())
				)
				.toList();
	}
}
