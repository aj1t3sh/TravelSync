import React, { useState, useEffect } from 'react';
import { Sparkles, Compass, MapPin, Search, ArrowRight, ShieldCheck, Cpu, Star, Calendar, CheckCircle2, TrendingUp, ChevronRight } from 'lucide-react';
import { api } from '../api/apiClient';
import DestinationCard from '../components/DestinationCard';
import DestinationDetailModal from '../components/DestinationDetailModal';
import { getDestinationImage } from '../assets/destinationImages';

export default function HomePage({ setActiveTab, setSelectedDestinationForPlan }) {
  const [destinations, setDestinations] = useState([]);
  const [reviews, setReviews] = useState([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [searchResults, setSearchResults] = useState([]);
  const [searching, setSearching] = useState(false);
  const [selectedModalDest, setSelectedModalDest] = useState(null);

  useEffect(() => {
    let isMounted = true;
    api.getDestinations()
      .then((data) => {
        if (isMounted) setDestinations(data);
      })
      .catch((err) => console.error('Failed to load destinations:', err));

    api.getAnalyticsSummary()
      .then((data) => {
        // Use summary or destinations
      })
      .catch(() => {});

    return () => { isMounted = false; };
  }, []);

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    if (searchQuery.trim()) {
      setActiveTab('destinations');
    }
  };

  const handlePlanDestination = (cityName) => {
    if (setSelectedDestinationForPlan) {
      setSelectedDestinationForPlan(cityName);
    }
    setActiveTab('planner');
  };

  const featuredDestinations = destinations.slice(0, 6);

  return (
    <div className="space-y-20 pb-20">
      
      {/* 1. SCENIC HERO SECTION */}
      <section className="relative overflow-hidden bg-forest-900 text-ivory rounded-b-[2.5rem] shadow-elevated">
        {/* Background Image with Ambient Gradient Overlay */}
        <div className="absolute inset-0 z-0">
          <img
            src="https://images.unsplash.com/photo-1506461883276-594a12b11cf3?auto=format&fit=crop&w=2000&q=85"
            alt="Scenic Travel Adventure"
            className="w-full h-full object-cover opacity-25 scale-105"
          />
          <div className="absolute inset-0 bg-gradient-to-t from-forest-950 via-forest-900/80 to-transparent" />
          <div className="absolute inset-0 bg-[radial-gradient(ellipse_at_top_right,_var(--tw-gradient-stops))] from-forest-700/40 via-transparent to-transparent" />
        </div>

        <div className="relative z-10 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 pt-20 pb-28 text-center space-y-8">
          
          {/* Academic / Algorithm Trust Badge */}
          <div className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-forest-800/80 border border-forest-600/60 text-mutedLime text-xs font-semibold backdrop-blur-md shadow-soft">
            <Cpu className="w-3.5 h-3.5 text-mutedLime animate-pulse" />
            <span>DSA-3 Intelligent Optimization Platform • Bitmask DP & Network Flow</span>
          </div>

          {/* Editorial Display Heading */}
          <div className="space-y-4 max-w-4xl mx-auto">
            <h1 className="font-serif-heading text-4xl sm:text-6xl lg:text-7xl font-bold tracking-tight text-ivory leading-[1.12]">
              Your next adventure, <br className="hidden sm:block" />
              <span className="text-transparent bg-clip-text bg-gradient-to-r from-mutedLime via-sage-200 to-ivory">
                intelligently planned.
              </span>
            </h1>
            <p className="text-base sm:text-xl text-ivory/80 max-w-2xl mx-auto leading-relaxed font-sans">
              TravelSync pairs authentic Indian destination data with provable algorithmic optimization. We reconstruct optimal multi-city tours, solve transit network bottlenecks, and match your preferences with zero guesswork.
            </p>
          </div>

          {/* Quick Search Bar with Instant Querying */}
          <div className="max-w-2xl mx-auto">
            <form onSubmit={handleSearchSubmit} className="relative flex items-center bg-white/95 backdrop-blur-md p-2 rounded-2xl shadow-elevated border border-sage/50">
              <div className="pl-3 text-forest">
                <Search className="w-5 h-5" />
              </div>
              <input
                type="text"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                placeholder="Search destination, monument or city (e.g. Goa, Hyderabad, Taj Mahal)..."
                className="w-full px-3 py-2.5 text-charcoal bg-transparent text-sm focus:outline-none placeholder:text-charcoal/50"
              />
              <button
                type="submit"
                className="bg-forest hover:bg-forest-600 text-ivory font-semibold text-xs sm:text-sm px-5 py-3 rounded-xl transition-all shadow-soft flex items-center gap-1.5 flex-shrink-0"
              >
                <span>Search</span>
                <ArrowRight className="w-4 h-4 text-mutedLime" />
              </button>
            </form>

            {/* Quick popular tags */}
            <div className="flex flex-wrap items-center justify-center gap-2 pt-4 text-xs text-ivory/80">
              <span className="text-ivory/50">Popular Hubs:</span>
              {['Goa', 'Hyderabad', 'Jaipur', 'Manali', 'Udaipur', 'Varanasi'].map((city) => (
                <button
                  key={city}
                  onClick={() => handlePlanDestination(city)}
                  className="px-2.5 py-1 rounded-full bg-forest-800/80 hover:bg-forest-700 text-mutedLime text-[11px] font-medium border border-forest-600/50 transition-colors"
                >
                  {city}
                </button>
              ))}
            </div>
          </div>

          {/* Hero CTAs */}
          <div className="flex flex-col sm:flex-row items-center justify-center gap-4 pt-4">
            <button
              onClick={() => setActiveTab('planner')}
              className="w-full sm:w-auto px-8 py-4 rounded-xl bg-mutedLime hover:bg-mutedLime-400 text-forest font-bold text-base shadow-soft hover:shadow-card transition-all flex items-center justify-center gap-2 group"
            >
              <Sparkles className="w-5 h-5 group-hover:rotate-12 transition-transform" />
              <span>Plan My Journey</span>
            </button>
            <button
              onClick={() => setActiveTab('destinations')}
              className="w-full sm:w-auto px-8 py-4 rounded-xl bg-forest-800 hover:bg-forest-700 text-ivory font-semibold text-base border border-forest-600 transition-all flex items-center justify-center gap-2"
            >
              <Compass className="w-5 h-5 text-mutedLime" />
              <span>Explore Destinations</span>
            </button>
          </div>

        </div>

        {/* Live Metrics Ribbon */}
        <div className="bg-forest-950/90 border-t border-forest-800/80 py-5 px-4">
          <div className="max-w-7xl mx-auto grid grid-cols-2 md:grid-cols-4 gap-6 text-center">
            <div>
              <div className="font-serif-heading text-2xl sm:text-3xl font-bold text-mutedLime">20 Hubs</div>
              <div className="text-[11px] uppercase tracking-wider text-ivory/60 font-medium">Curated Destinations</div>
            </div>
            <div>
              <div className="font-serif-heading text-2xl sm:text-3xl font-bold text-mutedLime">50+ Routes</div>
              <div className="text-[11px] uppercase tracking-wider text-ivory/60 font-medium">Flight, Train & Road Corridors</div>
            </div>
            <div>
              <div className="font-serif-heading text-2xl sm:text-3xl font-bold text-mutedLime">O(K² · 2^K)</div>
              <div className="text-[11px] uppercase tracking-wider text-ivory/60 font-medium">Held-Karp Bitmask DP</div>
            </div>
            <div>
              <div className="font-serif-heading text-2xl sm:text-3xl font-bold text-mutedLime">100% Deterministic</div>
              <div className="text-[11px] uppercase tracking-wider text-ivory/60 font-medium">Verified Algorithmic Engine</div>
            </div>
          </div>
        </div>
      </section>


      {/* 2. FEATURED DESTINATIONS SECTION */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-8">
        <div className="flex flex-col sm:flex-row sm:items-end justify-between gap-4 border-b border-sage/30 pb-4">
          <div>
            <div className="flex items-center gap-2 text-forest font-semibold text-xs tracking-wider uppercase mb-1">
              <MapPin className="w-4 h-4 text-forest" />
              <span>Authentic Tourism Catalog</span>
            </div>
            <h2 className="font-serif-heading text-3xl sm:text-4xl font-bold text-forest">
              Featured Destinations
            </h2>
          </div>
          <button
            onClick={() => setActiveTab('destinations')}
            className="text-xs font-bold text-forest hover:text-forest-600 flex items-center gap-1 group"
          >
            <span>View all 20 destinations</span>
            <ChevronRight className="w-4 h-4 group-hover:translate-x-1 transition-transform" />
          </button>
        </div>

        {/* Cards Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {featuredDestinations.map((dest) => (
            <DestinationCard
              key={dest.id}
              destination={dest}
              onSelectForPlan={handlePlanDestination}
              onViewDetail={(d) => setSelectedModalDest(d)}
            />
          ))}
        </div>
      </section>


      {/* 3. HOW TRAVELSYNC WORKS (CO1 TO CO6 SHOWCASE) */}
      <section className="bg-sage-50/70 border-y border-sage/40 py-16">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-12">
          
          <div className="text-center max-w-3xl mx-auto space-y-3">
            <span className="text-xs font-bold tracking-widest text-forest-700 uppercase bg-sage/30 px-3 py-1 rounded-full">
              Computational Intelligence
            </span>
            <h2 className="font-serif-heading text-3xl sm:text-4xl font-bold text-forest">
              Inside the TravelSync Core Engine
            </h2>
            <p className="text-sm text-charcoal/70 leading-relaxed font-sans">
              Unlike generic booking aggregators with arbitrary mockups, TravelSync is directly powered by core algorithmic computer science from the DSA-3 syllabus (CO1–CO6).
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            
            {/* CO1 Card */}
            <div className="bg-white p-6 rounded-2xl border border-sage/40 shadow-soft space-y-3 hover:border-forest/40 transition-all">
              <div className="flex items-center justify-between">
                <span className="text-[10px] font-bold px-2 py-0.5 rounded bg-forest text-mutedLime font-mono">CO1</span>
                <span className="text-xs font-mono text-charcoal/50">O(1)</span>
              </div>
              <h3 className="font-serif-heading font-bold text-lg text-forest">Problem Classification</h3>
              <p className="text-xs text-charcoal/70 leading-relaxed">
                Evaluates query signatures to systematically select optimal algorithmic strategies across dynamic programming, network flow, and string metrics.
              </p>
              <div className="text-[11px] font-mono text-forest-700 bg-ivory p-2 rounded border border-sage/30">
                Class: ProblemClassifier.java
              </div>
            </div>

            {/* CO2 Card */}
            <div className="bg-white p-6 rounded-2xl border border-sage/40 shadow-soft space-y-3 hover:border-forest/40 transition-all">
              <div className="flex items-center justify-between">
                <span className="text-[10px] font-bold px-2 py-0.5 rounded bg-forest text-mutedLime font-mono">CO2</span>
                <span className="text-xs font-mono text-charcoal/50">O(N + M)</span>
              </div>
              <h3 className="font-serif-heading font-bold text-lg text-forest">Linear String Search & Suffix LCP</h3>
              <p className="text-xs text-charcoal/70 leading-relaxed">
                Applies KMP, Rabin-Karp rolling hashes, Aho-Corasick multi-trie, and Kasai LCP arrays to search destination inventories and analyze review similarity.
              </p>
              <div className="text-[11px] font-mono text-forest-700 bg-ivory p-2 rounded border border-sage/30">
                Algorithms: KMP, Rabin-Karp, Kasai
              </div>
            </div>

            {/* CO3 Card */}
            <div className="bg-white p-6 rounded-2xl border border-sage/40 shadow-soft space-y-3 hover:border-forest/40 transition-all">
              <div className="flex items-center justify-between">
                <span className="text-[10px] font-bold px-2 py-0.5 rounded bg-forest text-mutedLime font-mono">CO3</span>
                <span className="text-xs font-mono text-charcoal/50">O(K² · 2^K)</span>
              </div>
              <h3 className="font-serif-heading font-bold text-lg text-forest">Held-Karp Bitmask DP (TSP)</h3>
              <p className="text-xs text-charcoal/70 leading-relaxed">
                Reconstructs the mathematically exact shortest multi-destination travel sequence over 2^K subset bitmasks, avoiding exponential permutations.
              </p>
              <div className="text-[11px] font-mono text-forest-700 bg-ivory p-2 rounded border border-sage/30">
                Algorithm: BitmaskTSP.java
              </div>
            </div>

            {/* CO4 Card */}
            <div className="bg-white p-6 rounded-2xl border border-sage/40 shadow-soft space-y-3 hover:border-forest/40 transition-all">
              <div className="flex items-center justify-between">
                <span className="text-[10px] font-bold px-2 py-0.5 rounded bg-forest text-mutedLime font-mono">CO4</span>
                <span className="text-xs font-mono text-charcoal/50">O(V² · E)</span>
              </div>
              <h3 className="font-serif-heading font-bold text-lg text-forest">Residual Flow & Min-Cut Bottlenecks</h3>
              <p className="text-xs text-charcoal/70 leading-relaxed">
                Models Indian railway, flight, and bus transit capacities via Dinic / Edmonds-Karp networks, isolating S-T minimum cut transit corridors.
              </p>
              <div className="text-[11px] font-mono text-forest-700 bg-ivory p-2 rounded border border-sage/30">
                Network: Dinic, EdmondsKarp, MinCut
              </div>
            </div>

            {/* CO5 Card */}
            <div className="bg-white p-6 rounded-2xl border border-sage/40 shadow-soft space-y-3 hover:border-forest/40 transition-all">
              <div className="flex items-center justify-between">
                <span className="text-[10px] font-bold px-2 py-0.5 rounded bg-forest text-mutedLime font-mono">CO5</span>
                <span className="text-xs font-mono text-charcoal/50">Ratio ≥ 0.50</span>
              </div>
              <h3 className="font-serif-heading font-bold text-lg text-forest">Knapsack & Vertex Cover Approx</h3>
              <p className="text-xs text-charcoal/70 leading-relaxed">
                Resolves NP-hard monument fee allocation and transit surveillance hubs using provable 1/2-approximation and 2.0x maximal matching heuristics.
              </p>
              <div className="text-[11px] font-mono text-forest-700 bg-ivory p-2 rounded border border-sage/30">
                Theory: 3-SAT Reduction & Approx
              </div>
            </div>

            {/* CO6 Card */}
            <div className="bg-white p-6 rounded-2xl border border-sage/40 shadow-soft space-y-3 hover:border-forest/40 transition-all">
              <div className="flex items-center justify-between">
                <span className="text-[10px] font-bold px-2 py-0.5 rounded bg-forest text-mutedLime font-mono">CO6</span>
                <span className="text-xs font-mono text-charcoal/50">Span O(log N)</span>
              </div>
              <h3 className="font-serif-heading font-bold text-lg text-forest">Parallel Tree Reduction & Primality</h3>
              <p className="text-xs text-charcoal/70 leading-relaxed">
                Aggregates demand and revenues with ForkJoin work-span parallelism, Blelloch prefix scan, and verifies cryptographic tokens with Miller-Rabin.
              </p>
              <div className="text-[11px] font-mono text-forest-700 bg-ivory p-2 rounded border border-sage/30">
                Primitives: BlellochScan, MillerRabin
              </div>
            </div>

          </div>

          <div className="text-center pt-2">
            <button
              onClick={() => setActiveTab('algorithms')}
              className="inline-flex items-center gap-2 text-xs font-bold text-forest bg-white px-5 py-3 rounded-xl border border-sage/40 shadow-soft hover:shadow-card transition-all"
            >
              <span>Explore Interactive DSA Algorithm Lab</span>
              <ArrowRight className="w-4 h-4 text-forest" />
            </button>
          </div>

        </div>
      </section>


      {/* 4. TRAVEL CATEGORIES IN INDIA */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-8">
        <div className="text-center space-y-2">
          <h2 className="font-serif-heading text-3xl font-bold text-forest">
            Explore Indian Travel Styles
          </h2>
          <p className="text-xs text-charcoal/70 max-w-xl mx-auto">
            Filter your journey by heritage monuments, tropical coastlines, tranquil hill stations, or spiritual Ghats.
          </p>
        </div>

        <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
          {[
            { name: 'Royal Heritage & Forts', hubs: 'Jaipur, Hyderabad, Agra', img: 'https://images.unsplash.com/photo-1603262110263-fb010d6e59d4?auto=format&fit=crop&w=600&q=80', query: 'Jaipur' },
            { name: 'Tropical Beaches', hubs: 'Goa, Kochi, Chennai', img: 'https://images.unsplash.com/photo-1512343879784-a960bf40e7f2?auto=format&fit=crop&w=600&q=80', query: 'Goa' },
            { name: 'Himalayan Escapes', hubs: 'Manali, Shimla, Darjeeling', img: 'https://images.unsplash.com/photo-1626621341517-bbf3d9990a23?auto=format&fit=crop&w=600&q=80', query: 'Manali' },
            { name: 'Spiritual Ghats', hubs: 'Varanasi, Rishikesh, Amritsar', img: 'https://images.unsplash.com/photo-1561361513-2d000a50f0dc?auto=format&fit=crop&w=600&q=80', query: 'Varanasi' },
          ].map((cat, i) => (
            <div
              key={i}
              onClick={() => handlePlanDestination(cat.query)}
              className="relative h-64 rounded-2xl overflow-hidden cursor-pointer group shadow-soft border border-sage/30"
            >
              <img
                src={cat.img}
                alt={cat.name}
                className="w-full h-full object-cover group-hover:scale-110 transition-transform duration-500 ease-out"
              />
              <div className="absolute inset-0 bg-gradient-to-t from-charcoal/90 via-charcoal/30 to-transparent" />
              <div className="absolute bottom-4 left-4 right-4 text-white">
                <h4 className="font-serif-heading font-bold text-lg text-white">
                  {cat.name}
                </h4>
                <p className="text-[11px] text-mutedLime mt-0.5">
                  {cat.hubs}
                </p>
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* Destination Detail Modal */}
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
