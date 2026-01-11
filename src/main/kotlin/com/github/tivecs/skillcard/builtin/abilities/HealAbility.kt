package com.github.tivecs.skillcard.builtin.abilities

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.abilities.Ability
import com.github.tivecs.skillcard.core.abilities.AbilityExecuteResult
import org.bukkit.Material
import org.bukkit.entity.LivingEntity

data class HealAbilityAttribute(val target: LivingEntity, val amount: Double)

object HealAbility : Ability<HealAbilityAttribute>  {
    override val identifier: String = "heal"

    override val material: Material
        get() = XMaterial.GOLDEN_APPLE.get() ?: Material.GOLDEN_APPLE

    override val description: String
        get() = "&fHeals the target entity by a specified amount."

    override fun execute(attribute: HealAbilityAttribute): AbilityExecuteResult {
        if (attribute.target.isDead)
            return AbilityExecuteResult.CONDITION_NOT_MET

        attribute.target.health += attribute.amount

        return AbilityExecuteResult.EXECUTED
    }
}