package com.todo.rails.elite.starter.code.repository;

import com.todo.rails.elite.starter.code.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// User Repository
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);
}
