import { create } from 'zustand';
import { ThreatEvent } from '../types';

interface EventStoreState {
  // TODO: Implement a rolling window of events (max 100)
  // TODO: Track counts by severity and type
  // TODO: Support filtering the live feed by severity
  events: ThreatEvent[];
  totalReceived: number;
}

export const useEventStore = create<EventStoreState>(() => ({
  events: [],
  totalReceived: 0,
}));
