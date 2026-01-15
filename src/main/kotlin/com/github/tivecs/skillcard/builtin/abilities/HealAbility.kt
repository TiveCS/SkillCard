package com.github.tivecs.skillcard.builtin.abilities

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.abilities.*
import com.github.tivecs.skillcard.core.skills.SkillAbility
import com.github.tivecs.skillcard.core.skills.SkillExecutionContext
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Event

data class HealAbilityAttribute(
    val target: LivingEntity,

    @param:AbilityAttributeFieldConfigurable(AbilityAttributeDataType.DOUBLE)
    val amount: Double) : AbilityAttribute {

    companion object {
        const val AMOUNT_KEY = "amount"
    }


}

object HealAbility : Ability<HealAbilityAttribute>  {
    override val identifier: String = "heal"

    override val material: Material
        get() = XMaterial.GOLDEN_APPLE.get() ?: Material.GOLDEN_APPLE

    override val description: String
        get() = "&fHeals the target entity by a specified amount."

    override fun execute(attribute: HealAbilityAttribute?): AbilityExecuteResultState {
        if (attribute == null) return AbilityExecuteResultState.INVALID_ATTRIBUTE

        if (attribute.target.isDead)
            return AbilityExecuteResultState.CONDITION_NOT_MET

        attribute.target.health += attribute.amount

        return AbilityExecuteResultState.EXECUTED
    }

    override fun <TEvent : Event> createAttribute(
        context: SkillExecutionContext<TEvent>,
        skillAbility: SkillAbility
    ): HealAbilityAttribute? {
        TODO("Not yet implemented")
    }
}