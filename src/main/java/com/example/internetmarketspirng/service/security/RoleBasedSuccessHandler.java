package com.example.internetmarketspirng.service.security;

import com.example.internetmarketspirng.model.UserType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RoleBasedSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        Object principal = authentication.getPrincipal();
        if (principal instanceof SpringUser su) {
            if (su.getUser().getUserType() == UserType.ADMIN) {
                response.sendRedirect("/admin/home");
                return;
            }
        }
        response.sendRedirect("/categories");
    }
}
