package com.appdeveloperblog.app.ws.security;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import javax.crypto.SecretKey;

import com.appsdeveloperblog.app.ws.mobile_app_ws.SpringApplicationContext;
import com.appsdeveloperblog.app.ws.mobile_app_ws.model.request.UserLoginRequest;
import com.appsdeveloperblog.app.ws.mobile_app_ws.security.SecurityConstants;
import com.appsdeveloperblog.app.ws.mobile_app_ws.service.UserService;
import com.appsdeveloperblog.app.ws.mobile_app_ws.shared.dto.UserDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        // Call the parent constructor with the authentication manager
        super(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try {

            UserLoginRequest creds = new ObjectMapper().readValue(req.getInputStream(), UserLoginRequest.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));

        } catch (IOException e) {
            throw new AuthenticationException("Not able to login") {
                @Override
                public String getMessage() {
                    return super.getMessage();
                }
            };
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
                                            Authentication auth) throws AuthenticationException {

        // Generate JWT and add it to a Response Header
        byte[] secretKeyBytes = SecurityConstants.getTokenSecret().getBytes();
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);
        Instant now = Instant.now();

        String userName = ((User) auth.getPrincipal()).getUsername();

        String token = Jwts.builder()
                .setSubject(userName)
                .setExpiration(Date.from(now.plusMillis(SecurityConstants.EXPIRATION_TIME)))
                .setIssuedAt(Date.from(now))
                .signWith(secretKey)
                .compact();

        UserService userService = (UserService) SpringApplicationContext.getBean("userServiceImpl");
        UserDto userDto = userService.getUser(userName);

        res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
        res.addHeader("UserId", userDto.getUserId());
    }
}
