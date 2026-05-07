# ThreatStream — Real-Time Threat Intelligence Dashboard

Welcome! This is a take-home coding exercise. Your goal is to take a partially-built **real-time threat intelligence dashboard** and bring it to a working, demoable state. The scaffold compiles and starts, but most of the application logic is missing or incomplete.

You will be evaluated on the **backend** (Java / Spring Boot). You do not need to modify the frontend — it is provided so you can verify your backend end-to-end in a browser.

---

## What ThreatStream is

ThreatStream is a SOC-style monitoring tool. Simulated security events flow through Apache Kafka, are processed by a Spring Boot backend, and stream to a React dashboard over WebSocket.

```
Kafka topics                 Spring Boot backend             React frontend
┌──────────────┐  consume    ┌────────────────────┐  REST    ┌──────────────┐
│ threat-events│──────────►  │ Event processing   │────────► │  Dashboard   │
│ alert-rules  │──────────►  │ Alert management   │ WebSocket│  Event feed  │
│ system-metrics│─────────►  │ Metrics aggregation│────────► │  Alerts      │
└──────────────┘             └────────────────────┘          └──────────────┘
```

Three users with different roles must be able to log in: **VIEWER** (read-only), **ANALYST** (acknowledge alerts, add notes), **ADMIN** (manage rules, manage users).

---

## What we expect you to deliver

By the end of the exercise, the following must work end-to-end:

### 1. Authentication & authorization
- A user can log in with username/password and receive a JWT.
- The JWT is validated on every protected request and authorizes the caller.
- Role-based access is enforced: VIEWER cannot write, ANALYST can acknowledge alerts and add notes, ADMIN can do everything.
- **Carefully review the existing auth code before building on top of it.** Some of it is incomplete or incorrect; getting auth right is part of the exercise.

### 2. Kafka pipeline
- Three Kafka topics must be produced **and** consumed: `threat-events`, `alert-rules`, `system-metrics`.
- A simulator (running inside the backend) must publish synthetic messages to each topic at the cadences below. We have intentionally not provided this — building the producer is part of the exercise.
- Consumers must persist events/alerts to the database and update in-memory metrics.

| Topic           | Cadence            | Payload                                                                                                |
|-----------------|--------------------|--------------------------------------------------------------------------------------------------------|
| `threat-events` | 1 every 2–5 sec    | `id, sourceIp, destinationIp, type, severity, geo{country,lat,lon}, description, timestamp`            |
| `alert-rules`   | 1 every 10–15 sec  | `id, ruleId, ruleName, matchedEventId, action, severity, timestamp`                                    |
| `system-metrics`| 1 every 5 sec      | `eventsPerSecond, avgLatencyMs, activeConnections, cpuPercent, memoryPercent, kafkaLag, timestamp`     |

- `EventType`: `INTRUSION_ATTEMPT, MALWARE, DDOS, BRUTE_FORCE, DATA_EXFIL, RECON`
- `Severity`: `LOW, MEDIUM, HIGH, CRITICAL`
- `AlertAction`: `BLOCK_IP, QUARANTINE, NOTIFY, ESCALATE`
- **~10% of `threat-events` will have `geo: null`.** This is a legitimate case (events from internal/private IPs). Your pipeline must handle it without dropping the event or crashing.

### 3. REST API
All endpoints below must be implemented, paginated where applicable, and role-gated correctly.

**Auth**
- `POST /api/auth/login` — returns JWT
- `POST /api/auth/register` — registers a new user

**Events**
- `GET /api/events` — paginated (`?severity=`, `?type=`, `?from=`, `?to=`, `?page=`, `?size=`)
- `GET /api/events/{id}` — single event including any linked alerts
- `POST /api/events/{id}/notes` — add a note (ANALYST+)

**Alerts**
- `GET /api/alerts` — paginated (`?severity=`, `?action=`)
- `GET /api/alerts/{id}` — single alert including the matched event
- `POST /api/alerts/{id}/acknowledge` — acknowledge (ANALYST+); record who and when

**Dashboard**
- `GET /api/dashboard/metrics` — latest system metrics + aggregated event counts (by severity, by type, last hour, hourly breakdown for the last 24h, active alerts count)

**Subscriptions** (see section 6 for full requirements)
- `GET /api/subscriptions` — list the calling user's subscription rules
- `POST /api/subscriptions` — create a rule for the calling user
- `PUT /api/subscriptions/{id}` — update a rule (owner or ADMIN)
- `DELETE /api/subscriptions/{id}` — delete a rule (owner or ADMIN)
- `GET /api/subscriptions/users/{username}` — list another user's rules (ADMIN only)

### 4. Live streaming to the frontend
- A WebSocket endpoint at `/ws` (STOMP over SockJS) must stream incoming Kafka messages to connected clients.
- **Per-user delivery, not global topics.** Each user only receives messages matching their own subscription rules (see section 6). Use STOMP user destinations: `/user/queue/events`, `/user/queue/alerts`, `/user/queue/metrics`. Spring's `SimpMessagingTemplate.convertAndSendToUser(username, ...)` is the right primitive.
- Authentication on the WebSocket connection is required — the JWT must be validated at STOMP `CONNECT` (HTTP-level filtering does not run for WebSocket frames). Anonymous or unauthenticated clients must not receive data.

### 5. Quality bar
- Reasonable separation of concerns (controllers thin, services for business logic).
- Input validation on request bodies.
- Sensible error handling — clients should get the right HTTP status (400 / 401 / 403 / 404 / 500), not a generic 500 for everything.
- A handful of meaningful tests (auth flow, one Kafka consumer, one REST endpoint with role checks, the subscription matching predicate). `spring-kafka-test` and `spring-security-test` are already on the classpath.

### 6. Per-user event subscriptions (server-enforced filtering)

This is the differentiator from a vanilla broadcast pipeline. The server must only deliver messages a user has explicitly asked for. Filtering is the server's job — never trust the client to drop messages it didn't want.

**The model**
- A user has zero or more *subscription rules*.
- A rule says: "for this stream (`EVENTS` / `ALERTS` / `METRICS`), include messages matching these filters."
- Filter dimensions on a rule:
  - `severities` — set of `Severity` values (empty set = match any severity)
  - `eventTypes` — set of `EventType` values (empty = match any) — relevant for `EVENTS`
  - `geoCountries` — set of ISO-2 country codes (empty = match any) — relevant for `EVENTS`
  - `includeNullGeo` — boolean — relevant for `EVENTS` only when `geoCountries` is non-empty (see edge cases)
  - `alertActions` — set of `AlertAction` values (empty = match any) — relevant for `ALERTS`
  - `enabled` — boolean — disabled rules never match

**Match semantics**
- A rule matches an event iff **every configured filter on the rule** matches the event (AND across dimensions).
- A user receives an event iff **at least one of their enabled rules** matches it (OR across rules).
- A user with **zero rules receives nothing** — this is the entire point of the feature, not a bug. Document it in the UI.

**Edge cases to get right**
- ~10% of `threat-events` arrive with `geoCountry == null` (legitimate — internal IPs). When a rule constrains `geoCountries`, a null-geo event matches iff the rule's `includeNullGeo == true`. When `geoCountries` is empty (unconstrained), null-geo events match unconditionally.
- A user with multiple active WebSocket sessions (e.g. two browser tabs) should receive each matching message on **every** session. `convertAndSendToUser` handles this for free.
- When a user mutates one of their rules via REST, the next event broadcast must reflect the new rule — design your in-memory cache invalidation accordingly.
- An ADMIN editing another user's rules must invalidate the **owning user's** cache, not the admin's.
- Authorization: a user can only modify their own rules. ADMIN may modify anyone's. Anyone else's attempt → `403`. Validation failures → `400`. Missing JWT on WebSocket CONNECT → reject the session.

**Seed data**
- Seed at least one sample rule per demo user in `DataLoader` so the feature is demoable on first boot. Suggested:
  - `viewer` — events stream, severity `CRITICAL` only
  - `analyst` — events with severity `HIGH`/`CRITICAL` and types `BRUTE_FORCE`/`DATA_EXFIL`/`INTRUSION_ATTEMPT`; plus alerts with action `BLOCK_IP`/`ESCALATE`
  - `admin` — three catch-all rules (one per stream, no filters), so admin sees everything

**What "good" looks like**
- Filtering happens at publish time on the server, not in the browser.
- The Kafka consumer has one line at the end of each listener: hand the persisted entity to a broadcaster service that asks "which connected users want this?" and unicasts only to them.
- The broadcaster does not query the database on every event — there is an in-memory subscription cache, invalidated on rule mutation.
- Predicate logic (does this rule match this event?) is unit-tested without a Spring context.

---

## What is already provided

Read the existing code carefully before you write anything new. Some of it is correct, some of it is a starting point you should build on, and some of it has subtle issues you will need to fix. Treat the scaffold the same way you would treat code you inherited from a teammate: review it, don't trust it blindly.

**Already wired up**
- Spring Boot 3.2 + Java 21 project that builds and starts
- Embedded Kafka broker (no external Kafka needed) — see `KafkaConfig`
- H2 in-memory database, JPA entities for `User`, `ThreatEvent`, `Alert`
- A `SampleConsumer` showing the Spring Kafka listener pattern
- An `EventDeserializer` helper for converting Kafka JSON → `ThreatEvent`
- JPA repositories with the most useful query methods
- A `DashboardService` skeleton (review it carefully — the aggregation logic needs work)
- A `DataLoader` that seeds three demo users on startup
- `SecurityConfig` with JWT filter wired into the chain
- A `GlobalExceptionHandler`
- React frontend that consumes the API and the WebSocket

**Stubbed out — you implement these**
- `EventController`, `AlertController` — only TODOs today
- `StreamController` — empty; you decide how Kafka → WebSocket fan-out works
- Kafka **producer / simulator** for the three topics — does not exist
- Kafka **consumers** for the three real topics — only the sample consumer exists
- Persistence and aggregation paths from consumer → DB → dashboard
- WebSocket message broadcasting and connection-level auth (STOMP CONNECT JWT validation, per-user destinations)
- **Per-user event subscriptions** — entity, REST CRUD, in-memory match registry, and broadcaster that filters per user (see section 6)
- Notes on events (no entity yet — design it)
- Tests

---

## Tech stack (fixed — please don't swap libraries)

**Backend:** Java 21, Spring Boot 3.2, Spring Kafka (embedded broker), Spring WebSocket (STOMP + SockJS), Spring Data JPA, H2, JJWT 0.12.x, Lombok.
**Frontend (provided, do not modify):** React 18, TypeScript, Vite, Tailwind v4, Chart.js, Zustand, Axios.

---

## Running the project

**Backend**
```bash
cd backend
mvn spring-boot:run
```
Runs on `http://localhost:8080`. The embedded Kafka broker starts automatically — no Docker, no local Kafka needed.

**Frontend** (only needed for end-to-end verification)
```bash
cd frontend
npm install
npm run dev
```
Runs on `http://localhost:3000` and proxies API calls to `:8080`.

**Demo accounts** (seeded on first start, password `password123`):
- `viewer` — VIEWER role
- `analyst` — ANALYST role
- `admin` — ADMIN role

**H2 console:** `http://localhost:8080/h2-console` — JDBC URL `jdbc:h2:mem:threatstream`, user `sa`, no password.

---

## Time expectation

We expect this to take a focused engineer **roughly 6–8 hours**. If you find yourself going significantly longer, stop and submit what you have along with a short note on what you would do next — we'd rather see clean partial work than rushed complete work.

You do **not** need to finish every endpoint to submit. Prioritise:

1. Auth working correctly (including role enforcement)
2. Kafka producer + at least one consumer end-to-end
3. At least one REST list endpoint with filtering and pagination
4. **Subscription model + REST CRUD + per-user WebSocket delivery** (section 6) — this is what makes the system more than a broadcast pipe
5. WebSocket pushing live events to the frontend, gated by subscriptions

Then breadth (the rest of the endpoints), then tests, then polish.
