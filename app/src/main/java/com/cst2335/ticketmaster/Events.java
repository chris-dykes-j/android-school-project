package com.cst2335.ticketmaster;

/**
 * This Events class is for testing purpose
 */
public class Events {
    private String name;
    private String type;
    private String id;
    private String url;
    private String imgUrl;
    private String startDate;
    private String status;

    public Events(String name, String type, String id, String url, String imgUrl, String startDate, String status) {
        this.name = name;
        this.type = type;
        this.id = id;
        this.url = url;
        this.imgUrl = imgUrl;
        this.startDate = startDate;
        this.status = status;
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