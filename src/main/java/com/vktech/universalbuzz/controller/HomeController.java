package com.vktech.universalbuzz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vktech.universalbuzz.service.EventService;
import com.vktech.universalbuzz.service.NewsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final EventService eventService;
    private final NewsService newsService;
    private final ObjectMapper objectMapper;

    public HomeController(EventService eventService,
                          NewsService newsService,
                          ObjectMapper objectMapper) {
        this.eventService = eventService;
        this.newsService = newsService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) throws Exception {
        var topEvents = eventService.getTopUpcomingEvents(9);

        var eventPayload = topEvents.stream().map(event -> {
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

        String topEventsJson = objectMapper.writeValueAsString(eventPayload);

        model.addAttribute("topEventsJson", topEventsJson);
        model.addAttribute("topNews", newsService.getTopNews(3));
        return "dashboard";
    }
}