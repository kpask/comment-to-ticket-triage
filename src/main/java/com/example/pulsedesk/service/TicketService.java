package com.example.pulsedesk.service;

import com.example.pulsedesk.enums.Status;
import com.example.pulsedesk.exceptions.TicketNotFoundException;
import com.example.pulsedesk.models.Ticket;
import com.example.pulsedesk.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for managing support tickets.
 * Provides methods for adding, retrieving, updating, and closing tickets.
 */
@Service
public class TicketService {
    private final TicketRepository repo;

    public TicketService(TicketRepository repo){
        this.repo = repo;
    }

    /**
     * Updates the status of a ticket.
     *
     * @param ticketId the ID of the ticket to update.
     * @param status the new status to set for the ticket.
     * @return the updated ticket.
     * @throws IllegalArgumentException if the status is null.
     * @throws TicketNotFoundException if the ticket is not found.
     */
    public Ticket updateTicket(int ticketId, Status status){
        if(status == null){
            throw new IllegalArgumentException("Status can't be null.");
        }

        Ticket ticket = getTicketById(ticketId);
        ticket.setStatus(status);
        return repo.save(ticket);
    }

    /**
     * Closes a ticket by setting its status to CLOSED.
     *
     * @param ticketId the ID of the ticket to close.
     * @return the closed ticket.
     * @throws TicketNotFoundException if the ticket is not found.
     */
    public Ticket closeTicket(int ticketId) {
        return updateTicket(ticketId, Status.CLOSED);
    }

    /**
     * Retrieves a specific ticket by its ID.
     *
     * @param ticketId the ID of the ticket to retrieve.
     * @return the ticket with the specified ID.
     * @throws TicketNotFoundException if the ticket is not found.
     */
    public Ticket getTicketById(int ticketId){
        return repo.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException("Ticket with id " + ticketId + " not found."));
    }
    /**
     * Retrieves all tickets from the repository.
     *
     * @return a list of all tickets.
     */
    public List<Ticket> getAllTickets(){
        return repo.findAll();
    }

    /**
     * Retrieves tickets by their status.
     *
     * @param status the status of the tickets to retrieve.
     * @return a list of tickets with the specified status.
     */
    public List<Ticket> getTicketsByStatus(Status status){
        return repo.findByStatus(status);
    }

    /**
     * Adds a new ticket to the repository.
     *
     * @param ticket the ticket to add.
     * @return the saved ticket.
     * @throws IllegalArgumentException if the ticket is null.
     */
    public Ticket addTicket(Ticket ticket){
        if(ticket == null){
            throw new IllegalArgumentException("Ticket can't be null.");
        }
        return repo.save(ticket);
    }
}
