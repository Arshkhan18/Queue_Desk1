package com.arshkhan.queuedesk;

import com.arshkhan.queuedesk.model.*;
import com.arshkhan.queuedesk.repository.Repository;
import com.arshkhan.queuedesk.io.TicketFileLoader;
import com.arshkhan.queuedesk.report.ReportGenerator;
import com.arshkhan.queuedesk.exception.InvalidTicketDataException;
import com.arshkhan.queuedesk.exception.TicketNotFoundException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class App {

    private static final Repository<Ticket> repository = new Repository<>();

    private static final Scanner scanner = new Scanner(System.in);

    private static boolean ticketsLoaded = false;

    public static void main(String[] args) {

        System.out.println("Welcome to QueueDesk!");

        boolean running = true;

        while (running) {

            System.out.println("\n========== QUEUEDESK ==========");
            System.out.println("1. Load Tickets from File");
            System.out.println("2. View All Tickets");
            System.out.println("3. Search Ticket by ID");
            System.out.println("4. View Priority Queue");
            System.out.println("5. Group Tickets by Assignee");
            System.out.println("6. Generate Report");
            System.out.println("7. Exit");

            System.out.print("\nEnter your choice: ");

            String input = scanner.nextLine();

            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            switch (choice) {

                case 1:
                    loadTickets();
                    break;

                case 2:
                    viewAllTickets();
                    break;

                case 3:
                    searchTicket();
                    break;

                case 4:
                    viewPriorityQueue();
                    break;

                case 5:
                    groupTickets();
                    break;

                case 6:
                    generateReport();
                    break;

                case 7:
                    running = false;
                    System.out.println("Thank you for using QueueDesk!");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }

        scanner.close();
    }

    // Load tickets from file
    private static void loadTickets() {

        if (ticketsLoaded) {
            System.out.println("Tickets have already been loaded.");
            return;
        }

        try {

            Path file = Path.of("src/main/resources/tickets.txt");

            List<Ticket> loadedTickets = TicketFileLoader.load(file);

            for (Ticket ticket : loadedTickets) {
                repository.add(ticket);
            }

            ticketsLoaded = true;

            System.out.println(loadedTickets.size() + " tickets loaded successfully.");

        } catch (IOException | InvalidTicketDataException e) {

            System.out.println("Error loading tickets: " + e.getMessage());
        }
    }

    // Display all tickets
    private static void viewAllTickets() {

        List<Ticket> tickets = repository.all();

        if (tickets.isEmpty()) {
            System.out.println("No tickets available. Load tickets first.");
            return;
        }

        System.out.println("\n========== ALL TICKETS ==========");

        for (Ticket ticket : tickets) {
            printTicket(ticket);
        }
    }

    // Search ticket by ID
    private static void searchTicket() {

        System.out.print("Enter Ticket ID: ");

        try {

            int id = Integer.parseInt(scanner.nextLine());

            Ticket ticket = repository.findById(id);

            System.out.println("\nTicket Found:");
            printTicket(ticket);

        } catch (NumberFormatException e) {

            System.out.println("Please enter a valid ticket ID.");

        } catch (TicketNotFoundException e) {

            System.out.println(e.getMessage());
        }
    }

    // Display tickets according to natural ordering
    private static void viewPriorityQueue() {

        List<Ticket> tickets = repository.all();

        if (tickets.isEmpty()) {
            System.out.println("No tickets available.");
            return;
        }

        PriorityQueue<Ticket> priorityQueue =
                new PriorityQueue<>(tickets);

        System.out.println("\n========== PRIORITY QUEUE ==========");

        while (!priorityQueue.isEmpty()) {

            Ticket ticket = priorityQueue.poll();

            printTicket(ticket);
        }
    }

    // Group tickets by assignee
    private static void groupTickets() {

        Map<String, List<Ticket>> groupedTickets =
                repository.groupByAssignee();

        if (groupedTickets.isEmpty()) {
            System.out.println("No tickets available.");
            return;
        }

        System.out.println("\n========== TICKETS BY ASSIGNEE ==========");

        for (Map.Entry<String, List<Ticket>> entry : groupedTickets.entrySet()) {

            System.out.println("\nAssignee: " + entry.getKey());

            for (Ticket ticket : entry.getValue()) {
                System.out.println(
                        "ID: " + ticket.getId() +
                                " | Title: " + ticket.getTitle()
                );
            }
        }
    }

    // Generate and save report
    private static void generateReport() {

        List<Ticket> tickets = repository.all();

        if (tickets.isEmpty()) {
            System.out.println("No tickets available to generate a report.");
            return;
        }

        String report = ReportGenerator.generate(tickets);

        System.out.println("\n" + report);

        try {

            Path file = Path.of("reports/queuedesk-report.txt");

            ReportGenerator.write(report, file);

            System.out.println("Report saved successfully at: " + file);

        } catch (IOException e) {

            System.out.println("Error saving report: " + e.getMessage());
        }
    }

    // Common method to display ticket information
    private static void printTicket(Ticket ticket) {

        System.out.println("-----------------------------------");

        System.out.println("ID: " + ticket.getId());
        System.out.println("Title: " + ticket.getTitle());
        System.out.println("Description: " + ticket.getShortDescription(40));
        System.out.println("Priority: " + ticket.getPriority());
        System.out.println("Status: " + ticket.getStatus());
        System.out.println("Assignee: " + ticket.getAssignee());
        System.out.println("Tags: " + ticket.getTags());
        System.out.println("Estimated Effort: " +
                ticket.estimateEffortHours() + " hours");

        System.out.println("-----------------------------------");
    }
}