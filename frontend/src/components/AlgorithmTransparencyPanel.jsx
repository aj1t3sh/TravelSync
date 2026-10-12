import React, { useState } from 'react';
import { ChevronDown, ChevronUp, Cpu, CheckCircle2, Clock, Zap, AlertTriangle, ShieldCheck, Layers, ArrowRight } from 'lucide-react';

export default function AlgorithmTransparencyPanel({ algorithms = [], verificationToken, flowReport }) {
  const [isExpanded, setIsExpanded] = useState(true);
  const [selectedCardIndex, setSelectedCardIndex] = useState(null);

  if (!algorithms || algorithms.length === 0) return null;

  return (
    <div className="bg-white rounded-2xl border border-sage/40 shadow-soft overflow-hidden my-8 transition-all">
      {/* Header Bar */}
      <div 
        className="px-6 py-5 bg-gradient-to-r from-forest/5 via-sage/10 to-transparent flex items-center justify-between cursor-pointer select-none border-b border-sage/20"
        onClick={() => setIsExpanded(!isExpanded)}
      >
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-forest text-mutedLime flex items-center justify-center shadow-soft">
            <Cpu className="w-5 h-5" />
          </div>
          <div>
            <div className="flex items-center gap-2">
              <h3 className="text-lg font-bold font-serif-heading text-forest">
                Inside the TravelSync Engine
              </h3>
              <span className="text-[11px] font-semibold tracking-wide uppercase px-2.5 py-0.5 rounded-full bg-forest text-mutedLime">
                CO1 – CO6 Verified
              </span>
            </div>
            <p className="text-xs text-charcoal/70">
              Live algorithmic execution pipeline trace & complexity audit for this specific trip
            </p>
          </div>
        </div>

        <div className="flex items-center gap-3">
          {verificationToken && (
            <div className="hidden sm:flex items-center gap-1.5 px-3 py-1 rounded-md bg-ivory text-forest text-xs font-mono border border-sage/40">
              <ShieldCheck className="w-3.5 h-3.5 text-forest-600" />
              <span>Token: {verificationToken.substring(0, 16)}...</span>
            </div>
          )}
          <button className="text-forest hover:text-forest-600 p-1 rounded-lg hover:bg-sage/20 transition-colors">
            {isExpanded ? <ChevronUp className="w-5 h-5" /> : <ChevronDown className="w-5 h-5" />}
          </button>
        </div>
      </div>

      {/* Expandable Content */}
      {isExpanded && (
        <div className="p-6 space-y-6">
          
          {/* Timeline of Pipeline Stages */}
          <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-2">
            {algorithms.map((algo, idx) => (
              <div 
                key={idx}
                onClick={() => setSelectedCardIndex(selectedCardIndex === idx ? null : idx)}
                className={`p-2.5 rounded-xl border text-left cursor-pointer transition-all duration-150 ${
                  selectedCardIndex === idx
                    ? 'border-forest bg-forest text-ivory shadow-soft'
                    : 'border-sage/30 bg-ivory/50 hover:bg-sage/10 text-charcoal'
                }`}
              >
                <div className="flex items-center justify-between mb-1">
                  <span className={`text-[10px] font-bold px-1.5 py-0.5 rounded ${
                    selectedCardIndex === idx ? 'bg-forest-800 text-mutedLime' : 'bg-forest/10 text-forest'
                  }`}>
                    {algo.courseOutcome}
                  </span>
                  <CheckCircle2 className={`w-3.5 h-3.5 ${
                    selectedCardIndex === idx ? 'text-mutedLime' : 'text-forest-600'
                  }`} />
                </div>
                <div className="text-xs font-semibold truncate" title={algo.name}>
                  {algo.name}
                </div>
                <div className={`text-[10px] font-mono mt-1 ${
                  selectedCardIndex === idx ? 'text-ivory/80' : 'text-charcoal/60'
                }`}>
                  {algo.executionTimeMs > 0 ? `${algo.executionTimeMs.toFixed(3)} ms` : `${algo.executionTimeNs} ns`}
                </div>
              </div>
            ))}
          </div>

          {/* Detailed Cards for each CO */}
          <div className="space-y-4 pt-2">
            {algorithms.map((algo, index) => {
              const isSelected = selectedCardIndex === index;
              return (
                <div 
                  key={index}
                  className={`rounded-xl border p-5 transition-all duration-200 ${
                    isSelected 
                      ? 'border-forest/60 bg-forest-50/40 shadow-soft'
                      : 'border-sage/30 bg-ivory/30 hover:bg-ivory/60'
                  }`}
                >
                  <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-2 pb-3 border-b border-sage/20">
                    <div className="flex items-center gap-2.5 flex-wrap">
                      <span className="px-2.5 py-1 rounded-md text-xs font-bold font-mono bg-forest text-mutedLime">
                        {algo.courseOutcome}
                      </span>
                      <h4 className="text-sm font-bold text-forest">
                        {algo.name}
                      </h4>
                      <span className="text-xs px-2 py-0.5 rounded bg-sage/20 text-charcoal/80 font-medium">
                        Stage: {algo.stage}
                      </span>
                    </div>

                    <div className="flex items-center gap-3 text-xs font-mono text-charcoal/70">
                      <span className="flex items-center gap-1 bg-white px-2 py-1 rounded border border-sage/30">
                        <Clock className="w-3.5 h-3.5 text-forest" />
                        {algo.executionTimeMs > 0 ? `${algo.executionTimeMs.toFixed(3)} ms` : `${algo.executionTimeNs} ns`}
                      </span>
                      <span className="flex items-center gap-1 bg-white px-2 py-1 rounded border border-sage/30 font-semibold text-forest">
                        <Zap className="w-3.5 h-3.5 text-amber-600" />
                        {algo.complexity}
                      </span>
                    </div>
                  </div>

                  {/* Body grid */}
                  <div className="grid grid-cols-1 md:grid-cols-3 gap-4 pt-4 text-xs">
                    <div>
                      <span className="text-charcoal/50 font-medium uppercase text-[10px] tracking-wider block mb-1">
                        Operational Task
                      </span>
                      <p className="text-charcoal leading-relaxed">
                        {algo.description}
                      </p>
                      <div className="mt-2 text-charcoal/80 font-mono bg-white p-2 rounded border border-sage/30">
                        <strong>Input Size:</strong> {algo.inputSize}
                      </div>
                    </div>

                    <div>
                      <span className="text-charcoal/50 font-medium uppercase text-[10px] tracking-wider block mb-1">
                        Concrete Output Computed
                      </span>
                      <div className="bg-white p-2.5 rounded-lg border border-sage/30 text-charcoal font-medium leading-relaxed">
                        {algo.result}
                      </div>
                    </div>

                    <div>
                      <span className="text-charcoal/50 font-medium uppercase text-[10px] tracking-wider block mb-1">
                        Algorithmic Bounds & Limitations
                      </span>
                      <div className="bg-amber-50/70 text-charcoal/90 p-2.5 rounded-lg border border-amber-200/60 leading-relaxed">
                        <div className="flex items-start gap-1.5">
                          <AlertTriangle className="w-3.5 h-3.5 text-amber-700 flex-shrink-0 mt-0.5" />
                          <span>{algo.limitations}</span>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              );
            })}
          </div>

          {/* Transit Flow Bottleneck Summary if available */}
          {flowReport && flowReport.bottleneckCorridors && flowReport.bottleneckCorridors.length > 0 && (
            <div className="p-4 rounded-xl bg-forest-50 border border-forest-200 text-xs">
              <div className="flex items-center justify-between mb-2">
                <span className="font-bold text-forest flex items-center gap-1.5">
                  <Layers className="w-4 h-4 text-forest-600" />
                  CO4 Transport Residual Graph Max-Flow / Min-Cut Isolation:
                </span>
                <span className="font-mono text-forest-700 bg-white px-2 py-0.5 rounded border border-forest-200">
                  Daily Corridor Flow: {flowReport.maxDailyPassengerFlow} pax/day
                </span>
              </div>
              <p className="text-charcoal/80 mb-2">
                Identified S-T Minimum Cut Bottleneck Corridors between {flowReport.originCity} and {flowReport.destinationCity}:
              </p>
              <div className="flex flex-wrap gap-2">
                {flowReport.bottleneckCorridors.map((b, i) => (
                  <span key={i} className="px-2.5 py-1 rounded bg-white font-mono text-[11px] text-charcoal border border-sage/40 shadow-xs">
                    {b}
                  </span>
                ))}
              </div>
            </div>
          )}

        </div>
      )}
    </div>
  );
}
