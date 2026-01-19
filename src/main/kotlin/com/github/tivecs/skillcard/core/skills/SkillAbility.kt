package com.github.tivecs.skillcard.core.skills

import com.github.tivecs.skillcard.core.abilities.Ability
import com.github.tivecs.skillcard.core.abilities.AbilityAttribute
import com.github.tivecs.skillcard.internal.exceptions.SkillAbilityObjectNotInitialized
import org.bukkit.event.Event
import java.util.UUID

class SkillAbility {

    val skillId: UUID
    val abilityIdentifier: String
    var executionOrder: Int
    val abilityAttributes: Map<String, Any>

    lateinit var ability: Ability<AbilityAttribute>

    constructor(
        skillId: UUID,
        abilityIdentifier: String,
        executionOrder: Int,
        abilityAttributes: Map<String, Any> = mutableMapOf()) {

        this.skillId = skillId
        this.abilityIdentifier = abilityIdentifier
        this.executionOrder = executionOrder
        this.abilityAttributes = abilityAttributes
    }

    fun <TEvent : Event> execute(context: SkillExecutionContext<TEvent>) {
        if (!::ability.isInitialized) {
            throw SkillAbilityObjectNotInitialized(this)
        }

        val attribute = ability.createAttribute(context, this) ?: return
        ability.execute(attribute)
    }
}