package com.cst2335.ticketmaster;

public class Event {

    private String name;
    private String type;
    private String id;
    private String url;
    private String imgUrl;
    private String startDate;
    private String status;
    private String genre;

    // Just for now, testing
    public Event(String name, String url, String genre) {
        this.name = name;
        this.url = url;
        this.genre = genre;
    }

    // from main branch
    public Event(String name, String type, String id, String url, String imgUrl, String startDate, String status, String genre) {
        this.name = name;
        this.type = type;
        this.id = id;
        this.url = url;
        this.imgUrl = imgUrl;
        this.startDate = startDate;
        this.status = status;
        this.genre = genre;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public String getId() {
        return this.id;
    }

    public String getUrl() {
        return this.url;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public String getStatus() {
        return this.status;
    }
}
