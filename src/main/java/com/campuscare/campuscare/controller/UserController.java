package com.campuscare.campuscare.controller;

import com.campuscare.campuscare.entity.Complaint;
import com.campuscare.campuscare.entity.User;
import com.campuscare.campuscare.service.ComplaintService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    private ComplaintService service;

    @GetMapping("/user")
    public String userDashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"USER".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("list", service.getByUser(user.getId()));
        return "user-dashboard";
    }

    @GetMapping("/complaint/new")
    public String complaintForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"USER".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("complaint", new Complaint());
        return "complaint";
    }

    @PostMapping("/submit")
    public String submit(@ModelAttribute Complaint c, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"USER".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        service.saveComplaint(c, user);
        return "redirect:/user"; // Clean redirect back to the user's dashboard table
    }

    @PostMapping("/feedback")
    public String feedback(@RequestParam("id") Long id,
                           @RequestParam("feedback") String feedback,
                           HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"USER".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        // Passes feedback string directly to the service layer to save or overwrite
        service.addFeedback(id, feedback);
        return "redirect:/user?feedbackSent=true";
    }

    @PostMapping("/feedback/delete")
    public String deleteFeedback(@RequestParam("id") Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"USER".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        service.deleteFeedback(id);
        return "redirect:/user?feedbackDeleted=true";
    }

    @PostMapping("/feedback/deleteAll")
    public String deleteAllFeedback(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"USER".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        service.deleteAllFeedbackByUser(user.getId());
        return "redirect:/user?allFeedbackDeleted=true";
    }
}