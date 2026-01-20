package com.github.tivecs.skillcard.builtin2.abilities

import com.github.tivecs.skillcard.core.abilities.AbilityAttribute
import com.github.tivecs.skillcard.core.abilities.AbilityExecuteResultState
import com.github.tivecs.skillcard.core.abilities.AbilityRequirement
import com.github.tivecs.skillcard.core.abilities.RequirementSource
import com.github.tivecs.skillcard.core.converters.TypeConverters
import com.github.tivecs.skillcard.core2.entities.abilities.Ability
import com.github.tivecs.skillcard.core2.entities.skills.SkillAbility
import com.github.tivecs.skillcard.core2.entities.skills.SkillExecutionContext
import org.bukkit.Location
import org.bukkit.Material

/**
 * Example ability using core2 architecture
 */
data class ThunderAbilityAttribute(
    val location: Location
) : AbilityAttribute

object ThunderAbility : Ability<ThunderAbilityAttribute> {

    override val identifier: String = "thunder"
    override val displayName: String = "Thunder Strike"
    override val material: Material = Material.LIGHTNING_ROD
    override val description: String = "&fStrikes lightning at the target location."

    fun getConfigurableRequirements(): List<AbilityRequirement> = listOf(
        AbilityRequirement(
            key = "target",
            targetType = Location::class,
            source = RequirementSource.TRIGGER,
            description = "The location to strike lightning"
        )
    )

    override fun createAttribute(
        context: SkillExecutionContext,
        skillAbility: SkillAbility,
        rawTarget: Any?
    ): ThunderAbilityAttribute? {
        if (rawTarget == null) return null

        // Convert rawTarget to Location using TypeConverters
        // This handles: Entity → location, LivingEntity → location, Location → identity
        val location = TypeConverters.convert(rawTarget, Location::class)
            ?: return null

        return ThunderAbilityAttribute(location)
    }

    override fun execute(attribute: ThunderAbilityAttribute): AbilityExecuteResultState {
        val world = attribute.location.world
            ?: return AbilityExecuteResultState.CONDITION_NOT_MET

        if (!attribute.location.isWorldLoaded) {
            return AbilityExecuteResultState.CONDITION_NOT_MET
        }

        world.strikeLightning(attribute.location)
        return AbilityExecuteResultState.EXECUTED
    }
}
