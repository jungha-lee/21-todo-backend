package com.junghalee.todo.config;

import com.junghalee.todo.security.JwtAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.CorsFilter;

@Slf4j
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Http security builder
        http.cors() // default cors setting (as already set in WebMcvConfig)
                .and()
                .csrf().disable() // disabled as we don't use it.
                .httpBasic().disable() // basic auth disabled as we use token
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // not session based
                .and()
                .authorizeRequests().antMatchers("/", "/auth/**").permitAll() // permit all / and /auth/**
                .anyRequest() // should auth all paths except / and /auth/**
                .authenticated();

        // register filter
        // for every request
        // after execute CorsFilter
        // run jwtAuthenticationFilter
        http.addFilterAfter(
                jwtAuthenticationFilter,
                CorsFilter.class
        );
    }
}
