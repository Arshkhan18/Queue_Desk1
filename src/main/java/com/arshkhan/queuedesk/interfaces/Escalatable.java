package com.arshkhan.queuedesk.interfaces;

public interface Escalatable {
    void escalate();

    boolean isEscalated();

    int escalationLevel();

    default String escalationBadge() {
        if (isEscalated()) {
            return "[ESCALATED - Level " + escalationLevel() + "]";
        }

        return "[NOT ESCALATED]";
    }
}