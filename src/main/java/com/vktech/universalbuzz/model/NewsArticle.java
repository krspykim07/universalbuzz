package com.vktech.universalbuzz.model;

import java.time.LocalDate;

public class NewsArticle {
    private Long id;
    private String title;
    private LocalDate publishDate;
    private String author;
    private String summary;
    private String content;
    private String imageUrl;
    private boolean builtIn;

    public NewsArticle() {
    }

    public NewsArticle(Long id, String title, LocalDate publishDate, String author, String summary, String content, String imageUrl, boolean builtIn) {
        this.id = id;
        this.title = title;
        this.publishDate = publishDate;
        this.author = author;
        this.summary = summary;
        this.content = content;
        this.imageUrl = imageUrl;
        this.builtIn = builtIn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isBuiltIn() {
        return builtIn;
    }

    public void setBuiltIn(boolean builtIn) {
        this.builtIn = builtIn;
    }
}
