/**
 * TravelSync API Client for communicating with the Java 17 REST API backend.
 */

/**
 * Safely resolves the API Base URL.
 * Handles Render hostname injection (e.g. 'travelsync-backend.onrender.com'),
 * full URLs ('https://travelsync-backend.onrender.com'), and local dev fallback ('/api').
 */
function resolveApiBaseUrl(raw) {
  if (!raw) return '/api';
  let url = raw.trim();
  if (!url) return '/api';

  // Relative path (e.g., '/api')
  if (url.startsWith('/')) {
    return url.endsWith('/') ? url.slice(0, -1) : url;
  }

  // Prepend protocol if missing (Render property: host supplies hostname only)
  if (!/^https?:\/\//i.test(url)) {
    const isLocal = /^localhost(:\d+)?/i.test(url) || /^127\.0\.0\.1(:\d+)?/i.test(url);
    url = (isLocal ? 'http://' : 'https://') + url;
  }

  // Strip trailing slashes
  url = url.replace(/\/+$/, '');

  // Append /api without duplicating
  return url.endsWith('/api') ? url : `${url}/api`;
}

const BASE_URL = resolveApiBaseUrl(import.meta.env.VITE_API_BASE_URL);


async function handleResponse(response) {
  if (!response.ok) {
    let errorMsg = `Server error (${response.status})`;
    try {
      const errJson = await response.json();
      if (errJson && errJson.message) {
        errorMsg = errJson.message;
      }
    } catch (_) {}
    throw new Error(errorMsg);
  }
  return response.json();
}

export const api = {
  // Backend health check
  getHealth: async () => {
    return handleResponse(await fetch(`${BASE_URL}/health`));
  },

  // Destinations catalog & filtering
  getDestinations: async (filters = {}) => {
    const params = new URLSearchParams();
    if (filters.season) params.set('season', filters.season);
    if (filters.priceRange) params.set('priceRange', filters.priceRange);
    if (filters.sort) params.set('sort', filters.sort);
    const qs = params.toString();
    return handleResponse(await fetch(`${BASE_URL}/destinations${qs ? `?${qs}` : ''}`));
  },

  // Destination detail by ID
  getDestinationDetail: async (id) => {
    return handleResponse(await fetch(`${BASE_URL}/destinations/${encodeURIComponent(id)}`));
  },

  // Destination search (exact + fuzzy KMP / Wagner-Fischer)
  searchDestinations: async (query) => {
    return handleResponse(await fetch(`${BASE_URL}/destinations/search?q=${encodeURIComponent(query || '')}`));
  },

  // Hotels catalog
  getHotels: async (city, type) => {
    const params = new URLSearchParams();
    if (city) params.set('city', city);
    if (type) params.set('type', type);
    const qs = params.toString();
    return handleResponse(await fetch(`${BASE_URL}/hotels${qs ? `?${qs}` : ''}`));
  },

  // Attractions catalog
  getAttractions: async (city, category) => {
    const params = new URLSearchParams();
    if (city) params.set('city', city);
    if (category) params.set('category', category);
    const qs = params.toString();
    return handleResponse(await fetch(`${BASE_URL}/attractions${qs ? `?${qs}` : ''}`));
  },

  // Plan a trip (Main Core Engine)
  planTrip: async (tripRequest) => {
    return handleResponse(
      await fetch(`${BASE_URL}/trips/plan`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(tripRequest),
      })
    );
  },

  // Saved trips
  getSavedTrips: async () => {
    return handleResponse(await fetch(`${BASE_URL}/trips`));
  },

  getTripById: async (tripId) => {
    return handleResponse(await fetch(`${BASE_URL}/trips/${encodeURIComponent(tripId)}`));
  },

  saveTrip: async (tripData) => {
    return handleResponse(
      await fetch(`${BASE_URL}/trips/save`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(tripData),
      })
    );
  },

  // Dataset analytics & CO6 statistics
  getAnalyticsSummary: async () => {
    return handleResponse(await fetch(`${BASE_URL}/analytics/summary`));
  },

  // Algorithms metadata list
  getAlgorithmsMetadata: async () => {
    return handleResponse(await fetch(`${BASE_URL}/engine/algorithms`));
  },

  // Interactive algorithm playground execution
  executeAlgorithm: async (algorithmId, params = {}) => {
    return handleResponse(
      await fetch(`${BASE_URL}/engine/execute`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ algorithmId, params }),
      })
    );
  },
};
