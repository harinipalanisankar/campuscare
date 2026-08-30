package com.campuscare.campuscare.controller;

import com.campuscare.campuscare.entity.Complaint;
import com.campuscare.campuscare.entity.User;
import com.campuscare.campuscare.repository.ComplaintRepository;
import com.campuscare.campuscare.repository.UserRepository;
import com.campuscare.campuscare.service.ComplaintService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {

    @Autowired
    private ComplaintService service;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ComplaintRepository complaintRepo;

    @GetMapping("/admin")
    public String adminDashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("list", service.getAll());
        model.addAttribute("workers", userRepo.findByRole("WORKER"));
        return "admin-dashboard";
    }

    // Handles both initial assignment and re-assignment via /reassign or /assign
    @PostMapping({"/assign", "/reassign"})
    public String assignOrReassign(@RequestParam("complaintId") Long complaintId,
                                   @RequestParam("workerId") Long workerId,
                                   @RequestParam(value = "status", defaultValue = "Pending") String status,
                                   HttpSession session) {
        User admin = (User) session.getAttribute("user");
        if (admin == null || !"ADMIN".equalsIgnoreCase(admin.getRole())) {
            return "redirect:/login";
        }

        User worker = userRepo.findById(workerId).orElse(null);
        Complaint complaint = complaintRepo.findById(complaintId).orElse(null);

        // Validation check: ensure the worker exists and has the WORKER role
        if (worker != null && "WORKER".equalsIgnoreCase(worker.getRole()) && complaint != null) {
            boolean isReassigning = complaint.getWorker() != null || "Resolved".equalsIgnoreCase(complaint.getStatus()) || (complaint.getFeedback() != null && !complaint.getFeedback().trim().isEmpty());
            if (isReassigning) {
                complaint.setReassigned(true);
            }
            complaint.setWorker(worker);
            // Reassigning automatically sets status back to Pending for the technician to process
            complaint.setStatus("Pending");
            complaintRepo.save(complaint);
        } else {
            return "redirect:/admin?error=invalid_worker";
        }

        return "redirect:/admin?assigned=true";
    }
}