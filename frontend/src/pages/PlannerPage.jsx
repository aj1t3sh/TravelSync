import React, { useState, useEffect } from 'react';
import { Sparkles, MapPin, Calendar, Users, DollarSign, Hotel, Plane, Clock, ShieldAlert, CheckCircle2, ChevronRight, Plus, Trash2, Tag, Compass, AlertCircle } from 'lucide-react';
import { api } from '../api/apiClient';

export default function PlannerPage({ onPlanGenerated, prefilledDestination }) {
  const [destinationsList, setDestinationsList] = useState([]);
  const [origin, setOrigin] = useState('Hyderabad');
  const [selectedDestinations, setSelectedDestinations] = useState(['Goa']);
  const [startDate, setStartDate] = useState(
    new Date(Date.now() + 10 * 86400000).toISOString().split('T')[0]
  );
  const [endDate, setEndDate] = useState(
    new Date(Date.now() + 14 * 86400000).toISOString().split('T')[0]
  );
  const [travelers, setTravelers] = useState(2);
  const [days, setDays] = useState(4);
  const [budgetInr, setBudgetInr] = useState(45000);
  const [travelStyle, setTravelStyle] = useState('balanced');
  const [accommodationPreference, setAccommodationPreference] = useState('Comfort');
  const [transportPreference, setTransportPreference] = useState('ANY');
  const [maxAttractionsPerDay, setMaxAttractionsPerDay] = useState(3);
  const [interests, setInterests] = useState(['nature', 'beaches', 'food']);
  const [mustVisitAttractions, setMustVisitAttractions] = useState([]);
  const [availableAttractions, setAvailableAttractions] = useState([]);

  // Processing state
  const [loading, setLoading] = useState(false);
  const [loadingStage, setLoadingStage] = useState('');
  const [errorMsg, setErrorMsg] = useState(null);

  useEffect(() => {
    api.getDestinations()
      .then((data) => {
        setDestinationsList(data);
        if (prefilledDestination && data.some(d => d.name === prefilledDestination)) {
          setSelectedDestinations([prefilledDestination]);
        }
      })
      .catch((err) => console.error('Failed to load destinations:', err));
  }, [prefilledDestination]);

  // Load attractions for the primary destination to populate must-visit picker
  useEffect(() => {
    if (selectedDestinations.length > 0) {
      const primary = selectedDestinations[0];
      api.getAttractions(primary)
        .then((attrs) => setAvailableAttractions(attrs))
        .catch(() => setAvailableAttractions([]));
    }
  }, [selectedDestinations]);

  // Calculate days between start and end date
  const handleDateChange = (start, end) => {
    setStartDate(start);
    setEndDate(end);
    try {
      const d1 = new Date(start);
      const d2 = new Date(end);
      const diffDays = Math.max(1, Math.round((d2 - d1) / (1000 * 60 * 60 * 24)));
      setDays(diffDays);
    } catch (_) {}
  };

  const toggleInterest = (tag) => {
    if (interests.includes(tag)) {
      setInterests(interests.filter(t => t !== tag));
    } else {
      setInterests([...interests, tag]);
    }
  };

  const addDestinationStop = (cityName) => {
    if (cityName && !selectedDestinations.includes(cityName) && selectedDestinations.length < 5) {
      setSelectedDestinations([...selectedDestinations, cityName]);
    }
  };

  const removeDestinationStop = (index) => {
    if (selectedDestinations.length > 1) {
      setSelectedDestinations(selectedDestinations.filter((_, i) => i !== index));
    }
  };

  const handleGenerateTrip = async (e) => {
    e.preventDefault();
    setErrorMsg(null);

    // Validation
    if (!origin) {
      setErrorMsg('Please select your departure origin city.');
      return;
    }
    if (selectedDestinations.length === 0) {
      setErrorMsg('Please select at least 1 destination stop.');
      return;
    }
    if (budgetInr < 3000) {
      setErrorMsg('Minimum practical budget is ₹3,000 for travel and lodging.');
      return;
    }

    setLoading(true);

    // Simulated step stages for UX feedback while Java REST API executes
    setLoadingStage('Classifying travel paradigms (CO1 Decision Tree)...');
    setTimeout(() => {
      setLoadingStage('Executing Held-Karp Bitmask DP for optimal stop sequence (CO3)...');
    }, 400);
    setTimeout(() => {
      setLoadingStage('Running Dinic / Edmonds-Karp Residual Flow & Room Matching (CO4)...');
    }, 800);
    setTimeout(() => {
      setLoadingStage('Approximating attraction budget yield (CO5) & Parallel Verification (CO6)...');
    }, 1200);

    const payload = {
      origin: origin,
      destination: selectedDestinations[0],
      destinations: selectedDestinations,
      startDate: startDate,
      endDate: endDate,
      travelers: travelers,
      days: days,
      budgetInr: budgetInr,
      interests: interests,
      travelStyle: travelStyle,
      accommodationPreference: accommodationPreference,
      transportPreference: transportPreference,
      maxAttractionsPerDay: maxAttractionsPerDay,
      mustVisitAttractions: mustVisitAttractions,
      excludedAttractions: [],
    };

    try {
      const response = await api.planTrip(payload);
      setLoading(false);
      if (onPlanGenerated) {
        onPlanGenerated(response);
      }
    } catch (err) {
      setLoading(false);
      setErrorMsg(err.message || 'Failed to generate itinerary. Please try adjusting your constraints.');
    }
  };

  const availableTags = [
    'nature', 'beaches', 'history', 'culture', 'food',
    'architecture', 'adventure', 'spirituality', 'shopping', 'scenery'
  ];

  return (
    <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8 animate-fadeIn">
      
      {/* Header */}
      <div className="space-y-2 text-center max-w-2xl mx-auto">
        <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-sage-100 text-forest text-xs font-semibold">
          <Sparkles className="w-3.5 h-3.5 text-forest" />
          <span>Intelligent Algorithmic Travel Planning</span>
        </div>
        <h1 className="font-serif-heading text-3xl sm:text-4xl font-bold text-forest">
          Plan Your Optimized Itinerary
        </h1>
        <p className="text-xs sm:text-sm text-charcoal/70">
          Specify your travel preferences below. The TravelSync Java engine will run Held-Karp Bitmask DP, Dinic maximum flow, and bipartite matching to generate your trip.
        </p>
      </div>

      {/* Main Interactive Form Card */}
      <form onSubmit={handleGenerateTrip} className="bg-white rounded-3xl border border-sage/40 shadow-card p-6 sm:p-10 space-y-8">
        
        {/* Error Alert if any */}
        {errorMsg && (
          <div className="p-4 rounded-xl bg-red-50 border border-red-200 text-red-800 text-xs flex items-start gap-2.5">
            <AlertCircle className="w-4 h-4 text-red-600 flex-shrink-0 mt-0.5" />
            <div>
              <strong className="block font-semibold">Infeasible or Invalid Request:</strong>
              <span>{errorMsg}</span>
            </div>
          </div>
        )}

        {/* SECTION 1: TRAVEL ROUTE & DATES */}
        <div className="space-y-4">
          <h3 className="font-serif-heading text-lg font-bold text-forest border-b border-sage/30 pb-2 flex items-center gap-2">
            <MapPin className="w-4 h-4 text-forest" />
            <span>1. Transit Route & Journey Duration</span>
          </h3>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
            
            {/* Origin Hub */}
            <div>
              <label className="block text-xs font-bold text-charcoal/70 uppercase tracking-wider mb-1.5">
                Starting Location (Origin City)
              </label>
              <select
                value={origin}
                onChange={(e) => setOrigin(e.target.value)}
                className="w-full p-3 rounded-xl border border-sage/40 bg-ivory/30 text-sm font-semibold text-charcoal focus:border-forest focus:outline-none"
              >
                {destinationsList.map((d) => (
                  <option key={d.id} value={d.name}>{d.name} ({d.state})</option>
                ))}
              </select>
            </div>

            {/* Destination Selection with Multi-Stop capability */}
            <div>
              <label className="block text-xs font-bold text-charcoal/70 uppercase tracking-wider mb-1.5 flex items-center justify-between">
                <span>Destination Stops (Bitmask TSP)</span>
                <span className="text-[10px] text-charcoal/50 font-normal">Max 5 stops</span>
              </label>

              <div className="space-y-2">
                {selectedDestinations.map((dest, idx) => (
                  <div key={idx} className="flex items-center gap-2">
                    <select
                      value={dest}
                      onChange={(e) => {
                        const updated = [...selectedDestinations];
                        updated[idx] = e.target.value;
                        setSelectedDestinations(updated);
                      }}
                      className="w-full p-3 rounded-xl border border-sage/40 bg-ivory/30 text-sm font-semibold text-charcoal focus:border-forest focus:outline-none"
                    >
                      {destinationsList.map((d) => (
                        <option key={d.id} value={d.name}>{d.name} ({d.state})</option>
                      ))}
                    </select>
                    {selectedDestinations.length > 1 && (
                      <button
                        type="button"
                        onClick={() => removeDestinationStop(idx)}
                        className="p-3 rounded-xl border border-red-200 text-red-600 hover:bg-red-50"
                      >
                        <Trash2 className="w-4 h-4" />
                      </button>
                    )}
                  </div>
                ))}

                {selectedDestinations.length < 5 && (
                  <div className="pt-1">
                    <select
                      onChange={(e) => {
                        if (e.target.value) {
                          addDestinationStop(e.target.value);
                          e.target.value = '';
                        }
                      }}
                      defaultValue=""
                      className="text-xs font-semibold text-forest bg-sage/20 border border-sage/40 p-2 rounded-lg cursor-pointer hover:bg-sage/30"
                    >
                      <option value="" disabled>+ Add another destination stop (Multi-City Tour)...</option>
                      {destinationsList
                        .filter(d => !selectedDestinations.includes(d.name) && d.name !== origin)
                        .map(d => (
                          <option key={d.id} value={d.name}>Add {d.name}</option>
                        ))}
                    </select>
                  </div>
                )}
              </div>
            </div>

          </div>

          {/* Dates & Travelers Row */}
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 pt-2">
            <div>
              <label className="block text-xs font-bold text-charcoal/70 uppercase tracking-wider mb-1.5">
                Departure Date
              </label>
              <input
                type="date"
                value={startDate}
                onChange={(e) => handleDateChange(e.target.value, endDate)}
                className="w-full p-3 rounded-xl border border-sage/40 bg-ivory/30 text-sm font-semibold text-charcoal focus:border-forest focus:outline-none"
              />
            </div>

            <div>
              <label className="block text-xs font-bold text-charcoal/70 uppercase tracking-wider mb-1.5">
                Return Date ({days} Days)
              </label>
              <input
                type="date"
                value={endDate}
                onChange={(e) => handleDateChange(startDate, e.target.value)}
                className="w-full p-3 rounded-xl border border-sage/40 bg-ivory/30 text-sm font-semibold text-charcoal focus:border-forest focus:outline-none"
              />
            </div>

            <div>
              <label className="block text-xs font-bold text-charcoal/70 uppercase tracking-wider mb-1.5">
                Travelers Count
              </label>
              <div className="flex items-center gap-2">
                <input
                  type="number"
                  min="1"
                  max="12"
                  value={travelers}
                  onChange={(e) => setTravelers(Math.max(1, parseInt(e.target.value) || 1))}
                  className="w-full p-3 rounded-xl border border-sage/40 bg-ivory/30 text-sm font-semibold text-charcoal focus:border-forest focus:outline-none"
                />
                <span className="text-xs text-charcoal/60 whitespace-nowrap font-mono">
                  ({Math.ceil(travelers / 2)} rooms)
                </span>
              </div>
            </div>
          </div>

        </div>

        {/* SECTION 2: BUDGET & PREFERENCES */}
        <div className="space-y-4">
          <h3 className="font-serif-heading text-lg font-bold text-forest border-b border-sage/30 pb-2 flex items-center gap-2">
            <DollarSign className="w-4 h-4 text-forest" />
            <span>2. Budget & Travel Preferences</span>
          </h3>

          {/* Budget Slider & Input */}
          <div className="p-4 rounded-2xl bg-ivory/60 border border-sage/30 space-y-3">
            <div className="flex items-center justify-between">
              <div>
                <span className="text-xs font-bold text-forest uppercase tracking-wider block">
                  Total Trip Budget in INR
                </span>
                <span className="text-[11px] text-charcoal/60">
                  Target ceiling for inter-city transit, hotel accommodation, and attraction fees.
                </span>
              </div>
              <div className="text-right">
                <span className="font-serif-heading font-bold text-2xl text-forest">
                  ₹{budgetInr.toLocaleString()}
                </span>
                <span className="text-[10px] text-charcoal/50 block font-mono">
                  ~₹{Math.round(budgetInr / Math.max(1, days * travelers)).toLocaleString()} / person / day
                </span>
              </div>
            </div>

            <input
              type="range"
              min="8000"
              max="150000"
              step="2000"
              value={budgetInr}
              onChange={(e) => setBudgetInr(parseInt(e.target.value))}
              className="w-full accent-forest cursor-pointer"
            />
            <div className="flex justify-between text-[10px] font-mono text-charcoal/50">
              <span>Budget (₹8,000)</span>
              <span>Comfort (₹45,000)</span>
              <span>Luxury (₹1,50,000)</span>
            </div>
          </div>

          {/* Accommodation & Transport Mode */}
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
            <div>
              <label className="block text-xs font-bold text-charcoal/70 uppercase tracking-wider mb-1.5">
                Hotel Accommodation Tier
              </label>
              <select
                value={accommodationPreference}
                onChange={(e) => setAccommodationPreference(e.target.value)}
                className="w-full p-3 rounded-xl border border-sage/40 bg-white text-xs font-semibold text-charcoal focus:border-forest"
              >
                <option value="Any">Any Standard (Engine Best Match)</option>
                <option value="Budget">Budget Hostels & Lodges (₹800 - ₹2,500)</option>
                <option value="Comfort">Comfort Hotels & Resorts (₹2,500 - ₹5,000)</option>
                <option value="Premium">Premium Boutique (₹5,000 - ₹10,000)</option>
                <option value="Heritage">Heritage & Luxury Palaces (₹12,000+)</option>
              </select>
            </div>

            <div>
              <label className="block text-xs font-bold text-charcoal/70 uppercase tracking-wider mb-1.5">
                Inter-City Transport Mode
              </label>
              <select
                value={transportPreference}
                onChange={(e) => setTransportPreference(e.target.value)}
                className="w-full p-3 rounded-xl border border-sage/40 bg-white text-xs font-semibold text-charcoal focus:border-forest"
              >
                <option value="ANY">Any / Fastest Available (Engine Selects)</option>
                <option value="FLIGHT">Flight Priority (Fastest Transit)</option>
                <option value="TRAIN">Train Priority (Scenic & Cost-Effective)</option>
                <option value="BUS">Bus / Highway (Budget Express)</option>
              </select>
            </div>

            <div>
              <label className="block text-xs font-bold text-charcoal/70 uppercase tracking-wider mb-1.5">
                Pace / Travel Style
              </label>
              <select
                value={travelStyle}
                onChange={(e) => setTravelStyle(e.target.value)}
                className="w-full p-3 rounded-xl border border-sage/40 bg-white text-xs font-semibold text-charcoal focus:border-forest"
              >
                <option value="relaxed">Relaxed (1-2 major spots/day, leisurely)</option>
                <option value="balanced">Balanced (2-3 spots/day, culture & rest)</option>
                <option value="packed">Packed (3-4 spots/day, maximize sights)</option>
              </select>
            </div>
          </div>

        </div>

        {/* SECTION 3: INTERESTS & ATTRACTIONS */}
        <div className="space-y-4">
          <h3 className="font-serif-heading text-lg font-bold text-forest border-b border-sage/30 pb-2 flex items-center gap-2">
            <Compass className="w-4 h-4 text-forest" />
            <span>3. Interests & Sightseeing Preferences</span>
          </h3>

          <div>
            <label className="block text-xs font-bold text-charcoal/70 uppercase tracking-wider mb-2">
              Select Interest Themes (Aho-Corasick Multi-Pattern Matching)
            </label>
            <div className="flex flex-wrap gap-2">
              {availableTags.map((tag) => {
                const selected = interests.includes(tag);
                return (
                  <button
                    type="button"
                    key={tag}
                    onClick={() => toggleInterest(tag)}
                    className={`px-3.5 py-1.5 rounded-xl text-xs font-semibold capitalize transition-all flex items-center gap-1.5 ${
                      selected
                        ? 'bg-forest text-mutedLime border border-forest-800 shadow-xs'
                        : 'bg-ivory/60 text-charcoal/70 hover:bg-sage/20 border border-sage/30'
                    }`}
                  >
                    {selected && <CheckCircle2 className="w-3.5 h-3.5 text-mutedLime" />}
                    <span>{tag}</span>
                  </button>
                );
              })}
            </div>
          </div>

          {/* Must-Visit Attractions in Destination */}
          {availableAttractions.length > 0 && (
            <div className="pt-2">
              <label className="block text-xs font-bold text-charcoal/70 uppercase tracking-wider mb-1.5">
                Must-Visit Attractions in {selectedDestinations[0]} (Knapsack Priority)
              </label>
              <div className="flex flex-wrap gap-2 max-h-32 overflow-y-auto p-2 bg-ivory/30 rounded-xl border border-sage/30">
                {availableAttractions.map((a) => {
                  const selected = mustVisitAttractions.includes(a.name);
                  return (
                    <button
                      type="button"
                      key={a.id}
                      onClick={() => {
                        if (selected) {
                          setMustVisitAttractions(mustVisitAttractions.filter(n => n !== a.name));
                        } else {
                          setMustVisitAttractions([...mustVisitAttractions, a.name]);
                        }
                      }}
                      className={`text-xs px-2.5 py-1 rounded-lg border text-left transition-colors ${
                        selected
                          ? 'bg-forest text-ivory border-forest font-semibold'
                          : 'bg-white text-charcoal/80 border-sage/30 hover:border-forest/40'
                      }`}
                    >
                      {a.name} ({a.category})
                    </button>
                  );
                })}
              </div>
            </div>
          )}

        </div>

        {/* SUBMIT BUTTON & PROGRESS STATE */}
        <div className="pt-4 border-t border-sage/30 space-y-3">
          <button
            type="submit"
            disabled={loading}
            className="w-full py-4 rounded-2xl bg-forest hover:bg-forest-600 disabled:bg-charcoal/40 text-ivory font-bold text-base shadow-soft hover:shadow-card transition-all flex items-center justify-center gap-2 group"
          >
            {loading ? (
              <div className="flex items-center gap-3">
                <div className="w-5 h-5 rounded-full border-2 border-ivory border-t-transparent animate-spin" />
                <span>Processing TravelSync Engine Pipeline...</span>
              </div>
            ) : (
              <>
                <Sparkles className="w-5 h-5 text-mutedLime group-hover:rotate-12 transition-transform" />
                <span>Generate My Trip (Run TravelSync DSA Engine)</span>
              </>
            )}
          </button>

          {/* Animated Algorithm Stage Tracker */}
          {loading && (
            <div className="p-3 rounded-xl bg-forest-50 border border-forest-200 text-xs text-forest-800 text-center font-mono animate-pulse">
              {loadingStage}
            </div>
          )}

          <div className="flex items-center justify-center gap-4 text-[11px] text-charcoal/60 pt-1">
            <span className="flex items-center gap-1">
              <CheckCircle2 className="w-3.5 h-3.5 text-forest" /> Real Indian Datasets
            </span>
            <span className="flex items-center gap-1">
              <CheckCircle2 className="w-3.5 h-3.5 text-forest" /> Zero Fabricated Itineraries
            </span>
            <span className="flex items-center gap-1">
              <CheckCircle2 className="w-3.5 h-3.5 text-forest" /> Deterministic Java Algorithms
            </span>
          </div>
        </div>

      </form>

    </div>
  );
}
