/**
 * Dashboard Page
 *
 * TODO: Build the main threat intelligence dashboard with the following layout:
 *
 * ┌──────────────────────────────────────────────────────┐
 * │  MetricsBar (events/sec, latency, connections, CPU)  │
 * ├─────────────────────────┬────────────────────────────┤
 * │  SeverityChart           │  EventTypeChart            │
 * │  (doughnut/pie -         │  (bar chart - count by     │
 * │   count by severity)     │   event type, last hour)   │
 * ├─────────────────────────┴────────────────────────────┤
 * │  ThreatMap                                            │
 * │  (scatter/bubble chart showing events by geo,         │
 * │   size = severity, color = type)                      │
 * ├──────────────────────────────────────────────────────┤
 * │  Live Event Feed (last 20 events, auto-scrolling,     │
 * │  severity-colored badges, click to expand)            │
 * └──────────────────────────────────────────────────────┘
 *
 * Components to implement:
 * - MetricsBar: 4 stat cards with color-coded thresholds
 * - SeverityChart: Chart.js doughnut chart
 * - EventTypeChart: Chart.js horizontal bar chart
 * - ThreatMap: Chart.js scatter chart (x=lon, y=lat)
 * - EventTable: Live auto-scrolling table
 *
 * Data sources:
 * - REST: GET /api/dashboard/metrics for initial load
 * - WebSocket: Real-time updates via useWebSocket hook
 * - Zustand stores: eventStore and dashboardStore
 *
 * Performance requirements:
 * - Charts should NOT re-render on every event (batch updates)
 * - Use React.memo or useMemo where appropriate
 * - Event table: limit DOM nodes or use virtualization
 */
export default function Dashboard() {
  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold text-white mb-6">Threat Dashboard</h1>

      <div className="bg-gray-800 rounded-lg p-12 flex items-center justify-center border border-gray-700">
        <div className="text-center">
          <p className="text-gray-400 text-lg">Dashboard components not yet implemented</p>
          <p className="text-gray-500 text-sm mt-2">
            Implement MetricsBar, SeverityChart, EventTypeChart, ThreatMap, and EventTable
          </p>
        </div>
      </div>
    </div>
  );
}
