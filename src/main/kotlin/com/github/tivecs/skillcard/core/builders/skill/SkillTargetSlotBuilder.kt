package com.github.tivecs.skillcard.core.builders.skill

import com.github.tivecs.skillcard.core.entities.skills.SkillTargetSlot

class SkillTargetSlotBuilder(val skillBuilder: SkillBuilder) {

    var slotIdentifier: String = ""

    fun setSlotIdentifier(id: String): SkillTargetSlotBuilder {
        this.slotIdentifier = id
        return this
    }

    fun validate(): List<String> {
        val errors = arrayListOf<String>()

        if (slotIdentifier.isBlank()) {
            errors.add("Slot Identifier cannot be blank")
        }

        return errors
    }

    fun build(): SkillTargetSlot {
        val errors = validate()

        if (errors.isNotEmpty()) {
            throw IllegalStateException("There are missing or invalid fields during build SkillTargetSlot. Errors: $errors")
        }

        return SkillTargetSlot(slotIdentifier)
    }

}