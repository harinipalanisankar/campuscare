package com.campuscare.campuscare.controller;

import com.campuscare.campuscare.entity.User;
import com.campuscare.campuscare.service.ComplaintService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WorkerController {

    @Autowired
    private ComplaintService service;

    @GetMapping("/worker")
    public String workerDashboard(Model model, HttpSession session) {
        User worker = (User) session.getAttribute("user");
        if (worker == null || !"WORKER".equalsIgnoreCase(worker.getRole())) return "redirect:/login";

        model.addAttribute("list", service.getByWorker(worker.getId()));
        return "worker-dashboard";
    }

    @PostMapping("/updateStatus")
    public String updateStatus(@RequestParam("id") Long id, @RequestParam("status") String status, HttpSession session) {
        User worker = (User) session.getAttribute("user");
        if (worker == null || !"WORKER".equalsIgnoreCase(worker.getRole())) return "redirect:/login";

        service.updateStatus(id, status);
        return "redirect:/worker";
    }
}