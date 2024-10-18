package com.example.gamenet;

public class Post {
    private String username;
    private String title;
    private String description;
    private String imageUrl;

    // Default constructor
    public Post() {
    }

    public Post(String username, String title, String description, String imageUrl) {
        this.username = username;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
