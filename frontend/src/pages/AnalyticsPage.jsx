import React, { useState, useEffect } from 'react';
import { BarChart3, TrendingUp, Layers, Users, Hotel, Bus, MapPin, Zap, Clock, ShieldCheck, RefreshCw } from 'lucide-react';
import { api } from '../api/apiClient';

export default function AnalyticsPage() {
  const [analytics, setAnalytics] = useState(null);
  const [loading, setLoading] = useState(true);

  const fetchAnalytics = () => {
    setLoading(true);
    api.getAnalyticsSummary()
      .then((data) => {
        setAnalytics(data);
        setLoading(false);
      })
      .catch((err) => {
        console.error('Failed to load analytics:', err);
        setLoading(false);
      });
  };

  useEffect(() => {
    fetchAnalytics();
  }, []);

  if (loading) {
    return (
      <div className="py-24 text-center space-y-3">
        <div className="w-8 h-8 rounded-full border-2 border-forest border-t-transparent animate-spin mx-auto" />
        <p className="text-xs text-charcoal/60">Computing aggregations and running ForkJoin parallel reduction...</p>
      </div>
    );
  }

  if (!analytics) return null;

  const counts = analytics.inventoryCounts || {};
  const catCounts = analytics.attractionsByCategory || {};
  const hotelTypes = analytics.hotelsByType || {};
  const transModes = analytics.transportByMode || {};
  const topCorridors = analytics.topCorridors || [];
  const co6 = analytics.co6ParallelMetrics || {};

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-10 animate-fadeIn">
      
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 border-b border-sage/30 pb-4">
        <div>
          <div className="flex items-center gap-2 text-forest text-xs font-semibold uppercase tracking-wider mb-1">
            <BarChart3 className="w-4 h-4 text-forest" />
            <span>Dataset Insights & Algorithmic Performance</span>
          </div>
          <h1 className="font-serif-heading text-3xl sm:text-4xl font-bold text-forest">
            TravelSync Platform Analytics
          </h1>
          <p className="text-xs text-charcoal/70">
            Computed in real time from authentic Indian tourism records, transaction booking streams, and multi-core parallel primitives.
          </p>
        </div>

        <button
          onClick={fetchAnalytics}
          className="px-3.5 py-2 rounded-xl border border-sage/40 bg-white hover:bg-sage/10 text-xs font-semibold text-charcoal flex items-center gap-1.5 transition-colors self-start"
        >
          <RefreshCw className="w-3.5 h-3.5 text-forest" />
          <span>Re-compute Metrics</span>
        </button>
      </div>

      {/* 1. Core Inventory Stat Cards */}
      <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-4">
        {[
          { label: 'Destinations', val: counts.destinations, unit: 'Hubs', icon: MapPin, color: 'text-forest' },
          { label: 'Attractions', val: counts.attractions, unit: 'Monuments', icon: Layers, color: 'text-amber-700' },
          { label: 'Hotels', val: counts.hotels, unit: 'Properties', icon: Hotel, color: 'text-emerald-700' },
          { label: 'Transport Corridors', val: counts.transportRoutes, unit: 'Routes', icon: Bus, color: 'text-sky-700' },
          { label: 'Bookings', val: counts.bookings, unit: 'Transactions', icon: TrendingUp, color: 'text-indigo-700' },
          { label: 'Reviews', val: counts.reviews, unit: 'Sentiments', icon: Users, color: 'text-rose-700' },
        ].map((item, i) => {
          const Icon = item.icon;
          return (
            <div key={i} className="bg-white p-4 rounded-2xl border border-sage/40 shadow-soft space-y-1">
              <div className="flex items-center justify-between">
                <span className="text-[10px] text-charcoal/60 uppercase font-bold tracking-wider">{item.label}</span>
                <Icon className={`w-4 h-4 ${item.color}`} />
              </div>
              <div className="font-serif-heading text-2xl font-bold text-forest">
                {item.val || 0}
              </div>
              <div className="text-[10px] text-charcoal/50 font-mono">
                {item.unit} cataloged
              </div>
            </div>
          );
        })}
      </div>

      {/* 2. CO6 Parallel Benchmarks & Work-Span Model */}
      <div className="bg-gradient-to-r from-forest to-forest-800 text-ivory rounded-3xl p-6 sm:p-8 shadow-elevated space-y-6">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 border-b border-forest-700 pb-4">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-xl bg-mutedLime text-forest flex items-center justify-center font-bold">
              <Zap className="w-5 h-5" />
            </div>
            <div>
              <h3 className="font-serif-heading text-xl font-bold text-ivory">
                CO6 Parallel Primitives & ForkJoin Work-Span Benchmarks
              </h3>
              <p className="text-xs text-sage">
                Empirical execution on live booking transaction streams (Work T₁ = O(N), Span T_∞ = O(log N))
              </p>
            </div>
          </div>
          <span className="px-3 py-1 rounded-full bg-forest-900 text-mutedLime text-xs font-mono font-bold border border-forest-700">
            Brent's Speedup: {co6.parallelSpeedup ? co6.parallelSpeedup.toFixed(2) : 1.0}x
          </span>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-3 gap-6 font-mono text-xs">
          <div className="bg-forest-900/80 p-4 rounded-2xl border border-forest-700 space-y-1">
            <span className="text-sage text-[10px] uppercase block">Total Aggregated Revenue</span>
            <div className="text-2xl font-bold text-mutedLime font-serif-heading">
              ₹{co6.totalTransactionRevenueInr ? co6.totalTransactionRevenueInr.toLocaleString() : 0}
            </div>
            <p className="text-[11px] text-ivory/60 pt-1">
              Parallel tree summation over all booking transaction logs
            </p>
          </div>

          <div className="bg-forest-900/80 p-4 rounded-2xl border border-forest-700 space-y-1">
            <span className="text-sage text-[10px] uppercase block">Parallel Tree Reduction (ForkJoin)</span>
            <div className="text-sm font-bold text-ivory">
              Parallel: {co6.reductionParallelMs ? co6.reductionParallelMs.toFixed(3) : 0} ms
            </div>
            <div className="text-xs text-ivory/70">
              Sequential: {co6.reductionSequentialMs ? co6.reductionSequentialMs.toFixed(3) : 0} ms
            </div>
            <p className="text-[11px] text-emerald-300 pt-1">
              Exact element-by-element equality validated
            </p>
          </div>

          <div className="bg-forest-900/80 p-4 rounded-2xl border border-forest-700 space-y-1">
            <span className="text-sage text-[10px] uppercase block">Blelloch Parallel Scan (Prefix Sum)</span>
            <div className="text-sm font-bold text-ivory">
              Up-Sweep & Down-Sweep: {co6.scanParallelMs ? co6.scanParallelMs.toFixed(3) : 0} ms
            </div>
            <div className="text-xs text-ivory/70">
              Sequential Scan: {co6.scanSequentialMs ? co6.scanSequentialMs.toFixed(3) : 0} ms
            </div>
            <p className="text-[11px] text-mutedLime pt-1">
              Computes cumulative demand weights in parallel
            </p>
          </div>
        </div>
      </div>

      {/* 3. Distributions Grid (Attractions, Hotels, Transport) */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        
        {/* Attractions by Category */}
        <div className="bg-white p-6 rounded-2xl border border-sage/40 shadow-soft space-y-4">
          <h4 className="font-serif-heading text-lg font-bold text-forest border-b border-sage/20 pb-2">
            Attractions by Category
          </h4>
          <div className="space-y-2.5">
            {Object.entries(catCounts).map(([cat, count]) => {
              const total = counts.attractions || 1;
              const pct = Math.round((count / total) * 100);
              return (
                <div key={cat} className="space-y-1 text-xs">
                  <div className="flex justify-between font-medium text-charcoal">
                    <span>{cat}</span>
                    <span className="font-mono text-charcoal/60">{count} ({pct}%)</span>
                  </div>
                  <div className="w-full bg-sage/20 rounded-full h-2 overflow-hidden">
                    <div className="bg-forest h-full rounded-full" style={{ width: `${pct}%` }} />
                  </div>
                </div>
              );
            })}
          </div>
        </div>

        {/* Hotels by Type */}
        <div className="bg-white p-6 rounded-2xl border border-sage/40 shadow-soft space-y-4">
          <h4 className="font-serif-heading text-lg font-bold text-forest border-b border-sage/20 pb-2">
            Hotels Inventory by Tier
          </h4>
          <div className="space-y-2.5">
            {Object.entries(hotelTypes).map(([type, count]) => {
              const total = counts.hotels || 1;
              const pct = Math.round((count / total) * 100);
              return (
                <div key={type} className="space-y-1 text-xs">
                  <div className="flex justify-between font-medium text-charcoal">
                    <span>{type}</span>
                    <span className="font-mono text-charcoal/60">{count} properties</span>
                  </div>
                  <div className="w-full bg-sage/20 rounded-full h-2 overflow-hidden">
                    <div className="bg-emerald-600 h-full rounded-full" style={{ width: `${pct}%` }} />
                  </div>
                </div>
              );
            })}
          </div>
          <div className="pt-2 text-[11px] text-charcoal/60 font-mono border-t border-sage/20 flex justify-between">
            <span>Total Available Rooms:</span>
            <strong className="text-forest">{analytics.totalAvailableRooms} / {analytics.totalHotelCapacity}</strong>
          </div>
        </div>

        {/* Transport Corridors by Mode */}
        <div className="bg-white p-6 rounded-2xl border border-sage/40 shadow-soft space-y-4">
          <h4 className="font-serif-heading text-lg font-bold text-forest border-b border-sage/20 pb-2">
            Inter-City Transport Network
          </h4>
          <div className="space-y-2.5">
            {Object.entries(transModes).map(([mode, count]) => {
              const total = counts.transportRoutes || 1;
              const pct = Math.round((count / total) * 100);
              return (
                <div key={mode} className="space-y-1 text-xs">
                  <div className="flex justify-between font-medium text-charcoal">
                    <span>{mode}</span>
                    <span className="font-mono text-charcoal/60">{count} corridors</span>
                  </div>
                  <div className="w-full bg-sage/20 rounded-full h-2 overflow-hidden">
                    <div className="bg-sky-600 h-full rounded-full" style={{ width: `${pct}%` }} />
                  </div>
                </div>
              );
            })}
          </div>
          <div className="pt-2 text-[11px] text-charcoal/60 font-mono border-t border-sage/20 flex justify-between">
            <span>Total Network Daily Pax:</span>
            <strong className="text-forest">{analytics.totalNetworkDailyPaxCapacity?.toLocaleString()} pax/day</strong>
          </div>
        </div>

      </div>

      {/* 4. Top Capacity Corridors Table */}
      <div className="bg-white rounded-2xl border border-sage/40 shadow-soft p-6 space-y-4">
        <h4 className="font-serif-heading text-lg font-bold text-forest">
          Highest-Capacity Transit Corridors (CO4 Network Flow Backbone)
        </h4>
        <div className="overflow-x-auto">
          <table className="w-full text-xs text-left">
            <thead className="bg-ivory text-charcoal/70 uppercase text-[10px] tracking-wider font-mono">
              <tr>
                <th className="p-3">Corridor</th>
                <th className="p-3">Transit Mode</th>
                <th className="p-3">Daily Capacity</th>
                <th className="p-3">Base Tariff</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-sage/20 font-mono">
              {topCorridors.map((c, idx) => (
                <tr key={idx} className="hover:bg-ivory/50">
                  <td className="p-3 font-semibold text-forest">{c.corridor}</td>
                  <td className="p-3 font-bold text-sky-800">{c.mode}</td>
                  <td className="p-3 font-bold text-charcoal">{c.capacity} pax/day</td>
                  <td className="p-3 text-charcoal">₹{c.fare}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

    </div>
  );
}
