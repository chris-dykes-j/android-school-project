package com.cst2335.ticketmaster;

import java.io.Serializable;

/**
 * This Events class is for testing purpose
 */
public class Events implements Serializable {
    private String name;
    private String type;
    private String id;
    private String url;
    private String imgUrl;
    private String startDate;
    private String status;
    private String city;
    private Double price;   // price for tickets
    private Integer ticketNum; //number of tickets
    private String isActive; //Y: active N: deleted

    public Events(String name, String type, String id, String url, String imgUrl, String startDate, String status, String city) {
        this(name, type, id, url, imgUrl, startDate, status, city, 0.0, 1, "Y");
    }

    public Events(String name, String type, String id, String url, String imgUrl, String startDate, String status, String city, Double price, Integer ticketNum, String isActive) {
        this.name = name;
        this.type = type;
        this.id = id;
        this.url = url;
        this.imgUrl = imgUrl;
        this.startDate = startDate;
        this.status = status;
        this.city = city;
        this.price = price;
        this.ticketNum = ticketNum;
        this.isActive = isActive;
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

    public String getCity() {
        return this.city;
    }

    public Double getPrice() { return this.price; }

    public Integer getTicketNum() { return this.ticketNum; }

    public String getIsActive() { return this.isActive; }

    /**
     * Creates EventBuilder to for creating an event.
     * @return EventBuilder for making events.
     */
    public static EventBuilder buildEvent() {
        return new EventBuilder();
    }

    /**
     * CEventBuilder to create a new event. Nicer than using constructor sometimes.
     */
    public static class EventBuilder {
        private String name;
        private String type;
        private String id;
        private String url;
        private String imgUrl;
        private String startDate;
        private String status;
        private String city;
        private Double price;
        private Integer ticketNum;
        private String isActive;

        public EventBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public EventBuilder setType(String type) {
            this.type = type;
            return this;
        }

        public EventBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public EventBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        public EventBuilder setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
            return this;
        }

        public EventBuilder setStartDate(String date) {
            this.startDate = date;
            return this;
        }

        public EventBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public EventBuilder setCity(String city) {
            this.city = city;
            return this;
        }

        public EventBuilder setPrice(Double price) {
            this.price = price;
            return this;
        }

        public EventBuilder setIsActive(String isActive) {
            this.isActive = isActive;
            return this;
        }

        public EventBuilder setTicketNum(Integer ticketNum) {
            this.ticketNum = ticketNum;
            return this;
        }
        public Events build() {
            return new Events(name, type, id, url, imgUrl, startDate, status, city, price, ticketNum, isActive);
        }
    }
}