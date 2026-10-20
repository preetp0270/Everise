# Everise Database Design (MongoDB Atlas)

## Principles

- Every user-owned document has a `userId` (ObjectId) and is authorized by ownership.
- Habit completion history lives in **HabitLog** documents, not nested arrays on Habit.
- Indexes match real query patterns (user + date, user + habit, etc.).
- Soft archive preferred over hard delete for habits where history matters.

## Collections (planned)

### User
- email (unique), passwordHash, displayName, avatarUrl
- timezone, weekStartsOn, themePreference
- createdAt, updatedAt, deletedAt

### RefreshSession
- userId, tokenHash, deviceInfo, expiresAt, revokedAt
- createdAt, lastUsedAt

### Habit
- userId, name, description, icon, color, category
- schedule: { type: daily|weekdays|weekly|custom, daysOfWeek[], interval }
- target: { type: checkbox|duration|quantity|reps, value, unit }
- reminder: { enabled, times[], days[] }
- startDate, endDate?
- isArchived, isPinned, priority, order
- implementationIntention?, stackedAfterHabitId?, minVersion?
- syncVersion, createdAt, updatedAt

### HabitLog
- userId, habitId
- occurrenceDate (YYYY-MM-DD in user timezone)
- status: completed | skipped | incomplete
- value? (for measurable targets)
- note?, photoUrl?
- idempotencyKey (unique per user+habit+date+key)
- completedAt, createdAt, updatedAt
- Unique compound index on (userId, habitId, occurrenceDate) where appropriate

### Goal
- userId, title, description, targetDate, linkedHabitIds[]
- progress, milestones[], status

### Achievement / UserAchievement
- definition catalog + per-user unlock records

### FocusSession
- userId, habitId?, startedAt, endedAt, durationSeconds, status

### UserPreferences
- notification quiet hours, default reminder style, etc.

## Streak & analytics notes

- Streaks are computed from HabitLog + Habit.schedule, not stored as sole source of truth.
- Skipped days do not break streaks (product decision to encourage restart).
- Missed days (scheduled but no log) break current streak.
- Archive does not erase historical logs.
