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

enum class DashAbilityDirection {
    FORWARD,
    BACKWARD,
}

data class DashAbilityAttribute(
    val target: Entity,
    val direction: DashAbilityDirection,
    val force: Double
) : AbilityAttribute {}

object DashAbility : Ability<DashAbilityAttribute> {

    val REQUIREMENT_TARGET = AbilityRequirement(
        key = "target",
        source = RequirementSource.TRIGGER,
        targetType = Entity::class
    )

    val REQUIREMENT_FORCE = AbilityRequirement(
        key = "force",
        source = RequirementSource.USER_CONFIGURED,
        targetType = Double::class,
        defaultValue = 10.0
    )

    val REQUIREMENT_DIRECTION = AbilityRequirement(
        key = "direction",
        source = RequirementSource.USER_CONFIGURED,
        targetType = DashAbilityDirection::class,
    )

    override val identifier: String = "dash"

    override val targetRequirement: AbilityRequirement = REQUIREMENT_TARGET

    override val configurableRequirements: List<AbilityRequirement> = listOf(
        REQUIREMENT_FORCE,
        REQUIREMENT_DIRECTION
    )

    override fun execute(attribute: DashAbilityAttribute?): AbilityExecuteResultState {
        if (attribute == null) return AbilityExecuteResultState.INVALID_ATTRIBUTE
        val target = attribute.target

        if(target.isDead) return AbilityExecuteResultState.CONDITION_NOT_MET

        val location = target.location

        location.pitch = 0f

        val currentDirection = location.direction

        target.velocity = currentDirection.multiply(when (attribute.direction) {
            DashAbilityDirection.FORWARD -> attribute.force
            DashAbilityDirection.BACKWARD -> -attribute.force
        })

        return AbilityExecuteResultState.EXECUTED
    }

    override fun <TEvent : Event> createAttribute(
        abilityAttributeMap: Map<String, Any>,
        triggerTarget: Any?,
        triggerExecutionResult: TriggerExecutionResult
    ): DashAbilityAttribute? {
        if (triggerTarget == null) return null

        val force = abilityAttributeMap.getOrDefault(REQUIREMENT_FORCE.key, REQUIREMENT_FORCE.defaultValue) as? Double ?: return null
        val directionStr = abilityAttributeMap[REQUIREMENT_DIRECTION.key] ?: return null
        val direction = TypeConverters.convert(directionStr, DashAbilityDirection::class) ?: return null

        val correctTarget = TypeConverters.convert(triggerTarget, REQUIREMENT_TARGET.targetType) as? Entity ?: return null

        return DashAbilityAttribute(
            target = correctTarget,
            direction = direction,
            force = force
        )
    }
}