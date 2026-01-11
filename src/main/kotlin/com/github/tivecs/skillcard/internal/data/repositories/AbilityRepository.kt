package com.github.tivecs.skillcard.internal.data.repositories

import com.github.tivecs.skillcard.core.abilities.Ability
import org.bukkit.plugin.java.JavaPlugin

object AbilityRepository {

    private val registeredAbilities = mutableMapOf<String, Ability<*>>()

    private val sourcePluginAbilities = mutableMapOf<String, List<Ability<*>>>()

    fun count(): Int = registeredAbilities.size

    fun getAllIdentifiers(): Set<String> = registeredAbilities.keys

    fun getAllAbilitiesWithSources(): Map<String, List<Ability<*>>> = sourcePluginAbilities

    fun getAllAbilities(): Collection<Ability<*>> = registeredAbilities.values

    fun register(sourcePlugin: JavaPlugin, vararg abilities: Ability<*>) {
        abilities.forEach { ability ->
            if (registeredAbilities.containsKey(ability.identifier))
                throw IllegalArgumentException("Ability '${ability.identifier}' is already registered.")

            registeredAbilities[ability.identifier] = ability

            sourcePluginAbilities[sourcePlugin.name] =
                sourcePluginAbilities.getOrDefault(sourcePlugin.name, emptyList()) + ability
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <TAttribute> get(identifier: String): Ability<TAttribute> {
        val ability =
            registeredAbilities[identifier] ?: throw NullPointerException("Ability '${identifier}' was not registered.")

        return ability as Ability<TAttribute>
    }
}