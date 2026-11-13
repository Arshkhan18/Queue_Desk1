package com.arshkhan.queuedesk.report;

import com.arshkhan.queuedesk.model.Ticket;
import com.arshkhan.queuedesk.model.Priority;
import com.arshkhan.queuedesk.model.Status;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

public class ReportGenerator {

    // A CRITICAL ticket is overdue if it remains unresolved
    // for more than 24 hours.
    private static final double OVERDUE_HOURS = 24.0;

    public static String generate(List<Ticket> tickets) {

        StringBuilder report = new StringBuilder();

        double totalEffort = 0;

        report.append("========================================\n");
        report.append("          QUEUEDESK REPORT\n");
        report.append("========================================\n\n");

        report.append("Total Tickets: ")
                .append(tickets.size())
                .append("\n\n");

        // Individual ticket details
        report.append("========== TICKET DETAILS ==========\n\n");

        for (Ticket ticket : tickets) {

            report.append("Ticket ID: ")
                    .append(ticket.getId())
                    .append("\n");

            report.append("Title: ")
                    .append(ticket.getTitle())
                    .append("\n");

            report.append("Priority: ")
                    .append(ticket.getPriority())
                    .append("\n");

            report.append("Status: ")
                    .append(ticket.getStatus())
                    .append("\n");

            String assignee = ticket.getAssignee();

            if (assignee == null || assignee.isBlank()) {
                assignee = "Unassigned";
            }

            report.append("Assignee: ")
                    .append(assignee)
                    .append("\n");

            report.append("Tags: ")
                    .append(ticket.getTags())
                    .append("\n");

            report.append("Age: ")
                    .append(String.format(
                            Locale.ROOT, "%.2f", ticket.ageInHours()))
                    .append(" hours\n");

            double effort = ticket.estimateEffortHours();

            report.append("Estimated Effort: ")
                    .append(effort)
                    .append(" hours\n");

            totalEffort += effort;

            report.append("----------------------------------------\n");
        }

        // Priority summary
        report.append("\n========== PRIORITY SUMMARY ==========\n");

        for (Priority priority : Priority.values()) {

            int count = 0;

            for (Ticket ticket : tickets) {

                if (ticket.getPriority() == priority) {
                    count++;
                }
            }

            report.append(priority)
                    .append(": ")
                    .append(count)
                    .append("\n");
        }

        // Status summary
        report.append("\n========== STATUS SUMMARY ==========\n");

        for (Status status : Status.values()) {

            int count = 0;

            for (Ticket ticket : tickets) {

                if (ticket.getStatus() == status) {
                    count++;
                }
            }

            report.append(status)
                    .append(": ")
                    .append(count)
                    .append("\n");
        }

        // Total estimated effort
        report.append("\n========== EFFORT SUMMARY ==========\n");

        report.append("Total Estimated Effort: ")
                .append(totalEffort)
                .append(" hours\n");

        // Oldest OPEN ticket
        report.append("\n========== OLDEST OPEN TICKET ==========\n");

        Ticket oldestTicket = null;

        for (Ticket ticket : tickets) {

            if (ticket.getStatus() == Status.OPEN) {

                if (oldestTicket == null ||
                        ticket.getCreatedAt().isBefore(
                                oldestTicket.getCreatedAt())) {

                    oldestTicket = ticket;
                }
            }
        }

        if (oldestTicket == null) {

            report.append("No open tickets found.\n");

        } else {

            report.append("Ticket ID: ")
                    .append(oldestTicket.getId())
                    .append("\n");

            report.append("Title: ")
                    .append(oldestTicket.getTitle())
                    .append("\n");

            report.append("Age: ")
                    .append(String.format(
                            Locale.ROOT,
                            "%.2f",
                            oldestTicket.ageInHours()))
                    .append(" hours\n");
        }

        // Overdue CRITICAL tickets
        report.append("\n========== OVERDUE CRITICAL TICKETS ==========\n");

        int overdueCount = 0;

        for (Ticket ticket : tickets) {

            boolean unresolved =
                    ticket.getStatus() == Status.OPEN ||
                            ticket.getStatus() == Status.IN_PROGRESS;

            boolean critical =
                    ticket.getPriority() == Priority.CRITICAL;

            boolean overdue =
                    ticket.ageInHours() > OVERDUE_HOURS;

            if (critical && unresolved && overdue) {

                overdueCount++;

                report.append("Ticket ID: ")
                        .append(ticket.getId())
                        .append(" | Title: ")
                        .append(ticket.getTitle())
                        .append(" | Age: ")
                        .append(String.format(
                                Locale.ROOT,
                                "%.2f",
                                ticket.ageInHours()))
                        .append(" hours\n");
            }
        }

        if (overdueCount == 0) {
            report.append("No overdue CRITICAL tickets found.\n");
        }

        report.append("Total overdue CRITICAL tickets: ")
                .append(overdueCount)
                .append("\n");

        report.append("\n========================================\n");
        report.append("          END OF REPORT\n");
        report.append("========================================\n");

        return report.toString();
    }

    // Write report using BufferedWriter
    public static void write(String report, Path file)
            throws IOException {

        Path parent = file.getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(file)) {

            writer.write(report);
        }
    }
}