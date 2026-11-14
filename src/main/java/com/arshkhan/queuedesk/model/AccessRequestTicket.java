package com.arshkhan.queuedesk.model;

import com.arshkhan.queuedesk.interfaces.Escalatable;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AccessRequestTicket extends Ticket implements Escalatable {

    private String systemName;
    private String accessLevel;

    private int escalationLevel = 0;

    public AccessRequestTicket(String title,
                               String description,
                               String requester,
                               Priority priority,
                               String systemName,
                               String accessLevel) {

        super(title, description, requester, priority);

        this.systemName = systemName;
        this.accessLevel = accessLevel;
    }
    //No Argument Constructor
    public AccessRequestTicket() {
        super();
    }

    public String getSystemName() {
        return systemName;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    @Override
    public double estimateEffortHours() {

        if (accessLevel.equalsIgnoreCase("ADMIN")) {
            return 4.0;
        }

        if (accessLevel.equalsIgnoreCase("WRITE")) {
            return 2.0;
        }

        return 1.0;
    }

    @Override
    public void escalate() {
        escalationLevel++;
    }

    @Override
    @JsonIgnore
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
                ", System: " + systemName +
                ", Access Level: " + accessLevel;
    }
}