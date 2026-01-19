package com.github.tivecs.skillcard.core.skills

import com.github.tivecs.skillcard.core.abilities.Ability
import com.github.tivecs.skillcard.core.abilities.AbilityAttribute
import java.util.UUID

class SkillAbilityBuilder(val skillId: UUID, val ability: Ability<*>) {

    var abilityIdentifier: String = ""
        private set

    var executionOrder: Int = 1
        private set

    private val abilityAttributes: MutableMap<String, Any> = mutableMapOf()

    init {
        abilityIdentifier = ability.identifier
        val requirements = ability.getRequirements()

        if (requirements.isNotEmpty()) {
            for (requirement in requirements) {
                abilityAttributes[requirement.key] = requirement.defaultValue ?: continue
            }
        }
    }

    fun setAbilityIdentifier(identifier: String): SkillAbilityBuilder {
        this.abilityIdentifier = identifier
        return this
    }

    fun setExecutionOrder(order: Int): SkillAbilityBuilder {

        this.executionOrder = order
        return this
    }

    fun addAttribute(key: String, value: Any): SkillAbilityBuilder {
        this.abilityAttributes[key] = value
        return this
    }

    fun getAttribute(key: String): Any? {
        return this.abilityAttributes[key]
    }

    fun validate(): List<String> {
        val errors = mutableListOf<String>()
        if (abilityIdentifier.isBlank()) {
            errors.add("Ability identifier must be set and not blank.")
        }
        if (executionOrder < 1) {
            errors.add("Execution order must be greater than or equal to 1.")
        }
        return errors
    }

    @Suppress("UNCHECKED_CAST")
    fun build(): SkillAbility {
        val errors = validate()

        if (errors.isNotEmpty()) {
            throw IllegalStateException("Cannot build SkillAbility due to validation errors: ${errors.joinToString("; ")}")
        }

        return SkillAbility(
            skillId = skillId,
            abilityIdentifier = abilityIdentifier,
            executionOrder = executionOrder,
            abilityAttributes = abilityAttributes,
        ).also { skillAbility ->
            skillAbility.ability = ability as Ability<AbilityAttribute>
        }
    }
}