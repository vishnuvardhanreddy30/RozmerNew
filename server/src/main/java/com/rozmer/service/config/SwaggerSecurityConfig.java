package com.rozmer.service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(0) // Higher priority than APISecurityConfig
public class SwaggerSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers(
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/configuration/**",
                "/webjars/**",
                "/",
                "/login",
                "/register",
                "/payment/**",
                "/api/**",
                "/add-user/**"
            ).permitAll()
            .anyRequest().authenticated()
            .and()
            .csrf().disable();
    }
}
