package com.example.pulsedesk.controllers;

import com.example.pulsedesk.dtos.TicketResponse;
import com.example.pulsedesk.models.Ticket;
import com.example.pulsedesk.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing support tickets.
 * Provides endpoints to retrieve ticket information.
 */
@RestController
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService){
        this.ticketService = ticketService;
    }

    /**
     * Retrieves all tickets.
     *
     * @return a list of all tickets with their details.
     */
    @GetMapping
    public ResponseEntity<List<TicketResponse>> getAllTickets(){
        List<TicketResponse> list = ticketService.getAllTickets().stream()
                .map(t -> new TicketResponse(
                        t.getId(),
                        t.getTitle(),
                        t.getSummary(),
                        t.getCategory(),
                        t.getPriority(),
                        t.getStatus(),
                        t.getComment() != null ? t.getComment().getText() : null
                ))
                .toList();
        return ResponseEntity.ok(list);
    }

    /**
     * Retrieves a specific ticket by its ID.
     *
     * @param id the ID of the ticket to retrieve.
     * @return the ticket with its details.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicketById(@PathVariable int id){
        Ticket t = ticketService.getTicketById(id);
        TicketResponse response = new TicketResponse(
                t.getId(),
                t.getTitle(),
                t.getSummary(),
                t.getCategory(),
                t.getPriority(),
                t.getStatus(),
                t.getComment() != null ? t.getComment().getText() : null
        );

        return ResponseEntity.ok(response);
    }

}
