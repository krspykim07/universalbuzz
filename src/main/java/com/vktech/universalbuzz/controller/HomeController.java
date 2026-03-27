package com.vktech.universalbuzz.controller;

import com.vktech.universalbuzz.service.EventService;
import com.vktech.universalbuzz.service.NewsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final EventService eventService;
    private final NewsService newsService;

    public HomeController(EventService eventService, NewsService newsService) {
        this.eventService = eventService;
        this.newsService = newsService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("topEvents", eventService.getTopUpcomingEvents(3));
        model.addAttribute("topNews", newsService.getTopNews(3));
        return "dashboard";
    }
}