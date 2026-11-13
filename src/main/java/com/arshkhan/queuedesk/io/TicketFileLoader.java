package com.arshkhan.queuedesk.io;

import com.arshkhan.queuedesk.model.*;
import com.arshkhan.queuedesk.exception.InvalidTicketDataException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TicketFileLoader {

    public static List<Ticket> load(Path file)
            throws IOException, InvalidTicketDataException {

        List<Ticket> tickets = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(file)) {

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {

                lineNumber++;

                // Skip empty lines
                if (line.isBlank()) {
                    continue;
                }

                try {

                    // Split line into 10 fields
                    String[] fields = line.split("\\|", -1);

                    // Validate field count
                    if (fields.length != 10) {
                        throw new IllegalArgumentException(
                                "Expected 10 fields but found " + fields.length
                        );
                    }

                    // Remove unnecessary spaces
                    for (int i = 0; i < fields.length; i++) {
                        fields[i] = fields[i].trim();
                    }

                    // Extract common fields
                    String type = fields[0].toUpperCase();
                    String title = fields[1];
                    String description = fields[2];
                    String requester = fields[3];

                    String assignee = fields[6];
                    String tags = fields[7];

                    // Validate required fields
                    if (title.isBlank()) {
                        throw new IllegalArgumentException(
                                "Title cannot be empty"
                        );
                    } else if (description.isBlank()) {
                        throw new IllegalArgumentException(
                                "Description cannot be empty"
                        );
                    } else if (requester.isBlank()) {
                        throw new IllegalArgumentException(
                                "Requester cannot be empty"
                        );
                    } else if (fields[4].isBlank()) {
                        throw new IllegalArgumentException(
                                "Priority cannot be empty"
                        );
                    } else if (fields[5].isBlank()) {
                        throw new IllegalArgumentException(
                                "Status cannot be empty"
                        );
                    }

                    // Validate Priority
                    Priority priority;

                    try {
                        priority = Priority.valueOf(fields[4].toUpperCase());
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException(
                                "Invalid priority: " + fields[4]
                        );
                    }

                    // Validate Status
                    Status status;

                    try {
                        status = Status.valueOf(fields[5].toUpperCase());
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException(
                                "Invalid status: " + fields[5]
                        );
                    }

                    // Create appropriate ticket
                    Ticket ticket;

                    switch (type) {

                        case "BUG":

                            if (fields[8].isBlank()) {
                                throw new IllegalArgumentException(
                                        "Severity cannot be empty"
                                );
                            }

                            Severity severity;

                            try {
                                severity = Severity.valueOf(
                                        fields[8].toUpperCase()
                                );
                            } catch (IllegalArgumentException e) {
                                throw new IllegalArgumentException(
                                        "Invalid severity: " + fields[8]
                                );
                            }

                            if (fields[9].isBlank()) {
                                throw new IllegalArgumentException(
                                        "Steps to reproduce cannot be empty"
                                );
                            }

                            ticket = new BugTicket(
                                    title,
                                    description,
                                    requester,
                                    priority,
                                    severity,
                                    fields[9]
                            );

                            break;

                        case "FEATURE":

                            if (fields[8].isBlank()) {
                                throw new IllegalArgumentException(
                                        "Votes cannot be empty"
                                );
                            }

                            int votes;

                            try {
                                votes = Integer.parseInt(fields[8]);
                            } catch (NumberFormatException e) {
                                throw new IllegalArgumentException(
                                        "Votes must be a valid integer: " + fields[8]
                                );
                            }

                            if (votes < 0) {
                                throw new IllegalArgumentException(
                                        "Votes cannot be negative"
                                );
                            }

                            if (fields[9].isBlank()) {
                                throw new IllegalArgumentException(
                                        "Business justification cannot be empty"
                                );
                            }

                            ticket = new FeatureRequestTicket(
                                    title,
                                    description,
                                    requester,
                                    priority,
                                    votes,
                                    fields[9]
                            );

                            break;

                        case "ACCESS":

                            if (fields[8].isBlank()) {
                                throw new IllegalArgumentException(
                                        "System name cannot be empty"
                                );
                            }

                            if (fields[9].isBlank()) {
                                throw new IllegalArgumentException(
                                        "Access level cannot be empty"
                                );
                            }

                            ticket = new AccessRequestTicket(
                                    title,
                                    description,
                                    requester,
                                    priority,
                                    fields[8],
                                    fields[9]
                            );

                            break;

                        default:

                            throw new IllegalArgumentException(
                                    "Unknown ticket type: " + type
                            );
                    }

                    // Set common properties
                    ticket.setStatus(status);

                    if (!assignee.isBlank()) {
                        ticket.setAssignee(assignee);
                    }

                    // Add tags
                    if (!tags.isBlank()) {

                        String[] tagArray = tags.split(",");

                        for (String tag : tagArray) {

                            if (!tag.isBlank()) {
                                ticket.addTag(tag.trim());
                            }
                        }
                    }

                    // Add ticket to list
                    tickets.add(ticket);

                } catch (RuntimeException e) {

                    // Wrap errors with line number
                    throw new InvalidTicketDataException(
                            "Invalid data at line " + lineNumber +
                                    ": " + e.getMessage(),
                            e
                    );
                }
            }
        }

        return tickets;
    }
}