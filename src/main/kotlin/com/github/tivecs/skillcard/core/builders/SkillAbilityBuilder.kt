package com.github.tivecs.skillcard.core.builders

import com.github.tivecs.skillcard.core.entities.abilities.AbilityRequirement
import com.github.tivecs.skillcard.core.entities.skills.SkillTargetSlot

class SkillAbilityBuilder(val skillBuilder: SkillBuilder) {

    var abilityIdentifier: String = ""
    var attributes = mutableMapOf<String, Any>()

    val usedTargetSlots = mutableListOf<SkillTargetSlot>()

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

    fun setAbilityIdentifier(id: String): SkillAbilityBuilder {
        this.abilityIdentifier = id
        return this
    }

    fun addAttribute(requirement: AbilityRequirement, value: Any): SkillAbilityBuilder {
        this.attributes[requirement.key] = value
        return this
    }

    fun removeAttribute(requirement: AbilityRequirement): SkillAbilityBuilder {
        this.attributes.remove(requirement.key)
        return this
    }

}