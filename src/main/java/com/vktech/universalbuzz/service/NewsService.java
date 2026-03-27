package com.vktech.universalbuzz.service;

import com.vktech.universalbuzz.model.NewsArticle;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class NewsService {

    private final List<NewsArticle> newsArticles = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong(1);

    public NewsService() {
        newsArticles.add(new NewsArticle(
                counter.getAndIncrement(),
                "Sac State, CapRadio partner to raise $100,000 for paid student internships",
                LocalDate.of(2026, 2, 25),
                "Jennifer K. Morita",
                "CapRadio, with University support, established a new fund dedicated to giving students hands-on experience at the radio station.",
                "Sac State student Jacob Garcia Rodriguez was always told he had a voice for radio, but the English major did not hear it until he interned at CapRadio. Through this collaboration, students can gain hands-on media experience while helping strengthen local public radio.",
                "https://www.csus.edu/news/files/2026/02/capradio-internship.jpg",
                true
        ));

        newsArticles.add(new NewsArticle(
                counter.getAndIncrement(),
                "Student innovators build robotic arm and web app to teach American Sign Language",
                LocalDate.of(2026, 2, 20),
                "Campus News Team",
                "A multidisciplinary student team designed assistive learning tools to support ASL education.",
                "A group of Sac State students developed a robotic arm prototype and companion web app to help children and families learn American Sign Language through interactive activities.",
                "https://www.csus.edu/news/files/2026/02/asl-innovation.jpg",
                true
        ));

        newsArticles.add(new NewsArticle(
                counter.getAndIncrement(),
                "Sac State earns Carnegie Community Engagement designation for fourth time",
                LocalDate.of(2026, 2, 14),
                "University Communications",
                "Sac State received national recognition for its continued commitment to community-engaged teaching and research.",
                "Carnegie's Community Engagement Classification recognizes colleges and universities that demonstrate excellent collaboration with local communities. This marks Sac State's fourth designation.",
                "https://www.csus.edu/news/files/2026/02/carnegie-community.jpg",
                true
        ));
    }

    public List<NewsArticle> getAllNews() {
        return newsArticles.stream()
                .sorted(Comparator.comparing(NewsArticle::getPublishDate).reversed())
                .toList();
    }

    public List<NewsArticle> getTopNews(int limit) {
        return newsArticles.stream()
                .sorted(Comparator.comparing(NewsArticle::getPublishDate).reversed())
                .limit(limit)
                .toList();
    }

    public NewsArticle getNewsById(Long id) {
        return newsArticles.stream()
                .filter(newsArticle -> newsArticle.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void addNews(NewsArticle newsArticle) {
        newsArticle.setId(counter.getAndIncrement());
        newsArticle.setBuiltIn(false);

        if (newsArticle.getSummary() != null && newsArticle.getSummary().length() > 220) {
            newsArticle.setSummary(newsArticle.getSummary().substring(0, 220));
        }

        newsArticles.add(newsArticle);
    }

    public void updateNews(Long id, NewsArticle updatedNewsArticle) {
        NewsArticle existing = getNewsById(id);
        if (existing != null) {
            existing.setTitle(updatedNewsArticle.getTitle());
            existing.setPublishDate(updatedNewsArticle.getPublishDate());
            existing.setAuthor(updatedNewsArticle.getAuthor());

            String summary = updatedNewsArticle.getSummary();
            if (summary != null && summary.length() > 220) {
                summary = summary.substring(0, 220);
            }

            existing.setSummary(summary);
            existing.setContent(updatedNewsArticle.getContent());
            existing.setImageUrl(updatedNewsArticle.getImageUrl());
        }
    }

    public void deleteNews(Long id) {
        NewsArticle newsArticle = getNewsById(id);
        if (newsArticle != null && !newsArticle.isBuiltIn()) {
            newsArticles.remove(newsArticle);
        }
    }
}
