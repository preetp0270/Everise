# Everise Architecture

## Overview

Everise is an offline-first Android habit tracker with a Node.js/Express backend and MongoDB Atlas. A future React web dashboard will share the same API.

```
┌─────────────────┐     HTTPS      ┌──────────────────┐
│  Android App    │◄──────────────►│  Express API     │
│  (Compose)      │                │  (Render)        │
│  Room + DS      │                │                  │
└────────┬────────┘                └────────┬─────────┘
         │ Offline                           │
         ▼                                   ▼
   Local Room DB                      MongoDB Atlas
```

## Android Layers

- **ui/** — Compose screens, components, theme, navigation
- **domain/** — Models, repository interfaces, use cases
- **data/** — Room entities/DAOs, Retrofit APIs, mappers, repository implementations
- **core/** — Network, database, DataStore, security (Keystore), notifications, sync, common utils
- **di/** — Dependency injection (manual or Hilt later)
- **app/** — Application class, composition roots

Source of truth for screens: **Room**. The repository coordinates local writes, a sync queue, and remote API calls. WorkManager handles background sync and reminder rescheduling.

## Backend Layers

- **routes/** — Route definitions only
- **controllers/** — HTTP request/response
- **services/** — Business rules
- **models/** — Mongoose schemas
- **middleware/** — Auth, validation, rate limit, error handling, logging
- **validators/** — Zod (or similar) request schemas
- **config/** — env, db, cors
- **jobs/** — Scheduled maintenance
- **utils/** — Shared helpers

## Auth Model

- Short-lived access tokens (JWT)
- Rotating refresh tokens stored hashed in `RefreshSession`
- Android stores credentials via Android Keystore–backed secure storage
- Logout / password change / account deletion revokes sessions
- Rolling ~30-day refresh lifetime (configurable)

## Offline & Sync

1. User actions write to Room immediately.
2. Changes enqueue in a persistent sync queue (Room table).
3. WorkManager retries when connectivity returns.
4. Idempotency keys prevent duplicate completions.
5. Conflict rules: last-write-wins with version/timestamp metadata (documented per entity).

## Design Principles

- Calm, low-stimulus UI (no aggressive gamification)
- Consistency over perfection
- Clear cues, easy actions, satisfying feedback
- Accessible contrast and 48 dp touch targets where practical
