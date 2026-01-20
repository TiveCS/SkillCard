package com.github.tivecs.skillcard.core2.builtin.abilities

import com.github.tivecs.skillcard.core.abilities.AbilityAttribute
import com.github.tivecs.skillcard.core.abilities.AbilityExecuteResultState
import com.github.tivecs.skillcard.core.abilities.AbilityRequirement
import com.github.tivecs.skillcard.core.abilities.RequirementSource
import com.github.tivecs.skillcard.core.converters.TypeConverters
import com.github.tivecs.skillcard.core2.entities.abilities.Ability
import com.github.tivecs.skillcard.core2.entities.triggers.TriggerExecutionResult
import org.bukkit.Location
import org.bukkit.event.Event

data class ThunderAbilityAttribute(
    val location: Location
) : AbilityAttribute {

}

class ThunderAbility : Ability<ThunderAbilityAttribute> {

    companion object {
        const val REQUIREMENT_TARGET_KEY = "target"
    }

    override val identifier: String = "thunder"

    override val targetRequirement: AbilityRequirement = AbilityRequirement(
        key = REQUIREMENT_TARGET_KEY,
        targetType = Location::class,
        source = RequirementSource.TRIGGER,
    )

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

        if (!TypeConverters.canConvert(triggerTarget, targetRequirement.targetType)) return null

        val target = TypeConverters.convert(triggerTarget, targetRequirement.targetType) ?: return null

        return ThunderAbilityAttribute(target as Location)
    }

}