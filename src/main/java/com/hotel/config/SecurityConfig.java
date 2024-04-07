package com.hotel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;


@Configuration
public class SecurityConfig {

    private JWTRequestFilter jwtRequestFilter;

    public SecurityConfig(JWTRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }


    @Bean //As per new update on Spring Security we are using this lines of code
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf().disable().cors().disable();
        http.addFilterBefore(jwtRequestFilter, AuthorizationFilter.class);
        http.authorizeHttpRequests().
        //This line is for SignUp & Login URL is open other than this url all URL needs Authentication//
        requestMatchers("/api/v1/users/addUser", "/api/v1/users/login")
                .permitAll()
                //This line use for Role Authentication for Role Base Login
                .requestMatchers("/api/v1/countries/addCountry").hasRole("ADMIN")
                .requestMatchers("/api/v1/profile").hasAnyRole("ADMIN","USER")
                .anyRequest().authenticated();

                //For Java8 replace .requestMatchers with .antmatchers;
                //For Java8 replace http.authorizeHttpRequest with .authorizeRequest;
                //For Java8 replace jakarta with javax;

        return http.build();
    }
}
