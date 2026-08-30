package com.campuscare.campuscare.controller;

import com.campuscare.campuscare.entity.User;
import com.campuscare.campuscare.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/")
    public String indexRedirect() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        HttpSession session) {

        // Safeguard against hidden trailing or leading whitespaces from the form
        String cleanEmail = (email != null) ? email.trim() : "";
        String cleanPassword = (password != null) ? password.trim() : "";

        User user = userRepo.findByEmail(cleanEmail);

        if (user != null && user.getPassword().equals(cleanPassword)) {
            session.setAttribute("user", user);

            if ("ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/admin";
            if ("WORKER".equalsIgnoreCase(user.getRole())) return "redirect:/worker";
            if ("USER".equalsIgnoreCase(user.getRole())) return "redirect:/user";
        }
        return "redirect:/login?error=true";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}