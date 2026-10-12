/**
 * Curated authentic photography for TravelSync Indian tourism hubs and attraction categories.
 * Sourced from high-reliability Unsplash travel collections with elegant SVG fallbacks.
 */
export const DESTINATION_IMAGES = {
  "Hyderabad": "https://images.unsplash.com/photo-1605649487212-47bdab064df7?auto=format&fit=crop&w=1200&q=80",
  "New Delhi": "https://images.unsplash.com/photo-1587474260584-136574528ed5?auto=format&fit=crop&w=1200&q=80",
  "Goa": "https://images.unsplash.com/photo-1512343879784-a960bf40e7f2?auto=format&fit=crop&w=1200&q=80",
  "Jaipur": "https://images.unsplash.com/photo-1603262110263-fb010d6e59d4?auto=format&fit=crop&w=1200&q=80",
  "Mumbai": "https://images.unsplash.com/photo-1570168007204-dfb528c6958f?auto=format&fit=crop&w=1200&q=80",
  "Bengaluru": "https://images.unsplash.com/photo-1596176530529-78163a4f7af2?auto=format&fit=crop&w=1200&q=80",
  "Varanasi": "https://images.unsplash.com/photo-1561361513-2d000a50f0dc?auto=format&fit=crop&w=1200&q=80",
  "Agra": "https://images.unsplash.com/photo-1564507592333-c60657eea523?auto=format&fit=crop&w=1200&q=80",
  "Kochi": "https://images.unsplash.com/photo-1602216056096-3b40cc0c9944?auto=format&fit=crop&w=1200&q=80",
  "Kolkata": "https://images.unsplash.com/photo-1558431382-27e303142255?auto=format&fit=crop&w=1200&q=80",
  "Chennai": "https://images.unsplash.com/photo-1582510003544-4d00b7f74220?auto=format&fit=crop&w=1200&q=80",
  "Udaipur": "https://images.unsplash.com/photo-1615836245337-f5b9b2303f10?auto=format&fit=crop&w=1200&q=80",
  "Amritsar": "https://images.unsplash.com/photo-1588714477688-cf2fe2b94c03?auto=format&fit=crop&w=1200&q=80",
  "Manali": "https://images.unsplash.com/photo-1626621341517-bbf3d9990a23?auto=format&fit=crop&w=1200&q=80",
  "Mysuru": "https://images.unsplash.com/photo-1600100397608-f010f44383a8?auto=format&fit=crop&w=1200&q=80",
  "Rishikesh": "https://images.unsplash.com/photo-1600683072216-9ea6b1b5e3e3?auto=format&fit=crop&w=1200&q=80",
  "Shimla": "https://images.unsplash.com/photo-1588795914902-6017e8c33959?auto=format&fit=crop&w=1200&q=80",
  "Pondicherry": "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?auto=format&fit=crop&w=1200&q=80",
  "Darjeeling": "https://images.unsplash.com/photo-1544735716-392fe2489ffa?auto=format&fit=crop&w=1200&q=80",
  "Pune": "https://images.unsplash.com/photo-1580618672591-eb180b1a973f?auto=format&fit=crop&w=1200&q=80",
};

export const DEFAULT_FALLBACK_IMAGE = "https://images.unsplash.com/photo-1469854523086-cc02fe5d8800?auto=format&fit=crop&w=1200&q=80";

export const CATEGORY_IMAGES = {
  "Heritage": "https://images.unsplash.com/photo-1603262110263-fb010d6e59d4?auto=format&fit=crop&w=800&q=80",
  "Coastal": "https://images.unsplash.com/photo-1512343879784-a960bf40e7f2?auto=format&fit=crop&w=800&q=80",
  "Mountains": "https://images.unsplash.com/photo-1626621341517-bbf3d9990a23?auto=format&fit=crop&w=800&q=80",
  "Spiritual": "https://images.unsplash.com/photo-1561361513-2d000a50f0dc?auto=format&fit=crop&w=800&q=80",
  "Nature": "https://images.unsplash.com/photo-1602216056096-3b40cc0c9944?auto=format&fit=crop&w=800&q=80",
};

export function getDestinationImage(cityName) {
  if (!cityName) return DEFAULT_FALLBACK_IMAGE;
  return DESTINATION_IMAGES[cityName] || DEFAULT_FALLBACK_IMAGE;
}
