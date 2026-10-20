---
name: Everise
colors:
  surface: '#f8f9ff'
  surface-dim: '#ccdbf3'
  surface-bright: '#f8f9ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#eff4ff'
  surface-container: '#e5eeff'
  surface-container-high: '#dce9ff'
  surface-container-highest: '#d4e4fc'
  on-surface: '#0d1c2e'
  on-surface-variant: '#40484d'
  inverse-surface: '#233144'
  inverse-on-surface: '#eaf1ff'
  outline: '#70787e'
  outline-variant: '#c0c7ce'
  surface-tint: '#126588'
  primary: '#126588'
  on-primary: '#ffffff'
  primary-container: '#5b9fc5'
  on-primary-container: '#003449'
  inverse-primary: '#8ccff7'
  secondary: '#436650'
  on-secondary: '#ffffff'
  secondary-container: '#c4ecd0'
  on-secondary-container: '#496c56'
  tertiary: '#605e56'
  on-tertiary: '#ffffff'
  tertiary-container: '#9a978e'
  on-tertiary-container: '#313029'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#c4e7ff'
  primary-fixed-dim: '#8ccff7'
  on-primary-fixed: '#001e2d'
  on-primary-fixed-variant: '#004c69'
  secondary-fixed: '#c4ecd0'
  secondary-fixed-dim: '#a9d0b5'
  on-secondary-fixed: '#002111'
  on-secondary-fixed-variant: '#2b4e39'
  tertiary-fixed: '#e6e2d8'
  tertiary-fixed-dim: '#cac6bc'
  on-tertiary-fixed: '#1c1c15'
  on-tertiary-fixed-variant: '#48473f'
  background: '#f8f9ff'
  on-background: '#0d1c2e'
  surface-variant: '#d4e4fc'
typography:
  display-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 40px
    fontWeight: '700'
    lineHeight: 48px
    letterSpacing: -0.02em
  display-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 32px
    fontWeight: '600'
    lineHeight: 40px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 26px
    fontWeight: '600'
    lineHeight: 34px
    letterSpacing: -0.015em
  headline-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 22px
    fontWeight: '600'
    lineHeight: 28px
    letterSpacing: -0.01em
  headline-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 24px
    letterSpacing: 0em
  title-md:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '600'
    lineHeight: 22px
    letterSpacing: 0em
  body-lg:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
    letterSpacing: 0.01em
  body-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
    letterSpacing: 0.01em
  body-sm:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '400'
    lineHeight: 16px
    letterSpacing: 0.02em
  label-lg:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '500'
    lineHeight: 20px
    letterSpacing: 0.01em
  label-md:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
    letterSpacing: 0.02em
  stat-numeric:
    fontFamily: Plus Jakarta Sans
    fontSize: 24px
    fontWeight: '700'
    lineHeight: 28px
    letterSpacing: -0.02em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  gutter: 1rem
  margin: 1.25rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
---

## Brand & Style

The design system embodies calm productivity, intentional mindfulness, and premium self-mastery. Inspired by the philosophy of gradual compounding gains, the experience avoids aggressive streak gamification, harsh alerts, or anxiety-inducing metrics. Instead, the interface serves as a serene sanctuary where users build sustainable identity shifts through clear, unhurried routines.

The visual style merges Nordic minimalism with tactile warmth and editorial refinement. Soft, breathing surfaces pair with balanced, human-centered typography and purposeful micro-interactions. The emotional response should be one of relief, quiet confidence, and focus—creating a reflective digital notebook rather than a high-pressure to-do list.

## Colors

The palette is tuned specifically for calm daytime interaction and minimal visual fatigue:

- **Canvas Background (`#F7F9FC`)**: A cool, airy tinted white that cushions elevated surfaces without harsh contrast.
- **Primary Brand Blue (`#5B9FC5`) & Deep Blue (`#286D91`)**: Evokes focus, flow, and composure. `#5B9FC5` serves as the primary focal tone, while `#286D91` provides high-contrast accents, selected text states, and key icons.
- **Pale Blue Surface (`#E4F0FA`)**: Used for selected interactive states, progress container fills, and current-day calendar accents.
- **Success Sage (`#8FB59B`) & Pale Sage (`#E8F1E9`)**: Replaces aggressive electric greens with organic, grounding tones to signify completion, habit maintenance, and gentle celebration.
- **Warm Surface Cream (`#FAF6EB`)**: Applied sparingly to reflective cards, streak milestones, and thoughtful habit insights to inject human warmth into analytical charts.
- **Surface Neutral (`#FFFFFF`)**: Pure white base for primary cards and floating sheets, ensuring crisp separation from the cool canvas.
- **Typography**: Primary copy is deep navy (`#172638`) for legible, glare-free reading. Secondary metadata and unselected icons use muted slate (`#718096`).
- **Dividers & Structural Borders (`#E4E8EF`)**: Delicate, hairline boundaries that structure layout items without visual noise.
- **Destructive Accent (`#D96C6C`)**: A restrained, desaturated coral for destructive prompts or habit deletion, avoiding panic-inducing alerts.

## Typography

The type system blends the contemporary, geometric approachability of Plus Jakarta Sans for structural titles with the legibility of Inter for continuous reading and interface labels.

- Enable OpenType tabular figures (`fontFeatureSettings = "tnum"`) across all numerical metrics, streak tickers, timers, percentages, and progress tallies to ensure stable vertical alignment without horizontal jitter during animations.
- Headlines maintain a snug line-height and tight tracking to produce a confident, editorial presence.
- Body levels utilize standard open tracking and generous line spacing to ensure frictionless reading in daily reflections, journals, and habit notes.

## Layout & Spacing

Built strictly on an 8dp / 4dp spacing system aligned with Jetpack Compose idioms:

- **Mobile Viewports (<600dp)**: Single-column fluid stream anchored by an edge margin of `20dp` (`margin`) and internal vertical gaps of `12dp` to `16dp` (`gutter`) between habit modules.
- **Tablet & Foldable Viewports (≥600dp)**: Splits into an adaptive dual-column layout—a navigational dashboard and routine timeline on the primary panel (8 columns), paired with an analytical breakdown and reflection log on the secondary pane (4 columns) using `24dp` gutters.
- **Rhythm & Touch Anchors**: Every interactive element enforces a minimum bounding touch target of `48dp × 48dp`, regardless of visual icon size. Habit list items enforce a minimum height of `64dp` to permit effortless single-handed check-offs on moving displays.

## Elevation & Depth

Visual depth is achieved through delicate surface tinting and soft, ambient diffused shadows rather than heavy structural elevation:

- **Level 0 (Base Canvas)**: Flat `#F7F9FC` background.
- **Level 1 (Habit Cards & Metric Pods)**: Solid `#FFFFFF` fill resting upon `#F7F9FC`, held together by a hairline border (`1dp` of `#E4E8EF`) and a subtle ambient drop shadow: `0dp 4dp 16dp rgba(23, 38, 56, 0.04)`.
- **Level 2 (Active Sheets & Modals)**: Elevated surfaces utilize a diffused anchor shadow: `0dp 8dp 32dp rgba(23, 38, 56, 0.08)` paired with an inner border of `#FFFFFF` to simulate edge refraction.
- **Interactive Check-In Elevation**: Habit nodes transition from flat elevation to a slight inset well when toggled, utilizing pale sage `#E8F1E9` fills rather than heavy drop shadows, reinforcing a physical tactile imprint.

## Shapes

The design system embraces balanced, organic curves that convey warmth while maintaining structural order:

- **Cards & Habit Blocks (`rounded-2xl` / 16dp–20dp)**: Large structural containers use 16dp to 20dp corner radii to maintain a friendly, calm silhouette.
- **Buttons & Input Targets (12dp–16dp)**: Interactive inputs and actionable buttons utilize refined radii that harmonize with parent cards.
- **Progress Capsules & Frequency Chips**: Continuous curvature (Pill / 999dp) for streak status, time filters, and progress bar ends, emphasizing fluid forward movement.

## Components

### Buttons
- **Primary Action**: Filled with Primary Brand Blue (`#5B9FC5`), text in pure white (`#FFFFFF`), with an elevation-less flat layout. Minimum height `52dp`, corner radius `14dp`. On press, smooth scale animation to `0.98` with background deepening to `#286D91`.
- **Secondary / Ghost**: Hairline border (`1dp` `#E4E8EF`), background transparent or `#FFFFFF`, text `#172638`.
- **Tonal Gentle**: Pale Blue (`#E4F0FA`) container with Deep Blue (`#286D91`) label for secondary milestones.

### Habit Cards & List Rows
- Minimum height `72dp`, container `#FFFFFF`, border `1dp` `#E4E8EF`, corner radius `18dp`.
- Left icon badge: `44dp × 44dp` capsule tinted with the habit's category tint or `#E4F0FA`.
- Title aligned with `title-md`, streak meta beneath in `label-md` (`#718096`).
- Right completion node: `40dp × 40dp` circular checkbox anchor.

### Checkboxes & Habit Check Nodes
- Custom ring trigger (`36dp` outer, `48dp` target footprint).
- Unchecked state: empty ring with `2dp` stroke of `#E4E8EF`.
- Checked state: animated morphing fill to Success Sage (`#8FB59B`), with a centered pure-white stroke icon (`#FFFFFF`). Micro-scale bounce (`0.85` -> `1.05` -> `1.0`) over `220ms` in Jetpack Compose `spring()`.

### Chips & Routine Selectors
- Pill shape (`RoundedCornerShape(percent = 50)`), height `36dp`.
- Unselected: background `#FFFFFF`, border `1dp` `#E4E8EF`, text `#718096`.
- Selected: background `#172638`, text `#FFFFFF`, zero border.

### Input Fields
- Outlined style with filled background (`#FFFFFF`), height `56dp`, corner radius `14dp`.
- Unfocused border `1dp` `#E4E8EF`; focused border `1.5dp` `#5B9FC5` with label animating upward.
- Text input in deep navy (`#172638`), placeholder in muted slate (`#718096`).

### Progress Trackers & Metric Bars
- Height `8dp` to `12dp`, track background `#E4E8EF`, rounded capsule ends.
- Fill indicator: smooth transition with Success Sage (`#8FB59B`) or Brand Blue (`#5B9FC5`).
- Stat labels leverage `stat-numeric` with tabular numerals placed cleanly above or adjacent to the track.

### Insight & Milestone Highlights
- Containers styled using Warm Cream (`#FAF6EB`) with a delicate amber border tint (`#F0E5CB`), displaying daily motivational reflections and compounding gains insights.