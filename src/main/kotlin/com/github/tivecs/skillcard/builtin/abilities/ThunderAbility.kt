package com.github.tivecs.skillcard.builtin.abilities

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.abilities.Ability
import com.github.tivecs.skillcard.core.abilities.AbilityAttribute
import com.github.tivecs.skillcard.core.abilities.AbilityExecuteResultState
import com.github.tivecs.skillcard.core.skills.SkillAbility
import com.github.tivecs.skillcard.core.skills.SkillExecutionContext
import com.github.tivecs.skillcard.core.triggers.TriggerAttributeKey
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.event.Event

data class ThunderAbilityAttribute(val location: Location) : AbilityAttribute {
    companion object {
        const val LOCATION_KEY = "location"
    }
}

object ThunderAbility : Ability<ThunderAbilityAttribute> {
    override val identifier: String = "thunder"

    override val material: Material
        get() = XMaterial.NETHER_STAR.get() ?: Material.NETHER_STAR

    override val description: String
        get() = "&fStrikes lightning at the specified location."

    override fun execute(attribute: ThunderAbilityAttribute?): AbilityExecuteResultState {
        if (attribute == null) return AbilityExecuteResultState.INVALID_ATTRIBUTE

        val world = attribute.location.world

        if (world == null || !attribute.location.isWorldLoaded)
            return AbilityExecuteResultState.CONDITION_NOT_MET

        world.strikeLightning(attribute.location)

        return AbilityExecuteResultState.EXECUTED
    }

    override fun <TEvent : Event> createAttribute(
        context: SkillExecutionContext<TEvent>,
        skillAbility: SkillAbility
    ): ThunderAbilityAttribute? {
        val trigger = context.skillBookContext.getTrigger()
        val triggerResult = context.skillBookContext.getTriggerResult()

        val triggerTarget = trigger.getTarget(triggerResult, TriggerAttributeKey.TARGET_TYPE.key) ?: return null

        val targetLocation: Location = when (triggerTarget) {
            is Entity -> triggerTarget.location
            is Location -> triggerTarget
            else -> return null
        }

        return ThunderAbilityAttribute(targetLocation)
    }

}