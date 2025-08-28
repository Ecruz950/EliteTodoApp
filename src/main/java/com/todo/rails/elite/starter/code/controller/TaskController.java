package com.todo.rails.elite.starter.code.controller;

import com.todo.rails.elite.starter.code.model.Task;
import com.todo.rails.elite.starter.code.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * REST controller for managing tasks in the to-do application.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

	private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
	private final TaskService taskService;

	@Autowired
	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}

	@GetMapping("/all")
	public ResponseEntity<List<Task>> getAllTasks() {
		try {
			return ResponseEntity.ok(taskService.getAllTasks());
		} catch (Exception exception) {
			logger.error("Error getting all tasks", exception);
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Task> getTaskById(@PathVariable(name = "id") Long id) {
		try {
			return ResponseEntity.ok(taskService.getTaskById(id));
		} catch (Exception exception) {
			logger.error("Error getting task by id: {}", id, exception);
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/title/{title}")
	public ResponseEntity<Task> getTaskByTitle(@PathVariable(name = "title") String title) {
		try {
			return ResponseEntity.ok(taskService.getTaskByTitle(title));
		} catch (Exception exception) {
			logger.error("Error getting task by title: {}", title, exception);
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ModelAndView addTask(@ModelAttribute Task task) {
		try {
			Task addedTask = taskService.addTask(task);
			return new ModelAndView("redirect:/tasks");
		} catch (Exception exception) {
			logger.error("Error adding task: {}", task.getTitle(), exception);
			return new ModelAndView("redirect:/tasks/add", "task", task);
		}
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public ModelAndView updateTask(@PathVariable(name = "id") Long id) {
		Task taskById = taskService.getTaskById(id);
		if (taskById != null) {
			taskById.setDueDate(
					LocalDate.parse(
							DateTimeFormatter.ofPattern("yyyy-MM-dd")
									.format(
											taskById.getDueDate()
									)
					)
			);
			return new ModelAndView("edit", "task", taskById);
		} else {
			logger.error("Task not found for update with id: {}", id);
			throw new RuntimeException("Task not found");
		}
	}

	@PostMapping("/update")
	public ModelAndView updateTask(@ModelAttribute Task task) {
		try {
			Task updatedTask = taskService.updateTask(task);
			return new ModelAndView("redirect:/tasks", "task", updatedTask);
		} catch (Exception exception) {
			logger.error("Error updating task: {}", task.getTitle(), exception);
			throw new RuntimeException("Task not found");
		}
	}

	@PostMapping("/complete/{id}")
	public ModelAndView completeTask(@PathVariable Long id) {
		try {
			Task taskById = taskService.getTaskById(id);
			taskById.setCompleted(true);
			taskService.updateTask(taskById);
			return new ModelAndView("redirect:/");
		} catch (Exception exception) {
			logger.error("Error completing task with id: {}", id, exception);
			return new ModelAndView("redirect:/");
		}
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ModelAndView deleteTask(@PathVariable Long id) {
		try {
			Task taskById = taskService.getTaskById(id);
			taskService.deleteTask(taskById);
			return new ModelAndView("redirect:/");
		} catch (Exception exception) {
			logger.error("Error deleting task with id: {}", id, exception);
			return new ModelAndView("redirect:/");
		}
	}

	@GetMapping("/pending")
	public ResponseEntity<List<Task>> getPendingTasks() {
		try {
			return ResponseEntity.ok(taskService.getPendingTasks());
		} catch (Exception exception) {
			logger.error("Error getting pending tasks", exception);
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/completed")
	public ResponseEntity<List<Task>> getCompletedTasks() {
		try {
			return ResponseEntity.ok(taskService.getCompletedTasks());
		} catch (Exception exception) {
			logger.error("Error getting completed tasks", exception);
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/today")
	public ResponseEntity<List<Task>> getTodayTasks() {
		try {
			return ResponseEntity.ok(taskService.getTodayTasks());
		} catch (Exception exception) {
			logger.error("Error getting today tasks", exception);
			return ResponseEntity.notFound().build();
		}
	}
}
