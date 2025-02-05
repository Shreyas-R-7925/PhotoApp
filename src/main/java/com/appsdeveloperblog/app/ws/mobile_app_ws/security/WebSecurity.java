package com.appsdeveloperblog.app.ws.mobile_app_ws.security;

import com.appdeveloperblog.app.ws.security.AuthenticationFilter;
import com.appsdeveloperblog.app.ws.mobile_app_ws.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    private final UserService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public WebSecurity(UserService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception {
        // Configure AuthenticationManagerBuilder
        AuthenticationManagerBuilder authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        // Customize Login URL path
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager);
        authenticationFilter.setFilterProcessesUrl("/users/login");

        http
        .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
        .authorizeHttpRequests(authz -> authz
            .requestMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL).permitAll()
            .requestMatchers(SecurityConstants.H2_CONSOLE).permitAll()
            .anyRequest().authenticated()
        )
        .authenticationManager(authenticationManager)
        .addFilter(authenticationFilter)
        .addFilter(new AuthorizationFilter(authenticationManager));

        return http.build();
    }
}

/*
    .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
    to enable <iframe></iframe> in websites for example embedding an H2 console or another service
    u need to disable this security setting

    why this security setting is used ?
    prevents pages from being embedded in an <iframe> to protect against Clickjacking attacks.
    It does this by setting the X-Frame-Options HTTP response header to DENY.
 */