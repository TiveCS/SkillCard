package com.github.tivecs.skillcard.builtin.abilities

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.abilities.Ability
import com.github.tivecs.skillcard.core.abilities.AbilityExecuteResult
import org.bukkit.Material
import org.bukkit.entity.LivingEntity

data class DamageAbilityAttribute(val amount: Double, val target: LivingEntity)

object DamageAbility : Ability<DamageAbilityAttribute> {
    override val identifier: String = "damage"

    override val material: Material
        get() = XMaterial.IRON_SWORD.get() ?: Material.IRON_SWORD

    override val description: String
        get() = "&fDeals a specified amount of damage to the target entity."

    override fun execute(attribute: DamageAbilityAttribute): AbilityExecuteResult {
        if (attribute.target.isDead)
            return AbilityExecuteResult.CONDITION_NOT_MET

        attribute.target.damage(attribute.amount)

        return AbilityExecuteResult.EXECUTED
    }

}