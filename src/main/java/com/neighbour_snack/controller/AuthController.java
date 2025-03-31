package com.neighbour_snack.controller;

import static com.neighbour_snack.dto.AlertMessageDTO.MessageType.DANGER;
import static com.neighbour_snack.dto.AlertMessageDTO.MessageType.SUCCESS;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.neighbour_snack.dto.AlertMessageDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login(Model model, HttpServletResponse response) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() &&
                authentication.getAuthorities().stream()
                        .noneMatch(auth -> auth.getAuthority().equals("ROLE_ANONYMOUS"))) {

            String redirectUrl = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")) ? "/admin" : "/";

            response.sendRedirect(redirectUrl);
            return null;
        }
        model.addAttribute("title", "NS - Login");
        return "login";
    }

    @GetMapping("/login-fail")
    public String handleLoginFail(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        String errorMessage = "Login failed. Please try again."; // Default error message
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                if (ex.getMessage().contains("Bad credentials")) {
                    errorMessage = "Incorrect email or password";
                } else {
                    errorMessage = ex.getMessage();
                }
            }
        }
        redirectAttributes.addFlashAttribute("alertMessage",
                new AlertMessageDTO(DANGER, errorMessage));
        return "redirect:/login";
    }

    @GetMapping("/logout-success")
    public String signOut(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("alertMessage",
                new AlertMessageDTO(SUCCESS, "You have been logged out"));
        return "redirect:/login";
    }
}
