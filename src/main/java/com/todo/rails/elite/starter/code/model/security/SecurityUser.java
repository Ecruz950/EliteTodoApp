package com.todo.rails.elite.starter.code.model.security;

import com.todo.rails.elite.starter.code.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

// Implement UserDetails to integrate with Spring Security
public class SecurityUser implements UserDetails {

	private final User user;

	public SecurityUser(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
        //  fetch the user authority (role) from the user and map it to a new SimpleGrantedAuthority object.
		return Arrays.stream(user.getRoles().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

}
