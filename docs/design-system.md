# Everise Design System

Derived from Stitch UI export + `DESIGN.md` (Atomic Habit System).

## Brand feeling

Calm, deliberate, sustained growth. Warm minimalism + modern tactile Material.  
Friction-free tracking, quiet encouragement, low-stimulus focus. No aggressive streak guilt.

## Colors (Material 3 mapping)

### Light
| Role | Hex | Usage |
|------|-----|--------|
| Primary | `#166589` | Brand, interactive |
| Primary container | `#5D9FC6` | Filled actions, progress |
| Secondary | `#346479` | Supporting accents |
| Tertiary / Success | `#436650` / `#8FB49A` | Completions, positive |
| Background | `#F8F9FF` / `#FFFDF7` | Canvas |
| Surface / cards | `#F8F5ED` / cream | Habit tiles |
| On-surface | `#0E1C2C` / `#172535` | Primary text |
| On-surface variant | `#40484E` / `#697789` | Secondary text |
| Outline | `#E8EAF0` | Dividers, tracks |

### Dark
| Role | Hex |
|------|-----|
| Primary | `#79BDE5` |
| Background | `#111318` / `#171B22` |
| Card | `#202630` |
| Elevated | `#282F3A` |
| On-surface | `#F4F7FA` |
| Success | `#8FC6A1` |

Missed check-ins stay neutral gray — never loud red/yellow punishment.

## Typography

**Plus Jakarta Sans** throughout.

- Display / metrics: 32–40 sp, bold
- Headlines: 18–28 sp, semi-bold / bold
- Body: 14–16 sp
- Labels: 11–14 sp, medium/semi-bold, slight tracking for tags

## Shape & elevation

- Cards: 24–32 dp corner radius (rounded-2xl / 3xl)
- Pills / FABs: full radius
- Level 0: flat canvas
- Level 1: cream card + 1 dp stroke, no hard shadow
- Level 2–3: soft ambient tinted shadows only

## Components (Compose)

Reusable: HabitCard, CompletionRing/Checkbox, ProgressRing, StreakChip, Heatmap, CalendarCell, StatCard, EmptyState, BottomNav, Loading/Error states.

## Spacing

8 dp grid. Mobile margins 16 dp. Card padding 16–24 dp. Bottom nav ~80 dp.
