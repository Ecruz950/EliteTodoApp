package com.todo.rails.elite.starter.code.service;

import com.todo.rails.elite.starter.code.model.User;
import com.todo.rails.elite.starter.code.repository.UserRepository;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

// Service Layer: UserService. Implement business logic for managing users, including validation, password encoding, and error handling.
@Service
public class UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	// add a user
	public User addUser(@NotNull(message = "User cannot be null") User user) {
		if (userRepository.findByUsername(user.getUsername()).isPresent()) {
			logger.error("Username already exists: {}", user.getUsername());
			throw new RuntimeException("Username already exists");
		}
		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
			logger.error("Email already exists: {}", user.getEmail());
			throw new RuntimeException("Email already exists");
		}
        if (user.getRoles() == null || user.getRoles().isBlank()) {
            user.setRoles("USER"); // or "ROLE_USER" if you’re using Spring Security conventions
        }
		String password = user.getPassword();
		// Enable password encoder in user service
        user.setPassword(passwordEncoder.encode(password));

		return userRepository.save(user);
	}

	// get a user by username
	public User getUserByUsername(
			@NotNull(message = "Username cannot be null")
			@NotBlank(message = "Username cannot be blank")
			String username
	) {
		return userRepository.findByUsername(username)
				.orElseThrow(
						() -> {
							logger.error("User not found with username: {}", username);
							return new RuntimeException("User not found");
						}
				);
	}

	// get a user by email
	public User getUserByEmail(
			@NotNull(message = "Email cannot be null")
			@NotBlank(message = "Email cannot be blank")
			@Email(message = "Email should be valid")
			String email
	) {
		return userRepository.findByEmail(email)
				.orElseThrow(
						() -> {
							logger.error("User not found with email: {}", email);
							return new RuntimeException("User not found");
						}
				);
	}

	// get a user by id
	public User getUserById(
			@NotNull(message = "Id cannot be null")
			Long id
	) {
		return userRepository.findById(id)
				.orElseThrow(
						() -> {
							logger.error("User not found with id: {}", id);
							return new RuntimeException("User not found");
						}
				);
	}

	// update a user
	public User updateUser(@NotNull(message = "User cannot be null") User user) {
		if (userRepository.findByUsername(user.getUsername()).isEmpty()) {
			logger.error("User not found for update: {}", user.getUsername());
			throw new RuntimeException("User not found");
		}
		return userRepository.save(user);
	}

	// delete a user
	public void deleteUser(@NotNull(message = "User cannot be null") User user) {
		if (userRepository.findByUsername(user.getUsername()).isEmpty()) {
			logger.error("User not found for deletion: {}", user.getUsername());
			throw new RuntimeException("User not found");
		}
		userRepository.delete(user);
	}

	public List<User> getAllUsers() {
		if (userRepository.findAll().isEmpty()) {
			logger.error("No users found");
			throw new RuntimeException("No users found");
		}
		return userRepository.findAll();
	}
}
