package com.github.tivecs.skillcard.core.skills

import com.github.tivecs.skillcard.core.books.SkillBook
import java.util.UUID

data class SkillTargetSlotSource(
    val sourceId: UUID,
    val slotId: UUID,
    val bookId: UUID,
    val triggerIdentifier: String,
    val targetType: String
) {
    lateinit var book: SkillBook
    lateinit var slot: SkillTargetSlot
}