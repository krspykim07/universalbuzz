package com.vktech.universalbuzz.controller;

import com.vktech.universalbuzz.model.Event;
import com.vktech.universalbuzz.service.EventService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events")
    public String eventsPage(Model model) {
        model.addAttribute("events", eventService.getAllEvents());
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
        return "admin-event-form";
    }

    @PostMapping("/admin/events")
    public String addEvent(Event event) {
        eventService.addEvent(event);
        return "redirect:/admin/events";
    }
}