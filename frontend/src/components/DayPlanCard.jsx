import React from 'react';
import { Sun, CloudSun, Moon, Hotel, Plane, Train, Bus, Clock, MapPin, Tag, CheckCircle2, ChevronRight } from 'lucide-react';

export default function DayPlanCard({ dayPlan }) {
  if (!dayPlan) return null;

  const { dayNumber, date, city, theme, morning, afternoon, evening, hotel, transitLeg, dayCostInr, notes } = dayPlan;

  const renderSlot = (slot, icon, colorClass, timeBg) => {
    if (!slot) return null;
    const Icon = icon;

    return (
      <div className="p-4 rounded-xl border border-sage/30 bg-white/70 hover:bg-white hover:border-forest/40 transition-all flex flex-col md:flex-row justify-between gap-4">
        <div className="flex items-start gap-3">
          <div className={`w-9 h-9 rounded-lg flex items-center justify-center flex-shrink-0 mt-0.5 ${colorClass}`}>
            <Icon className="w-5 h-5" />
          </div>
          <div className="space-y-1">
            <div className="flex items-center gap-2 flex-wrap">
              <span className={`text-[11px] font-bold px-2 py-0.5 rounded font-mono ${timeBg}`}>
                {slot.timeWindow}
              </span>
              <span className="text-[10px] uppercase font-bold tracking-wider px-2 py-0.5 rounded bg-sage/20 text-forest">
                {slot.category}
              </span>
            </div>
            <h4 className="text-sm font-bold text-forest pt-0.5">{slot.title}</h4>
            <p className="text-xs text-charcoal/75 leading-relaxed max-w-2xl">
              {slot.description}
            </p>
          </div>
        </div>

        <div className="md:text-right flex md:flex-col justify-between items-end text-xs flex-shrink-0 font-mono pt-2 md:pt-0 border-t md:border-t-0 border-sage/10">
          <div className="text-forest font-bold text-sm">
            {slot.estimatedCostInr > 0 ? `₹${slot.estimatedCostInr}` : 'Included / Free'}
          </div>
          {slot.durationHours > 0 && (
            <div className="text-charcoal/60 text-[11px] flex items-center gap-1">
              <Clock className="w-3 h-3" />
              ~{slot.durationHours} hrs
            </div>
          )}
        </div>
      </div>
    );
  };

  return (
    <div className="bg-white rounded-2xl border border-sage/40 shadow-soft overflow-hidden transition-all hover:border-forest/30">
      
      {/* Day Header Banner */}
      <div className="bg-gradient-to-r from-forest to-forest-800 text-ivory px-6 py-4 flex flex-col sm:flex-row sm:items-center justify-between gap-2">
        <div className="flex items-center gap-3">
          <div className="w-9 h-9 rounded-xl bg-mutedLime text-forest font-bold font-serif-heading text-lg flex items-center justify-center shadow-xs">
            {dayNumber}
          </div>
          <div>
            <div className="flex items-center gap-2">
              <span className="font-bold text-sm text-ivory tracking-tight">
                Day {dayNumber}: {city}
              </span>
              <span className="text-xs text-sage font-mono">
                • {date}
              </span>
            </div>
            <h3 className="text-xs text-mutedLime/90 font-medium font-serif-heading">
              {theme}
            </h3>
          </div>
        </div>

        <div className="sm:text-right font-mono text-xs">
          <span className="text-sage text-[11px] uppercase block">Est. Day Cost</span>
          <span className="font-bold text-ivory text-sm">₹{dayCostInr.toLocaleString()}</span>
        </div>
      </div>

      {/* Slots Body */}
      <div className="p-5 sm:p-6 space-y-4 bg-ivory/20">
        
        {/* Morning */}
        {renderSlot(morning, Sun, 'bg-amber-100 text-amber-700', 'bg-amber-50 text-amber-800')}

        {/* Afternoon */}
        {renderSlot(afternoon, CloudSun, 'bg-emerald-100 text-emerald-800', 'bg-emerald-50 text-emerald-900')}

        {/* Evening */}
        {renderSlot(evening, Moon, 'bg-indigo-100 text-indigo-800', 'bg-indigo-50 text-indigo-900')}

        {/* Transport Connection Banner if transit day */}
        {transitLeg && (
          <div className="p-3.5 rounded-xl border border-sky-200 bg-sky-50/70 text-xs flex items-center justify-between text-sky-950 font-mono">
            <div className="flex items-center gap-2.5">
              {transitLeg.mode === 'FLIGHT' ? <Plane className="w-4 h-4 text-sky-700" /> :
               transitLeg.mode === 'TRAIN' ? <Train className="w-4 h-4 text-amber-700" /> : <Bus className="w-4 h-4 text-emerald-700" />}
              <span className="font-bold">Inter-City Transit:</span>
              <span>{transitLeg.fromCity} ➔ {transitLeg.toCity} via {transitLeg.mode} ({transitLeg.distanceKm} km, ~{transitLeg.travelTimeHours}h)</span>
            </div>
            <div className="font-bold">₹{transitLeg.costInr}</div>
          </div>
        )}

        {/* Overnight Stay Card */}
        {hotel && (
          <div className="p-4 rounded-xl border border-sage/30 bg-sage-50/50 flex flex-col sm:flex-row sm:items-center justify-between gap-3 text-xs">
            <div className="flex items-center gap-3">
              <div className="w-8 h-8 rounded-lg bg-forest/10 text-forest flex items-center justify-center flex-shrink-0">
                <Hotel className="w-4 h-4" />
              </div>
              <div>
                <div className="flex items-center gap-2">
                  <span className="font-bold text-forest">{hotel.name}</span>
                  <span className="text-[10px] font-bold px-1.5 py-0.2 rounded bg-forest text-mutedLime uppercase font-mono">
                    {hotel.hotelType}
                  </span>
                </div>
                <p className="text-charcoal/60 text-[11px] pt-0.5">
                  {hotel.amenities}
                </p>
              </div>
            </div>

            <div className="sm:text-right font-mono flex sm:flex-col justify-between items-end flex-shrink-0">
              <span className="text-forest font-bold">₹{hotel.pricePerNightInr} / night</span>
              <span className="text-[10px] text-charcoal/60">Rating {hotel.rating}★ • {hotel.matchedRooms} Room(s) matched</span>
            </div>
          </div>
        )}

      </div>

    </div>
  );
}
