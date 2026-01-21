package com.github.tivecs.skillcard.core.builtin.abilities

import com.github.tivecs.skillcard.core.converters.TypeConverters
import com.github.tivecs.skillcard.core.entities.abilities.*
import com.github.tivecs.skillcard.core.entities.triggers.TriggerExecutionResult
import org.bukkit.Location
import org.bukkit.event.Event

data class ThunderAbilityAttribute(
    val location: Location
) : AbilityAttribute

object ThunderAbility : Ability<ThunderAbilityAttribute> {

    val REQUIREMENT_TARGET = AbilityRequirement(
        key = "target",
        targetType = Location::class,
        source = RequirementSource.TRIGGER,
    )

    override val identifier: String = "thunder"

    override val targetRequirement: AbilityRequirement = REQUIREMENT_TARGET

    override val configurableRequirements: List<AbilityRequirement> = listOf()

    override fun execute(attribute: ThunderAbilityAttribute?): AbilityExecuteResultState {
        if (attribute == null) return AbilityExecuteResultState.INVALID_ATTRIBUTE
        val world = attribute.location.world ?: return AbilityExecuteResultState.INVALID_ATTRIBUTE

        if (!attribute.location.isWorldLoaded) return AbilityExecuteResultState.CONDITION_NOT_MET

        world.strikeLightning(attribute.location)

        return AbilityExecuteResultState.EXECUTED
    }

    override fun <TEvent : Event> createAttribute(
        abilityAttributeMap: Map<String, Any>,
        triggerTarget: Any?,
        triggerExecutionResult: TriggerExecutionResult
    ): ThunderAbilityAttribute? {
        if (triggerTarget == null) return null

        val target = TypeConverters.convert(triggerTarget, targetRequirement.targetType) as? Location ?: return null

        return ThunderAbilityAttribute(target)
    }

}