package com.campuscare.campuscare.service;

import com.campuscare.campuscare.entity.Complaint;
import com.campuscare.campuscare.entity.User;
import com.campuscare.campuscare.repository.ComplaintRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComplaintServiceTest {

    @Mock
    private ComplaintRepository repo;

    @InjectMocks
    private ComplaintService complaintService;

    private User testUser;
    private Complaint complaint1;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test Student");

        complaint1 = new Complaint();
        complaint1.setId(101L);
        complaint1.setUser(testUser);
        complaint1.setTitle("Tube Light");
        complaint1.setFeedback("GoodGoodGoodGoodStill it is not sloved");
    }

    @Test
    void testAddFeedback_ReplacesOldFeedbackForSpecificTask() {
        when(repo.findById(101L)).thenReturn(Optional.of(complaint1));

        complaintService.addFeedback(101L, "Still it is not sloved");

        // Assert that old concatenated feedback on complaint1 was replaced by only the new feedback
        assertEquals("Still it is not sloved", complaint1.getFeedback(), "Old feedback for this task should be replaced by only the new feedback");
        verify(repo).save(complaint1);
    }

    @Test
    void testDeleteFeedback_ClearsFeedbackForComplaint() {
        when(repo.findById(101L)).thenReturn(Optional.of(complaint1));

        complaintService.deleteFeedback(101L);

        assertNull(complaint1.getFeedback(), "Feedback should be set to null");
        verify(repo).save(complaint1);
    }

    @Test
    void testDeleteAllFeedbackByUser_ClearsFeedbackForAllUserComplaints() {
        Complaint complaint2 = new Complaint();
        complaint2.setId(102L);
        complaint2.setUser(testUser);
        complaint2.setFeedback("Some other feedback");

        when(repo.findByUserId(1L)).thenReturn(java.util.List.of(complaint1, complaint2));

        complaintService.deleteAllFeedbackByUser(1L);

        assertNull(complaint1.getFeedback());
        assertNull(complaint2.getFeedback());
        verify(repo).saveAll(anyList());
    }
}
