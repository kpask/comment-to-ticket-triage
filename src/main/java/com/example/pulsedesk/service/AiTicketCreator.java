package com.example.pulsedesk.service;

import com.example.pulsedesk.dtos.AiTicketResponse;
import com.example.pulsedesk.models.Comment;
import com.example.pulsedesk.models.Ticket;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Service for creating support tickets based on AI analysis of user comments.
 * Handles the integration between the AI analysis and ticket creation logic.
 */
@Service
public class AiTicketCreator {

    private final TicketService ticketService;
    private final AiAnalysisService aiAnalysisService;

    public AiTicketCreator(TicketService ticketService, AiAnalysisService aiAnalysisService){
        this.ticketService = ticketService;
        this.aiAnalysisService = aiAnalysisService;
    }

    /**
     * Asynchronously creates a support ticket if the AI analysis determines
     * that the provided comment should be converted into a ticket.
     *
     * @param comment the user comment to analyze and potentially convert into a ticket.
     */
    @Async
    public void createTicket(Comment comment){
        try{
            AiTicketResponse response = aiAnalysisService.analyzeComment(comment.getText());
            if(response.createTicket()){
                Ticket ticket = new Ticket();
                ticket.setTitle(response.title());
                ticket.setSummary(response.summary());
                ticket.setCategory(response.category());
                ticket.setPriority(response.priority());
                ticket.setComment(comment);
                ticketService.addTicket(ticket);
            }
        } catch(Exception e){
            System.err.println("Failed to create AI ticket: " + e.getMessage());
        }
    }
}
