package com.arshkhan.queuedesk.model;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

//Creating For json conversion of Java Object
//Identifying the Type of Ticket Raised
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonAutoDetect;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BugTicket.class, name = "bug"),
        @JsonSubTypes.Type(value = FeatureRequestTicket.class, name = "feature"),
        @JsonSubTypes.Type(value = AccessRequestTicket.class, name = "access")
})
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public abstract class Ticket implements Comparable<Ticket> {
    private Integer id;
    private String title;
    private String description;
    private String requester;
    private Priority priority;
    private Status status;
    private LocalDateTime createdAt;

    //phase 4 part
    //Adding assignee
    private String assignee;
    private Set<String> tags = new HashSet<>();


    private static Integer nextId = 1;

    // Full constructor
    public Ticket(String title, String description,
                  String requester, Priority priority) {

        this.id = nextId++;
        this.title = title;
        this.description = description;
        this.requester = requester;
        this.priority = priority;
        this.status = Status.OPEN;
        this.createdAt = LocalDateTime.now();

    }

    // Short constructor
    public Ticket(String title, String requester) {

        this(title, "No description provided",
                requester, Priority.MEDIUM);

    }
    //No argument constructor for jackson conversion
    protected Ticket() {
    }

    //Abstract Methods
    public abstract double estimateEffortHours();

    // Getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getRequester() {
        return requester;
    }

    public Priority getPriority() {
        return priority;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    //assignee
    public String getAssignee() {return assignee;}

    public Set<String> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    // Setters
    public void setDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }

        this.description = description.trim();
    }


    public void setPriority(Priority priority) {
        if (priority == null) {
            throw new IllegalArgumentException("Priority cannot be null");
        }

        this.priority = priority;
    }

    public void setStatus(Status status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }

        this.status = status;
    }

    //assignee
    public void setAssignee(String assignee) {
        if (assignee == null || assignee.isBlank()) {
            this.assignee = null;
        } else {
            this.assignee = assignee.trim();
        }
    }
    public void addTag(String tag){ tags.add(tag);}

    // Calculate ticket age
    public double ageInHours() {
        Duration duration = Duration.between(
                createdAt,
                LocalDateTime.now()
        );

        return duration.toMillis() / (1000.0 * 60 * 60);
    }

    // Total tickets created
    public static int totalCreated() {
        return nextId - 1;
    }

    //Return the highest Ticket created to resolve during the assigning ticket id for the json
    public static void updateNextId(int highestExistingId) {
        if (nextId <= highestExistingId) {
            nextId = highestExistingId + 1;
        }
    }

    //Implementing Comparable Interface
    @Override
    public int compareTo(Ticket other) {

        // Higher priority should come first
        int priorityComparison =
                other.getPriority().compareTo(this.getPriority());

        if (priorityComparison != 0) {
            return priorityComparison;
        }

        // Older ticket should come first
        return this.createdAt.compareTo(other.createdAt);
    }

    //Shorter Description
    public String getShortDescription(int maxLength) {
        if (maxLength <= 0) {
            throw new IllegalArgumentException(
                    "Maximum length must be positive"
            );
        }

        if (description.length() <= maxLength) {
            return description;
        }

        if (maxLength <= 3) {
            return description.substring(0, maxLength);
        }

        return description.substring(0, maxLength - 3) + "...";
    }
    @Override
    public String toString() {
        return "Ticket ID: " + id +
                ", Title: " + title +
                ", Requester: " + requester +
                ", Priority: " + priority +
                ", Status: " + status +
                ", Assignee: " + assignee +
                ", Tags: " + tags;
    }
}