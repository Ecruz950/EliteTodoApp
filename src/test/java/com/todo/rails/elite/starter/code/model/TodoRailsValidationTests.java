package com.todo.rails.elite.starter.code.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Test Validation Logic. Write tests to verify that invalid inputs (e.g., empty title) are rejected.
@SpringBootTest
public class TodoRailsValidationTests {
    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        } catch (Exception exception) {
            System.err.println("Error setting up validator: " + exception.getMessage());
        }
    }

    @Test
    void validateTask_Success() {
        Task task = new Task("Title", "Description", false, LocalDate.now());
        Set<ConstraintViolation<Task>> violations = validator.validate(task);

        assertTrue(violations.isEmpty());
    }

    @Test
    void validateTask_Failure_EmptyTitle() {
        Task task = new Task("", "Description", false, LocalDate.now());
        Set<ConstraintViolation<Task>> violations = validator.validate(task);

        assertFalse(violations.isEmpty());
    }
}
