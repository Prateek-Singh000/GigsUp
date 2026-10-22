# GigsUp — Hyper-Local Micro-Job Engine

Spring Boot + PostGIS backend matching open shifts to nearby, available students in real time.

## Stack
Java 17, Spring Boot 3.3, PostgreSQL + PostGIS, Spring Security (JWT), Docker Compose, Gradle.

## Run it (only Docker required — no local Java/Gradle needed)

```bash
docker compose up --build
```

This starts:
- `db` — Postgres 16 with the PostGIS extension (port 5432)
- `app` — the Spring Boot API (port 8080), built inside a Gradle container

First boot takes a couple minutes (downloading deps). The app waits for the DB healthcheck before starting.

Tables are auto-created via `ddl-auto: update`. The `location` column on `shifts` is a PostGIS `geometry(Point, 4326)`.

> Note: PostGIS extension creation (`CREATE EXTENSION postgis`) happens automatically because the `postgis/postgis` image has it preinstalled and enabled on the default DB.

## API Overview

### Auth
| Method | Endpoint | Body | Notes |
|---|---|---|---|
| POST | `/api/auth/register` | `{ name, email, password, role: "VENDOR"|"STUDENT" }` | Returns JWT |
| POST | `/api/auth/login` | `{ email, password }` | Returns JWT |

Use the returned token as `Authorization: Bearer <token>` on all other endpoints.

### Shifts (VENDOR posts, STUDENT matches/accepts)
| Method | Endpoint | Role | Notes |
|---|---|---|---|
| POST | `/api/shifts` | VENDOR | Create a shift with `lat`, `lng`, `payRate`, `startTime`, `endTime` |
| POST | `/api/shifts/match` | STUDENT | Body: `{ lat, lng, radiusMeters }` — geospatial `ST_DWithin` match, filtered by availability |
| POST | `/api/shifts/{id}/accept` | STUDENT | OPEN → MATCHED (optimistic-locked, race-safe) |
| POST | `/api/shifts/{id}/complete` | VENDOR/STUDENT | MATCHED → COMPLETED |
| GET | `/api/shifts/mine` | any | List shifts relevant to the current user |

### Availability (STUDENT)
| Method | Endpoint | Notes |
|---|---|---|
| POST | `/api/availability` | `{ availableFrom, availableTo }` — declares a free-time window |
| GET | `/api/availability` | List your declared windows |

## Testing with Postman
Import `postman_collection.json`. It includes a pre-request flow: register a VENDOR, register a STUDENT, post a shift, set student location, search for matches, and accept.

## Concurrency design
`Shift.version` is a JPA `@Version` column. Two simultaneous "accept" requests on the same shift
race at the DB level — Hibernate's optimistic lock catches the loser and the global exception
handler returns `409 Conflict`, so a shift can never be double-booked under load.

## What to build next
- Refresh tokens / token revocation
- Pagination on `/shifts/match`
- A frontend (React/Next.js) consuming this API — happy to scaffold that next if you want a true full-stack UI on top of this.
