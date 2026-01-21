package com.github.tivecs.skillcard.core.entities.books

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.entities.triggers.TriggerExecutionResult
import com.github.tivecs.skillcard.core.entities.triggers.TriggerExecutionResultStatus

class SkillBook {

    lateinit var name: String
    lateinit var displayName: String
    lateinit var description: String
    lateinit var material: XMaterial

    val skillSets = arrayListOf<TriggerSkillSet>()

    fun execute(triggerResult: TriggerExecutionResult) {
        if (triggerResult.status != TriggerExecutionResultStatus.EXECUTED) return

        val skillSet = skillSets.singleOrNull { it.triggerIdentifier == triggerResult.trigger.identifier }

        if (skillSet == null) return

        skillSet.execute(triggerResult)
    }
}