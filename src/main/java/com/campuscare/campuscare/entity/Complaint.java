package com.campuscare.campuscare.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "complaints")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String status;  // "Pending", "Assigned", "In Progress", "Resolved"

    @Column(columnDefinition = "TEXT")
    private String feedback; // User feedback field

    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // The student/user who raised it

    @ManyToOne
    @JoinColumn(name = "worker_id")
    private User worker; // The technician/worker assigned to fix it

    @Column(name = "reassigned")
    private Boolean reassigned = false; // Flag to track if grievance was reassigned by admin

    public Boolean getReassigned() {
        return reassigned != null && reassigned;
    }

    public boolean isReassigned() {
        return reassigned != null && reassigned;
    }

    public void setReassigned(Boolean reassigned) {
        this.reassigned = reassigned != null ? reassigned : false;
    }
}