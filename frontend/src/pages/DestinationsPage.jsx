import React, { useState, useEffect } from 'react';
import { Search, Filter, SlidersHorizontal, MapPin, Star, Calendar, Sparkles, X, ChevronDown, Check } from 'lucide-react';
import { api } from '../api/apiClient';
import DestinationCard from '../components/DestinationCard';
import DestinationDetailModal from '../components/DestinationDetailModal';

export default function DestinationsPage({ setActiveTab, setSelectedDestinationForPlan }) {
  const [destinations, setDestinations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedSeason, setSelectedSeason] = useState('ALL');
  const [selectedPrice, setSelectedPrice] = useState('ALL');
  const [sortBy, setSortBy] = useState('rating');
  const [selectedTag, setSelectedTag] = useState('ALL');
  const [selectedModalDest, setSelectedModalDest] = useState(null);
  const [fuzzySuggestion, setFuzzySuggestion] = useState(null);

  useEffect(() => {
    loadDestinations();
  }, [selectedSeason, selectedPrice, sortBy]);

  const loadDestinations = () => {
    setLoading(true);
    const filters = {};
    if (selectedSeason !== 'ALL') filters.season = selectedSeason;
    if (selectedPrice !== 'ALL') filters.priceRange = selectedPrice;
    if (sortBy) filters.sort = sortBy;

    api.getDestinations(filters)
      .then((data) => {
        setDestinations(data);
        setLoading(false);
      })
      .catch((err) => {
        console.error('Failed to load destinations:', err);
        setLoading(false);
      });
  };

  // Live search with Java backend (KMP / Wagner-Fischer)
  const handleSearchChange = (query) => {
    setSearchQuery(query);
    setFuzzySuggestion(null);

    if (!query.trim()) {
      loadDestinations();
      return;
    }

    api.searchDestinations(query)
      .then((results) => {
        if (results && results.length > 0) {
          const matchedDests = results.map(r => r.destination);
          setDestinations(matchedDests);

          // Check if there was a typo correction / fuzzy match
          const topResult = results[0];
          if (topResult.matchType === 'FUZZY' || topResult.score < 5.0) {
            setFuzzySuggestion(topResult.destination.name);
          }
        } else {
          setDestinations([]);
        }
      })
      .catch((err) => console.error('Search error:', err));
  };

  const handlePlanDestination = (cityName) => {
    if (setSelectedDestinationForPlan) {
      setSelectedDestinationForPlan(cityName);
    }
    setActiveTab('planner');
  };

  const filteredDestinations = destinations.filter(d => {
    if (selectedTag === 'ALL') return true;
    return (d.tags || '').toLowerCase().includes(selectedTag.toLowerCase());
  });

  const allTags = ['nature', 'history', 'culture', 'food', 'beaches', 'adventure', 'spirituality', 'shopping'];

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8 animate-fadeIn">
      
      {/* Header section */}
      <div className="space-y-3">
        <div className="flex items-center gap-2 text-forest text-xs font-semibold uppercase tracking-wider">
          <MapPin className="w-4 h-4" />
          <span>India Tourism Hubs Directory</span>
        </div>
        <h1 className="font-serif-heading text-3xl sm:text-4xl font-bold text-forest">
          Explore All Destinations
        </h1>
        <p className="text-xs sm:text-sm text-charcoal/70 max-w-2xl">
          Search, filter, and inspect verified historical monuments, natural landmarks, hotel inventories, and transport corridors across 20 authentic Indian tourist hubs.
        </p>
      </div>

      {/* Search & Filter Bar */}
      <div className="bg-white p-5 rounded-2xl border border-sage/40 shadow-soft space-y-4">
        
        {/* Search Input Row */}
        <div className="relative">
          <Search className="w-5 h-5 absolute left-3.5 top-3.5 text-forest/70" />
          <input
            type="text"
            value={searchQuery}
            onChange={(e) => handleSearchChange(e.target.value)}
            placeholder="Search by city name, description keyword or tag (supports typo auto-correction)..."
            className="w-full pl-11 pr-4 py-3 rounded-xl border border-sage/40 bg-ivory/40 text-charcoal text-sm focus:outline-none focus:border-forest focus:ring-1 focus:ring-forest transition-all"
          />
          {searchQuery && (
            <button
              onClick={() => handleSearchChange('')}
              className="absolute right-3.5 top-3.5 text-charcoal/40 hover:text-charcoal"
            >
              <X className="w-4 h-4" />
            </button>
          )}
        </div>

        {/* Typo Suggestion Notice (Wagner-Fischer) */}
        {fuzzySuggestion && (
          <div className="text-xs text-forest bg-forest-50 p-2.5 rounded-lg border border-forest-200 flex items-center justify-between">
            <span>
              Did you mean <strong className="underline cursor-pointer" onClick={() => handleSearchChange(fuzzySuggestion)}>{fuzzySuggestion}</strong>? (Detected via Wagner-Fischer edit distance)
            </span>
            <button 
              onClick={() => handleSearchChange(fuzzySuggestion)}
              className="font-bold underline text-xs ml-2"
            >
              Apply
            </button>
          </div>
        )}

        {/* Filter Controls Row */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-3 pt-2">
          
          {/* Price Range Filter */}
          <div>
            <label className="block text-[11px] font-bold text-charcoal/60 uppercase tracking-wider mb-1">
              Tariff / Price Tier
            </label>
            <select
              value={selectedPrice}
              onChange={(e) => setSelectedPrice(e.target.value)}
              className="w-full p-2.5 rounded-xl border border-sage/40 bg-white text-xs text-charcoal font-medium focus:outline-none focus:border-forest"
            >
              <option value="ALL">All Price Ranges</option>
              <option value="BUDGET">Budget Friendly</option>
              <option value="MODERATE">Moderate</option>
              <option value="EXPENSIVE">Luxury / Expensive</option>
            </select>
          </div>

          {/* Season Filter */}
          <div>
            <label className="block text-[11px] font-bold text-charcoal/60 uppercase tracking-wider mb-1">
              Best Season
            </label>
            <select
              value={selectedSeason}
              onChange={(e) => setSelectedSeason(e.target.value)}
              className="w-full p-2.5 rounded-xl border border-sage/40 bg-white text-xs text-charcoal font-medium focus:outline-none focus:border-forest"
            >
              <option value="ALL">All Seasons</option>
              <option value="October">October to March (Winter)</option>
              <option value="November">November to February (Peak)</option>
              <option value="March">March to June (Summer Hills)</option>
              <option value="Throughout">Throughout the Year</option>
            </select>
          </div>

          {/* Sort By Filter */}
          <div>
            <label className="block text-[11px] font-bold text-charcoal/60 uppercase tracking-wider mb-1">
              Sort Criteria
            </label>
            <select
              value={sortBy}
              onChange={(e) => setSortBy(e.target.value)}
              className="w-full p-2.5 rounded-xl border border-sage/40 bg-white text-xs text-charcoal font-medium focus:outline-none focus:border-forest"
            >
              <option value="rating">Highest Rated First</option>
              <option value="reviews">Most Reviewed First</option>
              <option value="name">Alphabetical (A - Z)</option>
            </select>
          </div>

          {/* Reset Filters Button */}
          <div className="flex items-end">
            <button
              onClick={() => {
                setSearchQuery('');
                setSelectedSeason('ALL');
                setSelectedPrice('ALL');
                setSelectedTag('ALL');
                setSortBy('rating');
                loadDestinations();
              }}
              className="w-full py-2.5 px-3 rounded-xl border border-sage/40 text-charcoal/70 hover:text-forest hover:bg-sage/10 text-xs font-semibold transition-colors"
            >
              Reset Filters
            </button>
          </div>

        </div>

        {/* Tag Filters Row */}
        <div className="flex items-center gap-1.5 overflow-x-auto pt-2 no-scrollbar">
          <span className="text-[11px] font-bold text-charcoal/50 uppercase tracking-wider mr-1 flex-shrink-0">
            Tags:
          </span>
          <button
            onClick={() => setSelectedTag('ALL')}
            className={`px-2.5 py-1 rounded-full text-xs font-medium whitespace-nowrap transition-colors ${
              selectedTag === 'ALL'
                ? 'bg-forest text-ivory font-bold'
                : 'bg-ivory text-charcoal/70 hover:bg-sage/20 border border-sage/30'
            }`}
          >
            All Themes
          </button>
          {allTags.map((tag) => {
            const active = selectedTag.toLowerCase() === tag.toLowerCase();
            return (
              <button
                key={tag}
                onClick={() => setSelectedTag(tag)}
                className={`px-2.5 py-1 rounded-full text-xs font-medium whitespace-nowrap capitalize transition-colors ${
                  active
                    ? 'bg-forest text-ivory font-bold'
                    : 'bg-ivory text-charcoal/70 hover:bg-sage/20 border border-sage/30'
                }`}
              >
                #{tag}
              </button>
            );
          })}
        </div>

      </div>

      {/* Destinations Grid */}
      {loading ? (
        <div className="py-20 text-center space-y-3">
          <div className="w-8 h-8 rounded-full border-2 border-forest border-t-transparent animate-spin mx-auto" />
          <p className="text-xs text-charcoal/60">Searching authentic Indian hubs repository...</p>
        </div>
      ) : filteredDestinations.length === 0 ? (
        <div className="bg-white p-12 rounded-3xl border border-sage/40 text-center space-y-3">
          <MapPin className="w-10 h-10 text-charcoal/30 mx-auto" />
          <h3 className="font-serif-heading text-xl font-bold text-forest">No Destinations Found</h3>
          <p className="text-xs text-charcoal/60 max-w-md mx-auto">
            No destinations matched your criteria. Try loosening your price filters or searching with another term.
          </p>
        </div>
      ) : (
        <div className="space-y-4">
          <div className="flex items-center justify-between text-xs text-charcoal/60 px-1">
            <span>Showing <strong>{filteredDestinations.length}</strong> destination hubs</span>
            <span className="font-mono text-forest">KMP & Wagner-Fischer Query Active</span>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {filteredDestinations.map((dest) => (
              <DestinationCard
                key={dest.id}
                destination={dest}
                onSelectForPlan={handlePlanDestination}
                onViewDetail={(d) => setSelectedModalDest(d)}
              />
            ))}
          </div>
        </div>
      )}

      {/* Hub Detail Modal Drawer */}
      {selectedModalDest && (
        <DestinationDetailModal
          destination={selectedModalDest}
          onClose={() => setSelectedModalDest(null)}
          onPlanTrip={handlePlanDestination}
        />
      )}

    </div>
  );
}
