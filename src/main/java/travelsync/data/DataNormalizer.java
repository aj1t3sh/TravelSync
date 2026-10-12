package travelsync.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Utility for string normalization, tag parsing, and geographic calculations.
 */
public class DataNormalizer {

    /**
     * Normalizes text for case-insensitive and punctuation-insensitive matching.
     */
    public static String normalizeText(String input) {
        if (input == null) return "";
        return input.toLowerCase(Locale.ROOT).trim();
    }

    /**
     * Splits comma-separated tags into normalized lowercase tokens.
     */
    public static List<String> parseTags(String tagsString) {
        List<String> tags = new ArrayList<>();
        if (tagsString == null || tagsString.trim().isEmpty()) {
            return tags;
        }
        String[] tokens = tagsString.split("[,;]");
        for (String t : tokens) {
            String clean = t.trim().toLowerCase(Locale.ROOT);
            if (!clean.isEmpty()) {
                tags.add(clean);
            }
        }
        return tags;
    }

    /**
     * Computes great-circle distance between two GPS coordinates using the Haversine formula.
     * @return Distance in kilometers
     */
    public static double haversineDistanceKm(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371.0; // Earth's mean radius in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
