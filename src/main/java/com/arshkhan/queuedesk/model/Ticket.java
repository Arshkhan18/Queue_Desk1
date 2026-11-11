package com.arshkhan.queuedesk.model;

import java.time.LocalDateTime;
import java.time.Duration;
public abstract class Ticket{
    private Integer id;
    private String title;
    private String description;
    private String requester;
    private Priority priority;
    private Status status;
    private LocalDateTime createdAt;

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

    // Setters
    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

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

    @Override
    public String toString() {
        return "Ticket ID: " + id +
                ", Title: " + title +
                ", Requester: " + requester +
                ", Priority: " + priority +
                ", Status: " + status;
    }
}