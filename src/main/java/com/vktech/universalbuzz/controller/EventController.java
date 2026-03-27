package com.vktech.universalbuzz.controller;

import com.vktech.universalbuzz.model.Event;
import com.vktech.universalbuzz.service.EventService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events")
    public String eventsPage(Model model) {
        model.addAttribute("events", eventService.getUpcomingEvents());
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
    public String addEvent(Event event) {
        eventService.addEvent(event);
        return "redirect:/admin/events";
    }

    @GetMapping("/admin/events/edit/{id}")
    public String editEventPage(@PathVariable Long id, Model model) {
        Event event = eventService.getEventById(id);
        if (event == null) {
            return "redirect:/admin/events";
        }

        model.addAttribute("event", event);
        model.addAttribute("formMode", "edit");
        return "admin-event-form";
    }

    @PostMapping("/admin/events/update/{id}")
    public String updateEvent(@PathVariable Long id, Event event) {
        eventService.updateEvent(id, event);
        return "redirect:/admin/events";
    }

    @PostMapping("/admin/events/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return "redirect:/admin/events";
    }
}