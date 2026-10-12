import React, { useState, useEffect } from 'react';
import { Compass, Sparkles, MapPin, Calendar, BarChart3, Terminal, BookmarkCheck, CheckCircle2, AlertCircle } from 'lucide-react';
import { api } from '../api/apiClient';

export default function Navbar({ activeTab, setActiveTab }) {
  const [engineStatus, setEngineStatus] = useState({ online: false, checking: true });

  useEffect(() => {
    let isMounted = true;
    const checkBackend = async () => {
      try {
        const res = await api.getHealth();
        if (isMounted) {
          setEngineStatus({ online: res.status === 'UP', checking: false });
        }
      } catch (e) {
        if (isMounted) {
          setEngineStatus({ online: false, checking: false });
        }
      }
    };

    checkBackend();
    const interval = setInterval(checkBackend, 15000);
    return () => {
      isMounted = false;
      clearInterval(interval);
    };
  }, []);

  const navItems = [
    { id: 'discover', label: 'Discover', icon: Compass },
    { id: 'destinations', label: 'Destinations', icon: MapPin },
    { id: 'planner', label: 'Plan My Trip', icon: Sparkles },
    { id: 'saved', label: 'My Itineraries', icon: BookmarkCheck },
    { id: 'analytics', label: 'Analytics', icon: BarChart3 },
    { id: 'algorithms', label: 'Algorithm Lab', icon: Terminal },
  ];

  return (
    <header className="sticky top-0 z-40 bg-ivory/90 backdrop-blur-md border-b border-sage/30 transition-all">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-20">
          
          {/* Logo & Brand */}
          <div 
            className="flex items-center gap-3 cursor-pointer select-none group"
            onClick={() => setActiveTab('discover')}
          >
            <div className="w-11 h-11 rounded-xl bg-forest flex items-center justify-center text-ivory shadow-soft group-hover:scale-105 transition-transform duration-200">
              <Compass className="w-6 h-6 stroke-[2.2] text-mutedLime" />
            </div>
            <div>
              <span className="font-serif-heading text-2xl font-bold tracking-tight text-forest group-hover:text-forest-600 transition-colors">
                Travel<span className="text-sage-600">Sync</span>
              </span>
              <p className="text-[10px] tracking-wider uppercase font-semibold text-charcoal/60 -mt-1">
                Intelligent Travel Engine
              </p>
            </div>
          </div>

          {/* Desktop Navigation Links */}
          <nav className="hidden md:flex items-center gap-1 lg:gap-2">
            {navItems.map((item) => {
              const Icon = item.icon;
              const isActive = activeTab === item.id;
              return (
                <button
                  key={item.id}
                  onClick={() => setActiveTab(item.id)}
                  className={`flex items-center gap-2 px-3.5 py-2 rounded-lg text-sm font-medium transition-all duration-150 ${
                    isActive
                      ? 'bg-forest text-ivory shadow-soft'
                      : 'text-charcoal/80 hover:text-forest hover:bg-sage/20'
                  }`}
                >
                  <Icon className={`w-4 h-4 ${isActive ? 'text-mutedLime' : 'text-forest/70'}`} />
                  {item.label}
                </button>
              );
            })}
          </nav>

          {/* Right Actions: Status & Quick CTA */}
          <div className="flex items-center gap-3">
            {/* Backend status indicator */}
            <div 
              className={`hidden sm:flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-medium border ${
                engineStatus.online
                  ? 'bg-sage-50 text-forest-700 border-sage/40'
                  : 'bg-red-50 text-red-700 border-red-200'
              }`}
              title={engineStatus.online ? 'Java 17 DSA Engine Connected' : 'Connecting to Java API Server'}
            >
              <span className={`w-2 h-2 rounded-full ${engineStatus.online ? 'bg-forest-500 animate-pulse' : 'bg-red-500'}`} />
              <span>{engineStatus.online ? 'DSA Engine Online' : 'Connecting...'}</span>
            </div>

            {/* Plan Button */}
            <button
              onClick={() => setActiveTab('planner')}
              className="bg-forest hover:bg-forest-600 text-ivory text-sm font-semibold px-4 py-2.5 rounded-xl shadow-soft hover:shadow-card transition-all flex items-center gap-2"
            >
              <Sparkles className="w-4 h-4 text-mutedLime" />
              <span>Plan Journey</span>
            </button>
          </div>
        </div>
      </div>
      
      {/* Mobile Navigation Scrollbar */}
      <div className="md:hidden flex overflow-x-auto px-4 py-2 gap-2 border-t border-sage/20 bg-ivory/95 no-scrollbar">
        {navItems.map((item) => {
          const Icon = item.icon;
          const isActive = activeTab === item.id;
          return (
            <button
              key={item.id}
              onClick={() => setActiveTab(item.id)}
              className={`flex items-center gap-1.5 whitespace-nowrap px-3 py-1.5 rounded-lg text-xs font-medium ${
                isActive ? 'bg-forest text-ivory' : 'bg-sage/15 text-charcoal'
              }`}
            >
              <Icon className="w-3.5 h-3.5" />
              {item.label}
            </button>
          );
        })}
      </div>
    </header>
  );
}
