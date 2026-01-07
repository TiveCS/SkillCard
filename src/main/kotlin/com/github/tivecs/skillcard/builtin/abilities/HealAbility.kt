package com.github.tivecs.skillcard.builtin.abilities

import com.github.tivecs.skillcard.core.abilities.Ability
import com.github.tivecs.skillcard.core.abilities.AbilityExecuteResult
import org.bukkit.entity.LivingEntity

data class HealAbilityAttribute(val target: LivingEntity, val amount: Double)

object HealAbility : Ability<HealAbilityAttribute>  {
    override val identifier: String = "heal"

    override fun execute(attribute: HealAbilityAttribute): AbilityExecuteResult {
        if (attribute.target.isDead)
            return AbilityExecuteResult.CONDITION_NOT_MET

        attribute.target.health += attribute.amount

        return AbilityExecuteResult.EXECUTED
    }
}