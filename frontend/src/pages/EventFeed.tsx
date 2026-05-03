/**
 * Event Feed Page
 *
 * TODO: Build a full-page event feed with:
 * - Live event table showing last 50 events
 * - Columns: Time, Source IP, Destination IP, Type, Severity, Description
 * - Severity badges with color coding (CRITICAL=red, HIGH=orange, MEDIUM=yellow, LOW=green)
 * - Auto-scroll as new events arrive (with a "pause" button)
 * - Click row to expand full event details
 * - Filter controls: severity dropdown, event type dropdown, search by IP
 * - Data from eventStore (real-time) + REST API (historical)
 */
export default function EventFeed() {
  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold text-white mb-6">Live Event Feed</h1>

      <div className="bg-gray-800 rounded-lg p-12 flex items-center justify-center border border-gray-700">
        <div className="text-center">
          <p className="text-gray-400 text-lg">Event feed not yet implemented</p>
          <p className="text-gray-500 text-sm mt-2">
            Build a live-updating table with filtering and event expansion
          </p>
        </div>
      </div>
    </div>
  );
}
