package com.github.tivecs.skillcard.core.builtin.abilities

import com.github.tivecs.skillcard.core.converters.TypeConverters
import com.github.tivecs.skillcard.core.entities.abilities.*
import com.github.tivecs.skillcard.core.entities.triggers.TriggerExecutionResult
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Event
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

data class PotionEffectAbilityAttribute(
    val type: PotionEffectType,
    val duration: Int,
    val target: LivingEntity,
    val amplifier: Int
) : AbilityAttribute {

}

object PotionEffectAbility : Ability<PotionEffectAbilityAttribute> {

    val REQUIREMENT_TARGET = AbilityRequirement(
        key = "target",
        source = RequirementSource.TRIGGER,
        targetType = LivingEntity::class
    )

    val REQUIREMENT_DURATION = AbilityRequirement(
        key = "duration",
        source = RequirementSource.USER_CONFIGURED,
        targetType = Int::class,
        defaultValue = 100
    )

    val REQUIREMENT_AMPLIFIER = AbilityRequirement(
        key = "amplifier",
        source = RequirementSource.USER_CONFIGURED,
        targetType = Int::class,
        defaultValue = 1
    )

    val REQUIREMENT_TYPE = AbilityRequirement(
        key = "type",
        source = RequirementSource.USER_CONFIGURED,
        targetType = PotionEffectType::class,
    )

    override val identifier: String = "potion_effect"

    override val targetRequirement: AbilityRequirement = REQUIREMENT_TARGET

    override val configurableRequirements: List<AbilityRequirement> = listOf(
        REQUIREMENT_AMPLIFIER,
        REQUIREMENT_DURATION,
        REQUIREMENT_TYPE
    )

    override fun execute(attribute: PotionEffectAbilityAttribute?): AbilityExecuteResultState {
        if (attribute == null) return AbilityExecuteResultState.INVALID_ATTRIBUTE

        val target = attribute.target

        if (target.isDead) return AbilityExecuteResultState.CONDITION_NOT_MET

        val amplifier = attribute.amplifier
        val duration = attribute.duration
        val type = attribute.type

        val potionEffect = PotionEffect(type, duration, amplifier)

        target.addPotionEffect(potionEffect)

        return AbilityExecuteResultState.EXECUTED
    }

    override fun <TEvent : Event> createAttribute(
        abilityAttributeMap: Map<String, Any>,
        triggerTarget: Any?,
        triggerExecutionResult: TriggerExecutionResult
    ): PotionEffectAbilityAttribute? {

        val duration = abilityAttributeMap.getOrDefault(REQUIREMENT_DURATION.key, REQUIREMENT_DURATION.defaultValue) as? Int ?: return null
        val amplifier = abilityAttributeMap.getOrDefault(REQUIREMENT_AMPLIFIER.key, REQUIREMENT_AMPLIFIER.defaultValue) as? Int ?: return null
        val typeStr = abilityAttributeMap[REQUIREMENT_TYPE.key] ?: return null
        val type = TypeConverters.convert(typeStr, REQUIREMENT_TYPE.targetType) as? PotionEffectType ?: return null

        val correctTarget = TypeConverters.convert(typeStr, REQUIREMENT_TARGET.targetType) as? LivingEntity ?: return null

        return PotionEffectAbilityAttribute(
            duration = duration,
            amplifier = amplifier,
            type = type,
            target = correctTarget
        )
    }
}