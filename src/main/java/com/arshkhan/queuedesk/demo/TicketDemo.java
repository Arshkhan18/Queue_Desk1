//package com.arshkhan.queuedesk.demo;
//
//import com.arshkhan.queuedesk.model.Ticket;
//import com.arshkhan.queuedesk.model.Priority;
//
//public class TicketDemo {
//
//    public static void main(String[] args) {
//
//        // Creating ticket using the full constructor
//        Ticket t1 = new Ticket(
//                "Login Issue",
//                "Unable to login to company portal",
//                "Arsh",
//                Priority.HIGH
//        );
//
//        // Creating ticket using the short constructor
//        Ticket t2 = new Ticket(
//                "Software Installation",
//                "Rahul"
//        );
//
//        // Displaying first ticket
//        System.out.println("----- Ticket 1 -----");
//        System.out.println("ID: " + t1.getId());
//        System.out.println("Title: " + t1.getTitle());
//        System.out.println("Description: " + t1.getDescription());
//        System.out.println("Requester: " + t1.getRequester());
//        System.out.println("Priority: " + t1.getPriority());
//        System.out.println("Status: " + t1.getStatus());
//
//        // Displaying second ticket
//        System.out.println("\n----- Ticket 2 -----");
//        System.out.println("ID: " + t2.getId());
//        System.out.println("Title: " + t2.getTitle());
//        System.out.println("Description: " + t2.getDescription());
//        System.out.println("Requester: " + t2.getRequester());
//        System.out.println("Priority: " + t2.getPriority());
//        System.out.println("Status: " + t2.getStatus());
//
//        // Total tickets created
//        System.out.println("\nTotal tickets created: " + Ticket.totalCreated());
//    }
//}