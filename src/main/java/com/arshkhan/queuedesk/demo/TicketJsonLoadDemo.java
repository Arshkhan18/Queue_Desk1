package com.arshkhan.queuedesk.demo;


import com.arshkhan.queuedesk.io.TicketJsonStore;
import com.arshkhan.queuedesk.exception.InvalidTicketDataException;
import com.arshkhan.queuedesk.model.BugTicket;
import com.arshkhan.queuedesk.model.Ticket;
import com.arshkhan.queuedesk.model.Priority;
import com.arshkhan.queuedesk.model.Severity;

import java.nio.file.Path;
import java.util.List;
public class TicketJsonLoadDemo {

    public static void main(String[] args) {

        Path file = Path.of("tickets.json");

        try {
            List<Ticket> tickets = TicketJsonStore.load(file);

            System.out.println("Tickets loaded!");

            BugTicket newTicket = new BugTicket(
                    "Payment Failure",
                    "Payment is not processing",
                    "Arsh",
                    Priority.HIGH,
                    Severity.MEDIUM,
                    "Payment fails after clicking submit"
            );

            System.out.println("New ticket ID: " + newTicket.getId());

        } catch (InvalidTicketDataException e) {
            System.out.println(e.getMessage());
        }
    }
}