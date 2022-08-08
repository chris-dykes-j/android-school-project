package com.cst2335.ticketmaster;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * The Event class, with all the information about various events.
 */
public class Events implements Serializable {
    private final String name;
    private final String type;
    private final String id;
    private final String url;
    private final String imgUrl;
    private final String startDate;
    private final String status;
    private final String city;
    private final Double price;   // price for tickets
    private final Integer ticketNum; //number of tickets
    private final String isActive; //Y: active N: deleted

    /**
     * Constructor for the Event Class
     * @param name Event name.
     * @param type Event type (music, sports).
     * @param id Event's id.
     * @param url Url of the event.
     * @param imgUrl Event's image and branding.
     * @param startDate When the event starts.
     * @param status Event status
     * @param city Event city.
     */
    public Events(String name, String type, String id, String url, String imgUrl, String startDate, String status, String city) {
        this(name, type, id, url, imgUrl, startDate, status, city, 0.0, 1, "Y");
    }

    /**
     * Bigger constructor for the Event Class
     * @param name Event name.
     * @param type Event type (music, sports).
     * @param id Event's id.
     * @param url Url of the event.
     * @param imgUrl Event's image and branding.
     * @param startDate When the event starts.
     * @param status Event status
     * @param city Event city.
     * @param price Event cost
     * @param ticketNum The ticket number
     * @param isActive Is the event active or not.
     */
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

    /**
     * Get's event name.
     * @return Event name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get's event type.
     * @return Event type.
     */
    public String getType() {
        return this.type;
    }

    /**
     * Get's event id.
     * @return Event id.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Get's event url.
     * @return Event url.
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * Get's event imgUrl.
     * @return Event imgUrl.
     */
    public String getImgUrl() {
        return this.imgUrl;
    }

    /**
     * Get's event date.
     * @return Event date, as string.
     */
    public String getStartDate() {
        return this.startDate;
    }

    /**
     * Get's event status.
     * @return Event status.
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Get's event city
     * @return Event city.
     */
    public String getCity() {
        return this.city;
    }

    /**
     * Get's event price.
     * @return Event price.
     */
    public Double getPrice() { return this.price; }

    // Let's pretend this isn't here.
    // public Integer getTicketNum() { return this.ticketNum; }

    /**
     * Get's event activity (active or not, Y or N)
     * @return Return Y or N.
     */
    public String getIsActive() { return this.isActive; }

    /**
     * Creates EventBuilder to for creating an event.
     * @return EventBuilder for making events.
     */
    public static EventBuilder buildEvent() {
        return new EventBuilder();
    }

    /**
     * EventBuilder to create a new event. Nicer than using constructor sometimes.
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

        /**
         * Set event name
         * @param name Name to be set
         * @return EventBuilder; it keeps going.
         */
        public EventBuilder setName(String name) {
            this.name = name;
            return this;
        }

        /**
         * Set event type
         * @param type String to be set
         * @return EventBuilder; it keeps going.
         */
        public EventBuilder setType(String type) {
            this.type = type;
            return this;
        }

        /**
         * Set event id
         * @param id String to be set
         * @return EventBuilder; it keeps going.
         */
        public EventBuilder setId(String id) {
            this.id = id;
            return this;
        }

        /**
         * Set event url
         * @param url String to be set
         * @return EventBuilder; ain't done yet
         */
        public EventBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        /**
         * Set event imgUrl
         * @param imgUrl String to be set
         * @return EventBuilder; it never ends.
         */
        public EventBuilder setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
            return this;
        }

        /**
         * Set event date
         * @param date String to be set.
         * @return EventBuilder; it never ends.
         */
        public EventBuilder setStartDate(String date) {
            this.startDate = date;
            return this;
        }

        /**
         * Set event status
         * @param status String to be set.
         * @return EventBuilder; it never ends.
         */
        public EventBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        /**
         * Set event city
         * @param city String to be set.
         * @return EventBuilder; it never ends.
         */
        public EventBuilder setCity(String city) {
            this.city = city;
            return this;
        }

        /**
         * Set event price
         * @param price String to be set.
         * @return EventBuilder; it never ends.
         */
        public EventBuilder setPrice(Double price) {
            this.price = price;
            return this;
        }

        /**
         * Set event activity (active or not)
         * @param isActive String to be set.
         * @return EventBuilder; it never ends.
         */
        public EventBuilder setIsActive(String isActive) {
            this.isActive = isActive;
            return this;
        }

        /**
         * Set ticket number
         * @param ticketNum String to be set.
         * @return EventBuilder; almost done...
         */
        public EventBuilder setTicketNum(Integer ticketNum) {
            this.ticketNum = ticketNum;
            return this;
        }

        /**
         * Creates the event.
         * @return Finally, creates the event and gives it for use.
         */
        public Events build() {
            return new Events(name, type, id, url, imgUrl, startDate, status, city, price, ticketNum, isActive);
        }
    }

    /**
     * Event to String method, mostly for debugging.
     * @return String with event info.
     */
    @NonNull
    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s",
                name, type, id, url, imgUrl, startDate, status, city, price, ticketNum, isActive);
    }

}