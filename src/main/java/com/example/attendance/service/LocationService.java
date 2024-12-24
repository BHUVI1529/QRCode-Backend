package com.example.attendance.service;

import com.example.attendance.Util.LocationUtils;
import com.example.attendance.model.LocationData;
import com.example.attendance.model.LocationRequest;
import com.example.attendance.repository.LocationDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LocationService {

    @Autowired
    private LocationDataRepository locationDataRepository;

    private static final double OFFICE_LATITUDE = 12.9165171; // Example office location latitude
    private static final double OFFICE_LONGITUDE = 77.6014199; // Example office location longitude
    private static final double ALLOWED_RADIUS_M = 8000; // Allowed radius in meters (greater than the distance to test coordinates)

    public boolean isWithinAllowedRange(double userLat, double userLng) {
        double distance = LocationUtils.calculateDistance(OFFICE_LATITUDE, OFFICE_LONGITUDE, userLat, userLng);
        return distance <= ALLOWED_RADIUS_M / 1000; // Convert meters to kilometers for comparison
    }

    public void trackLocation(LocationRequest locationRequest) {
        boolean withinRange = isWithinAllowedRange(locationRequest.getLatitude(), locationRequest.getLongitude());
        if (withinRange) {
            LocationData locationData = new LocationData();
            locationData.setUserId(locationRequest.getUserId());
            
            locationData.setTimestamp(LocalDateTime.parse(locationRequest.getTimestamp()));

            locationDataRepository.save(locationData); // Save to the database if within range
        } else {
            // Optionally, log or handle cases where the user is outside the allowed range
            System.out.println("User is outside the allowed range. Location not stored.");
        }
    }

    public LocationData saveLocationData(LocationData locationData) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveLocationData'");
    }

}