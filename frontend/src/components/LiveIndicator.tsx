/**
 * LiveIndicator — Animated connection status dot.
 * Shows green when WebSocket is connected, yellow when reconnecting, red when disconnected.
 */
export default function LiveIndicator({ connected, reconnecting }: { connected: boolean; reconnecting: boolean }) {
  const color = connected ? 'bg-green-500' : reconnecting ? 'bg-yellow-500' : 'bg-red-500';
  const label = connected ? 'Live' : reconnecting ? 'Reconnecting...' : 'Disconnected';

  return (
    <div className="flex items-center gap-2">
      <span className={`relative flex h-2.5 w-2.5`}>
        {connected && (
          <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-green-400 opacity-75" />
        )}
        <span className={`relative inline-flex rounded-full h-2.5 w-2.5 ${color}`} />
      </span>
      <span className="text-xs text-gray-400">{label}</span>
    </div>
  );
}
