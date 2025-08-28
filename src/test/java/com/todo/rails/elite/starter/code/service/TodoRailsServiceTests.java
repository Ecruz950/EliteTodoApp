package com.todo.rails.elite.starter.code.service;

import com.todo.rails.elite.starter.code.model.Task;
import com.todo.rails.elite.starter.code.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Write Unit Tests for Services. Use JUnit to write tests for TaskService methods like addTask(), updateTask(), and deleteTask().
@SpringBootTest
public class TodoRailsServiceTests {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task sampleTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleTask = new Task("Sample Task", "This is a sample task.", false, LocalDate.now());
    }

    @Test
    void addTask_Success() {
        when(taskRepository.findByTitle(sampleTask.getTitle())).thenReturn(Optional.empty());
        when(taskRepository.save(sampleTask)).thenReturn(sampleTask);

        Task result = taskService.addTask(sampleTask);

        assertNotNull(result);
        assertEquals(sampleTask.getTitle(), result.getTitle());
        verify(taskRepository, times(1)).save(sampleTask);
    }

    @Test
    void addTask_Failure_TaskAlreadyExists() {
        when(taskRepository.findByTitle(sampleTask.getTitle())).thenReturn(Optional.of(sampleTask));

        Exception exception = assertThrows(RuntimeException.class, () -> taskService.addTask(sampleTask));

        assertEquals("Task already exists", exception.getMessage());
        verify(taskRepository, never()).save(sampleTask);
    }

    @Test
    void updateTask_Success() {
        Task updatedTask = new Task("Updated Task", "Updated description", true, LocalDate.now());
        when(taskRepository.findByTitle(sampleTask.getTitle())).thenReturn(Optional.of(sampleTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        Task result = taskService.updateTask(updatedTask);

        assertNotNull(result);
        assertEquals("Updated Task", result.getTitle());
    }

    @Test
    void deleteTask_Success() {
        when(taskRepository.findByTitle(sampleTask.getTitle())).thenReturn(Optional.of(sampleTask));

        taskService.deleteTask(sampleTask);

        verify(taskRepository, times(1)).delete(sampleTask);
    }
}
