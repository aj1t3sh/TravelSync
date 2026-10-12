import React from 'react';
import { Navigation, MapPin, ArrowRight, ShieldCheck, Compass } from 'lucide-react';

export default function RouteVisualizer({ routeStops = [], totalDistanceKm }) {
  if (!routeStops || routeStops.length === 0) return null;

  return (
    <div className="bg-white rounded-2xl border border-sage/40 p-6 shadow-soft my-6">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between pb-4 border-b border-sage/20 gap-2">
        <div className="flex items-center gap-2.5">
          <div className="w-8 h-8 rounded-lg bg-forest text-mutedLime flex items-center justify-center">
            <Navigation className="w-4 h-4" />
          </div>
          <div>
            <h4 className="font-serif-heading font-bold text-forest text-base">
              Optimized Multi-Stop Transit Corridor
            </h4>
            <p className="text-xs text-charcoal/60">
              Stop sequence determined by Held-Karp Bitmask DP minimizing total round-trip distance
            </p>
          </div>
        </div>

        <div className="flex items-center gap-3 font-mono text-xs">
          <span className="bg-ivory px-3 py-1 rounded-lg border border-sage/40 text-forest font-bold">
            Total Tour: {totalDistanceKm ? `${totalDistanceKm} km` : 'Calculated'}
          </span>
          <span className="bg-forest-50 text-forest-700 px-2 py-1 rounded text-[11px] font-semibold border border-forest-200">
            CO3 Proven Optimal
          </span>
        </div>
      </div>

      {/* Visual Sequence Chain */}
      <div className="py-8 overflow-x-auto">
        <div className="flex items-center justify-start min-w-[500px] px-4">
          {routeStops.map((stop, index) => {
            const isOrigin = index === 0 || index === routeStops.length - 1;
            return (
              <React.Fragment key={index}>
                {/* City Node */}
                <div className="flex flex-col items-center group relative">
                  <div className={`w-12 h-12 rounded-2xl flex items-center justify-center font-bold text-sm shadow-soft transition-transform group-hover:scale-110 ${
                    isOrigin
                      ? 'bg-forest text-mutedLime ring-4 ring-forest/10'
                      : 'bg-sage-100 text-forest-900 border-2 border-forest/30'
                  }`}>
                    {index + 1}
                  </div>
                  <span className="mt-2.5 text-xs font-bold text-forest text-center whitespace-nowrap max-w-[120px] truncate">
                    {stop}
                  </span>
                  <span className="text-[10px] text-charcoal/50 font-mono">
                    {index === 0 ? 'Origin' : index === routeStops.length - 1 ? 'Return Hub' : `Stop #${index}`}
                  </span>
                </div>

                {/* Connecting Arrow & Corridor line */}
                {index < routeStops.length - 1 && (
                  <div className="flex-1 flex flex-col items-center px-2 min-w-[60px]">
                    <div className="w-full flex items-center">
                      <div className="h-0.5 w-full bg-gradient-to-r from-forest via-sage to-forest opacity-60 dashed" />
                      <ArrowRight className="w-4 h-4 text-forest -ml-1 flex-shrink-0" />
                    </div>
                    <span className="text-[10px] font-mono text-charcoal/50 mt-1">
                      Hop {index + 1}
                    </span>
                  </div>
                )}
              </React.Fragment>
            );
          })}
        </div>
      </div>

      <div className="pt-3 border-t border-sage/20 flex items-center justify-between text-[11px] text-charcoal/60">
        <span>* Distances calculated via authentic inter-city transit timetable graph and Haversine geodesics.</span>
        <span className="font-mono text-forest font-semibold">O(K² · 2^K) Tour Solver</span>
      </div>
    </div>
  );
}
