package com.cst2335.ticketmaster;

import org.json.JSONObject;

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
    private Integer tickeNum; //number of tickets
    private String isActive; //Y: active N: deleted

    public Events(String name, String type, String id, String url, String imgUrl, String startDate, String status, String city) {
        this(name, type, id, url, imgUrl, startDate, status, city, 0.0, 1, "Y");
    }

    public Events(String name, String type, String id, String url, String imgUrl, String startDate, String status, String city, Double price, Integer tickeNum, String isActive) {
        this.name = name;
        this.type = type;
        this.id = id;
        this.url = url;
        this.imgUrl = imgUrl;
        this.startDate = startDate;
        this.status = status;
        this.city = city;
        this.price = price;
        this.tickeNum = tickeNum;
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

}