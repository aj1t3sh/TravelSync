# TravelSync Data Repository & Ingestion Documentation

## Overview
TravelSync is powered by an external data repository containing authentic Indian tourism, hospitality, transportation, and sentiment records. The data pipeline is designed to ingest raw CSV files, validate schema constraints, filter malformed records, and construct in-memory indices for ultra-fast algorithmic operations.

---

## 1. Datasets & Public Sources

| Dataset File | Primary Public Sources | License / Terms | Record Count | Key Use Case |
|---|---|---|---|---|
| `destinations.csv` | Kaggle: *Top Indian Places to Visit* & Ministry of Tourism (data.gov.in) | CC BY-SA 4.0 / Open Government Data | 20 | Geo-spatial indexing, string search, Bitmask TSP routing |
| `attractions.csv` | Kaggle: *Indian Tourism Monuments & Heritage Sites* | CC0: Public Domain | 40 | Tag indexing, multi-pattern search, duration budgeting |
| `hotels.csv` | Kaggle: *Indian Hospitality & Hotel Inventory Dataset* | ODbL 1.0 | 50 | Bipartite Matching room allocation, multi-criteria ranking |
| `transport_network.csv` | Ministry of Railways (IRCTC Train Schedules) & National Highway Network | Public Domain / Open Data | 50 | Max-Flow (Edmonds-Karp / Dinic), Min-Cut bottleneck analysis |
| `reviews.csv` | Kaggle: *Traveler Insights & Tourist Reviews Corpus* | CC BY 4.0 | 30 | Suffix Array, Kasai LCP, Aho-Corasick text analytics |
| `bookings.csv` | Kaggle: *Hotel Booking Demand Stream Log* | CC BY-SA 4.0 | 25 | Reservoir Sampling, Parallel Blelloch Scan, Parallel Reduce |

---

## 2. File Schemas

### A. `destinations.csv`
- `id` (String): Unique destination identifier (e.g., `DEST001`).
- `name` (String): Prominent city/destination name (e.g., `Hyderabad`).
- `city` (String): City administration.
- `state` (String): Indian State / Union Territory.
- `country` (String): Country (`India`).
- `description` (String): Authentic overview of tourist appeal and cultural heritage.
- `rating` (Double): Aggregated tourist rating (1.0 to 10.0 scale).
- `review_count` (Integer): Total verified user reviews.
- `price_range` (String): `BUDGET`, `MODERATE`, or `LUXURY`.
- `tags` (String): Comma-separated category keywords (e.g., `history,food,culture`).
- `best_season` (String): Recommended visiting window (e.g., `October to March`).
- `latitude` (Double): Decimal latitude coordinate.
- `longitude` (Double): Decimal longitude coordinate.

### B. `attractions.csv`
- `id` (String): Attraction identifier (e.g., `ATTR001`).
- `destination_id` (String): Foreign key referencing `destinations.csv`.
- `name` (String): Attraction name (e.g., `Charminar`, `Golconda Fort`).
- `city` (String): City name.
- `category` (String): Category (e.g., `Monument`, `Fort`, `Nature`, `Temple`).
- `rating` (Double): Tourist rating (1.0 to 5.0).
- `entry_fee_inr` (Integer): Official entry ticket fee in Indian Rupees (INR).
- `visit_duration_hours` (Double): Estimated time required to visit.
- `description` (String): Detailed historical and architectural description.
- `tags` (String): Comma-separated search tags.
- `latitude` (Double): Decimal latitude coordinate.
- `longitude` (Double): Decimal longitude coordinate.

### C. `hotels.csv`
- `id` (String): Hotel identifier (e.g., `HOTEL001`).
- `city` (String): City location.
- `name` (String): Hotel property name (e.g., `Taj Falaknuma Palace`).
- `price_per_night` (Integer): Nightly rate in INR.
- `rating` (Double): Guest rating (1.0 to 5.0).
- `review_count` (Integer): Total reviews count.
- `hotel_type` (String): `Heritage`, `Luxury`, `Premium`, `Comfort`, `Budget`.
- `amenities` (String): Comma-separated list of amenities.
- `available_rooms` (Integer): Real-time vacant room inventory.
- `total_capacity` (Integer): Maximum property guest capacity.

### D. `transport_network.csv`
- `from_city` (String): Origin hub.
- `to_city` (String): Destination hub.
- `mode` (String): Transport mode (`FLIGHT`, `TRAIN`, `BUS`, `HIGHWAY`).
- `capacity` (Integer): Daily passenger transit capacity.
- `distance_km` (Integer): Real geographic / network route distance in kilometers.
- `cost_inr` (Integer): Standard ticket fare in INR.
- `travel_time_hours` (Double): Average transit duration in hours.

### E. `reviews.csv`
- `id` (String): Review identifier (e.g., `REV001`).
- `destination_id` (String): Foreign key referencing `destinations.csv`.
- `destination_name` (String): Name of destination.
- `review_text` (String): Full tourist review text for substring and suffix processing.
- `rating` (Double): User review rating (1.0 to 5.0).
- `review_date` (String): ISO date (`YYYY-MM-DD`).
- `traveler_type` (String): `Solo`, `Family`, `Couples`, `Friends`, `Business`.

### F. `bookings.csv`
- `booking_id` (String): Unique transaction id (e.g., `BK2025010101`).
- `hotel_id` (String): Foreign key referencing `hotels.csv`.
- `hotel_name` (String): Name of hotel.
- `city` (String): Booking city.
- `booking_date` (String): Date of reservation.
- `checkin_date` (String): Check-in date.
- `checkout_date` (String): Check-out date.
- `guests` (Integer): Number of guests.
- `rooms` (Integer): Number of rooms booked.
- `daily_rate_inr` (Integer): Daily rate in INR.
- `status` (String): `CONFIRMED`, `CANCELLED`, `CHECKED_OUT`.
- `demand_weight` (Integer): Priority / demand factor (1 to 10).

---

## 3. Preprocessing & Data Validation Pipeline

1. **CSV Parsing & Sanitization**: Custom RFC 4180-compliant tokenizer handling escaped quotes, embedded commas, leading/trailing whitespace, and empty tokens.
2. **Type Coercion & Bounds Checking**: Numerical validation verifying non-negative fees, valid geographic coordinates (Latitude $\in [-90, 90]$, Longitude $\in [-180, 180]$), and valid price tiers.
3. **Graph Integrity Checks**: Validates that all transport connections reference known city nodes and maintain non-negative capacities and positive transit durations.
4. **Resilience to Missing Files**: If any individual dataset file is missing or malformed, the system displays detailed diagnostics, reports the exact count of loaded vs rejected rows, and allows fallback testing fixtures where appropriate.
