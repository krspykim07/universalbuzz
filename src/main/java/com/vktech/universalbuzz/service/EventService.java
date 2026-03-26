package com.vktech.universalbuzz.service;

import com.vktech.universalbuzz.model.Event;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class EventService {

    private final List<Event> events = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong(1);

    public EventService() {
        events.add(new Event(
                counter.getAndIncrement(),
                "International Welcome Day",
                "March 30, 2026",
                "Student Union",
                "Meet fellow international students and learn about campus resources."
        ));

        events.add(new Event(
                counter.getAndIncrement(),
                "Career Workshop",
                "April 5, 2026",
                "Library Hall",
                "A workshop focused on resumes, internships, and career readiness."
        ));
    }

    public List<Event> getAllEvents() {
        return events;
    }

    public void addEvent(Event event) {
        event.setId(counter.getAndIncrement());
        events.add(event);
    }
}