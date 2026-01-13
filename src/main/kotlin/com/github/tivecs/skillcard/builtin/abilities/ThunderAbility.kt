package com.github.tivecs.skillcard.builtin.abilities

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.abilities.Ability
import com.github.tivecs.skillcard.core.abilities.AbilityExecuteResult
import org.bukkit.Location
import org.bukkit.Material

data class ThunderAbilityAttribute(val location: Location) : AbilityAttribute {

    companion object {
        const val LOCATION_KEY = "location"
    }

    override fun toConfigurableAttributesMutableMap(): MutableMap<String, Any> {
        return mutableMapOf(
            LOCATION_KEY to location,
        )
    }
}

object ThunderAbility : Ability<ThunderAbilityAttribute> {
    override val identifier: String = "thunder"

    override val material: Material
        get() = XMaterial.NETHER_STAR.get() ?: Material.NETHER_STAR

    override val description: String
        get() = "&fStrikes lightning at the specified location."

    override fun execute(attribute: ThunderAbilityAttribute): AbilityExecuteResult {
        if (attribute.location.world == null || !attribute.location.isWorldLoaded)
            return AbilityExecuteResult.CONDITION_NOT_MET

        attribute.location.world?.strikeLightning(attribute.location)

        return AbilityExecuteResult.EXECUTED
    }

}