/**
 * Alerts Page
 *
 * TODO: Build an alert management page with:
 * - Alert list with rule name, action taken, severity, timestamp
 * - Acknowledge button for ANALYST+ roles
 * - Filter by severity, action type, acknowledged/unacknowledged
 * - Link to the matched threat event
 * - Real-time updates: new alerts appear at the top
 */
export default function Alerts() {
  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold text-white mb-6">Alerts</h1>

      <div className="bg-gray-800 rounded-lg p-12 flex items-center justify-center border border-gray-700">
        <div className="text-center">
          <p className="text-gray-400 text-lg">Alert management not yet implemented</p>
          <p className="text-gray-500 text-sm mt-2">
            Build alert list with acknowledge functionality and real-time updates
          </p>
        </div>
      </div>
    </div>
  );
}
