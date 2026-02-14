package com.github.tivecs.skillcard.core.builders.skill

import com.github.tivecs.skillcard.core.entities.abilities.Ability
import com.github.tivecs.skillcard.core.entities.abilities.AbilityAttribute
import com.github.tivecs.skillcard.core.entities.abilities.AbilityRequirement
import com.github.tivecs.skillcard.core.entities.skills.SkillAbility
import com.github.tivecs.skillcard.core.entities.skills.SkillTargetSlot
import kotlin.reflect.KClass

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
        if (ability == null) {
            throw IllegalStateException("Ability must be set before adding target slot")
        }

        val slot = skillBuilder.targetSlots.firstOrNull { it.identifier == slotIdentifier }
            ?: throw IllegalArgumentException("Target slot with identifier '$slotIdentifier' not found in skill '${skillBuilder.identifier}'")

        val isCompatible =
            slot.targetType == null ||
            ability != null && ability!!.targetRequirement.acceptedSourceTypes.contains(slot.targetType)

        if (!isCompatible) {
            throw IllegalArgumentException("Target slot of '${slotIdentifier}' value type is incompatible with ability '${ability!!.identifier}' target requirement accepted source types: ${ability!!.targetRequirement.acceptedSourceTypes.joinToString { "${it.simpleName}" }}")
        }

        if (!usedTargetSlots.contains(slot)) {
            usedTargetSlots.add(slot)

            if (slot.targetType == null) {
                slot.targetType =
                    ability!!.targetRequirement.acceptedSourceTypes.firstOrNull { it == slot.targetType } ?: slot.targetType
            }
        }

        return this
    }

    fun addTargetSlot(slot: SkillTargetSlot): SkillAbilityBuilder {
        if (ability == null) {
            throw IllegalStateException("Ability must be set before adding target slot")
        }

        val isCompatible =
            slot.targetType == null ||
                    ability!!.targetRequirement.acceptedSourceTypes.contains(slot.targetType)

        if (!isCompatible) {
            throw IllegalArgumentException("Target slot of '${slot.identifier} (${slot.targetType?.simpleName})' value type is incompatible with ability '${ability!!.identifier}' target requirement accepted source types: ${ability!!.targetRequirement.acceptedSourceTypes.joinToString { "${it.simpleName}" }}")
        }

        if (!usedTargetSlots.contains(slot)) {
            usedTargetSlots.add(slot)

            if (slot.targetType == null) {
                slot.targetType =
                    ability!!.targetRequirement.acceptedSourceTypes.firstOrNull { it == slot.targetType } ?: slot.targetType
            }
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

    fun reset() {
        this.abilityIdentifier = ""
        this.attributes.clear()
    }

    fun reset(skillAbility: SkillAbility) {
        this.abilityIdentifier = skillAbility.abilityIdentifier

        this.attributes.clear()
        this.attributes.putAll(skillAbility.attributes)

        this.usedTargetSlots.clear()
        this.usedTargetSlots.add(skillAbility.targetSlot)
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

    @Suppress("UNCHECKED_CAST")
    fun build(): SkillAbility {
        val errors = validate()

        if (errors.isNotEmpty()) {
            throw IllegalStateException("Some fields are missing or invalid during build SkillAbility. Errors: $errors")
        }

        return SkillAbility().apply {
            abilityIdentifier = this@SkillAbilityBuilder.abilityIdentifier
            attributes.putAll(this@SkillAbilityBuilder.attributes)
            ability = this@SkillAbilityBuilder.ability as Ability<AbilityAttribute>

            // TODO: Later will support multiple target slot
            targetSlot = usedTargetSlots.first()
            targetSlotIdentifier = targetSlot.identifier
        }
    }

}