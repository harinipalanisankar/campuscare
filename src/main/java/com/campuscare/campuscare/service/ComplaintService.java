package com.campuscare.campuscare.service;

import com.campuscare.campuscare.entity.Complaint;
import com.campuscare.campuscare.entity.User;
import com.campuscare.campuscare.repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository repo;

    public Complaint saveComplaint(Complaint c, User user) {
        c.setUser(user);
        c.setStatus("Pending");
        c.setCreatedDate(LocalDateTime.now());
        return repo.save(c);
    }

    public List<Complaint> getAll() {
        return repo.findAll();
    }

    public List<Complaint> getByUser(Long userId) {
        return repo.findByUserId(userId);
    }

    public List<Complaint> getByWorker(Long workerId) {
        return repo.findByWorkerId(workerId);
    }

    public void assignWorker(Long complaintId, User worker) {
        Complaint c = repo.findById(complaintId).orElseThrow(() -> new IllegalArgumentException("Invalid Complaint ID"));
        c.setWorker(worker);
        c.setStatus("Pending"); // Updated from "Assigned" to "Pending"
        repo.save(c);
    }

    public void reassignWorker(Long complaintId, User worker, String status) {
        Complaint c = repo.findById(complaintId).orElseThrow(() -> new IllegalArgumentException("Invalid Complaint ID"));
        c.setWorker(worker);
        c.setStatus(status != null ? status : "Pending"); // Updated default fallback to "Pending"
        repo.save(c);
    }

    public void updateStatus(Long id, String status) {
        Complaint c = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Complaint ID"));
        c.setStatus(status);
        if ("Resolved".equalsIgnoreCase(status)) {
            c.setReassigned(false);
        }
        repo.save(c);
    }

    public void addFeedback(Long id, String feedback) {
        Complaint c = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Complaint ID"));
        // Replaces old feedback for this specific task with the new feedback string
        c.setFeedback(feedback != null && !feedback.trim().isEmpty() ? feedback.trim() : null);
        repo.save(c);
    }

    public void deleteFeedback(Long id) {
        Complaint c = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Complaint ID"));
        c.setFeedback(null);
        repo.save(c);
    }

    public void deleteAllFeedbackByUser(Long userId) {
        List<Complaint> userComplaints = repo.findByUserId(userId);
        for (Complaint c : userComplaints) {
            c.setFeedback(null);
        }
        repo.saveAll(userComplaints);
    }
}