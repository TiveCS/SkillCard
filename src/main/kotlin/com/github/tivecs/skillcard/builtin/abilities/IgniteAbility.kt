package com.github.tivecs.skillcard.builtin.abilities

import com.github.tivecs.skillcard.core.abilities.Ability
import com.github.tivecs.skillcard.core.abilities.AbilityExecuteResult
import org.bukkit.entity.Entity

enum class IgniteAbilityDurationType {
    SET,
    ADD
}

data class IgniteAbilityAttribute(
    val target: Entity,
    val duration: Int,
    val type: IgniteAbilityDurationType = IgniteAbilityDurationType.SET)

object IgniteAbility : Ability<IgniteAbilityAttribute> {
    override val identifier: String = "ignite"

    override fun execute(attribute: IgniteAbilityAttribute): AbilityExecuteResult {
        if (attribute.duration <= 0) return AbilityExecuteResult.CONDITION_NOT_MET
        if (attribute.target.isDead) return AbilityExecuteResult.CONDITION_NOT_MET

        attribute.target.fireTicks = when (attribute.type) {
            IgniteAbilityDurationType.SET -> attribute.duration
            IgniteAbilityDurationType.ADD -> attribute.target.fireTicks + attribute.duration
        }

        return AbilityExecuteResult.EXECUTED
    }

}