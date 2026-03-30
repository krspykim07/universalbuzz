package com.vktech.universalbuzz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vktech.universalbuzz.model.Event;
import com.vktech.universalbuzz.model.User;
import com.vktech.universalbuzz.repository.UserRepository;
import com.vktech.universalbuzz.service.EventService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class EventController {

    private final EventService eventService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public EventController(EventService eventService,
                           UserRepository userRepository,
                           ObjectMapper objectMapper) {
        this.eventService = eventService;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/events")
    public String eventsPage(Model model) throws Exception {
        var publishedEvents = eventService.getPublishedEvents();

        var eventPayload = publishedEvents.stream().map(event -> {
            java.util.Map<String, Object> map = new java.util.HashMap<String, Object>();
            map.put("id", event.getId());
            map.put("title", event.getTitle());
            map.put("description", event.getDescription());
            map.put("imageUrl", event.getImageUrl());
            map.put("location", event.getLocation());
            map.put("organizerName", event.getOrganizerName());
            map.put("startTime", event.getStartTime() != null ? event.getStartTime().toString() : null);
            map.put("endTime", event.getEndTime() != null ? event.getEndTime().toString() : null);
            map.put("status", event.getStatus() != null ? event.getStatus().name() : null);
            return map;
        }).toList();

        String eventsJson = objectMapper.writeValueAsString(eventPayload);

        model.addAttribute("eventsJson", eventsJson);
        return "events";
    }

    @GetMapping("/admin/events")
    public String adminEventsPage(Model model) {
        model.addAttribute("events", eventService.getAllEvents());
        return "admin-events";
    }

    @GetMapping("/admin/events/new")
    public String newEventPage(Model model) {
        model.addAttribute("event", new Event());
        model.addAttribute("formMode", "create");
        return "admin-event-form";
    }

    @PostMapping("/admin/events")
    public String addEvent(Event event,
                           @AuthenticationPrincipal UserDetails userDetails) {

        User createdBy = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        eventService.createEvent(event, createdBy);
        return "redirect:/admin/events";
    }

    @GetMapping("/admin/events/edit/{id}")
    public String editEventPage(@PathVariable Long id, Model model) {
        Event event = eventService.getEventById(id);
        model.addAttribute("event", event);
        model.addAttribute("formMode", "edit");
        return "admin-event-form";
    }

    @PostMapping("/admin/events/update/{id}")
    public String updateEvent(@PathVariable Long id, Event event) {
        eventService.updateEvent(id, event);
        return "redirect:/admin/events";
    }

    @PostMapping("/admin/events/publish/{id}")
    public String publishEvent(@PathVariable Long id) {
        eventService.publishEvent(id);
        return "redirect:/admin/events";
    }

    @PostMapping("/admin/events/unpublish/{id}")
    public String unpublishEvent(@PathVariable Long id) {
        eventService.unpublishEvent(id);
        return "redirect:/admin/events";
    }

    @PostMapping("/admin/events/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return "redirect:/admin/events";
    }
}