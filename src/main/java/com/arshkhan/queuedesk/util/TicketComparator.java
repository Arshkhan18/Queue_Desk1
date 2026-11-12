package com.arshkhan.queuedesk.util;

import com.arshkhan.queuedesk.model.Ticket;
import java.util.Comparator;


public class TicketComparator{
    //Sort by assigning then priority
    public static Comparator<Ticket> byAssigneeThenPriority(){
        return Comparator.comparing(
                Ticket::getAssignee,
                Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)
        ).thenComparing(
                Ticket::getPriority,
                Comparator.nullsLast(Comparator.reverseOrder())
        );

    }

    //Sorting by status and then by age
    public static Comparator<Ticket> byStatusThenAge() {
        return Comparator.comparing(Ticket::getStatus)
                .thenComparing(Ticket::getCreatedAt);
    }
}
