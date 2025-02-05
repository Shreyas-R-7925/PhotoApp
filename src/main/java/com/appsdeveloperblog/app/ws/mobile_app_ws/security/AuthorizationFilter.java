package com.appsdeveloperblog.app.ws.mobile_app_ws.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.crypto.SecretKey;

import com.appsdeveloperblog.app.ws.mobile_app_ws.security.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Slf4j
public class AuthorizationFilter extends BasicAuthenticationFilter {

    public AuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {

        String header = req.getHeader(SecurityConstants.HEADER_STRING);

        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

        String authorizationHeader = request.getHeader(SecurityConstants.HEADER_STRING);

        if (authorizationHeader == null) {
            return null;
        }

        String token = authorizationHeader.replace(SecurityConstants.TOKEN_PREFIX, "");

        byte[] secretKeyBytes = SecurityConstants.getTokenSecret().getBytes();
        SecretKey key = Keys.hmacShaKeyFor(secretKeyBytes);

        JwtParser parser = Jwts.parser()
                .verifyWith(key)
                .build();

        Claims claims = parser.parseSignedClaims(token).getPayload();
        String subject = (String) claims.get("sub");

        log.info("AuthorizationFilter [ getAuthentication ] {} ", subject); // this gives email

        log.info("AuthorizationFilter [ getAuthentication ] {} ", claims); //this gives sub, exp, iat

        if (subject == null) {
            return null;
        }

        return new UsernamePasswordAuthenticationToken(subject, null, new ArrayList<>());

    }

}
