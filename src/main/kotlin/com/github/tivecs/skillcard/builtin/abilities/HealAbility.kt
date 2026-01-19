package com.github.tivecs.skillcard.builtin.abilities

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.abilities.*
import com.github.tivecs.skillcard.core.skills.SkillAbility
import com.github.tivecs.skillcard.core.skills.SkillExecutionContext
import com.github.tivecs.skillcard.core.triggers.TriggerAttributeKey
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Event

data class HealAbilityAttribute(
    val target: LivingEntity,

    val amount: Double) : AbilityAttribute {

    companion object {
        const val AMOUNT_KEY = "amount"
        const val TARGET_KEY = "target"
    }
}

object HealAbility : Ability<HealAbilityAttribute>  {
    override val identifier: String = "heal"

    override val material: Material
        get() = XMaterial.GOLDEN_APPLE.get() ?: Material.GOLDEN_APPLE

    override val description: String
        get() = "&fHeals the target entity by a specified amount."

    override fun execute(attribute: HealAbilityAttribute?): AbilityExecuteResultState {
        if (attribute == null) return AbilityExecuteResultState.INVALID_ATTRIBUTE

        if (attribute.target.isDead)
            return AbilityExecuteResultState.CONDITION_NOT_MET

        attribute.target.health += attribute.amount

        return AbilityExecuteResultState.EXECUTED
    }

    override fun <TEvent : Event> createAttribute(
        context: SkillExecutionContext<TEvent>,
        skillAbility: SkillAbility
    ): HealAbilityAttribute? {
        val trigger = context.skillBookContext.getTrigger()
        val triggerResult = context.skillBookContext.getTriggerResult()

        val targetToHeal = trigger.getTarget(triggerResult, TriggerAttributeKey.TARGET_TYPE.key) as? LivingEntity ?: return null
        val healAmount = skillAbility.abilityAttributes[HealAbilityAttribute.AMOUNT_KEY] as? Double ?: return null

        return HealAbilityAttribute(targetToHeal, healAmount)
    }

    override fun getRequirements(): List<AbilityRequirement> {
        return listOf(
            AbilityRequirement(
                key = HealAbilityAttribute.AMOUNT_KEY,
                targetType = Double::class,
                source = RequirementSource.USER_CONFIGURED,
                defaultValue = 2.0
            ),
            AbilityRequirement(
                key = HealAbilityAttribute.TARGET_KEY,
                targetType = LivingEntity::class,
                source = RequirementSource.TRIGGER
            ),
        )
    }
}