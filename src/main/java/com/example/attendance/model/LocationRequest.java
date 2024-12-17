package com.example.attendance.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LocationRequest {
    
    @JsonProperty("userId")
    private String userId;

    @JsonProperty("latitude")
    private Double latitude;

    @JsonProperty("longitude")
    private Double longitude;

    @JsonProperty("timestamp")
    private String timestamp;

    // Default constructor
    public LocationRequest() {
    }

    // Parameterized constructor
    public LocationRequest(String userId, Double latitude, Double longitude, String timestamp) {
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
   
}
