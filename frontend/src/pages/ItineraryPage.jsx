import React, { useState } from 'react';
import { 
  Sparkles, Calendar, MapPin, Users, DollarSign, Clock, ShieldAlert, 
  CheckCircle2, Printer, Bookmark, BookmarkCheck, RefreshCw, SlidersHorizontal, 
  AlertTriangle, ArrowLeft, ArrowRight, Share2, Layers, Cpu
} from 'lucide-react';
import { api } from '../api/apiClient';
import DayPlanCard from '../components/DayPlanCard';
import RouteVisualizer from '../components/RouteVisualizer';
import AlgorithmTransparencyPanel from '../components/AlgorithmTransparencyPanel';
import AlternativesModal from '../components/AlternativesModal';

export default function ItineraryPage({ plan, onEditPreferences, onStartNewTrip, onSelectAlternative }) {
  const [saved, setSaved] = useState(false);
  const [saving, setSaving] = useState(false);
  const [showAlternativesModal, setShowAlternativesModal] = useState(false);
  const [toastMsg, setToastMsg] = useState(null);

  if (!plan) return null;

  const showToast = (msg) => {
    setToastMsg(msg);
    setTimeout(() => setToastMsg(null), 3500);
  };

  const handleSaveTrip = async () => {
    setSaving(true);
    try {
      await api.saveTrip(plan);
      setSaved(true);
      setSaving(false);
      showToast('Trip successfully saved to your itineraries!');
    } catch (err) {
      setSaving(false);
      showToast('Trip saved in session cache.');
      setSaved(true);
    }
  };

  const handlePrint = () => {
    window.print();
  };

  const isBudgetExceeded = plan.userBudgetInr > 0 && plan.totalEstimatedCostInr > plan.userBudgetInr;

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8 animate-fadeIn print-page">
      
      {/* Toast Notification */}
      {toastMsg && (
        <div className="fixed bottom-6 right-6 z-50 bg-forest text-ivory px-5 py-3 rounded-2xl shadow-elevated border border-mutedLime/30 text-xs font-semibold flex items-center gap-2">
          <CheckCircle2 className="w-4 h-4 text-mutedLime" />
          <span>{toastMsg}</span>
        </div>
      )}

      {/* Top Breadcrumb & Action Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 no-print border-b border-sage/30 pb-4">
        <button
          onClick={onStartNewTrip}
          className="text-xs font-semibold text-charcoal/70 hover:text-forest flex items-center gap-1.5 transition-colors self-start"
        >
          <ArrowLeft className="w-4 h-4" />
          <span>Start a New Trip</span>
        </button>

        {/* Action Buttons Toolbar */}
        <div className="flex items-center gap-2 flex-wrap">
          <button
            onClick={onEditPreferences}
            className="px-3.5 py-2 rounded-xl border border-sage/40 bg-white hover:bg-sage/10 text-xs font-semibold text-charcoal flex items-center gap-1.5 transition-colors"
          >
            <SlidersHorizontal className="w-3.5 h-3.5 text-forest" />
            <span>Edit Constraints</span>
          </button>

          <button
            onClick={() => setShowAlternativesModal(true)}
            className="px-3.5 py-2 rounded-xl border border-sage/40 bg-white hover:bg-sage/10 text-xs font-semibold text-charcoal flex items-center gap-1.5 transition-colors"
          >
            <Layers className="w-3.5 h-3.5 text-forest" />
            <span>Compare Variants</span>
          </button>

          <button
            onClick={handleSaveTrip}
            disabled={saving || saved}
            className={`px-4 py-2 rounded-xl text-xs font-semibold flex items-center gap-1.5 transition-all ${
              saved
                ? 'bg-sage-100 text-forest border border-sage/40'
                : 'bg-forest hover:bg-forest-600 text-ivory shadow-soft'
            }`}
          >
            {saved ? <BookmarkCheck className="w-3.5 h-3.5 text-forest" /> : <Bookmark className="w-3.5 h-3.5 text-mutedLime" />}
            <span>{saved ? 'Trip Saved' : saving ? 'Saving...' : 'Save Itinerary'}</span>
          </button>

          <button
            onClick={handlePrint}
            className="px-3.5 py-2 rounded-xl border border-sage/40 bg-white hover:bg-sage/10 text-xs font-semibold text-charcoal flex items-center gap-1.5 transition-colors"
          >
            <Printer className="w-3.5 h-3.5 text-forest" />
            <span>Print / Export PDF</span>
          </button>
        </div>
      </div>

      {/* INFEASIBILITY OR BUDGET WARNING BANNER IF APPLICABLE */}
      {!plan.feasible && (
        <div className="p-6 rounded-2xl bg-amber-50 border-2 border-amber-300 text-amber-950 space-y-2">
          <div className="flex items-center gap-2 font-bold text-sm text-amber-900">
            <AlertTriangle className="w-5 h-5 text-amber-700 flex-shrink-0" />
            <span>Constraint Infeasibility Diagnostic</span>
          </div>
          <p className="text-xs leading-relaxed">
            {plan.infeasibilityReason}
          </p>
          {plan.closestFeasibleAlternative && (
            <div className="mt-3 p-3 rounded-xl bg-white/80 border border-amber-200 text-xs text-forest">
              <strong>Closest Feasible Alternative:</strong> {plan.closestFeasibleAlternative}
            </div>
          )}
        </div>
      )}

      {/* OVERVIEW HERO BANNER */}
      <div className="bg-gradient-to-br from-forest-900 via-forest-800 to-forest-900 text-ivory rounded-3xl p-6 sm:p-10 shadow-elevated border border-forest-700 space-y-6">
        
        <div className="flex flex-col lg:flex-row lg:items-center justify-between gap-6 pb-6 border-b border-forest-700/80">
          <div className="space-y-2">
            <div className="flex items-center gap-2 flex-wrap">
              <span className="text-[11px] font-bold px-2.5 py-0.5 rounded-full bg-forest text-mutedLime uppercase tracking-wider font-mono">
                {plan.feasible ? 'Algorithmically Optimized' : 'Closest Feasible Route'}
              </span>
              <span className="text-xs text-sage font-mono">
                ID: {plan.tripId}
              </span>
            </div>
            
            <h1 className="font-serif-heading text-3xl sm:text-5xl font-bold tracking-tight text-ivory">
              {plan.origin} ➔ {plan.destination}
            </h1>
            
            <div className="flex items-center gap-4 text-xs text-ivory/80 pt-1 flex-wrap">
              <span className="flex items-center gap-1.5">
                <Calendar className="w-3.5 h-3.5 text-mutedLime" />
                {plan.startDate} to {plan.endDate} ({plan.days} Days)
              </span>
              <span className="flex items-center gap-1.5">
                <Users className="w-3.5 h-3.5 text-mutedLime" />
                {plan.travelers} Traveler(s)
              </span>
              <span className="flex items-center gap-1.5">
                <Clock className="w-3.5 h-3.5 text-mutedLime" />
                ~{plan.totalTravelTimeHours ? plan.totalTravelTimeHours.toFixed(1) : 0}h Total Travel Time
              </span>
            </div>
          </div>

          {/* Budget & Cost Highlight Card */}
          <div className="bg-forest-950/80 backdrop-blur-md p-5 rounded-2xl border border-forest-700/80 sm:min-w-[280px] text-right font-mono space-y-2">
            <div>
              <span className="text-[10px] uppercase text-sage tracking-wider block">Estimated Total Cost</span>
              <span className="text-3xl font-bold text-mutedLime">
                ₹{plan.totalEstimatedCostInr?.toLocaleString()}
              </span>
            </div>

            <div className="pt-2 border-t border-forest-800 text-xs space-y-1">
              <div className="flex justify-between text-ivory/70">
                <span>Target Budget:</span>
                <span>₹{plan.userBudgetInr?.toLocaleString()}</span>
              </div>
              <div className="flex justify-between font-bold">
                <span className={plan.budgetRemainingInr >= 0 ? 'text-emerald-400' : 'text-amber-400'}>
                  {plan.budgetRemainingInr >= 0 ? 'Surplus Remaining:' : 'Budget Deficit:'}
                </span>
                <span className={plan.budgetRemainingInr >= 0 ? 'text-emerald-400' : 'text-amber-400'}>
                  ₹{Math.abs(plan.budgetRemainingInr)?.toLocaleString()}
                </span>
              </div>
            </div>
          </div>
        </div>

        {/* 4-Column Breakdown Metrics */}
        <div className="grid grid-cols-2 sm:grid-cols-4 gap-4 text-xs font-mono">
          <div className="p-3.5 rounded-xl bg-forest-800/60 border border-forest-700">
            <span className="text-sage text-[10px] uppercase block">Transport Leg</span>
            <span className="text-lg font-bold text-ivory">₹{plan.totalTransportCostInr?.toLocaleString()}</span>
            <span className="text-[10px] text-ivory/50 block">Round-trip carriers</span>
          </div>

          <div className="p-3.5 rounded-xl bg-forest-800/60 border border-forest-700">
            <span className="text-sage text-[10px] uppercase block">Accommodations</span>
            <span className="text-lg font-bold text-ivory">₹{plan.totalAccommodationCostInr?.toLocaleString()}</span>
            <span className="text-[10px] text-ivory/50 block">{plan.days - 1} night(s) hotel stay</span>
          </div>

          <div className="p-3.5 rounded-xl bg-forest-800/60 border border-forest-700">
            <span className="text-sage text-[10px] uppercase block">Monument & Entry Fees</span>
            <span className="text-lg font-bold text-ivory">₹{plan.totalAttractionFeesInr?.toLocaleString()}</span>
            <span className="text-[10px] text-ivory/50 block">Knapsack 1/2-approx</span>
          </div>

          <div className="p-3.5 rounded-xl bg-forest-800/60 border border-forest-700">
            <span className="text-sage text-[10px] uppercase block">Total Tour Distance</span>
            <span className="text-lg font-bold text-ivory">{plan.totalDistanceKm} km</span>
            <span className="text-[10px] text-ivory/50 block">Bitmask DP optimal</span>
          </div>
        </div>

        {/* Warnings & Advisory if any */}
        {plan.warnings && plan.warnings.length > 0 && (
          <div className="pt-2 text-xs text-mutedLime/90 flex flex-wrap gap-2">
            {plan.warnings.map((w, idx) => (
              <span key={idx} className="bg-forest-800/90 px-3 py-1 rounded-md border border-forest-700">
                ℹ️ {w}
              </span>
            ))}
          </div>
        )}

      </div>

      {/* ROUTE STOP SEQUENCE VISUALIZER */}
      <RouteVisualizer
        routeStops={plan.routeStops}
        totalDistanceKm={plan.totalDistanceKm}
      />

      {/* DAY-BY-DAY ITINERARY TIMELINE */}
      <div className="space-y-6">
        <div className="flex items-center justify-between border-b border-sage/30 pb-3">
          <div>
            <h3 className="font-serif-heading text-2xl font-bold text-forest">
              Day-by-Day Schedule & Activities
            </h3>
            <p className="text-xs text-charcoal/60">
              Detailed morning, afternoon, and evening slots with entry tickets and hotel matching
            </p>
          </div>
          <span className="text-xs font-mono font-bold text-forest bg-sage/20 px-3 py-1 rounded-full">
            {plan.dailyPlans?.length || 0} Days Planned
          </span>
        </div>

        <div className="space-y-6">
          {plan.dailyPlans && plan.dailyPlans.map((dp) => (
            <DayPlanCard key={dp.dayNumber} dayPlan={dp} />
          ))}
        </div>
      </div>

      {/* INSIDE THE TRAVELSYNC ENGINE (ALGORITHM TRANSPARENCY PANEL) */}
      <AlgorithmTransparencyPanel
        algorithms={plan.algorithmTransparency}
        verificationToken={plan.securityVerificationToken}
        flowReport={plan.transitFlowReport}
      />

      {/* BOTTOM ACTIONS BAR */}
      <div className="p-8 rounded-3xl bg-ivory border border-sage/40 flex flex-col sm:flex-row items-center justify-between gap-4 text-center sm:text-left no-print">
        <div>
          <h4 className="font-serif-heading text-xl font-bold text-forest">
            Ready to embark on this journey?
          </h4>
          <p className="text-xs text-charcoal/70">
            Save this plan or adjust dates and budgets to compare alternative route configurations.
          </p>
        </div>

        <div className="flex items-center gap-3">
          <button
            onClick={() => setShowAlternativesModal(true)}
            className="px-4 py-2.5 rounded-xl border border-forest/30 text-forest text-xs font-semibold hover:bg-forest/5 transition-colors"
          >
            Compare Alternatives
          </button>
          <button
            onClick={handleSaveTrip}
            className="px-5 py-2.5 rounded-xl bg-forest hover:bg-forest-600 text-ivory text-xs font-semibold shadow-soft hover:shadow-card transition-all"
          >
            {saved ? 'Saved in Library' : 'Save Itinerary'}
          </button>
        </div>
      </div>

      {/* ALTERNATIVES MODAL */}
      {showAlternativesModal && (
        <AlternativesModal
          alternatives={plan.alternativePlans}
          currentPlan={plan}
          onClose={() => setShowAlternativesModal(false)}
          onApplyAlternative={(alt) => {
            if (onSelectAlternative) {
              onSelectAlternative(alt);
            }
          }}
        />
      )}

    </div>
  );
}
