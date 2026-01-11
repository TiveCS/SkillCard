package com.github.tivecs.skillcard.builtin.abilities

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.abilities.Ability
import com.github.tivecs.skillcard.core.abilities.AbilityExecuteResult
import org.bukkit.Material
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

    override val material: Material
        get() = XMaterial.BLAZE_POWDER.get() ?: Material.BLAZE_POWDER

    override val description: String
        get() = "&fSets or adds fire ticks to the target entity."

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