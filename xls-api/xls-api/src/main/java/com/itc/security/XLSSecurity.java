package com.itc.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This is for handling the security for All Rest API.
 * 
 * @author RohitSharma
 *
 */
@EnableWebSecurity
@Configuration
public class XLSSecurity extends WebSecurityConfigurerAdapter {

	private static final String[] AUTH_WHITELIST = { "/v2/api-docs", "/swagger-resources", "/swagger-resources/**",
			"/configuration/ui", "/configuration/security", "/swagger-ui.html", "/webjars/**" };

	@Value("${spring.security.user.name}")
	private String userName;

	@Value("${spring.security.user.password}")
	private String password;

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable().authorizeRequests().antMatchers(AUTH_WHITELIST).permitAll().anyRequest()
				.authenticated().and().httpBasic();

	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder authentication) throws Exception {
		authentication.inMemoryAuthentication().withUser(userName).password(passwordEncoder().encode(password))
				.authorities("ROLE_USER");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
