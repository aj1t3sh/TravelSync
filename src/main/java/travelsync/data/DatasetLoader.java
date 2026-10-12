package travelsync.data;

import travelsync.model.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Robust data loader that parses raw CSV datasets with schema validation
 * and error tracking for malformed rows.
 */
public class DatasetLoader {

    private int validDestinations = 0;
    private int rejectedDestinations = 0;

    private int validAttractions = 0;
    private int rejectedAttractions = 0;

    private int validHotels = 0;
    private int rejectedHotels = 0;

    private int validTransport = 0;
    private int rejectedTransport = 0;

    private int validReviews = 0;
    private int rejectedReviews = 0;

    private int validBookings = 0;
    private int rejectedBookings = 0;

    private String resolvedDatasetDir = "";

    /**
     * Resolves the active dataset directory from standard candidates.
     */
    public String resolveDatasetDirectory() {
        String[] candidateDirs = {
            "dataset",
            "data/raw",
            "data",
            "../dataset",
            "../data/raw",
            "."
        };

        for (String dir : candidateDirs) {
            File d = new File(dir);
            if (d.exists() && d.isDirectory()) {
                File f = new File(d, "destinations.csv");
                if (f.exists()) {
                    resolvedDatasetDir = d.getAbsolutePath();
                    return resolvedDatasetDir;
                }
            }
        }
        resolvedDatasetDir = "dataset";
        return resolvedDatasetDir;
    }

    public String getResolvedDatasetDir() {
        return resolvedDatasetDir;
    }

    public List<Destination> loadDestinations(String dirPath) {
        List<Destination> list = new ArrayList<>();
        File file = new File(dirPath, "destinations.csv");
        if (!file.exists()) {
            return list;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line = reader.readLine(); // Header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                List<String> cols = CSVParser.parseLine(line);
                if (cols.size() < 13) {
                    rejectedDestinations++;
                    continue;
                }
                try {
                    String id = cols.get(0);
                    String name = cols.get(1);
                    String city = cols.get(2);
                    String state = cols.get(3);
                    String country = cols.get(4);
                    String description = cols.get(5);
                    double rating = Double.parseDouble(cols.get(6));
                    int reviewCount = Integer.parseInt(cols.get(7));
                    String priceRange = cols.get(8);
                    String tags = cols.get(9);
                    String bestSeason = cols.get(10);
                    double lat = Double.parseDouble(cols.get(11));
                    double lon = Double.parseDouble(cols.get(12));

                    list.add(new Destination(id, name, city, state, country, description,
                            rating, reviewCount, priceRange, tags, bestSeason, lat, lon));
                    validDestinations++;
                } catch (Exception e) {
                    rejectedDestinations++;
                }
            }
        } catch (Exception e) {
            System.err.println("Warning: Error reading destinations.csv: " + e.getMessage());
        }
        return list;
    }

    public List<Attraction> loadAttractions(String dirPath) {
        List<Attraction> list = new ArrayList<>();
        File file = new File(dirPath, "attractions.csv");
        if (!file.exists()) {
            return list;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line = reader.readLine(); // Header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                List<String> cols = CSVParser.parseLine(line);
                if (cols.size() < 12) {
                    rejectedAttractions++;
                    continue;
                }
                try {
                    String id = cols.get(0);
                    String destId = cols.get(1);
                    String name = cols.get(2);
                    String city = cols.get(3);
                    String category = cols.get(4);
                    double rating = Double.parseDouble(cols.get(5));
                    int entryFee = Integer.parseInt(cols.get(6));
                    double duration = Double.parseDouble(cols.get(7));
                    String desc = cols.get(8);
                    String tags = cols.get(9);
                    double lat = Double.parseDouble(cols.get(10));
                    double lon = Double.parseDouble(cols.get(11));

                    list.add(new Attraction(id, destId, name, city, category, rating,
                            entryFee, duration, desc, tags, lat, lon));
                    validAttractions++;
                } catch (Exception e) {
                    rejectedAttractions++;
                }
            }
        } catch (Exception e) {
            System.err.println("Warning: Error reading attractions.csv: " + e.getMessage());
        }
        return list;
    }

    public List<Hotel> loadHotels(String dirPath) {
        List<Hotel> list = new ArrayList<>();
        File file = new File(dirPath, "hotels.csv");
        if (!file.exists()) {
            return list;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line = reader.readLine(); // Header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                List<String> cols = CSVParser.parseLine(line);
                if (cols.size() < 10) {
                    rejectedHotels++;
                    continue;
                }
                try {
                    String id = cols.get(0);
                    String city = cols.get(1);
                    String name = cols.get(2);
                    int price = Integer.parseInt(cols.get(3));
                    double rating = Double.parseDouble(cols.get(4));
                    int reviewCount = Integer.parseInt(cols.get(5));
                    String type = cols.get(6);
                    String amenities = cols.get(7);
                    int rooms = Integer.parseInt(cols.get(8));
                    int capacity = Integer.parseInt(cols.get(9));

                    list.add(new Hotel(id, city, name, price, rating, reviewCount, type, amenities, rooms, capacity));
                    validHotels++;
                } catch (Exception e) {
                    rejectedHotels++;
                }
            }
        } catch (Exception e) {
            System.err.println("Warning: Error reading hotels.csv: " + e.getMessage());
        }
        return list;
    }

    public List<TransportConnection> loadTransport(String dirPath) {
        List<TransportConnection> list = new ArrayList<>();
        File file = new File(dirPath, "transport_network.csv");
        if (!file.exists()) {
            return list;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line = reader.readLine(); // Header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                List<String> cols = CSVParser.parseLine(line);
                if (cols.size() < 7) {
                    rejectedTransport++;
                    continue;
                }
                try {
                    String from = cols.get(0);
                    String to = cols.get(1);
                    String mode = cols.get(2);
                    int capacity = Integer.parseInt(cols.get(3));
                    int distance = Integer.parseInt(cols.get(4));
                    int cost = Integer.parseInt(cols.get(5));
                    double time = Double.parseDouble(cols.get(6));

                    list.add(new TransportConnection(from, to, mode, capacity, distance, cost, time));
                    validTransport++;
                } catch (Exception e) {
                    rejectedTransport++;
                }
            }
        } catch (Exception e) {
            System.err.println("Warning: Error reading transport_network.csv: " + e.getMessage());
        }
        return list;
    }

    public List<Review> loadReviews(String dirPath) {
        List<Review> list = new ArrayList<>();
        File file = new File(dirPath, "reviews.csv");
        if (!file.exists()) {
            return list;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line = reader.readLine(); // Header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                List<String> cols = CSVParser.parseLine(line);
                if (cols.size() < 7) {
                    rejectedReviews++;
                    continue;
                }
                try {
                    String id = cols.get(0);
                    String destId = cols.get(1);
                    String destName = cols.get(2);
                    String text = cols.get(3);
                    double rating = Double.parseDouble(cols.get(4));
                    String date = cols.get(5);
                    String travelerType = cols.get(6);

                    list.add(new Review(id, destId, destName, text, rating, date, travelerType));
                    validReviews++;
                } catch (Exception e) {
                    rejectedReviews++;
                }
            }
        } catch (Exception e) {
            System.err.println("Warning: Error reading reviews.csv: " + e.getMessage());
        }
        return list;
    }

    public List<Booking> loadBookings(String dirPath) {
        List<Booking> list = new ArrayList<>();
        File file = new File(dirPath, "bookings.csv");
        if (!file.exists()) {
            return list;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line = reader.readLine(); // Header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                List<String> cols = CSVParser.parseLine(line);
                if (cols.size() < 12) {
                    rejectedBookings++;
                    continue;
                }
                try {
                    String id = cols.get(0);
                    String hotelId = cols.get(1);
                    String hotelName = cols.get(2);
                    String city = cols.get(3);
                    String bDate = cols.get(4);
                    String ciDate = cols.get(5);
                    String coDate = cols.get(6);
                    int guests = Integer.parseInt(cols.get(7));
                    int rooms = Integer.parseInt(cols.get(8));
                    int rate = Integer.parseInt(cols.get(9));
                    String status = cols.get(10);
                    int weight = Integer.parseInt(cols.get(11));

                    list.add(new Booking(id, hotelId, hotelName, city, bDate, ciDate, coDate,
                            guests, rooms, rate, status, weight));
                    validBookings++;
                } catch (Exception e) {
                    rejectedBookings++;
                }
            }
        } catch (Exception e) {
            System.err.println("Warning: Error reading bookings.csv: " + e.getMessage());
        }
        return list;
    }

    public int getValidDestinations() { return validDestinations; }
    public int getRejectedDestinations() { return rejectedDestinations; }
    public int getValidAttractions() { return validAttractions; }
    public int getRejectedAttractions() { return rejectedAttractions; }
    public int getValidHotels() { return validHotels; }
    public int getRejectedHotels() { return rejectedHotels; }
    public int getValidTransport() { return validTransport; }
    public int getRejectedTransport() { return rejectedTransport; }
    public int getValidReviews() { return validReviews; }
    public int getRejectedReviews() { return rejectedReviews; }
    public int getValidBookings() { return validBookings; }
    public int getRejectedBookings() { return rejectedBookings; }
    public int getTotalValidRecords() {
        return validDestinations + validAttractions + validHotels + validTransport + validReviews + validBookings;
    }
}
