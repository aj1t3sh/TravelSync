import React, { useState, useEffect } from 'react';
import { Terminal, Cpu, Play, CheckCircle2, Zap, Clock, ShieldCheck, RefreshCw, Layers } from 'lucide-react';
import { api } from '../api/apiClient';

export default function AlgorithmLabPage() {
  const [activeTab, setActiveTab] = useState('CO2');
  const [loading, setLoading] = useState(false);
  const [algorithmsMetadata, setAlgorithmsMetadata] = useState([]);
  const [executionResult, setExecutionResult] = useState(null);

  // Form states for test inputs
  // CO1
  const [queryInput, setQueryInput] = useState('Find shortest round trip visiting Hyderabad, Goa and Mumbai within budget');
  // CO2
  const [textInput, setTextInput] = useState('Hyderabad is known for historic Charminar and Golconda Fort architecture in Hyderabad');
  const [patternInput, setPatternInput] = useState('Hyderabad');
  const [review1Input, setReview1Input] = useState('The historic architecture of the royal fort and palaces left us stunned with incredible heritage views.');
  const [review2Input, setReview2Input] = useState('We loved the historic architecture of the royal palace courtyards displaying exquisite craftsmanship.');
  // CO3
  const [str1Input, setStr1Input] = useState('Hyderabad');
  const [str2Input, setStr2Input] = useState('Hyderbad');
  const [tspCitiesInput, setTspCitiesInput] = useState('Hyderabad,Bengaluru,Goa,Mumbai');
  // CO4
  const [fromCityInput, setFromCityInput] = useState('Hyderabad');
  const [toCityInput, setToCityInput] = useState('Goa');
  // CO5
  const [budgetInput, setBudgetInput] = useState('1200');
  // CO6
  const [candidateNumber, setCandidateNumber] = useState('1000000007');
  const [witnessRounds, setWitnessRounds] = useState('10');

  useEffect(() => {
    api.getAlgorithmsMetadata()
      .then((data) => setAlgorithmsMetadata(data))
      .catch((err) => console.error(err));
  }, []);

  const runAlgorithm = async (algoId, params) => {
    setLoading(true);
    setExecutionResult(null);
    try {
      const res = await api.executeAlgorithm(algoId, params);
      setExecutionResult(res);
      setLoading(false);
    } catch (err) {
      setExecutionResult({ error: err.message });
      setLoading(false);
    }
  };

  const tabs = [
    { id: 'CO1', label: 'CO1: Classification', desc: 'Decision Tree & Upper Bounds' },
    { id: 'CO2', label: 'CO2: String Matching', desc: 'Naive vs KMP vs Rabin-Karp & Kasai LCP' },
    { id: 'CO3', label: 'CO3: Dynamic Programming', desc: 'Wagner-Fischer & Bitmask TSP' },
    { id: 'CO4', label: 'CO4: Network Flow', desc: 'Dinic / Edmonds-Karp & Min-Cut' },
    { id: 'CO5', label: 'CO5: Approximation', desc: 'Knapsack 1/2-Approx vs Exact DP' },
    { id: 'CO6', label: 'CO6: Parallel & Random', desc: 'Miller-Rabin & ForkJoin Reduction' },
  ];

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8 animate-fadeIn">
      
      {/* Header */}
      <div className="space-y-2">
        <div className="flex items-center gap-2 text-forest text-xs font-semibold uppercase tracking-wider">
          <Terminal className="w-4 h-4 text-forest" />
          <span>Academic Evaluation Playground</span>
        </div>
        <h1 className="font-serif-heading text-3xl sm:text-4xl font-bold text-forest">
          DSA-3 Interactive Algorithm Lab
        </h1>
        <p className="text-xs sm:text-sm text-charcoal/70 max-w-2xl">
          Execute and benchmark the underlying Java implementations directly from the browser to verify syllabus compliance for Course Outcomes CO1 through CO6.
        </p>
      </div>

      {/* Tabs */}
      <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-2">
        {tabs.map((tab) => {
          const active = activeTab === tab.id;
          return (
            <button
              key={tab.id}
              onClick={() => {
                setActiveTab(tab.id);
                setExecutionResult(null);
              }}
              className={`p-3 rounded-xl border text-left transition-all ${
                active
                  ? 'bg-forest text-ivory border-forest shadow-soft'
                  : 'bg-white hover:bg-sage/10 text-charcoal border-sage/40'
              }`}
            >
              <div className={`text-xs font-bold ${active ? 'text-mutedLime' : 'text-forest'}`}>
                {tab.label}
              </div>
              <div className={`text-[10px] truncate mt-0.5 ${active ? 'text-ivory/80' : 'text-charcoal/60'}`}>
                {tab.desc}
              </div>
            </button>
          );
        })}
      </div>

      {/* Interactive Testing Console */}
      <div className="grid grid-cols-1 lg:grid-cols-12 gap-6">
        
        {/* Left Column: Form Controls (7 cols) */}
        <div className="lg:col-span-7 bg-white rounded-3xl border border-sage/40 p-6 sm:p-8 shadow-soft space-y-6">
          
          {/* CO1: Problem Classifier */}
          {activeTab === 'CO1' && (
            <div className="space-y-4">
              <div className="space-y-1">
                <span className="text-[10px] font-bold px-2 py-0.5 rounded bg-forest text-mutedLime uppercase font-mono">
                  CO1 Problem Classification
                </span>
                <h3 className="font-serif-heading text-xl font-bold text-forest">
                  Problem Classifier & Complexity Evaluator
                </h3>
                <p className="text-xs text-charcoal/70">
                  Evaluates travel queries and returns formal asymptotic complexity and algorithmic strategy.
                </p>
              </div>

              <div>
                <label className="block text-xs font-bold text-charcoal/70 uppercase tracking-wider mb-1">
                  Sample Travel Planning Query
                </label>
                <textarea
                  rows="3"
                  value={queryInput}
                  onChange={(e) => setQueryInput(e.target.value)}
                  className="w-full p-3 rounded-xl border border-sage/40 text-xs text-charcoal focus:outline-none focus:border-forest"
                />
              </div>

              <button
                onClick={() => runAlgorithm('CLASSIFY', { query: queryInput })}
                disabled={loading}
                className="bg-forest hover:bg-forest-600 text-ivory text-xs font-semibold px-5 py-2.5 rounded-xl shadow-soft flex items-center gap-2"
              >
                <Play className="w-3.5 h-3.5 text-mutedLime" />
                <span>Run CO1 Problem Classifier</span>
              </button>
            </div>
          )}

          {/* CO2: String Algorithms */}
          {activeTab === 'CO2' && (
            <div className="space-y-6">
              <div className="space-y-1">
                <span className="text-[10px] font-bold px-2 py-0.5 rounded bg-forest text-mutedLime uppercase font-mono">
                  CO2 Linear Pattern Matchers
                </span>
                <h3 className="font-serif-heading text-xl font-bold text-forest">
                  Naïve vs KMP vs Rabin-Karp Comparison
                </h3>
                <p className="text-xs text-charcoal/70">
                  Measures character-by-character comparisons, LPS prefix failure transitions, and rolling hash collisions.
                </p>
              </div>

              <div className="space-y-3">
                <div>
                  <label className="block text-xs font-bold text-charcoal/70 uppercase tracking-wider mb-1">
                    Corpus Text
                  </label>
                  <textarea
                    rows="2"
                    value={textInput}
                    onChange={(e) => setTextInput(e.target.value)}
                    className="w-full p-2.5 rounded-xl border border-sage/40 text-xs text-charcoal focus:border-forest"
                  />
                </div>
                <div>
                  <label className="block text-xs font-bold text-charcoal/70 uppercase tracking-wider mb-1">
                    Search Pattern
                  </label>
                  <input
                    type="text"
                    value={patternInput}
                    onChange={(e) => setPatternInput(e.target.value)}
                    className="w-full p-2.5 rounded-xl border border-sage/40 text-xs text-charcoal focus:border-forest"
                  />
                </div>
                <button
                  onClick={() => runAlgorithm('STRING_MATCH_COMPARE', { text: textInput, pattern: patternInput })}
                  disabled={loading}
                  className="bg-forest hover:bg-forest-600 text-ivory text-xs font-semibold px-5 py-2.5 rounded-xl shadow-soft flex items-center gap-2"
                >
                  <Play className="w-3.5 h-3.5 text-mutedLime" />
                  <span>Execute String Matchers Benchmark</span>
                </button>
              </div>

              <div className="pt-4 border-t border-sage/20 space-y-3">
                <h4 className="font-bold text-sm text-forest">Kasai LCP Longest Common Review Substring</h4>
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-2">
                  <textarea
                    rows="2"
                    value={review1Input}
                    onChange={(e) => setReview1Input(e.target.value)}
                    placeholder="Review 1..."
                    className="w-full p-2 rounded-lg border border-sage/30 text-xs"
                  />
                  <textarea
                    rows="2"
                    value={review2Input}
                    onChange={(e) => setReview2Input(e.target.value)}
                    placeholder="Review 2..."
                    className="w-full p-2 rounded-lg border border-sage/30 text-xs"
                  />
                </div>
                <button
                  onClick={() => runAlgorithm('LCS_REVIEWS', { text1: review1Input, text2: review2Input })}
                  disabled={loading}
                  className="bg-sage-600 hover:bg-forest text-ivory text-xs font-semibold px-4 py-2 rounded-xl flex items-center gap-1.5"
                >
                  <Play className="w-3.5 h-3.5" />
                  <span>Compute LCS via Suffix Array & Kasai LCP</span>
                </button>
              </div>
            </div>
          )}

          {/* CO3: Dynamic Programming */}
          {activeTab === 'CO3' && (
            <div className="space-y-6">
              <div className="space-y-1">
                <span className="text-[10px] font-bold px-2 py-0.5 rounded bg-forest text-mutedLime uppercase font-mono">
                  CO3 Dynamic Programming
                </span>
                <h3 className="font-serif-heading text-xl font-bold text-forest">
                  Wagner-Fischer & Held-Karp Bitmask DP
                </h3>
              </div>

              {/* Edit Distance Test */}
              <div className="space-y-3">
                <h4 className="font-bold text-sm text-forest">1. Wagner-Fischer Levenshtein & Damerau Distance</h4>
                <div className="grid grid-cols-2 gap-2">
                  <input
                    type="text"
                    value={str1Input}
                    onChange={(e) => setStr1Input(e.target.value)}
                    placeholder="String 1"
                    className="p-2.5 rounded-xl border border-sage/40 text-xs text-charcoal font-mono"
                  />
                  <input
                    type="text"
                    value={str2Input}
                    onChange={(e) => setStr2Input(e.target.value)}
                    placeholder="String 2 (typo)"
                    className="p-2.5 rounded-xl border border-sage/40 text-xs text-charcoal font-mono"
                  />
                </div>
                <button
                  onClick={() => runAlgorithm('EDIT_DISTANCE', { str1: str1Input, str2: str2Input })}
                  disabled={loading}
                  className="bg-forest hover:bg-forest-600 text-ivory text-xs font-semibold px-4 py-2 rounded-xl flex items-center gap-1.5"
                >
                  <Play className="w-3.5 h-3.5 text-mutedLime" />
                  <span>Calculate Edit Distance</span>
                </button>
              </div>

              {/* Bitmask TSP Test */}
              <div className="pt-4 border-t border-sage/20 space-y-3">
                <h4 className="font-bold text-sm text-forest">2. Held-Karp Bitmask DP Traveling Salesperson Tour</h4>
                <div>
                  <label className="block text-[11px] text-charcoal/60 mb-1">
                    Comma-separated Indian hubs (N &le; 16):
                  </label>
                  <input
                    type="text"
                    value={tspCitiesInput}
                    onChange={(e) => setTspCitiesInput(e.target.value)}
                    className="w-full p-2.5 rounded-xl border border-sage/40 text-xs text-charcoal font-mono"
                  />
                </div>
                <button
                  onClick={() => runAlgorithm('BITMASK_TSP', { cities: tspCitiesInput })}
                  disabled={loading}
                  className="bg-forest hover:bg-forest-600 text-ivory text-xs font-semibold px-4 py-2 rounded-xl flex items-center gap-1.5"
                >
                  <Play className="w-3.5 h-3.5 text-mutedLime" />
                  <span>Solve Bitmask TSP Tour O(N² · 2^N)</span>
                </button>
              </div>
            </div>
          )}

          {/* CO4: Network Flow */}
          {activeTab === 'CO4' && (
            <div className="space-y-4">
              <div className="space-y-1">
                <span className="text-[10px] font-bold px-2 py-0.5 rounded bg-forest text-mutedLime uppercase font-mono">
                  CO4 Network Flow & Min-Cut
                </span>
                <h3 className="font-serif-heading text-xl font-bold text-forest">
                  Edmonds-Karp / Dinic Residual Flow Analyzer
                </h3>
                <p className="text-xs text-charcoal/70">
                  Computes maximum passenger transit capacity and extracts minimum cut bottlenecks.
                </p>
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-bold text-charcoal/70 uppercase tracking-wider mb-1">
                    Source City (s)
                  </label>
                  <input
                    type="text"
                    value={fromCityInput}
                    onChange={(e) => setFromCityInput(e.target.value)}
                    className="w-full p-2.5 rounded-xl border border-sage/40 text-xs font-semibold text-charcoal"
                  />
                </div>
                <div>
                  <label className="block text-xs font-bold text-charcoal/70 uppercase tracking-wider mb-1">
                    Sink City (t)
                  </label>
                  <input
                    type="text"
                    value={toCityInput}
                    onChange={(e) => setToCityInput(e.target.value)}
                    className="w-full p-2.5 rounded-xl border border-sage/40 text-xs font-semibold text-charcoal"
                  />
                </div>
              </div>

              <button
                onClick={() => runAlgorithm('MAX_FLOW_MIN_CUT', { from: fromCityInput, to: toCityInput })}
                disabled={loading}
                className="bg-forest hover:bg-forest-600 text-ivory text-xs font-semibold px-5 py-2.5 rounded-xl shadow-soft flex items-center gap-2"
              >
                <Play className="w-3.5 h-3.5 text-mutedLime" />
                <span>Run Dinic Max-Flow & Min-Cut Isolation</span>
              </button>
            </div>
          )}

          {/* CO5: Approximation */}
          {activeTab === 'CO5' && (
            <div className="space-y-4">
              <div className="space-y-1">
                <span className="text-[10px] font-bold px-2 py-0.5 rounded bg-forest text-mutedLime uppercase font-mono">
                  CO5 NP-Completeness & Approximation
                </span>
                <h3 className="font-serif-heading text-xl font-bold text-forest">
                  Knapsack 1/2-Approximation vs Exact DP
                </h3>
                <p className="text-xs text-charcoal/70">
                  Compares exact pseudo-polynomial DP with polynomial greedy 1/2-approximation for attraction budget selection.
                </p>
              </div>

              <div>
                <label className="block text-xs font-bold text-charcoal/70 uppercase tracking-wider mb-1">
                  Monument Budget Capacity (INR)
                </label>
                <input
                  type="number"
                  value={budgetInput}
                  onChange={(e) => setBudgetInput(e.target.value)}
                  className="w-full p-2.5 rounded-xl border border-sage/40 text-xs font-mono text-charcoal"
                />
              </div>

              <button
                onClick={() => runAlgorithm('KNAPSACK_APPROX', { budget: budgetInput })}
                disabled={loading}
                className="bg-forest hover:bg-forest-600 text-ivory text-xs font-semibold px-5 py-2.5 rounded-xl shadow-soft flex items-center gap-2"
              >
                <Play className="w-3.5 h-3.5 text-mutedLime" />
                <span>Compare Exact DP vs 1/2-Approximation</span>
              </button>
            </div>
          )}

          {/* CO6: Parallel & Randomized */}
          {activeTab === 'CO6' && (
            <div className="space-y-6">
              <div className="space-y-1">
                <span className="text-[10px] font-bold px-2 py-0.5 rounded bg-forest text-mutedLime uppercase font-mono">
                  CO6 Randomized & Parallel Primitives
                </span>
                <h3 className="font-serif-heading text-xl font-bold text-forest">
                  Miller-Rabin & Blelloch Parallel Scan
                </h3>
              </div>

              {/* Miller-Rabin Primality */}
              <div className="space-y-3">
                <h4 className="font-bold text-sm text-forest">1. Miller-Rabin Probabilistic Primality Test</h4>
                <div className="grid grid-cols-2 gap-2">
                  <input
                    type="text"
                    value={candidateNumber}
                    onChange={(e) => setCandidateNumber(e.target.value)}
                    placeholder="Candidate Number"
                    className="p-2.5 rounded-xl border border-sage/40 text-xs font-mono"
                  />
                  <input
                    type="number"
                    value={witnessRounds}
                    onChange={(e) => setWitnessRounds(e.target.value)}
                    placeholder="Witness Rounds"
                    className="p-2.5 rounded-xl border border-sage/40 text-xs font-mono"
                  />
                </div>
                <button
                  onClick={() => runAlgorithm('MILLER_RABIN', { number: candidateNumber, rounds: witnessRounds })}
                  disabled={loading}
                  className="bg-forest hover:bg-forest-600 text-ivory text-xs font-semibold px-4 py-2 rounded-xl flex items-center gap-1.5"
                >
                  <Play className="w-3.5 h-3.5 text-mutedLime" />
                  <span>Execute Miller-Rabin Test</span>
                </button>
              </div>

              {/* Parallel Primitives */}
              <div className="pt-4 border-t border-sage/20 space-y-3">
                <h4 className="font-bold text-sm text-forest">2. Multi-Core ForkJoin Tree Reduction & Blelloch Scan</h4>
                <div className="flex gap-2">
                  <button
                    onClick={() => runAlgorithm('PARALLEL_REDUCE', {})}
                    disabled={loading}
                    className="bg-sage-600 hover:bg-forest text-ivory text-xs font-semibold px-4 py-2 rounded-xl flex items-center gap-1.5"
                  >
                    <Play className="w-3.5 h-3.5" />
                    <span>Run ForkJoin Parallel Sum</span>
                  </button>
                  <button
                    onClick={() => runAlgorithm('BLELLOCH_SCAN', {})}
                    disabled={loading}
                    className="bg-sage-600 hover:bg-forest text-ivory text-xs font-semibold px-4 py-2 rounded-xl flex items-center gap-1.5"
                  >
                    <Play className="w-3.5 h-3.5" />
                    <span>Run Blelloch Parallel Scan</span>
                  </button>
                </div>
              </div>
            </div>
          )}

        </div>

        {/* Right Column: Execution Output Console (5 cols) */}
        <div className="lg:col-span-5 bg-charcoal text-ivory rounded-3xl p-6 shadow-card font-mono text-xs flex flex-col justify-between space-y-4">
          
          <div className="space-y-3">
            <div className="flex items-center justify-between border-b border-charcoal-500 pb-3">
              <span className="flex items-center gap-2 text-mutedLime text-xs font-bold">
                <Terminal className="w-4 h-4" /> Java DSA Execution Console
              </span>
              <span className="text-[10px] text-ivory/50">Port 8080 Active</span>
            </div>

            {loading ? (
              <div className="py-16 text-center text-ivory/70 space-y-2">
                <div className="w-6 h-6 rounded-full border-2 border-mutedLime border-t-transparent animate-spin mx-auto" />
                <p className="text-[11px]">Executing algorithm on Java 17 runtime...</p>
              </div>
            ) : executionResult ? (
              <div className="space-y-3 max-h-[460px] overflow-y-auto pr-1">
                <div className="text-emerald-400 font-bold">
                  ✓ Execution Succeeded in {((executionResult.totalPipelineNs || 0) / 1_000_000).toFixed(3)} ms
                </div>
                <pre className="text-[11px] text-ivory/90 bg-charcoal-800 p-4 rounded-xl overflow-x-auto whitespace-pre-wrap leading-relaxed border border-charcoal-600">
                  {JSON.stringify(executionResult, null, 2)}
                </pre>
              </div>
            ) : (
              <div className="py-20 text-center text-ivory/40 space-y-1">
                <p>Click "Run" or "Execute" on any algorithm to inspect real-time metrics.</p>
                <p className="text-[10px] text-ivory/30">Displays exact operation counters, state transitions, and memory traces.</p>
              </div>
            )}
          </div>

          <div className="border-t border-charcoal-600 pt-3 text-[10px] text-ivory/50 flex items-center justify-between">
            <span>KL University CSE (Course 25CS2103E)</span>
            <span className="text-mutedLime font-bold">Verified DSA-3</span>
          </div>

        </div>

      </div>

    </div>
  );
}
