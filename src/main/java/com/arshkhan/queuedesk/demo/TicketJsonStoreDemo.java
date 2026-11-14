package com.arshkhan.queuedesk.demo;

import com.arshkhan.queuedesk.io.TicketJsonStore;
import com.arshkhan.queuedesk.model.*;

import com.arshkhan.queuedesk.exception.InvalidTicketDataException;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TicketJsonStoreDemo {

    public static void main(String[] args) {

        List<Ticket> tickets = new ArrayList<>();

        // Bug ticket
        BugTicket bug = new BugTicket(
                "Login Error",
                "Unable to login",
                "Arsh",
                Priority.HIGH,
                Severity.CRITICAL,
                "Login crashes after submitting credentials"
        );
        bug.setAssignee("John");
        bug.addTag("backend");
        bug.addTag("authentication");
        tickets.add(bug);


// Feature ticket
        FeatureRequestTicket feature = new FeatureRequestTicket(
                "Dark Mode",
                "Add dark mode",
                "Rahul",
                Priority.LOW,
                10,
                "Improve user experience"
        );
        feature.setAssignee("Sarah");
        feature.addTag("frontend");
        feature.addTag("UI");
        tickets.add(feature);


// Access ticket
        AccessRequestTicket access = new AccessRequestTicket(
                "Admin Access",
                "Need admin access",
                "Aman",
                Priority.CRITICAL,
                "Database",
                "ADMIN"
        );
        access.setAssignee("Michael");
        access.addTag("security");
        access.addTag("database");
        tickets.add(access);

        Path file = Path.of("tickets.json");

        try {

            // Save tickets
            TicketJsonStore.save(tickets, file);

            System.out.println("Tickets saved successfully!");

            // Load tickets
            List<Ticket> loadedTickets = TicketJsonStore.load(file);

            System.out.println("\nTickets loaded successfully!");

            for (Ticket ticket : loadedTickets) {

                System.out.println(ticket);
                System.out.println("Actual class: " +
                        ticket.getClass().getSimpleName());

                System.out.println("-------------------------");
            }

        } catch (InvalidTicketDataException e) {
            System.out.println(e.getMessage());
        }
    }
}