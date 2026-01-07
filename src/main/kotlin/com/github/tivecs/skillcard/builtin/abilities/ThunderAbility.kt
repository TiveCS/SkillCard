package com.github.tivecs.skillcard.builtin.abilities

import com.github.tivecs.skillcard.core.abilities.Ability
import com.github.tivecs.skillcard.core.abilities.AbilityExecuteResult
import org.bukkit.Location

data class ThunderAbilityAttribute(val location: Location)

object ThunderAbility : Ability<ThunderAbilityAttribute> {
    override val identifier: String = "thunder"

    override fun execute(attribute: ThunderAbilityAttribute): AbilityExecuteResult {
        if (attribute.location.world == null || !attribute.location.isWorldLoaded)
            return AbilityExecuteResult.CONDITION_NOT_MET

        attribute.location.world?.strikeLightning(attribute.location)

        return AbilityExecuteResult.EXECUTED
    }

}