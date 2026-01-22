package com.github.tivecs.skillcard.core.builders.skill

import com.github.tivecs.skillcard.core.entities.abilities.Ability
import com.github.tivecs.skillcard.core.entities.abilities.AbilityRequirement
import com.github.tivecs.skillcard.core.entities.skills.SkillAbility
import com.github.tivecs.skillcard.core.entities.skills.SkillTargetSlot

class SkillAbilityBuilder(val skillBuilder: SkillBuilder) {

    var abilityIdentifier: String = ""
    var ability: Ability<*>? = null
    var attributes = mutableMapOf<String, Any>()

    val usedTargetSlots = mutableListOf<SkillTargetSlot>()

    fun setAbility(ability: Ability<*>): SkillAbilityBuilder {
        this.ability = ability
        this.abilityIdentifier = ability.identifier
        return this
    }

    fun addTargetSlot(slotIdentifier: String): SkillAbilityBuilder {
        val slot = skillBuilder.targetSlots.firstOrNull { it.identifier == slotIdentifier }
        if (slot != null && !usedTargetSlots.contains(slot)) {
            usedTargetSlots.add(slot)
        }
        return this
    }

    fun removeTargetSlot(slotIdentifier: String): SkillAbilityBuilder {
        usedTargetSlots.removeIf { it.identifier == slotIdentifier }
        return this
    }

    fun setAttributeValue(requirement: AbilityRequirement, value: Any): SkillAbilityBuilder {
        this.attributes[requirement.key] = value
        return this
    }

    fun removeAttribute(requirement: AbilityRequirement): SkillAbilityBuilder {
        this.attributes.remove(requirement.key)
        return this
    }

    fun getRequiredAttributes(): List<AbilityRequirement> {
        return ability?.configurableRequirements?.filter {
            it.required && it.defaultValue == null
        } ?: emptyList()
    }

    fun validate(): List<String> {
        val errors = mutableListOf<String>()

        if (ability == null) {
            errors.add("Ability cannot be blank")
        }

        val missingRequiredRequirements = getRequiredAttributes().filter { !attributes.containsKey(it.key) }

        if (missingRequiredRequirements.isNotEmpty()) {
            for (requiredRequirement in missingRequiredRequirements) {
                errors.add("Attribute '${requiredRequirement.key}' is required")
            }
        }

        return errors
    }

    fun build(): SkillAbility {
        val errors = validate()

        if (errors.isNotEmpty()) {
            throw IllegalStateException("Some fields are missing or invalid during build SkillAbility. Errors: $errors")
        }

        return SkillAbility().apply {
            abilityIdentifier = this@SkillAbilityBuilder.abilityIdentifier
            attributes.putAll(this@SkillAbilityBuilder.attributes)

            // TODO: Later will support multiple target slot
            targetSlot = usedTargetSlots.first()
        }
    }

}