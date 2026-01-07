package com.github.tivecs.skillcard.builtin.abilities

import com.github.tivecs.skillcard.core.abilities.Ability
import com.github.tivecs.skillcard.core.abilities.AbilityExecuteResult
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect

data class PotionEffectAbilityAttribute(val potionEffect: PotionEffect, val target: LivingEntity)

object PotionEffectAbility : Ability<PotionEffectAbilityAttribute> {
    override val identifier: String = "potion_effect"

    override fun execute(attribute: PotionEffectAbilityAttribute): AbilityExecuteResult {
        if (attribute.target.isDead)
            return AbilityExecuteResult.CONDITION_NOT_MET

        attribute.target.addPotionEffect(attribute.potionEffect)
        return AbilityExecuteResult.EXECUTED
    }

}