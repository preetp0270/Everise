# Everise API Contracts (v1)

Base URL (production): `https://<your-render-service>.onrender.com/api/v1`  
Base URL (local): `http://10.0.2.2:3000/api/v1` (Android emulator → host machine)

## Response envelope

```json
{
  "success": true,
  "data": { ... },
  "meta": { "page": 1, "limit": 20, "total": 42 }
}
```

Error:

```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Human-readable message",
    "details": [ ... ]
  }
}
```

## Health

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| GET | `/health` | No | Liveness + DB status |

## Auth (implemented — Phase 2)

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/auth/register` | No | Create account → tokens |
| POST | `/auth/login` | No | Issue access + refresh |
| POST | `/auth/refresh` | No | Rotate refresh token |
| POST | `/auth/logout` | No | Revoke current refresh |
| GET | `/auth/me` | Yes | Current user profile |
| POST | `/auth/logout-all` | Yes | Revoke all sessions |
| POST | `/auth/password/change` | Yes | Change password (revokes all) |
| DELETE | `/auth/account` | Yes | Soft-delete account |

### Register body
```json
{ "email": "user@example.com", "password": "SecurePass1", "displayName": "Preet", "timezone": "Asia/Kolkata" }
```

### Login body
```json
{ "email": "user@example.com", "password": "SecurePass1", "deviceInfo": "Pixel 8" }
```

### Success data
```json
{
  "user": { "id": "...", "email": "...", "displayName": "..." },
  "accessToken": "<jwt>",
  "refreshToken": "<opaque>",
  "expiresAt": "2026-11-19T..."
}
```

## Habits (Phase 5+)

| Method | Path | Description |
|--------|------|-------------|
| GET | `/habits` | List |
| POST | `/habits` | Create |
| GET | `/habits/:id` | Detail |
| PATCH | `/habits/:id` | Update |
| POST | `/habits/:id/archive` | Archive |
| POST | `/habits/:id/restore` | Restore |
| DELETE | `/habits/:id` | Delete |

## Habit logs (Phase 5+)

| Method | Path | Description |
|--------|------|-------------|
| POST | `/habit-logs` | Record completion (idempotent) |
| GET | `/habit-logs` | Query |
| DELETE | `/habit-logs/:id` | Undo |

## Other groups (later)

- `/calendar`, `/analytics`, `/goals`, `/achievements`
- `/reminders`, `/focus-sessions`, `/sync`, `/users`
