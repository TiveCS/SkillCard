package com.github.tivecs.skillcard.core2.entities.skills

import com.github.tivecs.skillcard.core.abilities.AbilityAttribute
import com.github.tivecs.skillcard.core2.entities.abilities.Ability
import com.github.tivecs.skillcard.core2.entities.books.TriggerTargetSlotSource
import com.github.tivecs.skillcard.core2.entities.triggers.TriggerExecutionResult
import com.github.tivecs.skillcard.core2.entities.triggers.TriggerExecutionResultStatus
import org.bukkit.event.Event

class SkillAbility {

    lateinit var abilityIdentifier: String
    var targetSlotIdentifier: String? = null

    val attributes = mutableMapOf<String, Any>()

    lateinit var skill: Skill
    lateinit var ability: Ability<AbilityAttribute>
    lateinit var targetSlot: SkillTargetSlot

    fun execute(sourceTargetSlots: List<TriggerTargetSlotSource>, triggerResult: TriggerExecutionResult) {
        if (triggerResult.status != TriggerExecutionResultStatus.EXECUTED) return
        if (targetSlotIdentifier == null) return

        val targetSlotSource = sourceTargetSlots.singleOrNull { it.targetSlotIdentifier == targetSlotIdentifier }

        if (targetSlotSource == null) return

        val target = targetSlotSource.getTarget(triggerResult) ?: return
        val attribute = ability.createAttribute<Event>(attributes, target, triggerResult) ?: return

        ability.execute(attribute)
    }

}
