package com.example.attendance.Util;

public class LocationUtils {

    private static final double EARTH_RADIUS = 6371000; // Earth's radius in meters

    // Calculates the distance between two locations in meters
    public static double calculateDistance(double startLat, double startLng, double endLat, double endLng) {
        double latDistance = Math.toRadians(endLat - startLat);
        double lngDistance = Math.toRadians(endLng - startLng);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                 + Math.cos(Math.toRadians(startLat)) * Math.cos(Math.toRadians(endLat))
                 * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // returns the distance in meters
    }
}
