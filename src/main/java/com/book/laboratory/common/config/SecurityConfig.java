package com.book.laboratory.common.config;

import com.book.laboratory.common.filter.JwtTokenFilter;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

  private final JwtTokenFilter jwtTokenFilter;

  /**
   * Provides a delegating password encoder bean for encoding and verifying passwords.
   *
   * @return a PasswordEncoder that supports multiple encoding formats
   */
  @Bean
  public PasswordEncoder makePasswordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  /**
   * Configures the application's security filter chain, including CORS, CSRF, session management, and authorization rules.
   *
   * Permits all requests to paths under `/users/**` without authentication and requires authentication for all other requests.
   * Adds the JWT token filter before the username/password authentication filter to enable JWT-based authentication.
   *
   * @param http the {@link HttpSecurity} to modify
   * @return the configured {@link SecurityFilterChain}
   * @throws Exception if an error occurs during security configuration
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(cors -> cors.configurationSource(configurationSource()))
        .csrf(AbstractHttpConfigurer::disable) // csrf 비활성화
        .httpBasic(AbstractHttpConfigurer::disable)
        .sessionManagement(AbstractHttpConfigurer::disable);

    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/users/**").permitAll()
            .anyRequest().authenticated());

    http
        .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);



    return http.build();
  }

  /**
   * Defines a CORS configuration source bean allowing requests from http://localhost:3000 with all methods and headers permitted and credentials allowed.
   *
   * @return a CorsConfigurationSource configured for all paths
   */
  @Bean
  public CorsConfigurationSource configurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
    configuration.setAllowedMethods(Arrays.asList("*"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
  }
}
