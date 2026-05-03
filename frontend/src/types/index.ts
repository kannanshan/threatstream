export interface ThreatEvent {
  id: string;
  sourceIp: string;
  destinationIp: string;
  type: EventType;
  severity: Severity;
  geoCountry: string | null;
  geoLat: number | null;
  geoLon: number | null;
  description: string;
  timestamp: string;
}

export interface Alert {
  id: string;
  ruleId: string;
  ruleName: string;
  matchedEventId: string;
  action: AlertAction;
  severity: Severity;
  acknowledged: boolean;
  acknowledgedBy: string | null;
  acknowledgedAt: string | null;
  timestamp: string;
}

export interface User {
  id: string;
  username: string;
  displayName: string;
  role: Role;
}

export interface DashboardMetrics {
  eventsPerSecond: number;
  avgLatencyMs: number;
  activeConnections: number;
  cpuPercent: number;
  memoryPercent: number;
  kafkaLag: number;
  totalEventsLastHour: number;
  eventsBySeverity: Record<string, number>;
  eventsByType: Record<string, number>;
  activeAlertsCount: number;
  hourlyBreakdown: Record<number, number>;
}

export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  page: number;
}

export interface StreamMessage {
  type: 'THREAT_EVENT' | 'ALERT' | 'METRICS';
  data: ThreatEvent | Alert | Partial<DashboardMetrics>;
}

export type Severity = 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';
export type EventType = 'INTRUSION_ATTEMPT' | 'MALWARE' | 'DDOS' | 'BRUTE_FORCE' | 'DATA_EXFIL' | 'RECON';
export type AlertAction = 'BLOCK_IP' | 'QUARANTINE' | 'NOTIFY' | 'ESCALATE';
export type Role = 'VIEWER' | 'ANALYST' | 'ADMIN';
