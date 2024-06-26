package org.launchcode.buildMyAppTriangle_20.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/**").permitAll()
                )
                .csrf(csrf -> csrf.disable())
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll());

        return http.build();
    }
    @Bean
    @Order(1)
    public SecurityFilterChain registerChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/register")
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/register").anonymous()
                )
                .csrf(csrf -> csrf.disable());
        return http.build();
    }
    //Security Chain for Customers and Employees
    @Bean
    @Order(4)
    public SecurityFilterChain basicAuthenticationChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/contracts/**", "/accounts/**")
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/**").authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll());

        return http.build();
    }

    //Security Chain for Admin-Only endpoints
    @Bean
    @Order(2)
    public SecurityFilterChain adminAuthenticationChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/contracts/add", "/contracts/delete", "/contracts/view/{id}/update", "/accounts/add", "/accounts/delete", "/accounts/view/{id}/update")
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/**").hasRole("ADMIN")
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable())
                .logout((logout) -> logout.permitAll());

        return http.build();
    }
//    @Bean
//    @Order(3)
//    public SecurityFilterChain matchingIdOrAdminAuthenticationChain(HttpSecurity http) throws Exception {
//        http
//                .securityMatcher("/contracts/view/{id}**", "/accounts/view/{id}**")
//                .authorizeHttpRequests((authorize) -> authorize
//                        .requestMatchers("/**").hasRole("ADMIN")
//                )
//                .formLogin((form) -> form
//                        .loginPage("/login")
//                        .permitAll()
//                )
//                .csrf(csrf -> csrf.disable())
//                .logout((logout) -> logout.permitAll());
//
//        return http.build();
//    }
}
