# Trigger System Design

## Overview

This document covers the game design philosophy behind the skill trigger system, focusing on balancing player freedom with admin control.

---

## Core Problem

**Question:** Should players freely choose triggers for their skill books?

**Risk Example:** Player sets ThunderStrike to `OnAttack` trigger = lightning spam on every hit

---

## Design Philosophy

### Not Full Restriction (Wynncraft Style)
Fixed patterns per class. Simple but no player expression.

### Not Full Freedom
Players choose anything = balance nightmare.

### Our Approach: Controlled Freedom
- **Admin controls HOW triggers work** (modifiers)
- **Player chooses WHICH trigger to use** (from allowed list)

---

## Trigger Types

### Passive Triggers (Auto-execute)
| Trigger | Risk Level | Notes |
|---------|------------|-------|
| OnAttack | High | Spammable with fast weapons |
| OnDamaged | High | Can create proc loops |
| OnKill | Medium | Farm weak mobs for buffs |
| OnDeath | Low | One-time, situational |

### Pattern Triggers (Manual-execute)
| Trigger | Risk Level | Notes |
|---------|------------|-------|
| R-R-L | Low | Requires player skill |
| L-R-L | Low | Execution time = natural cooldown |
| HOLD-R | Low | Charge time limits spam |

**Pattern triggers are self-balancing** because they require:
- Execution time (~1-2 seconds)
- Player attention
- Can be interrupted
- Skill to execute correctly

---

## Trigger Modifier System

Admin defines modifiers per trigger per skill book:

```kotlin
data class TriggerModifier(
    val trigger: Trigger,
    val successChance: Double = 1.0,      // 0.5 = 50%
    val cooldown: Long = 0,               // milliseconds
    val executeOnNextAttack: Boolean = false,
    val chargeTime: Long = 0,
    val manaCost: Int = 0,
    val healthCost: Double = 0.0
)
```

### Example Configuration

```yaml
thunder_strike:
  abilities: [thunder, knockback]

  allowed_triggers:
    # Spammable but unreliable
    - trigger: on_attack
      success_chance: 0.5      # 50% per hit
      cooldown: 0s

    # Reliable but need kills
    - trigger: on_kill
      success_chance: 1.0
      cooldown: 0s

    # Guaranteed but needs skill + has cooldown
    - trigger: pattern_RRL
      success_chance: 1.0
      cooldown: 5s
      execute_on_next_attack: true

    # Defensive, rare proc
    - trigger: on_damaged
      success_chance: 0.25
      cooldown: 3s
```

### Player Trade-offs

| Trigger | Trade-off |
|---------|-----------|
| OnAttack 50% | Spammable but unreliable |
| OnKill 100% | Reliable but need to finish kills |
| Pattern R-R-L | Guaranteed but needs skill + cooldown |
| OnDamaged 25% | Defensive but rare |

---

## Pattern Binding System

### Wynncraft vs Our Design

| Aspect | Wynncraft | Our Design |
|--------|-----------|------------|
| Pattern binding | Fixed per class | Player chooses |
| Skills per pattern | 1 | Multiple (combo) |
| Balance control | Pattern = specific power | Modifiers per skill book |
| Skill ceiling | Learn fixed patterns | Build your own combos |
| Player expression | Low | High |

### Why Diverge from Wynncraft?

Wynncraft's system:
- Simple and balanced
- But: "Here's your spell, deal with it"
- No player ownership

Our system:
- "Here's your spell, make it yours"
- Every player's setup is unique
- Skill = building + execution

### Player-Controlled Pattern Bindings

```
Player's Pattern Bindings:

  Pattern "R-R-L" --> [Slot 1, Slot 3]  <-- Both execute simultaneously!
  Pattern "L-R-L" --> [Slot 2]
  Pattern "R-L-R" --> [Slot 1, Slot 2, Slot 4]  <-- Triple combo!

  Slot 1: Thunder Strike
  Slot 2: Speed Buff
  Slot 3: Heal
  Slot 4: Fire Burst
```

**Player casts R-R-L** = Thunder Strike + Heal execute together

### Architecture

```kotlin
data class PatternBinding(
    val playerId: UUID,
    val pattern: String,          // "R-R-L"
    val boundSlots: List<Int>,    // [1, 3]
    val executionMode: PatternExecutionMode
)

enum class PatternExecutionMode {
    SIMULTANEOUS,  // All skills at once (combo burst)
    SEQUENTIAL     // One after another (chain combo)
}
```

### Execution Flow

```kotlin
fun onPatternExecuted(player: Player, pattern: String) {
    val bindings = getPlayerBindings(player, pattern)

    bindings.boundSlots.forEach { slot ->
        val skillBook = getSkillBookInSlot(player, slot) ?: return@forEach

        // Check if this skill book allows pattern triggers
        val modifier = skillBook.getModifierForPattern(pattern)
        if (modifier == null) {
            player.sendMessage("${skillBook.name} doesn't support pattern triggers!")
            return@forEach
        }

        // Each skill has its own cooldown/chance check
        tryExecuteSkill(player, skillBook, modifier)
    }
}
```

---

## Balance Control Summary

### What Admin Controls

| Control | Purpose |
|---------|---------|
| Allowed trigger types | Prevent broken combinations |
| Success chance | Nerf spammy triggers |
| Cooldown per skill | Individual rate limiting |
| Resource costs | Economic balance |
| Whitelist/blacklist | Hard restrictions if needed |

### What Player Controls

| Control | Purpose |
|---------|---------|
| Which trigger to use | Personal preference |
| Which pattern for which slot | Comfort/ergonomics |
| Multiple skills per pattern | Combo building |
| Simultaneous vs sequential | Playstyle choice |

---

## Why This Design Works

### 1. Build Identity
```
Player A: "I run Thunder + Speed on R-R-L for hit-and-run"
Player B: "I stack Thunder + Fire + Knockback on R-R-L for burst"
```
Same skill books, different playstyles. Players discuss *their* builds.

### 2. Discovery Moments
Players finding synergies feels rewarding:
> "If I bind Heal and Thorns to the same pattern, I can tank AND reflect!"

### 3. Skill Ceiling Layers
- **Casual:** Use defaults, one skill per pattern
- **Intermediate:** Optimize patterns for comfort
- **Hardcore:** Multi-skill combos, situational bindings

### 4. Admin Safety Net
Freedom isn't unlimited:
- Triggers can be whitelisted/blacklisted per skill
- Cooldowns prevent spam
- Costs prevent everything-at-once
- Chance reduces reliability of "OP" triggers

---

## Risk Mitigation

| Risk | Mitigation |
|------|------------|
| Overwhelming new players | Good defaults + simple tutorial |
| Broken combos discovered | Admin can hotfix modifiers via config |
| Analysis paralysis | Preset "recommended builds" |
| Balance issues | Per-skill cooldowns still apply individually |

---

## Complete Architecture Overview

```
+------------------------------------------------------------------+
|                         SkillBook                                |
+------------------------------------------------------------------+
|  Abilities: [Thunder, Knockback]                                 |
|                                                                  |
|  Trigger Rules (Admin-defined):                                  |
|  +----------------+---------+----------+------------------------+|
|  | Trigger        | Chance  | Cooldown | Special                ||
|  +----------------+---------+----------+------------------------+|
|  | OnAttack       | 50%     | 0s       | -                      ||
|  | OnKill         | 100%    | 0s       | -                      ||
|  | Pattern (any)  | 100%    | 5s       | Execute on next attack ||
|  | OnDamaged      | 25%     | 3s       | -                      ||
|  +----------------+---------+----------+------------------------+|
|                                                                  |
|  Player Selection: [ Pattern R-R-L ] <-- Player's choice         |
+------------------------------------------------------------------+

+------------------------------------------------------------------+
|                    Player Pattern Bindings                       |
+------------------------------------------------------------------+
|                                                                  |
|  R-R-L --> [Slot 1: Thunder, Slot 3: Heal]  (simultaneous)       |
|  L-R-L --> [Slot 2: Speed Buff]                                  |
|  R-L-R --> [Slot 4: Fire Burst]                                  |
|                                                                  |
+------------------------------------------------------------------+

Execution:
  Player inputs R-R-L
    --> Check Slot 1 (Thunder): allowed? cooldown? cost? --> Execute
    --> Check Slot 3 (Heal): allowed? cooldown? cost? --> Execute
```

---

## Conclusion

**Design Goal:** Fresh air for players compared to rigid systems like Wynncraft.

**Key Differentiator:** Same combat feel (pattern casting) but with more player expression.

**Balance Strategy:** Admin controls power budget via modifiers, player controls ergonomics and combos.

**Worst Case Fallback:** If too chaotic, restrictions can be added later. But freedom is hard to add to a rigid system.

---

## References

- Wynncraft spell system (fixed patterns per class)
- Action RPGs with combo systems (Devil May Cry, Monster Hunter)
- MMOs with proc-based combat (Path of Exile, Black Desert Online)