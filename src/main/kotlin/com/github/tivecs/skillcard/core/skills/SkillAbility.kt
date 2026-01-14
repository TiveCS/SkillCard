package com.github.tivecs.skillcard.core.skills

import com.github.tivecs.skillcard.core.abilities.Ability
import org.bukkit.event.Event
import java.util.UUID

class SkillAbility {

    val skillId: UUID
    val abilityIdentifier: String
    var executionOrder: Int
    val triggerTargetType: String

    lateinit var ability: Ability<*>

    constructor(skillId: UUID, abilityIdentifier: String, executionOrder: Int, triggerTargetType: String) {
        this.skillId = skillId
        this.abilityIdentifier = abilityIdentifier
        this.executionOrder = executionOrder
        this.triggerTargetType = triggerTargetType
    }

    fun <TEvent : Event> execute(context: SkillExecutionContext<TEvent>) {
        val trigger = context.skillBookContext.getTrigger()
        val triggerResult = context.skillBookContext.getTriggerResult()
        val target = trigger.getTarget(triggerResult, triggerTargetType)
    }
}