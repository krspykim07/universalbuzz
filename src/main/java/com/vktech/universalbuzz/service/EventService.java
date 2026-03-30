package com.vktech.universalbuzz.service;

import com.vktech.universalbuzz.model.Event;
import com.vktech.universalbuzz.model.EventStatus;
import com.vktech.universalbuzz.model.User;
import com.vktech.universalbuzz.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getPublishedEvents() {
        return eventRepository.findByStatusAndStartTimeGreaterThanEqualOrderByStartTimeAsc(
                EventStatus.PUBLISHED,
                LocalDateTime.now()
        );
    }

    public List<Event> getTopUpcomingEvents(int limit) {
        return getPublishedEvents().stream()
                .limit(limit)
                .toList();
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + id));
    }

    public Event createEvent(Event event, User createdBy) {
        event.setCreatedBy(createdBy);
        return eventRepository.save(event);
    }

    public Event updateEvent(Long id, Event updatedEvent) {
        Event existing = getEventById(id);

        if (updatedEvent.getTitle() != null && !updatedEvent.getTitle().isBlank()) {
            existing.setTitle(updatedEvent.getTitle());
        }

        if (updatedEvent.getDescription() != null && !updatedEvent.getDescription().isBlank()) {
            existing.setDescription(updatedEvent.getDescription());
        }

        if (updatedEvent.getImageUrl() != null && !updatedEvent.getImageUrl().isBlank()) {
            existing.setImageUrl(updatedEvent.getImageUrl());
        }

        if (updatedEvent.getLocation() != null && !updatedEvent.getLocation().isBlank()) {
            existing.setLocation(updatedEvent.getLocation());
        }

        if (updatedEvent.getOrganizerName() != null && !updatedEvent.getOrganizerName().isBlank()) {
            existing.setOrganizerName(updatedEvent.getOrganizerName());
        }

        if (updatedEvent.getStartTime() != null) {
            existing.setStartTime(updatedEvent.getStartTime());
        }

        if (updatedEvent.getEndTime() != null) {
            existing.setEndTime(updatedEvent.getEndTime());
        }

        if (updatedEvent.getStatus() != null) {
            existing.setStatus(updatedEvent.getStatus());
        }

        return eventRepository.save(existing);
    }

    public Event publishEvent(Long id) {
        Event event = getEventById(id);
        event.setStatus(EventStatus.PUBLISHED);
        return eventRepository.save(event);
    }

    public Event unpublishEvent(Long id) {
        Event event = getEventById(id);
        event.setStatus(EventStatus.DRAFT);
        return eventRepository.save(event);
    }

    public Event saveDraft(Event event, User createdBy) {
        event.setStatus(EventStatus.DRAFT);
        event.setCreatedBy(createdBy);
        return eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}