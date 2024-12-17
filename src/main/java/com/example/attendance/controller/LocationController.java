package com.example.attendance.controller;

import com.example.attendance.model.LocationData;
import com.example.attendance.model.LocationRequest;
import com.example.attendance.service.LocationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LocationController {

    @PostMapping("/api/location/track")
    public ResponseEntity<String> trackLocation(@RequestBody LocationRequest locationRequest) {
        // Log or process the received location data
        System.out.println("User ID: " + locationRequest.getUserId());
        System.out.println("Latitude: " + locationRequest.getLatitude());
        System.out.println("Longitude: " + locationRequest.getLongitude());
        System.out.println("Timestamp: " + locationRequest.getTimestamp());

        // Here you can add logic to handle the location data, e.g., save to database

        return ResponseEntity.ok("Location tracked successfully");
    }
    @Autowired
    private LocationService locationService;

    @PostMapping
    public LocationData trackLocation(@RequestBody LocationData locationData) {
        return locationService.saveLocationData(locationData);
    }
}