package com.vktech.universalbuzz.model;

public class Event {
    private Long id;
    private String title;
    private String date;
    private String location;
    private String description;

    public Event() {
    }

    public Event(Long id, String title, String date, String location, String description) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.location = location;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}