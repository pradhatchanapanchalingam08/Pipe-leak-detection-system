package com.example.pipedetection.security;

import com.example.pipedetection.entity.AppUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class RoleAccessFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String uri = request.getRequestURI();

        AppUser user = AuthSupport.currentUser(request.getSession(false));

        if (uri.equals("/api/auth/login") || uri.equals("/api/auth/register") || uri.equals("/api/auth/me") || uri.equals("/api/auth/logout")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (uri.equals("/admin.html")) {
            if (user == null || user.getRole() != AppUser.Role.ADMIN) { response.sendRedirect("/"); return; }
            filterChain.doFilter(request, response); return;
        }
        if (uri.equals("/customer.html")) {
            if (user == null || user.getRole() != AppUser.Role.CUSTOMER) { response.sendRedirect("/"); return; }
            filterChain.doFilter(request, response); return;
        }

        if (!uri.startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }


        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Please login first");
            return;
        }

        if (user.getRole() == AppUser.Role.CUSTOMER && !uri.startsWith("/api/customer/")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Customer access is restricted");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
