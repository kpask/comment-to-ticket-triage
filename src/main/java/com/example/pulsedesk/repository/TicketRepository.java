package com.example.pulsedesk.repository;

import com.example.pulsedesk.enums.Status;
import com.example.pulsedesk.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Ticket entities.
 * Provides CRUD operations and query methods for Ticket data.
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByStatus(Status status);
}
