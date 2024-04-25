package com.otmetkaX.config;

import com.otmetkaX.model.Security;
import com.otmetkaX.service.SecurityService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static java.lang.System.out;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final SecurityService service;

    public JwtAuthenticationFilter(SecurityService service) {
        this.service = service;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = extractTokenFromRequest(request);
            if (token != null) {
                Security user = service.findByToken(token);
                if (user != null) {
                    List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_" + user.getRole()); // Добавляем "ROLE_" перед ролью
                    Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.info("User {} successfully authenticated with roles: {}" + user.getLogin() + " " + authorities);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            logger.error("Authentication error: " + ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            String errorMessage = "{\"result\": \"Failed\", \"message\": \"" + ex.getMessage() + "\", \"errorCode\": \"" + "404" + "\"}";
            response.getWriter().write(errorMessage);
        }
    }


    private String extractTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }


}
