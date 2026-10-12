import React from 'react';
import { Star, MapPin, Calendar, Sparkles, ArrowRight, Tag } from 'lucide-react';
import { getDestinationImage, DEFAULT_FALLBACK_IMAGE } from '../assets/destinationImages';

export default function DestinationCard({ destination, onSelectForPlan, onViewDetail }) {
  if (!destination) return null;

  const imageUrl = getDestinationImage(destination.name);
  const tagList = (destination.tags || '')
    .split(',')
    .map(t => t.trim())
    .filter(Boolean)
    .slice(0, 3);

  const getPriceBadgeStyle = (priceRange) => {
    switch ((priceRange || '').toUpperCase()) {
      case 'BUDGET':
        return 'bg-emerald-100 text-emerald-800 border-emerald-300';
      case 'EXPENSIVE':
        return 'bg-amber-100 text-amber-900 border-amber-300';
      default:
        return 'bg-sage-100 text-forest-800 border-sage/50';
    }
  };

  return (
    <div className="bg-white rounded-2xl border border-sage/30 overflow-hidden shadow-soft hover:shadow-elevated hover:border-forest/40 transition-all duration-300 flex flex-col group">
      
      {/* Destination Image Cover */}
      <div className="relative h-56 overflow-hidden bg-sage/20">
        <img
          src={imageUrl}
          alt={destination.name}
          className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500 ease-out"
          onError={(e) => {
            e.target.onerror = null;
            e.target.src = DEFAULT_FALLBACK_IMAGE;
          }}
          loading="lazy"
        />
        <div className="absolute inset-0 bg-gradient-to-t from-charcoal/80 via-transparent to-black/20" />

        {/* Badges on top */}
        <div className="absolute top-3 left-3 right-3 flex items-center justify-between">
          <span className={`text-[11px] font-bold uppercase tracking-wider px-2.5 py-1 rounded-full border shadow-xs ${getPriceBadgeStyle(destination.priceRange)}`}>
            {destination.priceRange || 'MODERATE'}
          </span>
          
          <div className="flex items-center gap-1 bg-white/90 backdrop-blur-xs text-charcoal font-bold text-xs px-2.5 py-1 rounded-full shadow-xs">
            <Star className="w-3.5 h-3.5 fill-amber-400 text-amber-500" />
            <span>{destination.rating ? destination.rating.toFixed(1) : '8.5'}</span>
            <span className="text-[10px] text-charcoal/50">({destination.reviewCount || 0})</span>
          </div>
        </div>

        {/* Destination Name Overlay */}
        <div className="absolute bottom-3 left-4 right-4 text-white">
          <h3 className="font-serif-heading text-2xl font-bold tracking-tight text-white drop-shadow-sm">
            {destination.name}
          </h3>
          <div className="flex items-center gap-1.5 text-xs text-ivory/90 mt-0.5">
            <MapPin className="w-3.5 h-3.5 text-mutedLime" />
            <span>{destination.state}, India</span>
          </div>
        </div>
      </div>

      {/* Card Content */}
      <div className="p-5 flex-1 flex flex-col justify-between space-y-4">
        
        {/* Description snippet */}
        <p className="text-xs text-charcoal/75 leading-relaxed line-clamp-3">
          {destination.description}
        </p>

        {/* Season & Tags */}
        <div className="space-y-2 pt-2 border-t border-sage/20">
          <div className="flex items-center gap-2 text-xs text-charcoal/70">
            <Calendar className="w-3.5 h-3.5 text-forest flex-shrink-0" />
            <span className="truncate">
              <strong>Best Season:</strong> {destination.bestSeason || 'October to March'}
            </span>
          </div>

          <div className="flex flex-wrap gap-1.5 pt-1">
            {tagList.map((tag, idx) => (
              <span key={idx} className="text-[11px] px-2 py-0.5 rounded-md bg-ivory text-forest font-medium border border-sage/40">
                #{tag}
              </span>
            ))}
          </div>
        </div>

        {/* Actions Bar */}
        <div className="grid grid-cols-2 gap-2 pt-2">
          <button
            onClick={() => onViewDetail && onViewDetail(destination)}
            className="w-full text-xs font-semibold py-2.5 px-3 rounded-xl border border-forest/30 text-forest hover:bg-forest/5 transition-colors text-center"
          >
            Hub Details
          </button>
          <button
            onClick={() => onSelectForPlan && onSelectForPlan(destination.name)}
            className="w-full text-xs font-semibold py-2.5 px-3 rounded-xl bg-forest hover:bg-forest-600 text-ivory shadow-soft hover:shadow-card transition-all flex items-center justify-center gap-1.5"
          >
            <Sparkles className="w-3.5 h-3.5 text-mutedLime" />
            <span>Plan Trip</span>
          </button>
        </div>

      </div>

    </div>
  );
}
