package com.arshkhan.queuedesk.demo;

import com.arshkhan.queuedesk.io.TicketFileLoader;
import com.arshkhan.queuedesk.model.Ticket;
import com.arshkhan.queuedesk.exception.InvalidTicketDataException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class TicketFileLoaderDemo {

    public static void main(String[] args) {

        try {

            // Path of the input file
            Path inputFile = Path.of("src/main/resources/tickets.txt");

            // Load tickets from file
            List<Ticket> tickets = TicketFileLoader.load(inputFile);

            // Display loaded tickets
            System.out.println("========== LOADED TICKETS ==========");

            for (Ticket ticket : tickets) {

                System.out.println("ID: " + ticket.getId());
                System.out.println("Title: " + ticket.getTitle());
                System.out.println("Priority: " + ticket.getPriority());
                System.out.println("Status: " + ticket.getStatus());
                System.out.println("Assignee: " + ticket.getAssignee());
                System.out.println("Tags: " + ticket.getTags());

                System.out.println("--------------------------------");
            }

            System.out.println("Total tickets loaded: " + tickets.size());

        } catch (IOException | InvalidTicketDataException e) {

            System.out.println("Error loading tickets: " + e.getMessage());
        }
    }
}