# SkillCard - Project Context

## Overview

SkillCard is a Minecraft Spigot/Paper plugin that provides a Wynncraft-inspired skill system with pattern-based casting and extensive customization.

**Target Market:** RPG/MMO Minecraft servers wanting unique combat systems.

---

## Core Architecture (core2 - WIP)

Migration in progress from `core` to `core2` package.

### Skill System Hierarchy

```
SkillBook
└── TriggerSkillSet (one per trigger)
    ├── Trigger (OnDamage, Pattern, etc.)
    ├── TriggerModifier (chance, cooldown, cost) [planned]
    └── Skills[]
        └── Skill
            ├── SkillTargetSlot[] (defines expected target types)
            └── SkillAbility[]
                ├── targetSlotIdentifier (references which slot)
                ├── ability reference
                └── attributes (user configured values)
```

### Key Classes (core2/entities/)

| Class | Purpose |
|-------|---------|
| `SkillBook` | Player-holdable item, contains TriggerSkillSets |
| `TriggerSkillSet` | Links trigger + skills for that trigger |
| `TriggerTargetSlotSource` | Maps skill's target slot to trigger's available target |
| `Skill` | Contains abilities and target slots |
| `SkillAbility` | References ability + target slot + configured attributes |
| `SkillTargetSlot` | Declares expected target type for abilities |
| `Trigger<TEvent>` | Abstract class with `handle()` and `getAvailableTargets()` |
| `AvailableTriggerTarget` | Declares what targets a trigger provides (key, outputType, getOutput lambda) |
| `Ability<TAttribute>` | Interface with `targetRequirement` + `configurableRequirements` |

### Execution Flow

```
1. Player triggers event (e.g., attacks)
2. Trigger.handle(event) → TriggerExecutionResult
3. SkillBook.execute(triggerResult)
4. Find matching TriggerSkillSet by trigger identifier
5. For each Skill in set:
   - Get TriggerTargetSlotSource mappings
   - For each SkillAbility:
     - Resolve target from slot source
     - Create attribute via TypeConverters
     - Execute ability
```

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
├── builtin/                # Old builtin (being migrated)
│   └── abilities/
├── builtin2/               # New builtin for core2
│   ├── abilities/
│   └── triggers/
├── cmds/                   # Commands (admin, player)
├── core/                   # Old architecture (being migrated)
│   ├── abilities/
│   ├── books/
│   ├── converters/         # TypeConverters (shared)
│   ├── player/
│   ├── skills/
│   └── triggers/
├── core2/                  # NEW architecture (WIP)
│   ├── builtin/
│   │   ├── abilities/      # DamageAbility, ThunderAbility
│   │   └── triggers/       # OnDamageTrigger
│   └── entities/
│       ├── abilities/      # Ability interface
│       ├── books/          # SkillBook, TriggerSkillSet, TriggerTargetSlotSource
│       ├── skills/         # Skill, SkillAbility, SkillTargetSlot
│       └── triggers/       # Trigger, AvailableTriggerTarget, TriggerExecutionResult
├── gui/
│   └── admin/              # Admin GUI menus
│       └── items/          # Menu items
└── internal/
    ├── data/tables/        # Database tables (SQLite only, MySQL not tested)
    ├── exceptions/
    └── extensions/

docs/
├── type-converters.md      # Type converter system documentation
└── trigger-system-design.md # Trigger/game design documentation
```

---

## Tech Stack

- **Language:** Kotlin
- **Platform:** Spigot/Paper API
- **GUI:** Custom inventory-based menus
- **Database:** SQLite only (MySQL not tested)
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