import { create } from 'zustand';
import { DashboardMetrics } from '../types';

interface DashboardStoreState {
  // TODO: Store latest metrics snapshot
  // TODO: Maintain a 60-point time series for charts (last 5 minutes of metrics)
  metrics: DashboardMetrics | null;
  timeSeries: DashboardMetrics[];
}

export const useDashboardStore = create<DashboardStoreState>(() => ({
  metrics: null,
  timeSeries: [],
}));
