package com.example.spocportal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService users(PasswordEncoder encoder) {
		UserDetails admin = User.withUsername("admin").password(encoder.encode("Admin@2714")).roles("ADMIN").build();
		UserDetails user = User.withUsername("user").password(encoder.encode("User@2714")).roles("USER").build();
		return new InMemoryUserDetailsManager(admin, user);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth.requestMatchers("/h2-console/**").permitAll()
				.requestMatchers("/css/**", "/js/**", "/login").permitAll().requestMatchers("/admin/**")
				
				.hasRole("ADMIN").requestMatchers("/api/activities").hasRole("ADMIN")
				.requestMatchers("/api/activities/**").hasAnyRole("ADMIN", "USER")
				.requestMatchers("/api/assignments/**").hasAnyRole("ADMIN", "USER")
				.requestMatchers("/spocs", "/activities", "/dashboard", "/").hasAnyRole("ADMIN", "USER").anyRequest()
				.authenticated())
				.formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/dashboard", true).permitAll())
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout").permitAll())
				.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
				.headers(headers -> headers.frameOptions().disable());
		return http.build();
	}
	
	
}


