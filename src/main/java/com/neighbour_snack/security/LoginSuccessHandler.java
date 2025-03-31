package com.neighbour_snack.security;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.neighbour_snack.entity.Role;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String redirectURL = request.getContextPath();

        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals(Role.ROLE_ADMIN.name()))) {
            redirectURL = "/admin";
        } else if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals(Role.ROLE_CUSTOMER.name()))) {
            redirectURL = "/";
        }

        response.sendRedirect(redirectURL);

    }

}
