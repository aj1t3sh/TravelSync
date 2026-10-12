import React, { useState, useEffect } from 'react';
import { X, Star, MapPin, Calendar, Clock, Hotel, Bus, MessageSquare, Sparkles, Navigation, Layers, ShieldCheck } from 'lucide-react';
import { api } from '../api/apiClient';
import { getDestinationImage, DEFAULT_FALLBACK_IMAGE } from '../assets/destinationImages';

export default function DestinationDetailModal({ destination, onClose, onPlanTrip }) {
  const [activeTab, setActiveTab] = useState('attractions');
  const [loading, setLoading] = useState(true);
  const [detailData, setDetailData] = useState(null);

  useEffect(() => {
    if (!destination) return;
    let isMounted = true;
    setLoading(true);

    api.getDestinationDetail(destination.id || destination.name)
      .then((data) => {
        if (isMounted) {
          setDetailData(data);
          setLoading(false);
        }
      })
      .catch((err) => {
        console.error('Failed to load destination details:', err);
        if (isMounted) setLoading(false);
      });

    return () => { isMounted = false; };
  }, [destination]);

  if (!destination) return null;

  const imageUrl = getDestinationImage(destination.name);
  const d = detailData ? detailData.destination : destination;
  const attractions = detailData ? detailData.attractions : [];
  const hotels = detailData ? detailData.hotels : [];
  const reviews = detailData ? detailData.reviews : [];
  const outboundTransport = detailData ? detailData.outboundTransport : [];

  return (
    <div className="fixed inset-0 z-50 overflow-y-auto bg-charcoal/60 backdrop-blur-sm flex items-center justify-center p-3 sm:p-6 animate-fadeIn">
      <div className="bg-white rounded-3xl max-w-4xl w-full max-h-[92vh] flex flex-col shadow-elevated border border-sage/40 overflow-hidden">
        
        {/* Cover Header */}
        <div className="relative h-64 sm:h-72 w-full flex-shrink-0 bg-sage/30">
          <img
            src={imageUrl}
            alt={d.name}
            className="w-full h-full object-cover"
            onError={(e) => { e.target.src = DEFAULT_FALLBACK_IMAGE; }}
          />
          <div className="absolute inset-0 bg-gradient-to-t from-charcoal/90 via-charcoal/40 to-transparent" />

          {/* Close button */}
          <button
            onClick={onClose}
            className="absolute top-4 right-4 w-9 h-9 rounded-full bg-black/40 hover:bg-black/60 text-white flex items-center justify-center transition-colors"
          >
            <X className="w-5 h-5" />
          </button>

          {/* Title Overlay */}
          <div className="absolute bottom-6 left-6 right-6 text-white flex flex-col sm:flex-row sm:items-end justify-between gap-4">
            <div>
              <div className="flex items-center gap-2 mb-1">
                <span className="text-[11px] font-bold px-2.5 py-0.5 rounded-full bg-forest text-mutedLime tracking-wider uppercase">
                  {d.priceRange || 'MODERATE'}
                </span>
                <span className="text-xs text-white/80 flex items-center gap-1 font-mono">
                  <MapPin className="w-3.5 h-3.5 text-mutedLime" /> {d.city}, {d.state}
                </span>
              </div>
              <h2 className="font-serif-heading text-3xl sm:text-4xl font-bold text-white">
                {d.name}
              </h2>
            </div>

            <div className="flex items-center gap-3">
              <div className="bg-white/20 backdrop-blur-md px-3 py-1.5 rounded-xl flex items-center gap-1.5 text-white">
                <Star className="w-4 h-4 fill-amber-400 text-amber-400" />
                <span className="font-bold text-sm">{d.rating ? d.rating.toFixed(1) : '8.6'}</span>
                <span className="text-xs text-white/80">/ 10.0</span>
              </div>
              <button
                onClick={() => {
                  onClose();
                  onPlanTrip(d.name);
                }}
                className="bg-mutedLime hover:bg-mutedLime-400 text-forest font-bold px-4 py-2 rounded-xl text-xs flex items-center gap-1.5 shadow-soft transition-all"
              >
                <Sparkles className="w-3.5 h-3.5" />
                Plan Trip to {d.name}
              </button>
            </div>
          </div>
        </div>

        {/* Navigation Tabs */}
        <div className="flex border-b border-sage/30 px-6 bg-ivory/60 overflow-x-auto gap-2">
          {[
            { id: 'attractions', label: `Attractions (${attractions.length})`, icon: Sparkles },
            { id: 'hotels', label: `Hotels (${hotels.length})`, icon: Hotel },
            { id: 'transit', label: `Transport (${outboundTransport.length})`, icon: Bus },
            { id: 'reviews', label: `Reviews (${reviews.length})`, icon: MessageSquare },
          ].map((tab) => {
            const Icon = tab.icon;
            const active = activeTab === tab.id;
            return (
              <button
                key={tab.id}
                onClick={() => setActiveTab(tab.id)}
                className={`py-3.5 px-3 border-b-2 text-xs font-semibold whitespace-nowrap flex items-center gap-2 transition-all ${
                  active
                    ? 'border-forest text-forest font-bold'
                    : 'border-transparent text-charcoal/60 hover:text-forest'
                }`}
              >
                <Icon className={`w-3.5 h-3.5 ${active ? 'text-forest' : 'text-charcoal/50'}`} />
                {tab.label}
              </button>
            );
          })}
        </div>

        {/* Modal Body */}
        <div className="p-6 overflow-y-auto space-y-6 flex-1 bg-white">
          
          {/* Destination Description & Metadata */}
          <div className="p-4 rounded-xl bg-ivory/60 border border-sage/30 text-xs text-charcoal/80 space-y-2">
            <p className="leading-relaxed">
              {d.description}
            </p>
            <div className="grid grid-cols-2 sm:grid-cols-4 gap-2 pt-2 border-t border-sage/20 font-mono text-[11px]">
              <div>
                <span className="text-charcoal/50 block">Best Season</span>
                <span className="font-semibold text-forest">{d.bestSeason || 'All Year'}</span>
              </div>
              <div>
                <span className="text-charcoal/50 block">Coordinates</span>
                <span className="font-semibold text-charcoal">{d.latitude ? d.latitude.toFixed(4) : 0}°N, {d.longitude ? d.longitude.toFixed(4) : 0}°E</span>
              </div>
              <div>
                <span className="text-charcoal/50 block">Total Reviews</span>
                <span className="font-semibold text-charcoal">{d.reviewCount || 0} authentic</span>
              </div>
              <div>
                <span className="text-charcoal/50 block">Hub ID</span>
                <span className="font-semibold text-forest">{d.id}</span>
              </div>
            </div>
          </div>

          {loading ? (
            <div className="py-12 text-center text-xs text-charcoal/60">
              Loading authentic dataset inventories...
            </div>
          ) : (
            <>
              {/* ATTRACTIONS TAB */}
              {activeTab === 'attractions' && (
                <div className="space-y-3">
                  {attractions.length === 0 ? (
                    <div className="text-xs text-charcoal/60 italic py-6 text-center">
                      No standalone monuments cataloged in dataset for this city.
                    </div>
                  ) : (
                    attractions.map((a) => (
                      <div key={a.id} className="p-4 rounded-xl border border-sage/30 bg-ivory/30 hover:bg-white hover:border-forest/40 transition-all flex flex-col sm:flex-row justify-between gap-3">
                        <div className="space-y-1">
                          <div className="flex items-center gap-2">
                            <span className="text-[10px] font-bold px-2 py-0.5 rounded bg-sage/30 text-forest">
                              {a.category}
                            </span>
                            <h4 className="text-sm font-bold text-forest">{a.name}</h4>
                            <div className="flex items-center text-xs text-amber-600 font-bold">
                              <Star className="w-3 h-3 fill-amber-400 mr-0.5" />
                              {a.rating}
                            </div>
                          </div>
                          <p className="text-xs text-charcoal/70 leading-relaxed">
                            {a.description}
                          </p>
                          <div className="flex flex-wrap gap-1 pt-1">
                            {(a.tags || '').split(',').map((t, idx) => (
                              <span key={idx} className="text-[10px] px-1.5 py-0.5 bg-white text-charcoal/60 rounded border border-sage/20">
                                #{t.trim()}
                              </span>
                            ))}
                          </div>
                        </div>

                        <div className="sm:text-right flex sm:flex-col justify-between sm:justify-center items-end text-xs flex-shrink-0 font-mono">
                          <div className="text-forest font-bold text-sm">
                            {a.entryFeeInr > 0 ? `₹${a.entryFeeInr}` : 'Free Entry'}
                          </div>
                          <div className="text-charcoal/60 text-[11px] flex items-center gap-1">
                            <Clock className="w-3 h-3" />
                            {a.visitDurationHours} hrs visit
                          </div>
                        </div>
                      </div>
                    ))
                  )}
                </div>
              )}

              {/* HOTELS TAB */}
              {activeTab === 'hotels' && (
                <div className="space-y-3">
                  {hotels.length === 0 ? (
                    <div className="text-xs text-charcoal/60 italic py-6 text-center">
                      No hotels cataloged for this hub.
                    </div>
                  ) : (
                    hotels.map((h) => (
                      <div key={h.id} className="p-4 rounded-xl border border-sage/30 bg-ivory/30 hover:bg-white hover:border-forest/40 transition-all flex flex-col sm:flex-row justify-between gap-3">
                        <div className="space-y-1">
                          <div className="flex items-center gap-2">
                            <span className="text-[10px] font-bold px-2 py-0.5 rounded bg-forest text-ivory">
                              {h.hotelType}
                            </span>
                            <h4 className="text-sm font-bold text-forest">{h.name}</h4>
                            <span className="text-xs text-amber-600 font-bold flex items-center">
                              <Star className="w-3 h-3 fill-amber-400 mr-0.5" />
                              {h.rating}
                            </span>
                          </div>
                          <p className="text-xs text-charcoal/70">
                            <strong>Amenities:</strong> {h.amenities}
                          </p>
                          <div className="text-[11px] text-charcoal/60 font-mono">
                            Available Inventory: {h.availableRooms} / {h.totalCapacity} rooms
                          </div>
                        </div>

                        <div className="sm:text-right flex sm:flex-col justify-between sm:justify-center items-end text-xs flex-shrink-0 font-mono">
                          <div className="text-forest font-bold text-base">
                            ₹{h.pricePerNight}
                            <span className="text-xs font-normal text-charcoal/60"> / night</span>
                          </div>
                          <div className="text-emerald-700 font-semibold text-[11px]">
                            Instant Matching Eligible
                          </div>
                        </div>
                      </div>
                    ))
                  )}
                </div>
              )}

              {/* TRANSIT TAB */}
              {activeTab === 'transit' && (
                <div className="space-y-3">
                  {outboundTransport.length === 0 ? (
                    <div className="text-xs text-charcoal/60 italic py-6 text-center">
                      No direct outbound transport corridors mapped in dataset.
                    </div>
                  ) : (
                    outboundTransport.map((t, idx) => (
                      <div key={idx} className="p-3.5 rounded-xl border border-sage/30 bg-ivory/30 flex items-center justify-between text-xs">
                        <div className="flex items-center gap-3">
                          <span className={`px-2 py-1 rounded font-bold text-[10px] uppercase font-mono ${
                            t.mode === 'FLIGHT' ? 'bg-sky-100 text-sky-800' :
                            t.mode === 'TRAIN' ? 'bg-amber-100 text-amber-800' : 'bg-emerald-100 text-emerald-800'
                          }`}>
                            {t.mode}
                          </span>
                          <div>
                            <div className="font-bold text-forest text-sm">
                              {t.fromCity} ➔ {t.toCity}
                            </div>
                            <div className="text-charcoal/60 text-[11px]">
                              {t.distanceKm} km • ~{t.travelTimeHours} hours duration
                            </div>
                          </div>
                        </div>

                        <div className="text-right font-mono">
                          <div className="font-bold text-forest">₹{t.costInr}</div>
                          <div className="text-[10px] text-charcoal/60">Capacity: {t.capacity} pax/day</div>
                        </div>
                      </div>
                    ))
                  )}
                </div>
              )}

              {/* REVIEWS TAB */}
              {activeTab === 'reviews' && (
                <div className="space-y-3">
                  {reviews.length === 0 ? (
                    <div className="text-xs text-charcoal/60 italic py-6 text-center">
                      No traveler reviews cataloged in dataset for this destination.
                    </div>
                  ) : (
                    reviews.map((r) => (
                      <div key={r.id} className="p-4 rounded-xl border border-sage/30 bg-ivory/30 space-y-2">
                        <div className="flex items-center justify-between text-xs">
                          <div className="flex items-center gap-2">
                            <span className="font-bold text-forest">{r.travelerType || 'Verified Traveler'}</span>
                            <span className="text-charcoal/50 text-[11px] font-mono">{r.reviewDate}</span>
                          </div>
                          <div className="flex items-center text-amber-600 font-bold">
                            <Star className="w-3.5 h-3.5 fill-amber-400 mr-1" />
                            {r.rating}★
                          </div>
                        </div>
                        <p className="text-xs text-charcoal/80 leading-relaxed italic">
                          "{r.reviewText}"
                        </p>
                      </div>
                    ))
                  )}
                </div>
              )}
            </>
          )}

        </div>

      </div>
    </div>
  );
}
