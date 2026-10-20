# Deployment Guide (Phase 10)

## MongoDB Atlas

1. Create a free cluster.
2. Database Access → create user with read/write on `everise`.
3. Network Access → allow Render IPs or `0.0.0.0/0` (tighten later).
4. Connect → copy SRV URI into `MONGODB_URI`.

## Render (Node web service)

1. New → Web Service → connect repo (or deploy from CLI).
2. Root directory: `backend`
3. Build: `npm install`
4. Start: `npm start`
5. Env vars:
   - `NODE_ENV=production`
   - `MONGODB_URI=...`
   - `JWT_ACCESS_SECRET=` (32+ random chars)
   - `JWT_REFRESH_SECRET=` (different 32+ chars)
   - `CORS_ORIGINS=https://your-web-domain.com`
6. Health check path: `/api/v1/health`
7. Note free-tier cold starts (~30–60s). Android remains usable offline.

## Android release

1. Set `API_BASE_URL` in `app/build.gradle.kts` release buildConfig to your Render HTTPS URL.
2. Generate signing keystore (store outside git).
3. `./gradlew :app:bundleRelease`
4. Upload AAB to Play Console (internal testing first).
5. Request notification / exact-alarm permissions carefully for API 33+.

## Emulator → local backend

- Base URL: `http://10.0.2.2:3000/api/v1/`
- Cleartext allowed via `network_security_config.xml` for 10.0.2.2 only.
