package com.cst2335.ticketmaster;

public class Event {
    private String name;
    private String url;
    private String imageUrl;
    private String genre;

    public Event(String name, String url, String imageUrl, String genre) {
        this.name = name;
        this.url = url;
        this.imageUrl = imageUrl;
        this.genre = genre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
