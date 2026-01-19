package com.github.tivecs.skillcard.builtin.abilities

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.abilities.Ability
import com.github.tivecs.skillcard.core.abilities.AbilityAttribute
import com.github.tivecs.skillcard.core.abilities.AbilityExecuteResultState
import com.github.tivecs.skillcard.core.abilities.AbilityRequirement
import com.github.tivecs.skillcard.core.abilities.RequirementSource
import com.github.tivecs.skillcard.core.skills.SkillAbility
import com.github.tivecs.skillcard.core.skills.SkillExecutionContext
import com.github.tivecs.skillcard.core.triggers.TriggerAttributeKey
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.event.Event

enum class IgniteAbilityDurationType {
    SET,
    ADD
}

data class IgniteAbilityAttribute(
    val target: Entity,
    val duration: Int,
    val type: IgniteAbilityDurationType = IgniteAbilityDurationType.SET) : AbilityAttribute {

    companion object {
        const val DURATION_KEY = "duration"
        const val TYPE_KEY = "type"
        const val TARGET_KEY = "target"
    }
}

object IgniteAbility : Ability<IgniteAbilityAttribute> {
    override val identifier: String = "ignite"

    override val material: Material
        get() = XMaterial.BLAZE_POWDER.get() ?: Material.BLAZE_POWDER

    override val description: String
        get() = "&fSets or adds fire ticks to the target entity."

    override fun execute(attribute: IgniteAbilityAttribute?): AbilityExecuteResultState {
        if (attribute == null) return AbilityExecuteResultState.INVALID_ATTRIBUTE

        if (attribute.duration <= 0) return AbilityExecuteResultState.CONDITION_NOT_MET
        if (attribute.target.isDead) return AbilityExecuteResultState.CONDITION_NOT_MET

        attribute.target.fireTicks = when (attribute.type) {
            IgniteAbilityDurationType.SET -> attribute.duration
            IgniteAbilityDurationType.ADD -> attribute.target.fireTicks + attribute.duration
        }

        return AbilityExecuteResultState.EXECUTED
    }

    override fun <TEvent : Event> createAttribute(
        context: SkillExecutionContext<TEvent>,
        skillAbility: SkillAbility
    ): IgniteAbilityAttribute? {
        val trigger = context.skillBookContext.triggerContext.trigger
        val triggerResult = context.skillBookContext.triggerContext.triggerResult

        val target = trigger.getTarget(triggerResult, TriggerAttributeKey.TARGET_TYPE.key) as? Entity ?: return null
        val duration = skillAbility.abilityAttributes[IgniteAbilityAttribute.DURATION_KEY] as? Int ?: return null
        val type = skillAbility.abilityAttributes[IgniteAbilityAttribute.TYPE_KEY] as? String ?: return null
        val durationIgniteType: IgniteAbilityDurationType = IgniteAbilityDurationType.valueOf(type.uppercase())

        return IgniteAbilityAttribute(
            target,
            duration,
            durationIgniteType,
        )
    }

    override fun getRequirements(): List<AbilityRequirement> {
        return listOf(
            AbilityRequirement(
                key = IgniteAbilityAttribute.TYPE_KEY,
                targetType = String::class,
                source = RequirementSource.USER_CONFIGURED,
                defaultValue = IgniteAbilityDurationType.SET.name
            ),
            AbilityRequirement(
                key = IgniteAbilityAttribute.DURATION_KEY,
                targetType = Int::class,
                source = RequirementSource.USER_CONFIGURED,
                defaultValue = 60
            ),
            AbilityRequirement(
                key = IgniteAbilityAttribute.TARGET_KEY,
                targetType = Entity::class,
                source = RequirementSource.TRIGGER
            ),
        )
    }

}