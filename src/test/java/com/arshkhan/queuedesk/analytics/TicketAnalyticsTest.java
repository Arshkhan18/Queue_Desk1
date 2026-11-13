package com.arshkhan.queuedesk.analytics;

import com.arshkhan.queuedesk.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TicketAnalyticsTest {

    private List<Ticket> tickets;

    @BeforeEach
    void setUp() {

        Ticket ticket1 = new BugTicket(
                "Login Error", "Unable to login", "Arsh",
                Priority.HIGH, Severity.CRITICAL, "Login steps"
        );

        Ticket ticket2 = new BugTicket(
                "Payment Failure", "Payment issue", "Rahul",
                Priority.HIGH, Severity.HIGH, "Payment steps"
        );

        Ticket ticket3 = new BugTicket(
                "UI Bug", "Alignment issue", "Aman",
                Priority.LOW, Severity.LOW, "UI steps"
        );

        Ticket ticket4 = new BugTicket(
                "Database Error", "Database connection failed", "Arsh",
                Priority.MEDIUM, Severity.MEDIUM, "Database steps"
        );

        // Assign tickets
        ticket1.setAssignee("Rahul");
        ticket2.setAssignee("Arsh");
        ticket3.setAssignee("Rahul");
        ticket4.setAssignee("Aman");

        ticket1.setStatus(Status.OPEN);
        ticket2.setStatus(Status.RESOLVED);
        ticket3.setStatus(Status.OPEN);
        ticket4.setStatus(Status.OPEN);

        //For getting the Tagline
        ticket1.addTag("backend");
        ticket1.addTag("login");
        ticket1.addTag("security");

        ticket2.addTag("backend");
        ticket2.addTag("payment");

        ticket3.addTag("frontend");
        ticket3.addTag("UI");

        ticket4.addTag("database");
        ticket4.addTag("security");

        tickets = List.of(ticket1, ticket2, ticket3, ticket4);
    }

    @Test
    void shouldCountTicketsByPriority() {

        Map<Priority, Long> result =
                TicketAnalytics.countByPriority(tickets);

        assertEquals(2L, result.get(Priority.HIGH));
        assertEquals(1L, result.get(Priority.LOW));
        assertEquals(1L, result.get(Priority.MEDIUM));
        assertEquals(3, result.size());
    }

    @Test
    void shouldGroupTicketsByAssignee() {

        Map<String, List<Ticket>> result =
                TicketAnalytics.groupByAssignee(tickets);

        assertEquals(2, result.get("Rahul").size());
        assertEquals(1, result.get("Arsh").size());
        assertEquals(1, result.get("Aman").size());

        assertEquals(3, result.size());
    }

    @Test
    void shouldCalculateAverageEffortHours() {
        double result=TicketAnalytics.averageEffortHours(tickets);
        assertEquals(3.75, result, 0.001);
    }

    @Test
    void shouldReturnZeroForAverageEffortWhenListIsEmpty() {

        double result = TicketAnalytics.averageEffortHours(List.of());

        assertEquals(0.0, result);
    }

    @Test
    void shouldCalculateTotalEffortHours() {

        double result = TicketAnalytics.totalEffortHours(tickets);

        assertEquals(15.0, result);
    }

    @Test
    void shouldFindOldestOpenTicket() {
        Optional<Ticket> result=TicketAnalytics.oldestOpenTicket(tickets);

        assertTrue(result.isPresent());
        assertEquals("Login Error", result.get().getTitle());
    }
    @Test
    void shouldReturnEmptyWhenNoOpenTicketsExist() {

        for (Ticket ticket : tickets) {
            ticket.setStatus(Status.RESOLVED);
        }

        Optional<Ticket> result =
                TicketAnalytics.oldestOpenTicket(tickets);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldGenerateSortedUniqueTagline(){
        String result= TicketAnalytics.tagLine(tickets);

        assertEquals(
                "backend, database, frontend, login, payment, security, ui",
                result
        );
    }

    @Test
    void shouldReturnEmptyStringWhenNoTagsExist() {

        String result = TicketAnalytics.tagLine(List.of());

        assertEquals("", result);
    }
}