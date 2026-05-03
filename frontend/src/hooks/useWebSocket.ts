import { useAuthStore } from '../stores/authStore';

/**
 * Hook for connecting to the real-time event stream.
 *
 * TODO: Implement WebSocket or SSE connection to /ws endpoint
 *
 * Requirements:
 * - Connect with JWT authentication
 * - Auto-reconnect on disconnect (use exponential backoff)
 * - Parse incoming messages by type (THREAT_EVENT, ALERT, METRICS)
 * - Route events to the appropriate Zustand store:
 *   - THREAT_EVENT → eventStore
 *   - ALERT → (alert handling)
 *   - METRICS → dashboardStore
 * - Clean up connection on unmount
 *
 * Message format from server:
 *   { "type": "THREAT_EVENT", "data": {...} }
 *   { "type": "ALERT", "data": {...} }
 *   { "type": "METRICS", "data": {...} }
 */
export function useWebSocket() {
  const token = useAuthStore((s) => s.token);

  // TODO: Implement connection logic
  // TODO: Return connection status (connected, disconnected, reconnecting)

  return {
    connected: false,
    reconnecting: false,
  };
}
