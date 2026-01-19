package com.github.tivecs.skillcard.core.skills

class SkillTriggerTargetSlotBuilder {

    var identifier: String = ""
        private set

    fun setIdentifier(identifier: String): SkillTriggerTargetSlotBuilder {
        this.identifier = identifier
        return this
    }
}