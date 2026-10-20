# Everise Backend

Node.js + Express REST API backed by MongoDB Atlas. Deployable to Render.

## Setup

```bash
cp .env.example .env
# Edit .env with MongoDB Atlas URI and JWT secrets
npm install
npm run dev
```

Health: `GET http://localhost:3000/api/v1/health`

## Auth endpoints (Phase 2)

| Method | Path | Notes |
|--------|------|-------|
| POST | `/api/v1/auth/register` | Argon2id password hash |
| POST | `/api/v1/auth/login` | Access JWT + rotating refresh |
| POST | `/api/v1/auth/refresh` | Rotate; reuse detection |
| POST | `/api/v1/auth/logout` | Revoke refresh |
| GET | `/api/v1/auth/me` | Bearer access token |
| POST | `/api/v1/auth/logout-all` | Revoke all sessions |
| POST | `/api/v1/auth/password/change` | Revokes all sessions |
| DELETE | `/api/v1/auth/account` | Soft delete |

## Models

- User, RefreshSession, Habit, HabitLog

## Tests

```bash
# Unit tests (no Mongo required)
node --test tests/auth.test.js

# With Mongo:
MONGODB_URI=mongodb://127.0.0.1:27017/everise_test node --test tests/auth.test.js
```

## Render

- Bind to `process.env.PORT`
- Set `MONGODB_URI`, JWT secrets in dashboard
- Health check path: `/api/v1/health`
