package com.arshkhan.queuedesk.demo;

import com.arshkhan.queuedesk.exception.TicketNotFoundException;
import com.arshkhan.queuedesk.model.*;
import com.arshkhan.queuedesk.repository.Repository;

import java.util.List;
import java.util.Map;

public class RepositoryDemo {

    public static void main(String[] args) {

        // 1. CREATE REPOSITORY

        Repository<Ticket> repository = new Repository<>();


        // 2. CREATE DIFFERENT TICKETS

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


        // 3. ASSIGN TICKETS

        t1.setAssignee("Rahul");
        t2.setAssignee("Arsh");
        t3.setAssignee("Rahul");

        // t4 intentionally remains unassigned


        // 4. ADD TICKETS TO REPOSITORY

        repository.add(t1);
        repository.add(t2);
        repository.add(t3);
        repository.add(t4);


        // 5. DISPLAY ALL TICKETS

        System.out.println("========== ALL TICKETS ==========");

        for (Ticket ticket : repository.all()) {

            System.out.println(
                    "ID: " + ticket.getId() +
                            " | Title: " + ticket.getTitle() +
                            " | Priority: " + ticket.getPriority()
            );
        }


        // 6. FIND TICKET BY ID

        System.out.println("\n========== FIND TICKET BY ID ==========");

        try{
            int searchId = t3.getId();

            Ticket foundTicket = repository.findById(searchId);


            System.out.println(
                    "Found Ticket: " +
                            foundTicket.getTitle() +
                            " | Priority: " +
                            foundTicket.getPriority()
            );
        }
         catch(TicketNotFoundException e){
            System.out.println("Ticket not found!");
        }


        // 7. GROUP TICKETS BY ASSIGNEE

        System.out.println("\n========== GROUP BY ASSIGNEE ==========");

        Map<String, List<Ticket>> groupedTickets =
                repository.groupByAssignee();

        for (String assignee : groupedTickets.keySet()) {

            System.out.println("\nAssignee: " + assignee);

            for (Ticket ticket : groupedTickets.get(assignee)) {

                System.out.println(
                        "Ticket #" + ticket.getId() +
                                " | " + ticket.getTitle()
                );
            }
        }


        // 8. FIND OLDEST TICKET

        System.out.println("\n========== OLDEST TICKET ==========");

        List<Ticket> allTickets = repository.all();

        Ticket oldestTicket = Repository.oldest(allTickets);

        if (oldestTicket != null) {

            System.out.println(
                    "Oldest Ticket: " + oldestTicket.getTitle() +
                            " | ID: " + oldestTicket.getId() +
                            " | Created: " + oldestTicket.getCreatedAt()
            );

        } else {
            System.out.println("No tickets available.");
        }

        // 9. EFFORT ESTIMATION REPORT

        System.out.println("\n========== EFFORT ESTIMATION ==========");

        double totalEffort = 0;

        for (Ticket ticket : repository.all()) {

            double effort = ticket.estimateEffortHours();

            System.out.println(
                    "Ticket #" + ticket.getId() +
                            " | Title: " + ticket.getTitle() +
                            " | Estimated Effort: " + effort + " hours"
            );

            totalEffort += effort;
        }

        System.out.println("\nTotal Estimated Effort: " + totalEffort + " hours");
    }
}