package com.github.tivecs.skillcard.internal.data.repositories

import com.github.tivecs.skillcard.core.abilities.Ability

object AbilityRepository {

    val registeredAbilities = mutableMapOf<String, Ability<*>>()

    fun register(vararg abilities: Ability<*>) {
        abilities.forEach { ability ->
            if (registeredAbilities.containsKey(ability.identifier))
                throw IllegalArgumentException("Ability '${ability.identifier}' is already registered.")

            registeredAbilities[ability.identifier] = ability
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <TAttribute> get(identifier: String): Ability<TAttribute> {
        val ability =
            registeredAbilities[identifier] ?: throw NullPointerException("Ability '${identifier}' was not registered.")

        return ability as Ability<TAttribute>
    }
}