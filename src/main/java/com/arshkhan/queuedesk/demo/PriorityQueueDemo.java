package com.arshkhan.queuedesk.demo;

import com.arshkhan.queuedesk.model.*;
import com.arshkhan.queuedesk.util.TicketComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class PriorityQueueDemo {

    public static void main(String[] args) {

        // 1. CREATE TICKETS

        List<Ticket> tickets = new ArrayList<>();

        BugTicket t1 = new BugTicket(
                "Login Error",
                "Unable to login",
                "Arsh",
                Priority.HIGH,
                Severity.HIGH,
                "Enter incorrect credentials"
        );

        FeatureRequestTicket t2 = new FeatureRequestTicket(
                "Dark Mode",
                "Add dark mode",
                "Rahul",
                Priority.LOW,
                10,
                "Improve user experience"
        );

        AccessRequestTicket t3 = new AccessRequestTicket(
                "Admin Access",
                "Need admin access",
                "Aman",
                Priority.CRITICAL,
                "Database",
                "ADMIN"
        );

        BugTicket t4 = new BugTicket(
                "Payment Failure",
                "Payment is not processing",
                "Arsh",
                Priority.MEDIUM,
                Severity.CRITICAL,
                "Try making a payment"
        );

        // 2. ASSIGN TICKETS

        t1.setAssignee("Rahul");
        t2.setAssignee("Arsh");
        t3.setAssignee("Rahul");
        t4.setAssignee("Arsh");

        // 3. SET TICKET STATUSES

        t1.setStatus(Status.OPEN);
        t2.setStatus(Status.RESOLVED);
        t3.setStatus(Status.IN_PROGRESS);
        t4.setStatus(Status.OPEN);

        // 4. ADD TICKETS TO LIST

        tickets.add(t1);
        tickets.add(t2);
        tickets.add(t3);
        tickets.add(t4);


        // 5. DISPLAY ORIGINAL TICKET LIST

        System.out.println("========== ALL TICKETS ==========");

        for (Ticket ticket : tickets) {
            System.out.println(
                    "ID: " + ticket.getId() +
                            " | Title: " + ticket.getTitle() +
                            " | Priority: " + ticket.getPriority() +
                            " | Assignee: " + ticket.getAssignee() +
                            " | Status: " + ticket.getStatus()
            );
        }


        // 6. NATURAL ORDERING QUEUE

        System.out.println("\n========== NATURAL ORDERING ==========");

        PriorityQueue<Ticket> triageQueue =
                new PriorityQueue<>();

        triageQueue.addAll(tickets);

        while (!triageQueue.isEmpty()) {

            Ticket nextTicket = triageQueue.poll();

            System.out.println(
                    "Serving Ticket #" + nextTicket.getId() +
                            " | Priority: " + nextTicket.getPriority() +
                            " | Title: " + nextTicket.getTitle()
            );
        }


        // 7. CUSTOM QUEUE: ASSIGNEE AND PRIORITY

        System.out.println(
                "\n========== QUEUE BY ASSIGNEE AND PRIORITY =========="
        );

        PriorityQueue<Ticket> customQueue =
                new PriorityQueue<>(
                        TicketComparator.byAssigneeThenPriority()
                );

        customQueue.addAll(tickets);

        while (!customQueue.isEmpty()) {

            Ticket ticket = customQueue.poll();

            System.out.println(
                    "Assignee: " + ticket.getAssignee() +
                            " | Priority: " + ticket.getPriority() +
                            " | Title: " + ticket.getTitle()
            );
        }


        // 8. CUSTOM QUEUE: STATUS AND AGE

        System.out.println(
                "\n========== QUEUE BY STATUS AND AGE =========="
        );

        PriorityQueue<Ticket> statusQueue =
                new PriorityQueue<>(
                        TicketComparator.byStatusThenAge()
                );

        statusQueue.addAll(tickets);

        while (!statusQueue.isEmpty()) {

            Ticket ticket = statusQueue.poll();

            System.out.println(
                    "Status: " + ticket.getStatus() +
                            " | Created: " + ticket.getCreatedAt() +
                            " | Title: " + ticket.getTitle()
            );
        }
    }
}