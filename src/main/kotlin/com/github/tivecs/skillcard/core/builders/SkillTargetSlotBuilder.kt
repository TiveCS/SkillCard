package com.github.tivecs.skillcard.core.builders

import java.util.UUID

class SkillTargetSlotBuilder(val skillBuilder: SkillBuilder) {

    val id: UUID = UUID.randomUUID()
    var slotIdentifier: String = ""
    var attributes = mutableMapOf<String, Any>()

    fun setSlotIdentifier(id: String): SkillTargetSlotBuilder {
        this.slotIdentifier = id
        return this
    }

    fun addAttribute(key: String, value: Any): SkillTargetSlotBuilder {
        this.attributes[key] = value
        return this
    }

    fun removeAttribute(key: String): SkillTargetSlotBuilder {
        this.attributes.remove(key)
        return this
    }

}