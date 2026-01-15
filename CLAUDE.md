# SkillCard - Project Context

## Overview

SkillCard is a Minecraft Spigot/Paper plugin that provides a Wynncraft-inspired skill system with pattern-based casting and extensive customization.

**Target Market:** RPG/MMO Minecraft servers wanting unique combat systems.

---

## Core Architecture

### Skill System Hierarchy

```
SkillBook (player holds this)
└── Skill
    └── SkillAbility (links to Ability with attributes)
        └── Ability (Thunder, Heal, PotionEffect, etc.)
            └── AbilityAttribute (execution parameters)
```

### Key Classes

| Class | Purpose |
|-------|---------|
| `Skill` | Definition of a skill with abilities |
| `SkillBook` | Player-holdable item containing a skill |
| `SkillAbility` | Links skill to ability with configured attributes |
| `Ability<TAttribute>` | Base interface for all abilities |
| `AbilityAttribute` | Data required to execute an ability |
| `Trigger` | When skill activates (OnAttack, Pattern, etc.) |

---

## Type Converter System

**Location:** `docs/type-converters.md`

Handles automatic type conversion for ability attributes:
- Inheritance-based resolution (register `Entity`, works for `Player`)
- Named converters (`eye_location` vs default `location`)
- Specificity priority (more specific class wins)

```kotlin
// Example: Thunder needs Location, accepts Entity/Player/Block
TypeConverters.convert(player, Location::class) // Uses entity.location
TypeConverters.convert(player, Location::class, "eye_location") // Uses livingEntity.eyeLocation
```

---

## Trigger System Design

**Location:** `docs/trigger-system-design.md`

### Design Philosophy: Controlled Freedom

- **Admin controls HOW** triggers work (modifiers: chance, cooldown, cost)
- **Player chooses WHICH** trigger to use (from allowed list)

### Trigger Types

| Type | Examples | Risk |
|------|----------|------|
| Passive | OnAttack, OnDamaged, OnKill | High (spammable) |
| Pattern | R-R-L, L-R-L, HOLD-R | Low (self-balancing) |

### Trigger Modifiers (Admin-defined per SkillBook)

```kotlin
data class TriggerModifier(
    val trigger: Trigger,
    val successChance: Double,    // 0.5 = 50%
    val cooldown: Long,           // milliseconds
    val executeOnNextAttack: Boolean,
    val manaCost: Int
)
```

### Pattern Binding (Player-controlled)

Players can:
- Bind any pattern to any skill slot
- Bind multiple skills to same pattern (combo execution)
- Choose simultaneous vs sequential execution

```
Pattern "R-R-L" → [Slot 1: Thunder, Slot 3: Heal] → Both execute!
```

---

## Key Design Decisions

### 1. Why Pattern Triggers?
- Wynncraft-style combat feel
- Self-balancing (execution time, skill required)
- Unique differentiator from other plugins

### 2. Why Player-Customizable Bindings?
- "Fresh air" compared to Wynncraft's fixed patterns
- Player ownership of builds
- Discovery moments when finding synergies

### 3. Why Admin Modifier System?
- Balance control without restricting freedom
- Hotfix balance issues via config
- Different triggers can have different trade-offs

### 4. Why Type Converters?
- Automatic detection of acceptable input types
- DRY principle (register once, works for all subclasses)
- Named converters for semantic differences (location vs eye_location)

---

## Builtin Abilities

| Ability | Attribute Requirements |
|---------|----------------------|
| `ThunderAbility` | `location: Location` (from trigger) |
| `HealAbility` | `target: LivingEntity`, `amount: Double` |
| `DamageAbility` | `target: LivingEntity`, `damage: Double` |
| `PotionEffectAbility` | `target: LivingEntity`, `type`, `duration`, `amplifier` |
| `IgniteAbility` | `target: Entity`, `duration: Int` |
| `ShootProjectileAbility` | `origin: Location` (eye_location), `direction`, `type` |

---

## Project Structure

```
src/main/kotlin/com/github/tivecs/skillcard/
├── builtin/
│   └── abilities/          # Built-in ability implementations
├── cmds/                   # Commands (admin, player)
├── core/
│   ├── abilities/          # Ability interfaces & base classes
│   ├── books/              # SkillBook system
│   ├── player/             # Player event handling
│   ├── skills/             # Skill, SkillAbility, SkillBuilder
│   └── triggers/           # Trigger system
├── gui/
│   └── admin/              # Admin GUI menus
│       └── items/          # Menu items
└── internal/
    ├── data/tables/        # Database tables
    ├── exceptions/         # Custom exceptions
    └── extensions/         # Kotlin extensions

docs/
├── type-converters.md      # Type converter system documentation
└── trigger-system-design.md # Trigger/game design documentation
```

---

## Tech Stack

- **Language:** Kotlin
- **Platform:** Spigot/Paper API
- **GUI:** Custom inventory-based menus
- **Database:** (check internal/data/tables)
- **Dependencies:** XSeries (cross-version materials)

---

## Development Notes

### When Adding New Abilities

1. Create `XxxAbilityAttribute` data class implementing `AbilityAttribute`
2. Create `XxxAbility` object implementing `Ability<XxxAbilityAttribute>`
3. Implement `getRequirements()` to declare needed inputs
4. Implement `createAttribute()` using `TypeConverters.convert()`
5. Implement `execute()` with the ability logic

### When Adding New Triggers

1. Consider if passive (admin-only) or pattern (player-configurable)
2. Add to trigger registry
3. Define default modifiers
4. Update documentation

---

## Pricing Estimate

Based on market analysis: **$18-25** at launch

Comparable to: MMOCore, MMOItems, AureliumSkills

Unique selling point: Wynncraft-style combat with admin control + player customization