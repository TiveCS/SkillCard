package com.github.tivecs.skillcard.core.builders.book

import com.github.tivecs.skillcard.core.converters.TypeConverters
import com.github.tivecs.skillcard.core.entities.skills.SkillTargetSlot
import com.github.tivecs.skillcard.core.entities.triggers.AvailableTriggerTarget

class TriggerTargetSlotSourceBuilder {

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

    fun validate(): List<String> {
        val errors = mutableListOf<String>()

        if (target == null) {
            errors.add("Target key is required")
        }

        if (skillTargetSlot == null) {
            errors.add("Target slot is required")
        }

        val currentTargetSlot = skillTargetSlot
        val currentTarget = target

        if (currentTargetSlot != null && currentTarget != null) {
            val outputType = currentTarget.outputType
            val acceptableSourceTypes = TypeConverters.getAcceptableSourceTypes(currentTargetSlot.targetType)
            val isValid = acceptableSourceTypes.any {
                it == outputType || TODO("add checking for 'outputType' assignable to 'it' type")
            }

            if (!isValid) {
                errors.add("Target slot of '${currentTargetSlot.identifier} (${currentTargetSlot.targetType.simpleName})' value type is incompatible with '${currentTarget.key} (${currentTarget.outputType.simpleName})'")
            }
        }

        return errors
    }

}