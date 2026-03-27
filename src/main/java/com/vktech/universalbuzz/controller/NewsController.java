package com.vktech.universalbuzz.controller;

import com.vktech.universalbuzz.model.NewsArticle;
import com.vktech.universalbuzz.service.NewsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/news")
    public String newsPage(Model model) {
        model.addAttribute("newsList", newsService.getAllNews());
        return "news";
    }

    @GetMapping("/news/{id}")
    public String newsDetailPage(@PathVariable Long id, Model model) {
        NewsArticle newsArticle = newsService.getNewsById(id);
        if (newsArticle == null) {
            return "redirect:/news";
        }

        model.addAttribute("news", newsArticle);
        model.addAttribute("latestNews", newsService.getTopNews(6));
        return "news-detail";
    }

    @GetMapping("/admin/news")
    public String adminNewsPage(Model model) {
        model.addAttribute("newsList", newsService.getAllNews());
        return "admin-news";
    }

    @GetMapping("/admin/news/new")
    public String newNewsPage(Model model) {
        model.addAttribute("news", new NewsArticle());
        model.addAttribute("formMode", "create");
        return "admin-news-form";
    }

    @PostMapping("/admin/news")
    public String addNews(NewsArticle newsArticle) {
        newsService.addNews(newsArticle);
        return "redirect:/admin/news";
    }

    @GetMapping("/admin/news/edit/{id}")
    public String editNewsPage(@PathVariable Long id, Model model) {
        NewsArticle newsArticle = newsService.getNewsById(id);
        if (newsArticle == null) {
            return "redirect:/admin/news";
        }

        model.addAttribute("news", newsArticle);
        model.addAttribute("formMode", "edit");
        return "admin-news-form";
    }

    @PostMapping("/admin/news/update/{id}")
    public String updateNews(@PathVariable Long id, NewsArticle newsArticle) {
        newsService.updateNews(id, newsArticle);
        return "redirect:/admin/news";
    }

    @PostMapping("/admin/news/delete/{id}")
    public String deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return "redirect:/admin/news";
    }
}
