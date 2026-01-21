package com.github.tivecs.skillcard.core.builtin.abilities

import com.github.tivecs.skillcard.core.converters.TypeConverters
import com.github.tivecs.skillcard.core.entities.abilities.Ability
import com.github.tivecs.skillcard.core.entities.abilities.AbilityAttribute
import com.github.tivecs.skillcard.core.entities.abilities.AbilityExecuteResultState
import com.github.tivecs.skillcard.core.entities.abilities.AbilityRequirement
import com.github.tivecs.skillcard.core.entities.abilities.RequirementSource
import com.github.tivecs.skillcard.core.entities.triggers.TriggerExecutionResult
import org.bukkit.entity.Entity
import org.bukkit.event.Event

data class IgniteAbilityAttribute(
    val target: Entity,
    val duration: Int,
) : AbilityAttribute {
}

object IgniteAbility : Ability<IgniteAbilityAttribute> {

    val REQUIREMENT_TARGET =AbilityRequirement(
        key = "target",
        targetType = Entity::class,
        source = RequirementSource.TRIGGER
    )

    val REQUIREMENT_DURATION = AbilityRequirement(
        key = "duration",
        targetType = Int::class,
        source = RequirementSource.USER_CONFIGURED,
        defaultValue = 40
    )

    override val identifier: String = "ignite"

    override val targetRequirement: AbilityRequirement = REQUIREMENT_TARGET

    override val configurableRequirements: List<AbilityRequirement> = listOf(
        REQUIREMENT_DURATION,
    )

    override fun execute(attribute: IgniteAbilityAttribute?): AbilityExecuteResultState {
        if (attribute == null) return AbilityExecuteResultState.INVALID_ATTRIBUTE
        val target = attribute.target

        if (target.isDead) return AbilityExecuteResultState.CONDITION_NOT_MET

        target.fireTicks = attribute.duration

        return AbilityExecuteResultState.EXECUTED
    }

    override fun <TEvent : Event> createAttribute(
        abilityAttributeMap: Map<String, Any>,
        triggerTarget: Any?,
        triggerExecutionResult: TriggerExecutionResult
    ): IgniteAbilityAttribute? {
        if (triggerTarget == null) return null

        val duration =
            abilityAttributeMap.getOrElse(REQUIREMENT_DURATION.key, { REQUIREMENT_DURATION.defaultValue }) as? Int ?: return null

        val correctTarget = TypeConverters.convert(triggerTarget, REQUIREMENT_TARGET.targetType) as? Entity ?: return null

        return IgniteAbilityAttribute(
            target = correctTarget,
            duration = duration
        )
    }
}