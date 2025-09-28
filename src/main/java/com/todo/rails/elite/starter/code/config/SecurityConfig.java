package com.todo.rails.elite.starter.code.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.HiddenHttpMethodFilter;

/**
 * Configuration class for security settings in the application.
 *
 * <p>This class defines various security configurations, including
 * authentication, authorization, password encoding, and support for
 * advanced HTTP methods like PUT and DELETE in forms. It utilizes
 * Spring Security features to ensure secure access to application resources.</p>
 *
 * <h3>Key Components:</h3>
 * <ul>
 *   <li><strong>SecurityFilterChain:</strong> Configures which resources are publicly accessible,
 *       sets up custom login and logout pages, and enforces authentication for restricted URLs.</li>
 *   <li><strong>Password Encoder:</strong> Provides a {@link BCryptPasswordEncoder}
 *       to securely hash user passwords with configurable strength.</li>
 *   <li><strong>Hidden HTTP Method Filter:</strong> Enables the use of advanced HTTP methods
 *       in HTML forms, like PUT and DELETE, by translating a hidden field's value into the desired HTTP method.</li>
 * </ul>
 *
 * <h3>Usage:</h3>
 * <p>This configuration is designed for a typical web application. Adjustments may be required
 * for production environments, particularly with regard to CSRF protection and API security.</p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	/**
	 * Defines the strength level for the password encoder.
	 *
	 * <p>This constant specifies the computational complexity of the
	 * password hashing process used by the password encoder. The strength
	 * value influences the time required to hash passwords, providing a balance
	 * between security and performance.</p>
	 *
	 * <h3>Key Details:</h3>
	 * <ul>
	 *   <li><strong>Value:</strong> Set to <code>4</code>, representing a moderate
	 *       level of computational effort.</li>
	 *   <li><strong>Usage:</strong> Used to configure the {@link BCryptPasswordEncoder}
	 *       or similar password encoding mechanisms.</li>
	 *   <li><strong>Security:</strong> Higher values increase security but require
	 *       more processing time, making brute-force attacks more difficult.</li>
	 * </ul>
	 *
	 * <p><strong>Note:</strong> Consider adjusting the strength value based on the
	 * security requirements and processing capabilities of your application.</p>
	 */
	public static final int PASSWORD_ENCODER_STRENGTH = 4;
	private final UserDetailsService jpaUserDetailsService;

	@Autowired
	public SecurityConfig(UserDetailsService jpaUserDetailsService) {
		this.jpaUserDetailsService = jpaUserDetailsService;
	}

	/**
	 * Configures the application's security settings.
	 *
	 * <p>This method defines how incoming HTTP requests are secured,
	 * which URLs are publicly accessible, and how users authenticate
	 * and log out. It also sets up a user details service for retrieving
	 * user information.</p>
	 *
	 * @param http an instance of {@link HttpSecurity}, used to configure
	 *             web-based security for specific HTTP requests.
	 * @return a {@link SecurityFilterChain} object representing the
	 * configured security filter chain.
	 * @throws Exception if an error occurs during the security configuration process.
	 *
	 *                   <h3>Key Features:</h3>
	 *                   <ul>
	 *                     <li><strong>Public Resources:</strong> Grants public access to static resources
	 *                         like CSS, JavaScript, images, and the registration page.</li>
	 *                     <li><strong>Authentication:</strong> Requires authentication for all other requests.</li>
	 *                     <li><strong>Login:</strong> Configures a custom login page at <code>/login</code>
	 *                         and redirects to the homepage upon successful login.</li>
	 *                     <li><strong>Logout:</strong> Provides a logout URL (<code>/logout</code>)
	 *                         that clears the session and cookies, redirecting users to the login page.</li>
	 *                     <li><strong>CSRF Protection: WARNING!</strong> Disables CSRF protection to simplify
	 *                         API usage (not recommended for production).</li>
	 *                     <li><strong>HTTP Basic Authentication:</strong> Disables HTTP Basic
	 *                         authentication in favor of form-based login.</li>
	 *                   </ul>
	 *
	 *                   <p><strong>Note:</strong> This configuration is designed for demonstration or
	 *                   educational purposes and should be adjusted for production environments,
	 *                   particularly with regard to CSRF protection.</p>
	 */
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.authorizeHttpRequests(
						auth -> auth
								// Enforce authentication
								//  permits access to the /css/** , /js/** , /images/** URLS to all users
                                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()


								// Makes /login and /register publicly accessible
                                .requestMatchers("/login", "/register").permitAll()

								//  permit the /api/tasks/** to all users (NOT RECOMMENDED)
								.requestMatchers("/api/tasks/**").permitAll()

								//  authenticate all other requests
								.anyRequest().authenticated()
				)
				.formLogin(
						form -> form
								// Sets the /login URL for the login process
								.loginPage("/login")

								// Set the redirect URL to / if login is successful
								.defaultSuccessUrl("/", true)

								// permit to all users
								.permitAll()
				)
				.logout(
						logout -> logout
								.logoutUrl("/logout")
								.logoutSuccessUrl("/login")
								.invalidateHttpSession(true)
								.deleteCookies("JSESSIONID")
								.permitAll()
				)
				.userDetailsService(jpaUserDetailsService)
				.csrf(AbstractHttpConfigurer::disable)
				.httpBasic(AbstractHttpConfigurer::disable)
				.build();
	}

	/**
	 * Creates and configures a password encoder.
	 *
	 * <p>This method provides a {@link BCryptPasswordEncoder} instance,
	 * which is used to securely encode passwords. The encoder uses the
	 * BCrypt hashing algorithm, ensuring that passwords are hashed in
	 * a secure and reliable manner.</p>
	 *
	 * @return a {@link BCryptPasswordEncoder} object configured with a
	 * strength value of 13.
	 *
	 * <h3>Key Details:</h3>
	 * <ul>
	 *   <li><strong>BCrypt Algorithm:</strong> A widely used and secure
	 *       algorithm designed for password hashing, resistant to brute-force attacks.</li>
	 *   <li><strong>Strength Parameter:</strong> Configured with a strength of 13,
	 *       which determines the computational complexity of the hashing process.
	 *       Higher values make it more secure but require more processing power.</li>
	 * </ul>
	 *
	 * <p><strong>Usage:</strong> This encoder is typically used when handling
	 * user passwords in authentication and security configurations.</p>
	 */
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(PASSWORD_ENCODER_STRENGTH);
	}

	/**
	 * Creates a filter to support HTTP methods like PUT and DELETE in forms.
	 *
	 * <p>This method provides a {@link HiddenHttpMethodFilter} instance, which allows
	 * web applications to use HTTP methods such as PUT, DELETE, and PATCH in forms
	 * by including a hidden input field named <code>_method</code>. This is especially
	 * useful for browsers that only support GET and POST methods in HTML forms.</p>
	 *
	 * @return a {@link HiddenHttpMethodFilter} object that processes the hidden
	 * <code>_method</code> field and translates it into the corresponding HTTP method.
	 *
	 * <h3>Key Details:</h3>
	 * <ul>
	 *   <li><strong>Supported Methods:</strong> Enables support for PUT, DELETE, and PATCH
	 *       methods in addition to standard GET and POST requests.</li>
	 *   <li><strong>Usage:</strong> Add a hidden input field named <code>_method</code>
	 *       to your form with the desired HTTP method as its value (e.g., <code>&lt;input type="hidden" name="_method" value="PUT"&gt;</code>).</li>
	 * </ul>
	 *
	 * <p><strong>Note:</strong> This filter is often used in RESTful applications to handle
	 * HTTP methods not natively supported by HTML forms.</p>
	 */
	@Bean
	public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new HiddenHttpMethodFilter();
	}
}
