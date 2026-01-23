package com.krowdless.usersmangement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.krowdless.usersmangement.util.Point;

@Service
public class CrowdCalculationService {

    public String calculateCrowdLevel(
            java.math.BigDecimal latitude,
            java.math.BigDecimal longitude) {

        if (latitude == null || longitude == null) {
            return "UNKNOWN";
        }

        double lat = latitude.doubleValue();
        double lng = longitude.doubleValue();

        // 1️⃣ generate points
        List<Point> nearPoints = generateRingPoints(lat, lng, 0.05);
        List<Point> farPoints  = generateRingPoints(lat, lng, 0.15);

        Point destination = new Point(lat, lng);

        // 2️⃣ calculate delay ratios
        double nearScore = calculateAvgDelayRatio(nearPoints, destination);
        double farScore  = calculateAvgDelayRatio(farPoints, destination);

        // 3️⃣ weighted score
        double finalScore = nearScore * 0.6 + farScore * 0.4;

        // 4️⃣ decision
        if (finalScore < 1.2) return "LOW";
        if (finalScore < 1.7) return "MEDIUM";
        return "HIGH";
    }

    /* ================== helpers ================== */

    private List<Point> generateRingPoints(
            double lat, double lng, double delta) {

        return List.of(
            new Point(lat + delta, lng),
            new Point(lat - delta, lng),
            new Point(lat, lng + delta),
            new Point(lat, lng - delta)
        );
    }

private double calculateAvgDelayRatio(
        List<Point> origins,
        Point destination) {

    int hash = Math.abs(
        (destination.getLat() + "," + destination.getLng()).hashCode()
    );

    int mod = hash % 100;

    if (mod < 40) return 1.1;   // LOW
    if (mod < 75) return 1.4;   // MEDIUM
    return 1.9;                // HIGH
}


    
    // private double calculateAvgDelayRatio(
    //         List<Point> origins,
    //         Point destination) {

    //     try {
    //         String originsStr = origins.stream()
    //                 .map(p -> p.getLat() + "," + p.getLng())
    //                 .reduce((a, b) -> a + "|" + b)
    //                 .orElse("");

    //         String destStr =
    //                 destination.getLat() + "," + destination.getLng();

    //         String url =
    //             "https://maps.googleapis.com/maps/api/distancematrix/json" +
    //             "?origins=" + originsStr +
    //             "&destinations=" + destStr +
    //             "&departure_time=now" +
    //             "&key=" + apiKey;

    //         Map<?, ?> response =
    //                 restTemplate.getForObject(url, Map.class);

    //         List<Map<String, Object>> rows =
    //                 (List<Map<String, Object>>) response.get("rows");

    //         List<Double> ratios = new ArrayList<>();

    //         for (Map<String, Object> row : rows) {
    //             List<Map<String, Object>> elements =
    //                     (List<Map<String, Object>>) row.get("elements");

    //             Map<String, Object> el = elements.get(0);

    //             if (!"OK".equals(el.get("status"))) {
    //                 continue;
    //             }

    //             Map<String, Object> normal =
    //                     (Map<String, Object>) el.get("duration");
    //             Map<String, Object> traffic =
    //                     (Map<String, Object>) el.get("duration_in_traffic");

    //             double normalSec =
    //                     ((Number) normal.get("value")).doubleValue();
    //             double trafficSec =
    //                     ((Number) traffic.get("value")).doubleValue();

    //             ratios.add(trafficSec / normalSec);
    //         }

    //         return ratios.stream()
    //                 .mapToDouble(Double::doubleValue)
    //                 .average()
    //                 .orElse(1.0);

    //     } catch (Exception e) {
    //         // Google down / quota / network
    //         return 1.0; // fallback = LOW impact
    //     }
    // }

    
}
