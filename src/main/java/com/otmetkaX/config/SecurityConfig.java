package com.otmetkaX.config;

import com.otmetkaX.exception.CustomException;
import com.otmetkaX.model.Security;
import com.otmetkaX.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.core.env.Environment;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityService service;
    @Autowired
    private Environment environment;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        String login = environment.getProperty("admin.login");
        String role = environment.getProperty("admin.role");
        String password = environment.getProperty("admin.password");

        try {
            auth.inMemoryAuthentication()
                    .withUser(login)
                    .password(passwordEncoder.encode(password))
                    .roles(role);

            auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder);

        } catch (CustomException e) {
        }
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return user -> {
            try {
                Security userFromDb = service.findByLogin(user);
                return User.withUsername(userFromDb.getLogin())
                        .password(userFromDb.getPassword())
                        .roles(userFromDb.getRole())
                        .build();
            } catch (CustomException e) {
                throw new UsernameNotFoundException(e.getMessage());
            }
        };
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(service);

        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/security/**").hasAnyRole("ADMIN", "USER", "EMPLOYEE") // Меняем hasAnyRole на hasRole
                .antMatchers(HttpMethod.POST, "/security/**").hasAnyRole("ADMIN", "USER", "EMPLOYEE" )
                .antMatchers(HttpMethod.PUT, "/security/**").hasAnyRole("ADMIN", "USER", "EMPLOYEE" )
                .antMatchers(HttpMethod.PATCH, "/security/**").hasAnyRole("ADMIN", "USER", "EMPLOYEE" )
                .antMatchers(HttpMethod.GET, "/notification/**").hasAnyRole("ADMIN", "USER", "EMPLOYEE")
                .antMatchers(HttpMethod.POST, "/notification/**").hasAnyRole("ADMIN", "USER", "EMPLOYEE")
                .antMatchers(HttpMethod.PUT, "/notification/**").hasAnyRole("ADMIN", "USER", "EMPLOYEE")
                .antMatchers(HttpMethod.PATCH, "/notification/**").hasAnyRole("ADMIN", "USER", "EMPLOYEE")
                .antMatchers(HttpMethod.DELETE, "/notification/**").hasAnyRole("ADMIN")
                .and()
                .httpBasic()
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf()
                .disable();
    }
}