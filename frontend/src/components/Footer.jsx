import React from 'react';
import { Compass, BookOpen, GitBranch, Cpu, ShieldCheck, Heart } from 'lucide-react';

export default function Footer({ setActiveTab }) {
  return (
    <footer className="bg-forest-900 text-ivory/80 pt-16 pb-12 border-t border-forest-800">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-10 mb-12">
          
          {/* Brand Info */}
          <div className="space-y-4 md:col-span-1">
            <div className="flex items-center gap-3">
              <div className="w-9 h-9 rounded-lg bg-forest-600 flex items-center justify-center text-ivory">
                <Compass className="w-5 h-5 text-mutedLime" />
              </div>
              <span className="font-serif-heading text-xl font-bold text-ivory">TravelSync</span>
            </div>
            <p className="text-xs text-ivory/70 leading-relaxed">
              Intelligent multi-stop travel itinerary planning and transit bottleneck optimization platform powered by algorithmic computer science.
            </p>
            <div className="text-xs text-sage font-mono">
              Pure Java 17 Core DSA Engine + React UI
            </div>
          </div>

          {/* Quick Links */}
          <div className="space-y-3">
            <h4 className="text-sm font-semibold text-ivory uppercase tracking-wider font-mono">Explore</h4>
            <ul className="space-y-2 text-xs">
              <li>
                <button onClick={() => setActiveTab('discover')} className="hover:text-mutedLime transition-colors">
                  Discover Hubs
                </button>
              </li>
              <li>
                <button onClick={() => setActiveTab('destinations')} className="hover:text-mutedLime transition-colors">
                  All Destinations (20)
                </button>
              </li>
              <li>
                <button onClick={() => setActiveTab('planner')} className="hover:text-mutedLime transition-colors">
                  Interactive Planner
                </button>
              </li>
              <li>
                <button onClick={() => setActiveTab('saved')} className="hover:text-mutedLime transition-colors">
                  Saved Itineraries
                </button>
              </li>
            </ul>
          </div>

          {/* Course Outcomes Matrix */}
          <div className="space-y-3">
            <h4 className="text-sm font-semibold text-ivory uppercase tracking-wider font-mono">Curriculum Syllabus</h4>
            <div className="flex flex-wrap gap-1.5 text-[11px]">
              <span className="px-2 py-0.5 rounded bg-forest-800 text-mutedLime border border-forest-700">CO1 Classification</span>
              <span className="px-2 py-0.5 rounded bg-forest-800 text-mutedLime border border-forest-700">CO2 KMP & Suffix</span>
              <span className="px-2 py-0.5 rounded bg-forest-800 text-mutedLime border border-forest-700">CO3 Bitmask TSP</span>
              <span className="px-2 py-0.5 rounded bg-forest-800 text-mutedLime border border-forest-700">CO4 Dinic & Matching</span>
              <span className="px-2 py-0.5 rounded bg-forest-800 text-mutedLime border border-forest-700">CO5 2-Approximation</span>
              <span className="px-2 py-0.5 rounded bg-forest-800 text-mutedLime border border-forest-700">CO6 Parallel Scan</span>
            </div>
            <div className="pt-2">
              <button 
                onClick={() => setActiveTab('algorithms')} 
                className="text-xs text-mutedLime hover:underline flex items-center gap-1"
              >
                <Cpu className="w-3.5 h-3.5" />
                Open DSA Interactive Lab &rarr;
              </button>
            </div>
          </div>

          {/* Academic Context */}
          <div className="space-y-3">
            <h4 className="text-sm font-semibold text-ivory uppercase tracking-wider font-mono">Academic Project</h4>
            <p className="text-xs text-ivory/70 leading-relaxed">
              <strong>DSA-3 (Course Code: 25CS2103E)</strong><br />
              Project No. 12: TravelSync — Intelligent Travel Planning & Information Platform.<br />
              Department of Computer Science & Engineering,<br />
              KL Deemed to be University.
            </p>
          </div>
        </div>

        {/* Bottom bar */}
        <div className="border-t border-forest-800/80 pt-8 flex flex-col sm:flex-row items-center justify-between text-xs text-ivory/60 gap-4">
          <p>© {new Date().getFullYear()} TravelSync. Authenticated with Indian Tourism Datasets.</p>
          <div className="flex items-center gap-4 text-xs">
            <span className="flex items-center gap-1 text-sage">
              <ShieldCheck className="w-3.5 h-3.5" /> Verified Against Syllabus Handout (CO1 - CO6)
            </span>
          </div>
        </div>
      </div>
    </footer>
  );
}
