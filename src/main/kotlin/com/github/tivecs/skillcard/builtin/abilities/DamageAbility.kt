package com.github.tivecs.skillcard.builtin.abilities

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.abilities.*
import com.github.tivecs.skillcard.core.skills.SkillAbility
import com.github.tivecs.skillcard.core.skills.SkillExecutionContext
import com.github.tivecs.skillcard.core.triggers.TriggerAttributeKey
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Event

data class DamageAbilityAttribute(
    val amount: Double,
    val target: LivingEntity) : AbilityAttribute {

    companion object  {
        const val AMOUNT_KEY = "amount"
    }
}

object DamageAbility : Ability<DamageAbilityAttribute> {
    override val identifier: String = "damage"

    override val material: Material
        get() = XMaterial.IRON_SWORD.get() ?: Material.IRON_SWORD

    override val description: String
        get() = "&fDeals a specified amount of damage to the target entity."

    override fun execute(attribute: DamageAbilityAttribute?): AbilityExecuteResultState {
        if (attribute == null) return AbilityExecuteResultState.INVALID_ATTRIBUTE

        if (attribute.target.isDead)
            return AbilityExecuteResultState.CONDITION_NOT_MET

        attribute.target.damage(attribute.amount)

        return AbilityExecuteResultState.EXECUTED
    }

    override fun <TEvent : Event> createAttribute(context: SkillExecutionContext<TEvent>, skillAbility: SkillAbility): DamageAbilityAttribute? {
        val trigger = context.skillBookContext.triggerContext.trigger
        val triggerResult = context.skillBookContext.triggerContext.triggerResult

        val target = trigger.getTarget(triggerResult, TriggerAttributeKey.TARGET_TYPE.key) as? LivingEntity ?: return null
        val damageAmount = skillAbility.abilityAttributes[DamageAbilityAttribute.AMOUNT_KEY] as? Double ?: return null

        return DamageAbilityAttribute(damageAmount, target)
    }

}