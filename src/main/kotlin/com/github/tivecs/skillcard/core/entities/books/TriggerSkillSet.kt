package com.github.tivecs.skillcard.core.entities.books

import com.github.tivecs.skillcard.core.entities.skills.Skill
import com.github.tivecs.skillcard.core.entities.triggers.Trigger
import com.github.tivecs.skillcard.core.entities.triggers.TriggerExecutionResult
import com.github.tivecs.skillcard.core.entities.triggers.TriggerExecutionResultStatus
import org.bukkit.event.Event
import java.util.UUID

class TriggerSkillSet(val triggerSkillSetId: UUID) {

    lateinit var triggerIdentifier: String

    val skills = arrayListOf<Skill>()
    val targetSlotSources = arrayListOf<TriggerTargetSlotSource>()

    lateinit var trigger: Trigger<Event>

    fun getSkillTargetSlots(skillIdentifier: String): List<TriggerTargetSlotSource> {
        return targetSlotSources.filter { it.skillIdentifier == skillIdentifier }
    }

    fun execute(triggerResult: TriggerExecutionResult) {
        if (triggerResult.status != TriggerExecutionResultStatus.EXECUTED) return

        skills.forEach { skill ->
            val sourceTargetSlots = getSkillTargetSlots(skill.identifier)
            skill.execute(sourceTargetSlots, triggerResult)
        }
    }
}