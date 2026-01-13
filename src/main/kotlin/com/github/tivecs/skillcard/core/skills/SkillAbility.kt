package com.github.tivecs.skillcard.core.skills

import com.github.tivecs.skillcard.core.abilities.Ability
import com.github.tivecs.skillcard.core.triggers.TriggerAttributeKey
import java.util.UUID

class SkillAbility {

    val skillId: UUID
    val abilityIdentifier: String
    var executionOrder: Int

    lateinit var ability: Ability<*>

    constructor(skillId: UUID, abilityIdentifier: String, executionOrder: Int) {
        this.skillId = skillId
        this.abilityIdentifier = abilityIdentifier
        this.executionOrder = executionOrder
    }

    fun execute(context: SkillExecutionContext) {
        val abilityAttributes = ability.
        ability.execute()
    }
}