package com.arshkhan.queuedesk.repository;

import com.arshkhan.queuedesk.model.Ticket;
import com.arshkhan.queuedesk.exception.TicketNotFoundException;
import java.util.*;

public class Repository<T extends Ticket>{

    private List<T> tickets = new ArrayList<>();

    //Add a ticket
    public void add(T t) {
        tickets.add(t);
    }

    //Return an unmodifiable view
    public List<T> all() {
        return Collections.unmodifiableList(tickets);
    }

    //Find ticket by ID
    public T findById(int id) {
        for (T t : tickets) {
            if (t.getId() == id) {
                return t;
            }
        }
//        return null;
        //throw ticketNotfound Exception
        throw new TicketNotFoundException(
                "Ticket with ID " + id + " not found."
        );
    }


    //Find Ticket By ID using streams
    //Throwing Optional Instead of Exception
    
    public Optional<T>findByIdSafe(int id){
        return tickets.stream()
                .filter(ticket -> ticket.getId()==id)
                .findFirst();
    }



    //Group Ticket by Assignee and return
    public Map<String, List<T>> groupByAssignee() {
        Map<String, List<T>> groupedTickets = new HashMap<>();

        for (T t : tickets) {
            String assignee = t.getAssignee();
            if (assignee == null || assignee.isBlank()) {
                assignee = "Unassigned";
            }
            groupedTickets.computeIfAbsent(assignee, k -> new ArrayList<>()).add(t);

        }
        return groupedTickets;
    }

    //Return the Oldest ticket
    public static <T extends Ticket>T oldest(List<T> tickets) {
        T oldestTicket = null;
        for(T t : tickets) {
            if(oldestTicket == null || t.getCreatedAt().isBefore(oldestTicket.getCreatedAt())) {
                oldestTicket = t;
            }
        }
        return oldestTicket;
    }
}