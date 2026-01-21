package com.github.tivecs.skillcard.core.converters

import com.github.tivecs.skillcard.core.builtin.abilities.DashAbilityDirection
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType
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
        register(LivingEntity::class, Location::class, NamedTypeConverter.LIVING_ENTITY_EYE_LOCATION.key) { it.eyeLocation }

        // Location converters - named "bed_spawn_location"
        // Only Player has bedSpawnLocation
        register(Player::class, Location::class, NamedTypeConverter.PLAYER_BED_SPAWN_LOCATION.key) { it.bedSpawnLocation }

        // LivingEntity converters
        register(LivingEntity::class, LivingEntity::class) { it }

        // Entity converters
        register(Entity::class, Entity::class) { it }

        // String to enums
        register(String::class, PotionEffectType::class) { PotionEffectType.getByName(it.uppercase()) }
        register(PotionEffectType::class, PotionEffectType::class) { it }

        register(String::class, DashAbilityDirection::class) { DashAbilityDirection.valueOf(it.uppercase()) }
        register(DashAbilityDirection::class, DashAbilityDirection::class) { it }
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