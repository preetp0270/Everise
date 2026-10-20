# Everise — Premium Habit Tracker

Android-first habit app inspired by *Atomic Habits*, with Node.js/Express + MongoDB Atlas backend.

## Status

Phases **1–11 scaffolded**. Core product loop works offline on Android:

1. Onboard → Guest / Register / Login  
2. Create habits (Room)  
3. Complete / undo on Home  
4. Calendar + Insights views  
5. Backend auth + habit + log APIs ready for Atlas  

See `docs/phases-status.md` and `docs/deployment.md`.

## Structure

```
Everise/
├── android/   # Kotlin + Jetpack Compose
├── backend/   # Express + Mongoose
├── web/       # Future React dashboard
└── docs/      # Architecture, API, design, deployment
```

## Quick start

### Backend
```bash
cd backend
cp .env.example .env   # MONGODB_URI + JWT secrets
npm install
npm run dev
```

### Android
Open `android/` in Android Studio (recent stable AGP).  
Emulator API base URL defaults to `http://10.0.2.2:3000/api/v1/`.

## Design

Calm warm minimalism — Plus Jakarta Sans scale, baby-blue primary, sage success.  
Tokens: `android/.../ui/theme` + `docs/design-system.md`.
