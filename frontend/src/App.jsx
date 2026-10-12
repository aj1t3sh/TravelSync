import React, { useState } from 'react';
import Navbar from './components/Navbar';
import Footer from './components/Footer';
import HomePage from './pages/HomePage';
import DestinationsPage from './pages/DestinationsPage';
import PlannerPage from './pages/PlannerPage';
import ItineraryPage from './pages/ItineraryPage';
import AnalyticsPage from './pages/AnalyticsPage';
import AlgorithmLabPage from './pages/AlgorithmLabPage';
import SavedTripsPage from './pages/SavedTripsPage';

export default function App() {
  const [activeTab, setActiveTab] = useState('discover');
  const [currentPlan, setCurrentPlan] = useState(null);
  const [prefilledDestination, setPrefilledDestination] = useState(null);

  const handlePlanGenerated = (planData) => {
    setCurrentPlan(planData);
    setActiveTab('itinerary');
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const handleSelectDestinationForPlan = (cityName) => {
    setPrefilledDestination(cityName);
    setActiveTab('planner');
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const handleSelectAlternativeVariant = (alt) => {
    // When an alternative is chosen, switch to planner with alternative settings or reload
    setActiveTab('planner');
  };

  return (
    <div className="min-h-screen flex flex-col bg-ivory text-charcoal selection:bg-sage-200 selection:text-forest-900 font-sans">
      
      {/* Sticky Navigation */}
      <Navbar activeTab={activeTab} setActiveTab={setActiveTab} />

      {/* Main Page Content Body */}
      <main className="flex-1">
        {activeTab === 'discover' && (
          <HomePage
            setActiveTab={setActiveTab}
            setSelectedDestinationForPlan={handleSelectDestinationForPlan}
          />
        )}

        {activeTab === 'destinations' && (
          <DestinationsPage
            setActiveTab={setActiveTab}
            setSelectedDestinationForPlan={handleSelectDestinationForPlan}
          />
        )}

        {activeTab === 'planner' && (
          <PlannerPage
            onPlanGenerated={handlePlanGenerated}
            prefilledDestination={prefilledDestination}
          />
        )}

        {activeTab === 'itinerary' && (
          <ItineraryPage
            plan={currentPlan}
            onEditPreferences={() => setActiveTab('planner')}
            onStartNewTrip={() => {
              setCurrentPlan(null);
              setPrefilledDestination(null);
              setActiveTab('planner');
            }}
            onSelectAlternative={handleSelectAlternativeVariant}
          />
        )}

        {activeTab === 'analytics' && (
          <AnalyticsPage />
        )}

        {activeTab === 'algorithms' && (
          <AlgorithmLabPage />
        )}

        {activeTab === 'saved' && (
          <SavedTripsPage
            onLoadTrip={(trip) => {
              setCurrentPlan(trip);
              setActiveTab('itinerary');
              window.scrollTo({ top: 0, behavior: 'smooth' });
            }}
            onPlanNewTrip={() => {
              setCurrentPlan(null);
              setActiveTab('planner');
            }}
          />
        )}
      </main>

      {/* Editorial Footer */}
      <Footer setActiveTab={setActiveTab} />

    </div>
  );
}
