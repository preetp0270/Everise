# Everise Web (Phase 11 — future)

React + TypeScript dashboard consuming the same `/api/v1` backend.

## Suggested stack

- Vite + React 18 + TypeScript
- React Query for API cache
- Same design tokens (see `docs/design-system.md`)
- Auth: store access token in memory; refresh token in httpOnly cookie or secure storage

## Bootstrap (when prioritized)

```bash
npm create vite@latest . -- --template react-ts
npm install
# point VITE_API_URL at Render backend
```

Do not block Android delivery on web work.
