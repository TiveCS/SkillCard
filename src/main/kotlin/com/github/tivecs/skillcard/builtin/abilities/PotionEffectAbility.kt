package com.github.tivecs.skillcard.builtin.abilities

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.abilities.Ability
import com.github.tivecs.skillcard.core.abilities.AbilityExecuteResult
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

data class PotionEffectAbilityAttribute(
    val type: PotionEffectType,
    val duration: Int,
    val amplifier: Int,
    val target: LivingEntity) : AbilityAttribute {

    companion object {
        const val TYPE_KEY = "type"
        const val DURATION_KEY = "duration"
        const val AMPLIFIER_KEY = "amplifier"
        const val TARGET_KEY = "target"
    }

    override fun toConfigurableAttributesMutableMap(): MutableMap<String, Any> {
        return mutableMapOf(
            TYPE_KEY to type,
            DURATION_KEY to duration,
            AMPLIFIER_KEY to amplifier,
            TARGET_KEY to target
        )
    }

}

object PotionEffectAbility : Ability<PotionEffectAbilityAttribute> {
    override val identifier: String = "potion_effect"

    override val material: Material
        get() = XMaterial.POTION.get() ?: Material.POTION

    override val description: String
        get() = "&fApplies a potion effect to the target entity."

    override fun execute(attribute: PotionEffectAbilityAttribute): AbilityExecuteResult {
        if (attribute.target.isDead)
            return AbilityExecuteResult.CONDITION_NOT_MET

        val potion = PotionEffect(
            attribute.type,
            attribute.duration,
            attribute.amplifier
        )

        attribute.target.addPotionEffect(potion)
        return AbilityExecuteResult.EXECUTED
    }

}