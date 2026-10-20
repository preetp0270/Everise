---
name: Atomic Habit System
colors:
  surface: '#f8f9ff'
  surface-dim: '#cddbf1'
  surface-bright: '#f8f9ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#eef4ff'
  surface-container: '#e5efff'
  surface-container-high: '#dbe9ff'
  surface-container-highest: '#d5e4f9'
  on-surface: '#0e1c2c'
  on-surface-variant: '#40484e'
  inverse-surface: '#243142'
  inverse-on-surface: '#eaf1ff'
  outline: '#70787e'
  outline-variant: '#c0c7ce'
  surface-tint: '#166589'
  primary: '#166589'
  on-primary: '#ffffff'
  primary-container: '#5d9fc6'
  on-primary-container: '#00344a'
  inverse-primary: '#8dcef7'
  secondary: '#346479'
  on-secondary: '#ffffff'
  secondary-container: '#b6e7ff'
  on-secondary-container: '#38687d'
  tertiary: '#436650'
  on-tertiary: '#ffffff'
  tertiary-container: '#7ca087'
  on-tertiary-container: '#143623'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#c6e7ff'
  primary-fixed-dim: '#8dcef7'
  on-primary-fixed: '#001e2d'
  on-primary-fixed-variant: '#004c6b'
  secondary-fixed: '#bee9ff'
  secondary-fixed-dim: '#9dcde5'
  on-secondary-fixed: '#001f2a'
  on-secondary-fixed-variant: '#184c60'
  tertiary-fixed: '#c5ecd0'
  tertiary-fixed-dim: '#aacfb4'
  on-tertiary-fixed: '#002110'
  on-tertiary-fixed-variant: '#2c4e39'
  background: '#f8f9ff'
  on-background: '#0e1c2c'
  surface-variant: '#d5e4f9'
typography:
  display-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 40px
    fontWeight: '700'
    lineHeight: 48px
  display-lg-mobile:
    fontFamily: Plus Jakarta Sans
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
  headline-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 28px
    fontWeight: '700'
    lineHeight: 36px
  headline-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 22px
    fontWeight: '600'
    lineHeight: 28px
  headline-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 24px
  body-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  body-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 12px
    fontWeight: '400'
    lineHeight: 16px
  label-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
  label-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 12px
    fontWeight: '600'
    lineHeight: 16px
  label-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 11px
    fontWeight: '500'
    lineHeight: 14px
rounded:
  sm: 0.5rem
  DEFAULT: 1rem
  md: 1.5rem
  lg: 2rem
  xl: 3rem
  full: 9999px
spacing:
  gutter: 1rem
  gutter-tablet: 1.5rem
  gutter-desktop: 2rem
  margin: 1rem
  margin-tablet: 2rem
  margin-desktop: 3rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
---

## Brand & Style

The design system embodies a calm, deliberate atmosphere geared toward sustained personal growth, intentional routines, and cognitive clarity. Drawing philosophy from atomic systems, the visual language prioritizes friction-free tracking, quiet encouragement, and low-stimulus focus over aggressive gamification or stress-inducing streak graphics.

The style fuses Warm Minimalism with Modern Tactile Material cues. Rather than cold, sterile white canvases, the interface balances pure structure with warm cream backdrops and serene atmospheric blue tones. Surface elements feel light, grounded, and physical through organic corner curves, gentle tonal nesting, and precise spatial pacing optimized for native Android ergonomics.

## Colors

The palette relies on gentle chromatic shifts rather than high-contrast jolts, maintaining focus during morning planning and evening reflection.

### Light Mode (Default)
- **Primary Accent / Interactive:** `#5D9FC6` (controls, filled action states, primary progress fills)
- **Secondary Atmospheric Accent:** `#A8D8F0` (soft accents, active tab indicators, habit card badges)
- **Tertiary / Success:** `#8FB49A` (routine completion states, celebration rings, positive trends)
- **Base Canvas:** `#FFFFFF` (underlying viewport surface)
- **Secondary Background:** `#FFFDF7` (root screen scaffold and container background)
- **Cream Card Surface:** `#F8F5ED` (primary component cards, section groupings)
- **Subtle Highlight Surface:** `#EAF6FC` (selected states, track highlights, input focus fills)
- **Primary Content Text:** `#172535` (high-emphasis headings, key counts)
- **Secondary Content Text:** `#697789` (supporting metadata, streak captions, timestamps)
- **Structural Outlines:** `#E8EAF0` (hairline dividers, uncompleted ring tracks)

### Dark Mode
- **Primary Accent / Interactive:** `#79BDE5`
- **Secondary Atmospheric Accent:** `#9DD9F5`
- **Tertiary / Success:** `#8FC6A1`
- **Base Canvas:** `#111318`
- **Secondary Background:** `#171B22`
- **Card Surface:** `#202630`
- **Elevated Surface:** `#282F3A`
- **Subtle Highlight Surface:** `#304454`
- **Primary Content Text:** `#F4F7FA`
- **Secondary Content Text:** `#A8B2C0`
- **Structural Outlines:** `#303846`

Apply color functionally: interactive elements use `#5D9FC6` (`#79BDE5` in dark mode); completions and habit satisfaction triggers resolve strictly to the sage `#8FB49A` (`#8FC6A1`). Do not use loud warning reds or high-saturation yellows; missed check-ins remain neutral secondary gray (`#697789`).

## Typography

Typography relies on Plus Jakarta Sans across all tiers, chosen for its friendly geometric architecture, balanced proportions, and high screen legibility within Android's Jetpack Compose layouts.

- **Numbers & Metric Data:** Habit counts, completion stats, and dates render in `headline-lg` or `display-lg-mobile` with medium or bold weights, maintaining tabular lining figures where applicable for progress counters.
- **Microcopy & Metadata:** Streak labels, repeat intervals, and category tags utilize `label-md` and `label-sm` with tight uppercase tracking (`letter-spacing: 0.04em`) to establish crisp hierarchy without competing with task titles.
- **Long-form Reflections:** Journal notes and routine prompts default to `body-md` with relaxed line spacing (`lineHeight: 20px`) for comfortable reading.

## Layout & Spacing

The layout is anchored to an 8dp base grid (with 4dp subdivisions for fine icon-to-text alignment and micro-paddings), aligning directly with Android Material standards.

- **Mobile (<600dp):** Single column fluid container with `16dp` (`1rem`) outer margins and `12dp` to `16dp` vertical card gap rhythm. Bottom navigation bar takes anchor at the screen base with a fixed height of `80dp` including system nav-pill insets.
- **Tablet / Foldables (600dp - 840dp):** 8-column layout with `24dp` (`1.5rem`) margins. Screen bifurcates into a primary habit stream (5 columns) and an analytics/journal rail (3 columns).
- **Desktop / Large Displays (>840dp):** 12-column structure centered up to a max width of `1040dp` with a permanent navigation rail docked on the start side.

Maintain vertical cadence through strict multiples of 8dp. Internal card padding is consistently `16dp` (`space-md`) for standard habit tiles and `20dp` to `24dp` (`space-lg`) for weekly summaries.

## Elevation & Depth

Visual hierarchy rejects harsh drop shadows and heavy multi-colored elevations in favor of tonal surface layering complemented by soft ambient diffusion.

- **Level 0 (App Canvas):** Secondary background (`#FFFDF7` light / `#171B22` dark). Flat, non-elevated base.
- **Level 1 (Default Habit & Metric Cards):** Cream surface (`#F8F5ED` light / `#202630` dark) paired with a 1dp stroke (`#E8EAF0` light / `#303846` dark). No vertical drop shadow in default resting state.
- **Level 2 (Interactive Floating Elements & Modals):** Pure background surface elevated with a soft tinted ambient shadow: `box-shadow: 0 8px 24px -4px rgba(23, 37, 53, 0.05), 0 2px 6px -1px rgba(23, 37, 53, 0.03)` in light mode, and a soft highlight layer with `box-shadow: 0 8px 24px -4px rgba(0, 0, 0, 0.45)` in dark mode.
- **Level 3 (Floating Action Button & Floating Sheets):** Primary control elevation using 12dp blur with 12% primary tint (`rgba(93, 159, 198, 0.20)`).

## Shapes

The shape system employs an organic, hyper-rounded aesthetic to soften daily task management:

- **Cards & Habit Tiles:** `rounded-2xl` (`1.5rem` / `24dp`) to `rounded-3xl` (`2rem` / `32dp`) corner radii, generating a friendly pebble-like tactile quality.
- **Pills & Badges:** Full pill radius (`9999px`) across category tags, frequency indicators, and streak markers.
- **Buttons & Touch Targets:** `rounded-full` (`9999px`) for floating triggers and primary action buttons; `16dp` for compact contextual buttons.
- **Progress Indicators:** True circular geometries (`rounded-full`) for daily rings, checkbox toggles, and habit status indicators.

## Components

### Buttons
- **Primary Action Button:** Full pill shape, filled with `#5D9FC6` (Dark: `#79BDE5`), label set in `label-lg` colored `#FFFFFF` (Dark: `#111318`). Height: `52dp`. Resting state has no shadow; active press displays subtle scaling (`0.98`) and elevation.
- **Secondary / Ghost Button:** Transparent background with subtle 1.5dp border in `#E8EAF0` (Dark: `#303846`), text in `#172535` (Dark: `#F4F7FA`).

### Habit Cards
- **Base Tile:** Surface `#F8F5ED` (Dark: `#202630`), border 1dp solid `#E8EAF0` (Dark: `#303846`), radius `24dp`.
- **Layout:** Leading circular progress ring or completion checkbox (`44dp` touch target), centered column with habit name in `headline-sm` and sub-label (streak count + timing) in `body-sm`, and trailing numeric micro-counter.
- **Completed State:** Card surface transitions to `#EAF6FC` (Dark: `#282F3A`) with subtle border in `#8FB49A`, ring turns solid sage green.

### Checkboxes & Check Rings
- Checkbox is built as a `32dp` circular ring with a `2.5dp` outline stroke.
- Default: `#E8EAF0` track.
- Checked: Fill `#8FB49A` displaying a centered checkmark in white with spring animation.

### Chips & Badges
- Height: `28dp` to `32dp`, full pill radius.
- Inactive: Background `#FFFFFF` (Dark: `#171B22`), border 1dp `#E8EAF0` (Dark: `#303846`), text `#697789` (Dark: `#A8B2C0`).
- Active: Background `#EAF6FC` (Dark: `#304454`), text `#5D9FC6` (Dark: `#9DD9F5`), no border.

### Input Fields
- Single-line and multi-line habit input fields utilize filled surface styling (`#FFFFFF` in light mode, `#171B22` in dark mode) encased in a soft container.
- Height: `56dp`, radius `16dp`.
- Resting border: 1dp `#E8EAF0`; focused border: 1.5dp `#5D9FC6`.
- Placeholder text in `#697789` using `body-md`.

### Circular Progress Rings
- Circular SVG/Canvas gauge with line cap rounded.
- Track width: `6dp`. Background track: `#E8EAF0` (Dark: `#303846`).
- Progress indicator: Gradient or solid `#5D9FC6` progressing to `#8FB49A` upon 100% completion.

### Bottom Navigation Bar
- Native Material 3 layout, height `80dp`, background `#FFFFFF` (Dark: `#171B22`) with top border 1dp `#E8EAF0` (Dark: `#303846`).
- 4 navigation destinations: Today (Routines), Habits (Manage), Analytics, Profile.
- Active item uses soft pill container `#EAF6FC` (Dark: `#304454`) with `#5D9FC6` icon accent.