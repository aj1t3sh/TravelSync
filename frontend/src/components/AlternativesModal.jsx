import React from 'react';
import { X, Sparkles, Check, ArrowRight, ShieldCheck, DollarSign, Hotel, Train, Plane } from 'lucide-react';

export default function AlternativesModal({ alternatives = [], currentPlan, onClose, onApplyAlternative }) {
  if (!alternatives || alternatives.length === 0) return null;

  return (
    <div className="fixed inset-0 z-50 overflow-y-auto bg-charcoal/60 backdrop-blur-sm flex items-center justify-center p-4 animate-fadeIn">
      <div className="bg-white rounded-3xl max-w-3xl w-full p-6 sm:p-8 shadow-elevated border border-sage/40 relative space-y-6">
        
        {/* Close Button */}
        <button
          onClick={onClose}
          className="absolute top-5 right-5 w-8 h-8 rounded-full bg-ivory hover:bg-sage/20 text-charcoal/70 flex items-center justify-center transition-colors"
        >
          <X className="w-5 h-5" />
        </button>

        <div className="space-y-1">
          <div className="flex items-center gap-2">
            <span className="text-[11px] font-bold px-2 py-0.5 rounded-full bg-forest text-mutedLime uppercase tracking-wider">
              Algorithmic Tradeoff Analysis
            </span>
          </div>
          <h3 className="font-serif-heading text-2xl font-bold text-forest">
            Compare Itinerary Alternatives
          </h3>
          <p className="text-xs text-charcoal/70">
            The TravelSync optimization engine dynamically computed these variants by varying accommodation tiers and transport flow capacities.
          </p>
        </div>

        {/* Current Active Plan summary banner */}
        {currentPlan && (
          <div className="p-4 rounded-xl bg-ivory border border-sage/40 flex items-center justify-between text-xs">
            <div>
              <span className="text-[10px] text-charcoal/50 uppercase block font-semibold">Active Plan</span>
              <strong className="text-forest text-sm">{currentPlan.origin} ➔ {currentPlan.destination}</strong>
              <div className="text-charcoal/70">{currentPlan.days} Days • {currentPlan.travelers} Traveler(s)</div>
            </div>
            <div className="text-right font-mono">
              <span className="text-charcoal/50 text-[10px] uppercase block">Total Cost</span>
              <strong className="text-forest text-base">₹{currentPlan.totalEstimatedCostInr?.toLocaleString()}</strong>
            </div>
          </div>
        )}

        {/* Alternatives Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4 pt-2">
          {alternatives.map((alt, idx) => (
            <div 
              key={idx}
              className="p-5 rounded-2xl border border-sage/40 bg-white hover:border-forest/50 hover:shadow-card transition-all flex flex-col justify-between space-y-4"
            >
              <div className="space-y-3">
                <div className="flex items-center justify-between">
                  <h4 className="font-bold text-forest text-base">
                    {alt.name}
                  </h4>
                  <span className="text-xs font-mono font-bold text-forest bg-sage/20 px-2 py-1 rounded">
                    ~₹{alt.estimatedCostInr?.toLocaleString()}
                  </span>
                </div>

                <p className="text-xs text-charcoal/75 leading-relaxed">
                  {alt.description}
                </p>

                <div className="space-y-1.5 text-xs font-mono pt-2 border-t border-sage/20">
                  <div className="flex items-center justify-between text-charcoal/70">
                    <span>Stay Class:</span>
                    <strong className="text-forest">{alt.accommodationType}</strong>
                  </div>
                  <div className="flex items-center justify-between text-charcoal/70">
                    <span>Transport:</span>
                    <strong className="text-forest">{alt.transportMode}</strong>
                  </div>
                  <div className="pt-1 text-[11px] text-emerald-800 font-sans italic bg-emerald-50 p-2 rounded border border-emerald-200">
                    {alt.tradeoff}
                  </div>
                </div>
              </div>

              <button
                onClick={() => {
                  onApplyAlternative(alt);
                  onClose();
                }}
                className="w-full py-2.5 rounded-xl bg-forest hover:bg-forest-600 text-ivory text-xs font-semibold shadow-soft hover:shadow-card transition-all flex items-center justify-center gap-1.5"
              >
                <span>Select & Re-optimize This Variant</span>
                <ArrowRight className="w-3.5 h-3.5 text-mutedLime" />
              </button>
            </div>
          ))}
        </div>

        <div className="pt-2 text-[11px] text-charcoal/60 text-center">
          * Alternatives satisfy all safety and travel day constraints; costs reflect authentic dataset inventory rates.
        </div>

      </div>
    </div>
  );
}
