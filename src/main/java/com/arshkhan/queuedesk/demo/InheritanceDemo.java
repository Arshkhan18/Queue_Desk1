//package com.arshkhan.queuedesk.demo;
//
//import com.arshkhan.queuedesk.model.*;
//
//import com.arshkhan.queuedesk.interfaces.Escalatable;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class InheritanceDemo {
//
//    public static void main(String[] args) {
//
//        BugTicket bug = new BugTicket(
//                "Login Failure",
//                "Users cannot login",
//                "Arsh",
//                Priority.CRITICAL,
//                Severity.CRITICAL,
//                "Enter credentials and click login"
//        );
//
//        FeatureRequestTicket feature = new FeatureRequestTicket(
//                "Dark Mode",
//                "Add dark mode to dashboard",
//                "Rahul",
//                Priority.MEDIUM,
//                10,
//                "Improves user experience"
//        );
//
//        AccessRequestTicket access = new AccessRequestTicket(
//                "Database Access",
//                "Need database access",
//                "Aman",
//                Priority.HIGH,
//                "Company Database",
//                "ADMIN"
//        );
//
//        // Escalating tickets
//        bug.escalate();
//        access.escalate();
//
//        // Polymorphism
//        List<Ticket> tickets = new ArrayList<>();
//
//        tickets.add(bug);
//        tickets.add(feature);
//        tickets.add(access);
//
//        printTriageBoard(tickets);
//    }
//
//    public static void printTriageBoard(List<Ticket> tickets) {
//
//        System.out.println("========== QUEUEDESK TRIAGE BOARD ==========");
//
//        for (Ticket ticket : tickets) {
//
//            System.out.println("\n" + ticket);
//
//            System.out.println(
//                    "Estimated Effort: " +
//                            ticket.estimateEffortHours() + " hours"
//            );
//
//            if (ticket instanceof Escalatable) {
//
//                Escalatable escalatable = (Escalatable) ticket;
//
//                System.out.println(
//                        "Escalation: " +
//                                escalatable.escalationBadge()
//                );
//
//                System.out.println(
//                        "Escalation Level: " +
//                                escalatable.escalationLevel()
//                );
//            }
//        }
//    }
//}