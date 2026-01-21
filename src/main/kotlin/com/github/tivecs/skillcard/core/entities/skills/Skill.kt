package com.github.tivecs.skillcard.core.entities.skills

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.entities.books.TriggerTargetSlotSource
import com.github.tivecs.skillcard.core.entities.triggers.TriggerExecutionResult
import com.github.tivecs.skillcard.core.entities.triggers.TriggerExecutionResultStatus

class Skill {

    lateinit var identifier: String
    lateinit var displayName: String
    lateinit var material: XMaterial

    val abilities = arrayListOf<SkillAbility>()
    val targetSlots = arrayListOf<SkillTargetSlot>()

    fun execute(sourceTargetSlots: List<TriggerTargetSlotSource>, triggerResult: TriggerExecutionResult) {
        if (triggerResult.status != TriggerExecutionResultStatus.EXECUTED) return

        abilities.forEach {
            it.execute(sourceTargetSlots, triggerResult)
        }
    }

}