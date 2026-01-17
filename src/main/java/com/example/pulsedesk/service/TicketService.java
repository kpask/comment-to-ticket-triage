package com.example.pulsedesk.service;

import com.example.pulsedesk.enums.Status;
import com.example.pulsedesk.models.Ticket;
import com.example.pulsedesk.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {
    private final TicketRepository repo;

    public TicketService(TicketRepository repo){
        this.repo = repo;
    }

    public Ticket updateTicket(int ticketId, Status status){
        if(status == null){
            throw new IllegalArgumentException("Status can't be null");
        }

        Ticket ticket = repo.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));

        ticket.setStatus(status);
        return repo.save(ticket);
    }

    public Ticket closeTicket(int ticketId) {
        return updateTicket(ticketId, Status.CLOSED);
    }

    public Ticket getTicketById(int ticketId){
        return repo.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));
    }
    public List<Ticket> getAllTickets(){
        return repo.findAll();
    }

    public List<Ticket> getTicketsByStatus(Status status){
        return repo.findByStatus(status);
    }

    public Ticket addTicket(Ticket ticket){
        if(ticket == null){
            throw new IllegalArgumentException("Ticket can't be null");
        }
        return repo.save(ticket);
    }
}
