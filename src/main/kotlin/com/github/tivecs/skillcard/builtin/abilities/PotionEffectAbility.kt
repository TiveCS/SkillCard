package com.github.tivecs.skillcard.builtin.abilities

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.abilities.Ability
import com.github.tivecs.skillcard.core.abilities.AbilityAttribute
import com.github.tivecs.skillcard.core.abilities.AbilityExecuteResultState
import com.github.tivecs.skillcard.core.skills.SkillAbility
import com.github.tivecs.skillcard.core.skills.SkillExecutionContext
import com.github.tivecs.skillcard.core.triggers.TriggerAttributeKey
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Event
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
    }
}

object PotionEffectAbility : Ability<PotionEffectAbilityAttribute> {
    override val identifier: String = "potion_effect"

    override val material: Material
        get() = XMaterial.POTION.get() ?: Material.POTION

    override val description: String
        get() = "&fApplies a potion effect to the target entity."

    override fun execute(attribute: PotionEffectAbilityAttribute?): AbilityExecuteResultState {
        if (attribute == null) return AbilityExecuteResultState.INVALID_ATTRIBUTE

        if (attribute.target.isDead)
            return AbilityExecuteResultState.CONDITION_NOT_MET

        val potion = PotionEffect(
            attribute.type,
            attribute.duration,
            attribute.amplifier
        )

        attribute.target.addPotionEffect(potion)
        return AbilityExecuteResultState.EXECUTED
    }

    override fun <TEvent : Event> createAttribute(
        context: SkillExecutionContext<TEvent>,
        skillAbility: SkillAbility
    ): PotionEffectAbilityAttribute? {
        val trigger = context.skillBookContext.getTrigger()
        val triggerResult = context.skillBookContext.getTriggerResult()

        val target = trigger.getTarget(triggerResult, TriggerAttributeKey.TARGET_TYPE.key) as? LivingEntity ?: return null

        val potionTypeStr = skillAbility.abilityAttributes[PotionEffectAbilityAttribute.TYPE_KEY] as? String ?: return null
        val potionType = PotionEffectType.getByName(potionTypeStr.uppercase()) ?: return null
        val duration = skillAbility.abilityAttributes[PotionEffectAbilityAttribute.DURATION_KEY] as? Int ?: return null
        val amplifier = skillAbility.abilityAttributes[PotionEffectAbilityAttribute.AMPLIFIER_KEY] as? Int ?: return null

        return PotionEffectAbilityAttribute(
            potionType,
            duration,
            amplifier,
            target
        )
    }

}