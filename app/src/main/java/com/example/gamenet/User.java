package com.example.gamenet;

public class User {

    private String id;
    private String username;
    private String photoUrl; // Add this field

    public User() {
    }

    public User(String id, String username, String photoUrl) {
        this.id = id;
        this.username = username;
        this.photoUrl = photoUrl; // Initialize this field
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhotoUrl() { // Add this getter
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) { // Add this setter
        this.photoUrl = photoUrl;
    }
}
