package com.github.tivecs.skillcard.core2.entities.books

import com.github.tivecs.skillcard.core2.entities.skills.Skill
import com.github.tivecs.skillcard.core2.entities.skills.SkillTargetSlot
import com.github.tivecs.skillcard.core2.entities.triggers.TriggerExecutionResult
import com.github.tivecs.skillcard.core2.entities.triggers.TriggerExecutionResultStatus

class TriggerTargetSlotSource(val targetSlotIdentifier: String, val skillIdentifier: String) {

    lateinit var targetKey: String

    lateinit var triggerSkillSet: TriggerSkillSet
    lateinit var skill: Skill
    lateinit var targetSlot: SkillTargetSlot

    fun getTarget(triggerExecutionResult: TriggerExecutionResult): Any? {
        val event = triggerExecutionResult.event

        if (triggerExecutionResult.status != TriggerExecutionResultStatus.EXECUTED) return null
        if (event == null) return null

        val targetGetter = triggerSkillSet.trigger.getAvailableTargets().singleOrNull { it.key == targetKey }
        if (targetGetter == null) return null

        return targetGetter.getOutput.invoke(event)
    }

}