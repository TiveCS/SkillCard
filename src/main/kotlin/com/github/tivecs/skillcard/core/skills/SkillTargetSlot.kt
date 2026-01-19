package com.github.tivecs.skillcard.core.skills

import java.util.UUID

data class SkillTargetSlot(
    val slotId: UUID,
    val skillId: UUID,
    val identifier: String
) {
    val sources = mutableListOf<SkillTargetSlotSource>()

    fun getSourceByTriggerIdentifier(identifier: String): SkillTargetSlotSource? {
        return sources.firstOrNull { it.triggerIdentifier == identifier }
    }
}