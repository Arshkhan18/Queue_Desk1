package com.arshkhan.queuedesk.analytics;

import com.arshkhan.queuedesk.model.Ticket;
import com.arshkhan.queuedesk.model.Priority;
import java.util.Comparator;
import java.util.Optional;
import com.arshkhan.queuedesk.model.Status;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Locale;


public class TicketAnalytics {
    //Counting By Priority
    public static Map<Priority, Long> countByPriority(List<Ticket> tickets) {

        return tickets.stream()
                .collect(Collectors.groupingBy(
                        Ticket::getPriority,
                        Collectors.counting()
                ));
    }
    //Group By Assignee
    public static Map<String, List<Ticket>> groupByAssignee(List<Ticket> tickets) {

        return tickets.stream()
                .collect(Collectors.groupingBy(Ticket::getAssignee, Collectors.toList()));
    }

    //Estimating EffortHours
    public static double averageEffortHours(List<Ticket> tickets) {

        return tickets.stream()
                .mapToDouble(Ticket :: estimateEffortHours)
                .average()
                .orElse(0.0);
    }

    //Calculating EffortHour
    public static double totalEffortHours(List<Ticket> tickets) {

        return tickets.stream()
                .mapToDouble(Ticket::estimateEffortHours)
                .reduce(0.0, Double::sum);
    }

    //Comparision to get the Oldest Ticket
    public static Optional<Ticket> oldestOpenTicket(List<Ticket> tickets) {

            return tickets.stream()
                    .filter(ticket ->ticket.getStatus()==Status.OPEN)
                    .min(Comparator.comparing(Ticket::getCreatedAt));
    }

    //Get Distinct Ticket tags
    public static String tagLine(List<Ticket> tickets) {

        return tickets.stream()
                .flatMap(ticket -> ticket.getTags().stream())
                .map(tag -> tag.toLowerCase(Locale.ROOT))
                .distinct()
                .sorted()
                .collect(Collectors.joining(", "));
    }
}
