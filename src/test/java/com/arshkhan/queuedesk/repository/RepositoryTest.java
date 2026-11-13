package com.arshkhan.queuedesk.repository;

import com.arshkhan.queuedesk.model.*;
import com.arshkhan.queuedesk.exception.TicketNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryTest {

    private Repository<Ticket> repository;

    private Ticket ticket1;
    private Ticket ticket2;
    private Ticket ticket3;
    private Ticket ticket4;

    @BeforeEach
    void setUp() {

        repository = new Repository<>();

        ticket1 = new BugTicket(
                "Login Error", "Unable to login", "Arsh",
                Priority.HIGH, Severity.CRITICAL, "Login steps"
        );

        ticket2 = new BugTicket(
                "Payment Failure", "Payment issue", "Rahul",
                Priority.HIGH, Severity.HIGH, "Payment steps"
        );

        ticket3 = new BugTicket(
                "UI Bug", "Alignment issue", "Aman",
                Priority.LOW, Severity.LOW, "UI steps"
        );

        ticket4 = new BugTicket(
                "Database Error", "Connection failed", "Arsh",
                Priority.MEDIUM, Severity.MEDIUM, "Database steps"
        );

        repository.add(ticket1);
        repository.add(ticket2);
        repository.add(ticket3);
        repository.add(ticket4);
    }

    // 1. Test findByIdSafe()
    @Test
    void shouldFindTicketByIdSafely() {

        Optional<Ticket> result =
                repository.findByIdSafe(ticket2.getId());

        assertTrue(result.isPresent());
        assertEquals("Payment Failure", result.get().getTitle());
    }

    @Test
    void shouldGetTicketTitleUsingMap() {

        String title = repository.findByIdSafe(ticket3.getId())
                .map(Ticket::getTitle)
                .orElse("Ticket not found");

        assertEquals("UI Bug", title);
    }

    //  Test ifPresentOrElse()
    @Test
    void shouldHandleTicketUsingIfPresentOrElse() {

        StringBuilder message = new StringBuilder();

        repository.findByIdSafe(ticket1.getId())
                .ifPresentOrElse(
                        ticket -> message.append(ticket.getTitle()),
                        () -> message.append("Ticket not found")
                );

        assertEquals("Login Error", message.toString());
    }

    // Test orElseThrow()
    @Test
    void shouldThrowExceptionWhenTicketDoesNotExist() {

        assertThrows(
                TicketNotFoundException.class,
                () -> repository.findByIdSafe(-1)
                        .orElseThrow(() ->
                                new TicketNotFoundException(
                                        "Ticket with ID -1 not found."
                                ))
        );
    }

    // Test Optional.empty()
    @Test
    void shouldReturnEmptyWhenTicketDoesNotExist() {

        Optional<Ticket> result =
                repository.findByIdSafe(-1);

        assertTrue(result.isEmpty());
    }
}