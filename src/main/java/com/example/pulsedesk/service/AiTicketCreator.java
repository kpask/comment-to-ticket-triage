package com.example.pulsedesk.service;

import com.example.pulsedesk.dtos.AiTicketResponse;
import com.example.pulsedesk.models.Comment;
import com.example.pulsedesk.models.Ticket;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AiTicketCreator {

    private final TicketService ticketService;
    private final AiAnalysisService aiAnalysisService;

    public AiTicketCreator(TicketService ticketService, AiAnalysisService aiAnalysisService){
        this.ticketService = ticketService;
        this.aiAnalysisService = aiAnalysisService;
    }

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
