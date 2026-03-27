package com.vktech.universalbuzz.service;

import com.vktech.universalbuzz.model.Event;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
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
                LocalDate.of(2026, 3, 30),
                "10:00 AM",
                "Student Union",
                "Meet fellow international students and learn about campus resources.",
                true
        ));

        events.add(new Event(
                counter.getAndIncrement(),
                "Career Workshop",
                LocalDate.of(2026, 4, 5),
                "2:00 PM",
                "Library Hall",
                "A workshop focused on resumes, internships, and career readiness.",
                true
        ));
    }

    public List<Event> getAllEvents() {
        return events.stream()
                .sorted(Comparator.comparing(Event::getEventDate))
                .toList();
    }

    public List<Event> getUpcomingEvents() {
        LocalDate today = LocalDate.now();

        return events.stream()
                .filter(event -> !event.getEventDate().isBefore(today))
                .sorted(Comparator.comparing(Event::getEventDate))
                .toList();
    }

    public List<Event> getTopUpcomingEvents(int limit) {
        LocalDate today = LocalDate.now();

        return events.stream()
                .filter(event -> !event.getEventDate().isBefore(today))
                .sorted(Comparator.comparing(Event::getEventDate))
                .limit(limit)
                .toList();
    }

    public void addEvent(Event event) {
        event.setId(counter.getAndIncrement());
        event.setBuiltIn(false);

        if (event.getDescription() != null && event.getDescription().length() > 180) {
            event.setDescription(event.getDescription().substring(0, 180));
        }

        events.add(event);
    }

    public Event getEventById(Long id) {
        return events.stream()
                .filter(event -> event.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void updateEvent(Long id, Event updatedEvent) {
        Event existing = getEventById(id);
        if (existing != null) {
            existing.setTitle(updatedEvent.getTitle());
            existing.setEventDate(updatedEvent.getEventDate());
            existing.setEventTime(updatedEvent.getEventTime());
            existing.setLocation(updatedEvent.getLocation());

            String description = updatedEvent.getDescription();
            if (description != null && description.length() > 180) {
                description = description.substring(0, 180);
            }
            existing.setDescription(description);
        }
    }

    public void deleteEvent(Long id) {
        Event event = getEventById(id);
        if (event != null && !event.isBuiltIn()) {
            events.remove(event);
        }
    }
}