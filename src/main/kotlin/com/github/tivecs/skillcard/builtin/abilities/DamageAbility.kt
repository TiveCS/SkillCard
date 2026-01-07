package com.github.tivecs.skillcard.builtin.abilities

import com.github.tivecs.skillcard.core.abilities.Ability
import com.github.tivecs.skillcard.core.abilities.AbilityExecuteResult
import org.bukkit.entity.LivingEntity

data class DamageAbilityAttribute(val amount: Double, val target: LivingEntity)

object DamageAbility : Ability<DamageAbilityAttribute> {
    override val identifier: String = "damage"

    override fun execute(attribute: DamageAbilityAttribute): AbilityExecuteResult {
        if (attribute.target.isDead)
            return AbilityExecuteResult.CONDITION_NOT_MET

        attribute.target.damage(attribute.amount)

        return AbilityExecuteResult.EXECUTED
    }

}