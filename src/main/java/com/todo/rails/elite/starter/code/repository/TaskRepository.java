package com.todo.rails.elite.starter.code.repository;

import com.todo.rails.elite.starter.code.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Task Repository
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
	Optional<Task> findByTitle(String title);
}
