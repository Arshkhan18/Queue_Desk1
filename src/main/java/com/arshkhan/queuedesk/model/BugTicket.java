package com.arshkhan.queuedesk.model;

import com.arshkhan.queuedesk.interfaces.Escalatable;

public class BugTicket extends Ticket implements Escalatable {
    private Severity severity;
    private String stepsToReproduce;

    private int escalationLevel=0;


    public BugTicket(String title,
                     String description,
                     String requester,
                     Priority priority,
                     Severity severity,
                     String stepsToReproduce) {

        super(title, description, requester, priority);

        this.severity = severity;
        this.stepsToReproduce = stepsToReproduce;
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getStepsToReproduce() {
        return stepsToReproduce;
    }

    @Override
    public double estimateEffortHours() {

        switch (severity) {
            case LOW:
                return 1.0;

            case MEDIUM:
                return 2.0;

            case HIGH:
                return 4.0;

            case CRITICAL:
                return 8.0;

            default:
                return 1.0;
        }
    }

    @Override
    public void escalate() {
        escalationLevel++;
    }

    @Override
    public boolean isEscalated() {
        return escalationLevel > 0;
    }

    @Override
    public int escalationLevel() {
        return escalationLevel;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", Severity: " + severity +
                ", Steps: " + stepsToReproduce;
    }


}