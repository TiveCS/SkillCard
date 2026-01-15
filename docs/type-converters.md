# Type Converters Pattern

## Problem

Abilities require specific types (e.g., `Location`), but triggers provide various types (e.g., `Entity`, `Player`, `Block`). We need:

1. **Automatic type detection** - Know what source types an ability can accept
2. **Named converters** - Different conversions for same types (e.g., `player.location` vs `player.eyeLocation`)
3. **Inheritance support** - Register `Entity` converter, automatically works for `Player`, `LivingEntity`, etc.
4. **Specificity priority** - If both `Entity` and `Player` converters exist, use `Player` for `Player` input

---

## Solution Architecture

### Core Components

1. **TypeConverters** - Central registry for all type conversions
2. **TypeConverter** - Individual converter definition
3. **AbilityRequirement** - Declares what an ability needs
4. **RequirementSource** - Whether value comes from trigger or user config

---

## Implementation

### TypeConverter Data Class

```kotlin
data class TypeConverter<TSource : Any, TTarget : Any>(
    val sourceType: KClass<TSource>,
    val targetType: KClass<TTarget>,
    val name: String,
    private val converter: (TSource) -> TTarget?
) {
    @Suppress("UNCHECKED_CAST")
    fun convert(value: Any): TTarget? = converter(value as TSource)
}
```

### TypeConverters Registry

```kotlin
package com.github.tivecs.skillcard.core.abilities

import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import kotlin.reflect.KClass

object TypeConverters {

    private const val DEFAULT_CONVERTER = "_default"

    // Map<TargetType, Map<Name, List<TypeConverter>>>
    private val converters = mutableMapOf<KClass<*>, MutableMap<String, MutableList<TypeConverter<*, *>>>>()

    init {
        // Location converters - default
        // Just register Entity, it will work for Player, LivingEntity, etc.
        register(Location::class, Location::class) { it }
        register(Entity::class, Location::class) { it.location }
        register(Block::class, Location::class) { it.location }

        // Location converters - named "eye_location"
        // Only LivingEntity has eyeLocation, so register that
        register(LivingEntity::class, Location::class, "eye_location") { it.eyeLocation }

        // Location converters - named "bed_location"
        // Only Player has bedSpawnLocation
        register(Player::class, Location::class, "bed_location") { it.bedSpawnLocation }

        // LivingEntity converters
        register(LivingEntity::class, LivingEntity::class) { it }

        // Entity converters
        register(Entity::class, Entity::class) { it }
    }

    private fun <TSource : Any, TTarget : Any> register(
        sourceType: KClass<TSource>,
        targetType: KClass<TTarget>,
        converter: (TSource) -> TTarget?
    ) {
        register(sourceType, targetType, DEFAULT_CONVERTER, converter)
    }

    private fun <TSource : Any, TTarget : Any> register(
        sourceType: KClass<TSource>,
        targetType: KClass<TTarget>,
        name: String,
        converter: (TSource) -> TTarget?
    ) {
        converters
            .getOrPut(targetType) { mutableMapOf() }
            .getOrPut(name) { mutableListOf() }
            .add(TypeConverter(sourceType, targetType, name, converter))
    }

    /**
     * Find the most specific converter for a given value
     * Priority: exact match > parent class > grandparent class > ...
     */
    private fun findMostSpecificConverter(
        value: Any,
        targetType: KClass<*>,
        name: String
    ): TypeConverter<*, *>? {
        val namedConverters = converters[targetType]?.get(name) ?: return null

        // Filter converters that can handle this value (via inheritance)
        val compatibleConverters = namedConverters.filter { converter ->
            converter.sourceType.java.isAssignableFrom(value::class.java)
        }

        if (compatibleConverters.isEmpty()) return null

        // Sort by specificity: more specific class = smaller inheritance distance
        // The most specific converter comes first
        return compatibleConverters.minByOrNull { converter ->
            getInheritanceDistance(value::class.java, converter.sourceType.java)
        }
    }

    /**
     * Calculate inheritance distance between two classes
     * Returns 0 for exact match, 1 for direct parent, 2 for grandparent, etc.
     * Returns Int.MAX_VALUE if not assignable
     */
    private fun getInheritanceDistance(valueClass: Class<*>, converterClass: Class<*>): Int {
        if (!converterClass.isAssignableFrom(valueClass)) return Int.MAX_VALUE
        if (valueClass == converterClass) return 0

        var distance = 0
        var current: Class<*>? = valueClass

        // Check class hierarchy
        while (current != null) {
            if (current == converterClass) return distance

            // Also check interfaces at each level
            for (iface in current.interfaces) {
                if (iface == converterClass) return distance + 1
                // Check parent interfaces
                val ifaceDistance = getInterfaceDistance(iface, converterClass, distance + 1)
                if (ifaceDistance < Int.MAX_VALUE) return ifaceDistance
            }

            current = current.superclass
            distance++
        }

        return Int.MAX_VALUE
    }

    private fun getInterfaceDistance(iface: Class<*>, target: Class<*>, currentDistance: Int): Int {
        if (iface == target) return currentDistance
        for (parent in iface.interfaces) {
            val dist = getInterfaceDistance(parent, target, currentDistance + 1)
            if (dist < Int.MAX_VALUE) return dist
        }
        return Int.MAX_VALUE
    }

    /**
     * Get all source types that can be converted to the target type
     */
    fun getAcceptableSourceTypes(targetType: KClass<*>, name: String? = null): List<KClass<*>> {
        val converterName = name ?: DEFAULT_CONVERTER
        return converters[targetType]?.get(converterName)?.map { it.sourceType } ?: emptyList()
    }

    /**
     * Get all available converter names for a target type
     */
    fun getAvailableConverterNames(targetType: KClass<*>): List<String> {
        return converters[targetType]?.keys
            ?.filter { it != DEFAULT_CONVERTER }
            ?.toList() ?: emptyList()
    }

    /**
     * Check if a named converter exists
     */
    fun hasConverter(targetType: KClass<*>, name: String): Boolean {
        return converters[targetType]?.containsKey(name) ?: false
    }

    /**
     * Check if a value can be converted to target type
     */
    fun canConvert(value: Any, targetType: KClass<*>, name: String? = null): Boolean {
        val converterName = name ?: DEFAULT_CONVERTER
        return findMostSpecificConverter(value, targetType, converterName) != null
    }

    /**
     * Convert a value to target type using default converter
     */
    fun <TTarget : Any> convert(value: Any, targetType: KClass<TTarget>): TTarget? {
        return convert(value, targetType, null)
    }

    /**
     * Convert a value to target type using named converter
     * @throws IllegalArgumentException if named converter doesn't exist
     */
    @Suppress("UNCHECKED_CAST")
    fun <TTarget : Any> convert(value: Any, targetType: KClass<TTarget>, name: String?): TTarget? {
        val converterName = name ?: DEFAULT_CONVERTER

        val namedConverters = converters[targetType]
            ?: return null

        // If name specified but doesn't exist, throw exception
        if (name != null && !namedConverters.containsKey(name)) {
            throw IllegalArgumentException(
                "Converter '$name' not found for target type ${targetType.simpleName}. " +
                "Available: ${getAvailableConverterNames(targetType)}"
            )
        }

        val converter = findMostSpecificConverter(value, targetType, converterName)
            ?: return null

        return (converter as TypeConverter<Any, TTarget>).convert(value)
    }
}
```

---

## Ability Requirement Classes

```kotlin
package com.github.tivecs.skillcard.core.abilities

import kotlin.reflect.KClass

enum class RequirementSource {
    TRIGGER,      // Value comes from trigger (e.g., attacker, defender)
    USER_CONFIG   // Value configured by user (e.g., potion type, duration)
}

data class AbilityRequirement(
    val key: String,
    val targetType: KClass<*>,
    val source: RequirementSource,
    val converterName: String? = null,  // Optional named converter
    val required: Boolean = true,
    val defaultValue: Any? = null
) {
    /**
     * Auto-resolved from TypeConverters registry
     */
    val acceptedSourceTypes: List<KClass<*>>
        get() = if (source == RequirementSource.TRIGGER) {
            TypeConverters.getAcceptableSourceTypes(targetType, converterName)
        } else {
            listOf(targetType)
        }
}
```

---

## Updated Ability Interface

```kotlin
interface Ability<TAttribute> where TAttribute : AbilityAttribute {

    val identifier: String
    val displayName: String
    val material: Material
    val description: String

    /**
     * Declares what this ability requires to execute
     */
    fun getRequirements(): List<AbilityRequirement> = emptyList()

    fun execute(attribute: TAttribute?): AbilityExecuteResultState

    fun <TEvent : Event> createAttribute(
        context: SkillExecutionContext<TEvent>,
        skillAbility: SkillAbility
    ): TAttribute?
}
```

---

## Example: ThunderAbility

```kotlin
data class ThunderAbilityAttribute(val location: Location) : AbilityAttribute

object ThunderAbility : Ability<ThunderAbilityAttribute> {
    override val identifier: String = "thunder"
    override val description: String = "&fStrikes lightning at the specified location."

    override fun getRequirements(): List<AbilityRequirement> = listOf(
        AbilityRequirement(
            key = "location",
            targetType = Location::class,
            source = RequirementSource.TRIGGER
            // converterName = null -> uses default converter
        )
    )

    override fun execute(attribute: ThunderAbilityAttribute?): AbilityExecuteResultState {
        if (attribute == null) return AbilityExecuteResultState.INVALID_ATTRIBUTE

        val world = attribute.location.world
        if (world == null || !attribute.location.isWorldLoaded)
            return AbilityExecuteResultState.CONDITION_NOT_MET

        world.strikeLightning(attribute.location)
        return AbilityExecuteResultState.EXECUTED
    }

    override fun <TEvent : Event> createAttribute(
        context: SkillExecutionContext<TEvent>,
        skillAbility: SkillAbility
    ): ThunderAbilityAttribute? {
        val trigger = context.skillBookContext.getTrigger()
        val triggerResult = context.skillBookContext.getTriggerResult()

        val triggerTarget = trigger.getTarget(triggerResult, TriggerAttributeKey.TARGET_TYPE.key)
            ?: return null

        // Use TypeConverters - works with Entity, Player, Location, Block, etc.
        val targetLocation = TypeConverters.convert(triggerTarget, Location::class)
            ?: return null

        return ThunderAbilityAttribute(targetLocation)
    }
}
```

---

## Example: ShootProjectileAbility (Named Converter)

```kotlin
data class ShootProjectileAbilityAttribute(
    val origin: Location,
    val direction: Vector,
    val projectileType: String
) : AbilityAttribute

object ShootProjectileAbility : Ability<ShootProjectileAbilityAttribute> {
    override val identifier: String = "shoot_projectile"

    override fun getRequirements(): List<AbilityRequirement> = listOf(
        AbilityRequirement(
            key = "origin",
            targetType = Location::class,
            source = RequirementSource.TRIGGER,
            converterName = "eye_location"  // Uses LivingEntity.eyeLocation
        ),
        AbilityRequirement(
            key = "projectile_type",
            targetType = String::class,
            source = RequirementSource.USER_CONFIG
        )
    )

    override fun <TEvent : Event> createAttribute(
        context: SkillExecutionContext<TEvent>,
        skillAbility: SkillAbility
    ): ShootProjectileAbilityAttribute? {
        // ...

        // Uses eye_location converter
        val origin = TypeConverters.convert(triggerTarget, Location::class, "eye_location")
            ?: return null

        // ...
    }
}
```

---

## Example: PotionEffectAbility (Mixed Sources)

```kotlin
object PotionEffectAbility : Ability<PotionEffectAbilityAttribute> {
    override val identifier: String = "potion_effect"

    override fun getRequirements(): List<AbilityRequirement> = listOf(
        // From trigger
        AbilityRequirement(
            key = "target",
            targetType = LivingEntity::class,
            source = RequirementSource.TRIGGER
        ),
        // User configured
        AbilityRequirement(
            key = "type",
            targetType = String::class,
            source = RequirementSource.USER_CONFIG
        ),
        AbilityRequirement(
            key = "duration",
            targetType = Int::class,
            source = RequirementSource.USER_CONFIG,
            defaultValue = 200
        ),
        AbilityRequirement(
            key = "amplifier",
            targetType = Int::class,
            source = RequirementSource.USER_CONFIG,
            defaultValue = 0
        )
    )
}
```

---

## Inheritance Resolution

### How It Works

```
Player extends LivingEntity extends Entity

Registered converters for Location (default):
- Entity::class -> Location (via entity.location)
- Location::class -> Location (identity)
- Block::class -> Location (via block.location)

Input: Player instance
```

**Resolution Process:**

1. Filter compatible converters: `Entity` matches (Player extends Entity)
2. Calculate inheritance distance:
   - `Player` to `Entity` = 2 (Player -> LivingEntity -> Entity)
3. Use converter with smallest distance

**Result:** Uses `Entity::class` converter, calls `player.location`

### Specificity Priority

```kotlin
// If we register a more specific converter:
register(Player::class, Location::class) { it.location }
register(Entity::class, Location::class) { it.location }

// Input: Player
// Distance to Player::class = 0 (exact match)
// Distance to Entity::class = 2

// Result: Uses Player::class converter (more specific)
```

---

## Usage for GUI/Validation

```kotlin
fun getAbilityInfo(ability: Ability<*>) {
    val requirements = ability.getRequirements()

    val triggerRequirements = requirements.filter { it.source == RequirementSource.TRIGGER }
    val configRequirements = requirements.filter { it.source == RequirementSource.USER_CONFIG }

    println("=== ${ability.identifier} ===")

    println("From Trigger:")
    triggerRequirements.forEach { req ->
        val converterInfo = if (req.converterName != null) " [${req.converterName}]" else ""
        println("  ${req.key}: ${req.targetType.simpleName}$converterInfo")
        println("    Accepts: ${req.acceptedSourceTypes.map { it.simpleName }}")
    }

    println("User Configurable:")
    configRequirements.forEach { req ->
        println("  ${req.key}: ${req.targetType.simpleName} (default: ${req.defaultValue})")
    }
}
```

**Output for ThunderAbility:**
```
=== thunder ===
From Trigger:
  location: Location
    Accepts: [Location, Entity, Block]
User Configurable:
  (none)
```

**Output for ShootProjectileAbility:**
```
=== shoot_projectile ===
From Trigger:
  origin: Location [eye_location]
    Accepts: [LivingEntity]
User Configurable:
  projectile_type: String (default: null)
```

**Output for PotionEffectAbility:**
```
=== potion_effect ===
From Trigger:
  target: LivingEntity
    Accepts: [LivingEntity]
User Configurable:
  type: String (default: null)
  duration: Int (default: 200)
  amplifier: Int (default: 0)
```

---

## Summary

| Feature | Implementation |
|---------|----------------|
| Auto type detection | `TypeConverters.getAcceptableSourceTypes()` |
| Named converters | `converterName` parameter in `AbilityRequirement` |
| Inheritance support | `isAssignableFrom()` + distance calculation |
| Specificity priority | `getInheritanceDistance()` finds closest match |
| Default fallback | `name = null` uses `_default` converter |
| Invalid name handling | Throws `IllegalArgumentException` with available names |