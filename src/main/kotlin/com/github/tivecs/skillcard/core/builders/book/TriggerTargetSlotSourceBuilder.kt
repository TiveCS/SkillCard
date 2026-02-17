package com.github.tivecs.skillcard.core.builders.book

import com.github.tivecs.skillcard.core.converters.TypeConverters
import com.github.tivecs.skillcard.core.entities.books.TriggerTargetSlotSource
import com.github.tivecs.skillcard.core.entities.skills.SkillTargetSlot
import com.github.tivecs.skillcard.core.entities.triggers.AvailableTriggerTarget

class TriggerTargetSlotSourceBuilder(val skillSetBuilder: TriggerSkillSetBuilder) {

    var target: AvailableTriggerTarget<*, *>? = null
    var skillTargetSlot: SkillTargetSlot? = null

    fun setSkillTargetSlot(slot: SkillTargetSlot): TriggerTargetSlotSourceBuilder {
        this.skillTargetSlot = slot
        return this
    }

    fun setTarget(target: AvailableTriggerTarget<*, *>): TriggerTargetSlotSourceBuilder {
        this.target = target
        return this
    }

    fun build(): TriggerTargetSlotSource {
        val errors = validate()

        if (errors.isNotEmpty()) {
            throw IllegalStateException("Cannot build TriggerTargetSlotSource due to validation errors: ${errors.joinToString("; ")}")
        }

        return TriggerTargetSlotSource(
            targetSlotIdentifier = skillTargetSlot!!.identifier,
            skillIdentifier = skillTargetSlot!!.skillIdentifier,
            targetKey = target!!.key
        ).apply {
            this.targetSlot = skillTargetSlot!!
            this.skill = skillTargetSlot!!.skill
        }
    }

    fun validate(): List<String> {
        val errors = mutableListOf<String>()

        val currentTargetSlot = skillTargetSlot
        val currentTarget = target

        if (currentTarget == null) {
            errors.add("Target key is required")
        }

        if (currentTargetSlot == null) {
            errors.add("Target slot is required")
        }

        return errors
    }

}