package com.example.gamenet;

public class Card {
    private String title;
    private String description;
    private String gameId; // Add this field

    public Card() {
        // Default constructor
    }

    public Card(String title, String description, String gameId) {
        this.title = title;
        this.description = description;
        this.gameId = gameId;
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

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
