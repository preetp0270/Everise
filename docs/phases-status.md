# Phase completion status

| Phase | Scope | Status |
|-------|--------|--------|
| 1 | Structure, design tokens, docs | Done |
| 2 | Models, validation, auth API | Done |
| 3 | Navigation, onboarding, auth UI | Done |
| 4 | Room, create habit, dashboard completion | Done |
| 5 | Habit + habit-log APIs, Retrofit clients | Done |
| 6 | Sync queue + WorkManager worker | Foundation done |
| 7 | Calendar, insights, goals/focus models | Foundation done |
| 8 | Notification channel, boot receiver | Foundation done |
| 9 | Auth unit tests; expand suite on device | Partial |
| 10 | Deployment docs | Done |
| 11 | Web placeholder ready for React | Placeholder |

## How to verify later

### Backend
```bash
cd backend && cp .env.example .env   # fill Atlas + JWT
npm install && npm run dev
curl localhost:3000/api/v1/health
# register / login / create habit with curl or Postman
```

### Android
Open `android/` in Android Studio → Sync → Run.
Flow: Splash → Onboarding → Guest or Register → Add habit → Toggle complete on Home.

### Offline
Create habits and complete them with airplane mode on; data stays in Room.
SyncWorker drains queue when network returns (API push to be hardened with live tokens).
